package com.lartimes.hotel.controller;

import com.lartimes.hotel.common.PageParams;
import com.lartimes.hotel.common.PageResult;
import com.lartimes.hotel.common.Result;
import com.lartimes.hotel.model.po.CheckinRecord;
import com.lartimes.hotel.service.CheckinRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("checkinRecord")
@Tag(name = "CheckinRecordController", description = "办理退房Controller")
public class CheckinRecordController {

    @Autowired
    private CheckinRecordService checkinRecordService;


    @Operation(summary = "办理酒店退房")
    @PostMapping("/checkout.do")
//    idcard
    public Result checkout(@RequestBody Map<String ,String >  data) {
        String idCard = data.get("idCard");
        return checkinRecordService.checkOut(idCard);
    }


    @Operation(summary = "分页查询" )
    @PostMapping("/pages/records")
    public PageResult<CheckinRecord> getEmpsByPage(PageParams pageParams ,
                                                   @RequestBody(required = false)
                                              Map<String , String> map){
//idcard
        return checkinRecordService.getRecordsByPage(pageParams ,map );
    }


}
