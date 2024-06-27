package com.lartimes.hotel.service.mq;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lartimes.hotel.model.dto.ReturnDepositDto;
import com.lartimes.hotel.model.po.Payment;
import com.lartimes.hotel.model.po.Room;
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
 * @since 2024/6/24 17:18
 */
@RocketMQMessageListener(consumerGroup = "APPOINTMENT_CONSUMER_GROUP",
        topic = "HOTEL_APPOINTMENT_TOPIC",
        consumeMode = ConsumeMode.ORDERLY, maxReconsumeTimes = 3
        , selectorExpression = "tag1")
@Slf4j
@Component
public class MQPaymentConsumer implements RocketMQListener<MessageExt> {
    private final PaymentService paymentService;
    private final RoomService roomService;
    private final HotelAppointmentService hotelAppointmentService;

    public MQPaymentConsumer(PaymentService paymentService, RoomService roomService, HotelAppointmentService hotelAppointmentService) {
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
            return;
        }
        int roomId = Integer.parseInt(payment.getPaymentId().split(":")[1]);
        Room room = roomService.getById(roomId);
        room.setRoomStatus("空闲");
        roomService.updateById(room);
        log.debug("已经完成支付 ， 支付订单编号为:{} ", key);
    }
}
