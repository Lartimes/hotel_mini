package com.lartimes.hotel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lartimes.hotel.common.Result;
import com.lartimes.hotel.common.ResultReturn;
import com.lartimes.hotel.mapper.CheckinInfoMapper;
import com.lartimes.hotel.model.po.*;
import com.lartimes.hotel.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 */
@Slf4j
@Service
public class CheckinInfoServiceImpl extends ServiceImpl<CheckinInfoMapper, CheckinInfo> implements CheckinInfoService {

    final Converter<String, LocalDate> localDateConverter;
    private final CheckinInfoMapper checkinInfoMapper;
    private final GuestCardService guestCardService;
    private final HotelAppointmentService appointmentService;
    private final RoomService roomService;
    private final GuestService guestService;



    public CheckinInfoServiceImpl(Converter<String, LocalDate>
                                          localDateConverter,
                                  CheckinInfoMapper checkinInfoMapper,
                                  GuestCardService guestCardService, HotelAppointmentService appointmentService, RoomService roomService, GuestService guestService) {
        this.localDateConverter = localDateConverter;
        this.checkinInfoMapper = checkinInfoMapper;
        this.guestCardService = guestCardService;
        this.appointmentService = appointmentService;
        this.roomService = roomService;
        this.guestService = guestService;
    }

    @Transactional
    @Override
    public Result hotelCheckIn(Map<String, String> data) {
        String idCard = data.get("idcard");
        LocalDate checkInDate = localDateConverter.convert(data.get("checkInDate"));
        LocalDate checkOutDate = localDateConverter.convert(data.get("checkOutDate"));
        GuestCard guestCard = guestCardService.getOne(new LambdaQueryWrapper<>(GuestCard.class).eq(GuestCard::getIdcard, idCard));
        HotelAppointment appointment = appointmentService.getAccurateCheckInHotel(guestCard.getId(), checkInDate, checkOutDate);
        if (appointment == null) {
            return ResultReturn.failure(0, "您未预约");
        }
        Integer roomId = appointment.getRoomId();
        String roomStatus = roomService.getById(roomId).getRoomStatus();
        if (!"已预定".equals(roomStatus)) {
            return ResultReturn.failure(0, "您未付押金");
        }
        Long l = checkinInfoMapper.selectCount(new LambdaQueryWrapper<CheckinInfo>().eq(CheckinInfo::getGuestId, guestCard.getId()).eq(CheckinInfo::getRoomId, roomId));
        if (l > 0) {
            return ResultReturn.success();
        }
        CheckinInfo checkinInfo = new CheckinInfo();
        checkinInfo.setRoomId(roomId);
        checkinInfo.setCheckinDate(checkInDate);
        checkinInfo.setCheckoutDate(checkOutDate);
        checkinInfo.setGuestId(guestCard.getId());
        checkinInfo.setStatus("已入住");
        checkinInfoMapper.insert(checkinInfo);
        return ResultReturn.success();
    }

    @Override
    public Result broadCastToGuests() {
        record GuestInform(Integer roomNumber, String guestName, String roomPosition, String roomFloor,
                           String roomType) {
        }
        ArrayList<GuestInform> arr = new ArrayList<>();
        LambdaQueryWrapper<CheckinInfo> query = new LambdaQueryWrapper<CheckinInfo>().eq(CheckinInfo::getCheckoutDate, LocalDate.now());
        List<CheckinInfo> checkinInfos = checkinInfoMapper.selectList(query);
        checkinInfos.forEach(checkinInfo -> {
            Integer guestId = checkinInfo.getGuestId();
            Integer roomId = checkinInfo.getRoomId();
            Room room = roomService.getById(roomId);
            Guest guest = guestService.getById(guestId);
//           TODO 短信通知或者app里面通知 。 这里不写具体步骤模拟
            String roomPosition = room.getRoomPosition();
            String roomFloor = room.getRoomFloor();
            String roomType = room.getRoomType();
            Integer id = room.getId();
            String name = guest.getName();
            GuestInform guestInform = new GuestInform(id, name, roomPosition, roomFloor, roomType);
            arr.add(guestInform);
        });
        return ResultReturn.success(arr);
    }

    @Transactional
    @Override
    public Result modifyLivingDate(Integer roomId, LocalDate from, LocalDate to) {
        appointmentService.updateDate(roomId, from, to);
//            appoint
        checkinInfoMapper.updateDate(roomId, from, to);
//            checkininfo
        return ResultReturn.success();
    }

    @Override
    public CheckinInfo getNowLivingRoom(Integer id) {
        CheckinInfo checkinInfo = checkinInfoMapper.selectOne(new LambdaQueryWrapper<CheckinInfo>().eq(CheckinInfo::getGuestId, id));
        return checkinInfo;
    }


}
