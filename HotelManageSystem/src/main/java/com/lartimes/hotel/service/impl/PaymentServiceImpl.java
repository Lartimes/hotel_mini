package com.lartimes.hotel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lartimes.hotel.common.HotelException;
import com.lartimes.hotel.common.Result;
import com.lartimes.hotel.common.ResultReturn;
import com.lartimes.hotel.mapper.GuestCardMapper;
import com.lartimes.hotel.mapper.GuestStockchargeMapper;
import com.lartimes.hotel.mapper.PaymentMapper;
import com.lartimes.hotel.mapper.StockMapper;
import com.lartimes.hotel.model.dto.ReturnDepositDto;
import com.lartimes.hotel.model.po.*;
import com.lartimes.hotel.service.CheckinInfoService;
import com.lartimes.hotel.service.PaymentService;
import com.lartimes.hotel.service.RoomService;
import com.lartimes.hotel.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements PaymentService {

    private final PaymentMapper paymentMapper;

    private final RoomService roomService;
    @Autowired

    private GuestCardMapper guestCardMapper;
    @Autowired
    private StockService service;
    @Autowired
    private CheckinInfoService checkinInfoService;
    @Autowired
    private GuestStockchargeMapper guestStockchargeMapper;
    @Autowired
    private StockMapper stockMapper;

    public PaymentServiceImpl(PaymentMapper paymentMapper, RoomService roomService) {
        this.paymentMapper = paymentMapper;
        this.roomService = roomService;
    }

    @Transactional
    @Override
    public Result doAppointPay(ReturnDepositDto depositDto, Integer guestId) {
        List<Integer> arr = depositDto.getArr();
        if (!arr.contains(guestId)) {
            return ResultReturn.failure(0, "您不是需付款人");
        }
//        行锁， 查询数据,如果已经支付，就return 已经支付
//        这里不做支付模块
//        不需要并发
        Payment payment = new Payment(null, guestId, LocalDate.now(), depositDto.getDeposit(), "已经支付", "WeChat", "", "QR", depositDto.getKey());
        Integer isPayed = paymentMapper.selectForUpdate(depositDto.getKey());
        if (isPayed == 1) {
            return ResultReturn.success("已经支付");
        }
        paymentMapper.insert(payment);
//修改订单信息
        int pk = Integer.parseInt(depositDto.getKey().split(":")[1]);
        roomService.modifyStatus(pk, "已预定");
        return ResultReturn.success("支付成功");
    }

    @Transactional
    @Override
    public Result doStockPay(String idCard, Integer stockId, Integer nums) {
        GuestCard guestCard = guestCardMapper.selectOne(new LambdaQueryWrapper<GuestCard>().eq(GuestCard::getIdcard, idCard));
        Integer id = guestCard.getId();
        CheckinInfo nowLivingRoom = checkinInfoService.getNowLivingRoom(id);
        Integer roomId = nowLivingRoom.getRoomId();
        GuestStockcharge stockCharge = new GuestStockcharge(roomId, stockId, nums, 0, "未支付", id, null);
        guestStockchargeMapper.insert(stockCharge);
        Stock stock = stockMapper.selectById(stockId);
        if (stock.getTotalNum() - stock.getUsingNum() < nums) {
            HotelException.cast("该物品库存不足");
        }
        Double price = stock.getPrice() * nums;
        stock.setUsingNum(stock.getUsingNum() + nums);
        stockMapper.updateById(stock);
        Payment payment = new Payment(null , id , LocalDate.now() , price ,
                "已支付" , "WeChat" , "" , "消费品" , UUID.randomUUID().toString());
        paymentMapper.insert(payment);
        return ResultReturn.success();
    }


}
