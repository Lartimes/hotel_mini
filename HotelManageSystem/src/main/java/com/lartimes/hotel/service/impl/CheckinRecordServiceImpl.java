package com.lartimes.hotel.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lartimes.hotel.common.*;
import com.lartimes.hotel.mapper.CheckinRecordMapper;
import com.lartimes.hotel.mapper.GuestStockchargeMapper;
import com.lartimes.hotel.mapper.PaymentMapper;
import com.lartimes.hotel.mapper.StockMapper;
import com.lartimes.hotel.model.dto.ReturnDepositDto;
import com.lartimes.hotel.model.po.*;
import com.lartimes.hotel.service.CheckinInfoService;
import com.lartimes.hotel.service.CheckinRecordService;
import com.lartimes.hotel.service.GuestCardService;
import com.lartimes.hotel.service.RoomService;
import com.lartimes.hotel.service.mq.MQProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
public class CheckinRecordServiceImpl extends ServiceImpl<CheckinRecordMapper, CheckinRecord> implements CheckinRecordService {

    private final CheckinRecordMapper checkinRecordMapper;


    private final GuestCardService guestCardService;
    private final StockMapper stockMapper;
    private final GuestStockchargeMapper stockchargeMapper;
    private final RoomService roomService;
    private final CheckinInfoService checkinInfoService;
    private final PaymentMapper paymentMapper;


    private final MQProducerService mqProducerService;

    public CheckinRecordServiceImpl(CheckinRecordMapper checkinRecordMapper, GuestCardService guestCardService, StockMapper stockMapper, GuestStockchargeMapper stockchargeMapper, RoomService roomService, CheckinInfoService checkinInfoService, PaymentMapper paymentMapper, MQProducerService mqProducerService) {
        this.checkinRecordMapper = checkinRecordMapper;
        this.guestCardService = guestCardService;
        this.stockMapper = stockMapper;
        this.stockchargeMapper = stockchargeMapper;
        this.roomService = roomService;
        this.checkinInfoService = checkinInfoService;
        this.paymentMapper = paymentMapper;
        this.mqProducerService = mqProducerService;
    }

    @Transactional
    @Override
    public Result checkOut(String idCard) {
        GuestCard guestCard = guestCardService.getOne(new LambdaQueryWrapper<GuestCard>().eq(GuestCard::getIdcard, idCard));
        Integer id = guestCard.getId();
        CheckinInfo nowLivingRoom = checkinInfoService.getNowLivingRoom(id);
        LocalDate checkoutDate = nowLivingRoom.getCheckoutDate();
        if (!checkoutDate.isEqual(LocalDate.now())) {
            return ResultReturn.failure(0, "");
        }
        Integer roomId = nowLivingRoom.getRoomId();
        Room room = roomService.getById(roomId);
        Double roomPrice = room.getRoomPrice();
        LocalDate checkinDate = nowLivingRoom.getCheckinDate();
//        guest_stockcharge
        int gap = checkoutDate.compareTo(checkinDate);
        List<GuestStockcharge> arr = stockchargeMapper.selectList(new LambdaQueryWrapper<GuestStockcharge>().eq(GuestStockcharge::getGuestId, id).eq(GuestStockcharge::getRoomId, roomId));
        AtomicReference<Double> temp = new AtomicReference<>((gap - 1.5) * roomPrice);
        arr.forEach(stockcharge -> {
            Integer nums = stockcharge.getNums();
            Integer stockId = stockcharge.getStockId();
            Stock stock = stockMapper.selectById(stockId);
            temp.updateAndGet(v -> v + nums * stock.getPrice());
        });
        CheckinRecord checkinRecord = new CheckinRecord(id, roomId, checkinDate, checkoutDate, temp.get(), "", null);
        Long l = checkinRecordMapper.selectCount(new LambdaQueryWrapper<CheckinRecord>().eq(CheckinRecord::getGuestId, id).eq(CheckinRecord::getRoomId, roomId));
        if (l > 0) {
            return ResultReturn.failure(0, "请于账单中支付");
        }
        checkinRecordMapper.insert(checkinRecord);
//        mq
        String key = "room:" + roomId + ":" + UUID.randomUUID();
        ReturnDepositDto dto = new ReturnDepositDto(key, temp.get(), List.of(id));
        Payment payment = new Payment(null, id, checkoutDate, temp.get(), "", "WeChat", "", "QR", dto.getKey());
        paymentMapper.insert(payment);
        mqProducerService.sendDelayMsg(JSON.toJSONString(dto), 4, "tag2");
        return ResultReturn.success(dto);
    }

    @Override
    public PageResult<CheckinRecord> getRecordsByPage(PageParams pageParams, Map<String, String> map) {
        String card = map.get("idCard");
        if (card == null) {
            HotelException.cast("请输入身份证查询");
        }
        GuestCard guestCard = guestCardService.getBaseMapper().selectOne(new LambdaQueryWrapper<GuestCard>().eq(GuestCard::getIdcard, card));
        Integer id = guestCard.getId();
        IPage<CheckinRecord> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        Wrapper<CheckinRecord> query = new LambdaQueryWrapper<CheckinRecord>().eq(CheckinRecord::getGuestId, id);
        IPage<CheckinRecord> records = checkinRecordMapper.selectPage(page, query);

        return new PageResult<CheckinRecord>(records.getRecords(), records.getTotal(), pageParams.getPageNo(), pageParams.getPageSize());
    }
}
