package com.lartimes.hotel.controller;

import com.lartimes.hotel.common.Result;
import com.lartimes.hotel.common.ResultReturn;
import com.lartimes.hotel.model.dto.ReturnDepositDto;
import com.lartimes.hotel.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("payment")
@Tag(name = "PaymentController", description = "支付Controller")
public class PaymentController {

    private final PaymentService paymentService;


    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Operation(summary = "酒店押金预定支付")
    @PostMapping("/appointPay.do")
    @ResponseBody
    public Result appointDepositPay(@RequestBody ReturnDepositDto returnDepositDto, HttpServletRequest request) {
        String header = request.getHeader("payer");
        if (header == null) {
            return ResultReturn.failure(0, "获取信息失败，请重试");
        }
        Integer guestId = Integer.parseInt(header);
        return paymentService.doAppointPay(returnDepositDto, guestId);
    }


    /**
     * idcard ，stockId ， nums ,
     *
     * @return
     */
    @Operation(summary = "进行消费")
    @PostMapping("/{stockId}/{nums}")
    public Result stockPay(@RequestBody Map<String, String> map, @PathVariable("stockId") Integer stockId, @PathVariable("nums") Integer nums) {
        String idCard = map.get("idCard");
        return paymentService.doStockPay(idCard, stockId, nums);
    }
}
