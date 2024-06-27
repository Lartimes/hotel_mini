package com.lartimes.hotel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lartimes.hotel.common.Result;
import com.lartimes.hotel.model.dto.ReturnDepositDto;
import com.lartimes.hotel.model.po.Payment;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author itcast
 * @since 2024-06-18
 */
public interface PaymentService extends IService<Payment> {


    Result doAppointPay(ReturnDepositDto returnDepositDto, Integer guestId);

    Result doStockPay(String idCard, Integer stockId, Integer nums);
}
