package com.lartimes.hotel.controller;

import com.lartimes.hotel.common.Result;
import com.lartimes.hotel.service.CheckinInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("checkinInfo")
@Tag(name = "CheckinInfoController", description = "入住controller")
public class CheckinInfoController {

    @Autowired
    private CheckinInfoService checkinInfoService;


    @Operation(summary = "办理酒店入住")
    @PostMapping("/checkIn.do")
    public Result checkIn(@RequestBody Map<String, String> data) {
        return checkinInfoService.hotelCheckIn(data);
    }

    @Operation(summary = "更改酒店入住期限")
    @GetMapping("/{roomId}/{checkIn}/{checkout}")
    public Result change(@PathVariable(value = "roomId") Integer roomId, @PathVariable("checkIn") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkIn, @PathVariable("checkout") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkOut) {
        return checkinInfoService.modifyLivingDate(roomId, checkIn, checkOut);
    }


}
