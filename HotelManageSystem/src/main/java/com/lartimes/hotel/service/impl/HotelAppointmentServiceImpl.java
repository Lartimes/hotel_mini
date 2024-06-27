package com.lartimes.hotel.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lartimes.hotel.common.HotelException;
import com.lartimes.hotel.common.PageParams;
import com.lartimes.hotel.common.PageResult;
import com.lartimes.hotel.common.ResultReturn;
import com.lartimes.hotel.mapper.GuestCardMapper;
import com.lartimes.hotel.mapper.HotelAppointmentMapper;
import com.lartimes.hotel.model.dto.GuestDTO;
import com.lartimes.hotel.model.dto.QueryAppointmentsDto;
import com.lartimes.hotel.model.dto.ReturnDepositDto;
import com.lartimes.hotel.model.dto.RoomAndAppointmentsDto;
import com.lartimes.hotel.model.po.GuestCard;
import com.lartimes.hotel.model.po.HotelAppointment;
import com.lartimes.hotel.model.po.Room;
import com.lartimes.hotel.service.GuestService;
import com.lartimes.hotel.service.HotelAppointmentService;
import com.lartimes.hotel.service.RoomService;
import com.lartimes.hotel.service.mq.MQProducerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class HotelAppointmentServiceImpl extends ServiceImpl<HotelAppointmentMapper, HotelAppointment> implements HotelAppointmentService {

    private static final int MAX_RETRY = 500;
    private final GuestService guestService;
    private final RedisTemplate<String, String> redisTemplate;
    private final RoomService roomService;


    private final SqlSessionFactory sqlSessionFactory;
    private final MQProducerService mqProducerService;
    private final HotelAppointmentMapper appointmentMapper;

    public HotelAppointmentServiceImpl(GuestService guestService, RedisTemplate<String, String> redisTemplate, RoomService roomService, SqlSessionFactory sqlSessionFactory, MQProducerService mqProducerService, HotelAppointmentMapper appointmentMapper) {
        this.guestService = guestService;
        this.redisTemplate = redisTemplate;
        this.roomService = roomService;
        this.sqlSessionFactory = sqlSessionFactory;
        this.mqProducerService = mqProducerService;
        this.appointmentMapper = appointmentMapper;
    }

    @Transactional
    @Override
    public Object appointedHotel(GuestDTO[] guests, Integer roomId, LocalDate checkIn, LocalDate checkOut) {
//        1. guest
        List<Integer> arr = guestService.insertGuest(guests);
//        获取roomId 这一类型的,  存到redis中，json字符串
        String uuid = UUID.randomUUID().toString();
        ArrayList<HotelAppointment> appointments = new ArrayList<>();
        int count = 100;
        while (count <= MAX_RETRY) {
            Boolean lock = redisTemplate.opsForValue().setIfAbsent("appoint" + roomId, uuid, Duration.ofSeconds(10));
//        redis write
            if (Boolean.TRUE.equals(lock)) {
//        2. 抢占该房间
//                更改room / appointment （status）
                Room room = roomService.getById(roomId);
                if (!"空闲".equals(room.getRoomStatus())) {// mq 消费端需要设置行锁了
                    log.debug("该房间已经被预定或正在预定 : {}", roomId);
                    roomId = getNextRoomId(roomId);
                    if (roomId == null) {
                        return ResultReturn.failure(0, "请重试");
                    }
                    continue;
                }
                room.setRoomStatus("正在预定");
                roomService.updateById(room);
                Double roomPrice = room.getRoomPrice();
                Double deposit = roomPrice + roomPrice * 1.5;
                for (int i = 0; i < guests.length; i++) {
                    Integer guestId = arr.get(i);
                    HotelAppointment hotelAppointment = new HotelAppointment();
                    hotelAppointment.setRoomId(roomId);
                    hotelAppointment.setDeposit(deposit);
                    hotelAppointment.setGuestId(guestId);
                    hotelAppointment.setCheckInDate(checkIn);
                    hotelAppointment.setCheckOutDate(checkOut);
                    appointments.add(hotelAppointment);
                }
                MybatisBatch<HotelAppointment> mybatisBatch = new MybatisBatch<>(sqlSessionFactory, appointments);
                MybatisBatch.Method<HotelAppointment> method = new MybatisBatch.Method<>(HotelAppointmentMapper.class);
                mybatisBatch.execute(method.insert());
//        拿到占用资格 10min付押金
//                只发给所有guests ， 优先付款 只允许一次，随后更改状态为预定完成
//                超时的话，更改room状态，删除预定行
//                发送一个押金金额，和roomId+UUID位置支付标识
                ReturnDepositDto returnDepositDto = new ReturnDepositDto("room:" + roomId + ":" + uuid, deposit, arr);
                String str = JSON.toJSONString(returnDepositDto);
                mqProducerService.sendDelayMsg(str, 9 , "tag1");
                String s = redisTemplate.opsForValue().get("appoint" + roomId);
                assert s != null;
                if (s.equals(uuid)) {
                    redisTemplate.delete("appoint" + roomId);
                } else {
                    HotelException.cast("该房间已经被预定，请稍后");
                }
                return str;
            }
//            roomId更改
//           抢不到,去这一类中的房间再找（redis中找）--- id map《k ，v》
//            TODO , 类型一样就放一起
            //TODO: roomId = 666;
            roomId = getNextRoomId(roomId);
            if (roomId == null) {
                return ResultReturn.failure(0, "请重试");
            }
            count += 100;
        }
        return ResultReturn.failure(0, "请重试");
    }

    @Override
    public HotelAppointment getAccurateCheckInHotel(Integer id, LocalDate checkInDate, LocalDate checkOutDate) {
        LambdaQueryWrapper<HotelAppointment> queryWrapper = new LambdaQueryWrapper<HotelAppointment>().eq(HotelAppointment::getGuestId, id).eq(HotelAppointment::getCheckInDate, checkInDate).eq(HotelAppointment::getCheckOutDate, checkOutDate);
        return appointmentMapper.selectOne(queryWrapper);
    }

    @Transactional
    @Override
    public void updateDate(Integer roomId, LocalDate from, LocalDate to) {
        appointmentMapper.updateLivingDate(roomId , from , to);
    }

    @Autowired
    private GuestCardMapper guestCardMapper;
    @Override
    public PageResult<RoomAndAppointmentsDto> getAppointmentsByPage(PageParams pageParams, QueryAppointmentsDto appointmentsDto) {
        GuestCard guestCard = guestCardMapper.selectOne(new LambdaQueryWrapper<GuestCard>()
                .eq(appointmentsDto.getIdCard() != null,
                        GuestCard::getIdcard, appointmentsDto.getIdCard()));
        Integer id = guestCard.getId();
        assert id != null;
        IPage<HotelAppointment> page = new Page<>(pageParams.getPageNo() , pageParams.getPageSize());
        Wrapper<HotelAppointment> query = new LambdaQueryWrapper<HotelAppointment>()
                .eq(HotelAppointment::getRoomId , id)
                .gt(appointmentsDto.getCheckInDate()!=null, HotelAppointment::getCheckInDate , appointmentsDto.getCheckInDate())
                .lt( appointmentsDto.getCheckOutDate()!=null, HotelAppointment::getCheckOutDate , appointmentsDto.getCheckOutDate());
        IPage<HotelAppointment> appointments = appointmentMapper.selectPage(page ,query );
        List<HotelAppointment> records = appointments.getRecords();
        ArrayList<Integer> arr = new ArrayList<>();
        records.forEach(record -> arr.add(record.getRoomId()));
        List<Room> rooms = roomService.getBaseMapper().selectBatchIds(arr);
        RoomAndAppointmentsDto dto = new RoomAndAppointmentsDto(rooms, records);
        List<RoomAndAppointmentsDto> dtoArr = List.of(dto);
        return new PageResult<RoomAndAppointmentsDto>(dtoArr ,appointments.getTotal(),
                pageParams.getPageNo(),  pageParams.getPageSize());
    }


    private Integer getNextRoomId(Integer roomId) {
        String str = redisTemplate.opsForValue().get(String.valueOf(roomId));
        List arr = JSON.parseObject(str, List.class);
        assert arr != null;
        int index = arr.indexOf(roomId) + 1;
        for (int i = index; i < arr.size(); i++) {
            Room room = roomService.getById(i);
            if ("空闲".equals(room.getRoomStatus())) {
                return i;
            }
        }
        return null;
    }


}
