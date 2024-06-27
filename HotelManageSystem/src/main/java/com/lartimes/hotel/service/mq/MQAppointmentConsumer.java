package com.lartimes.hotel.service.mq;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lartimes.hotel.model.dto.ReturnDepositDto;
import com.lartimes.hotel.model.po.HotelAppointment;
import com.lartimes.hotel.model.po.Payment;
import com.lartimes.hotel.service.HotelAppointmentService;
import com.lartimes.hotel.service.PaymentService;
import com.lartimes.hotel.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/6/21 11:34
 */

@RocketMQMessageListener(consumerGroup = "APPOINTMENT_CONSUMER_GROUP",
        topic = "HOTEL_APPOINTMENT_TOPIC",
        consumeMode = ConsumeMode.ORDERLY, maxReconsumeTimes = 3
        , selectorExpression = "tag1")
@Slf4j
@Component
public class MQAppointmentConsumer implements RocketMQListener<MessageExt> {
    private final PaymentService paymentService;
    private final RoomService roomService;
    private final HotelAppointmentService hotelAppointmentService;

    public MQAppointmentConsumer(PaymentService paymentService, RoomService roomService, HotelAppointmentService hotelAppointmentService) {
        this.paymentService = paymentService;
        this.roomService = roomService;
        this.hotelAppointmentService = hotelAppointmentService;
    }

    @Override
    public void onMessage(MessageExt messageExt) {
        log.info("监听到消息：str={}", messageExt);
        ReturnDepositDto returnDepositDto = JSON.parseObject(messageExt.getBody(), ReturnDepositDto.class);
        System.out.println(returnDepositDto);
        Double deposit = returnDepositDto.getDeposit();
        String key = returnDepositDto.getKey();
        Payment payment = paymentService.getOne(new LambdaQueryWrapper<Payment>().eq(Payment::getPaymentId, key).eq(Payment::getPaymentCost, deposit));
        if (payment == null) {
//            修改redis类型组
//                修改房间状态 ， 删除
            int pk = Integer.parseInt(key.split(":")[1]);
            System.out.println(pk);
            roomService.modifyStatus(pk, "空闲");
            hotelAppointmentService.remove(new LambdaQueryWrapper<HotelAppointment>().eq(HotelAppointment::getRoomId, pk));
            log.debug("时间内未完成支付，预约失败");
            return;
        }

        log.debug("已经完成支付 ， 支付订单编号为:{} ", key);
//                TODO: 一上线进行统一分组
//                修改redis类型组
    }
}
