package com.lartimes.hotel.controller;

import com.lartimes.hotel.common.PageParams;
import com.lartimes.hotel.common.PageResult;
import com.lartimes.hotel.model.dto.GuestDTO;
import com.lartimes.hotel.model.dto.QueryAppointmentsDto;
import com.lartimes.hotel.model.dto.RoomAndAppointmentsDto;
import com.lartimes.hotel.service.HotelAppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author itcast
 */
@Slf4j
@RestController
@RequestMapping("appoint")
@Tag(name = "HotelAppointmentController", description = "预约Room-Controller")
public class HotelAppointmentController {

    private final HotelAppointmentService hotelAppointmentService;

    public HotelAppointmentController(HotelAppointmentService hotelAppointmentService) {
        this.hotelAppointmentService = hotelAppointmentService;
    }


    @Operation(summary = "酒店预约", parameters = {@Parameter(name = "guests", description = "预定"), @Parameter(name = "roomId", description = "预定房间ID"), @Parameter(name = "checkIn", description = "入住日期"), @Parameter(name = "checkOut", description = "离店日期")})
    @RequestMapping("/appointment/{key}/{checkIn}/{checkout}")
    @ResponseBody
    public Object login(@RequestBody GuestDTO[] guests, @PathVariable(value = "key") Integer roomId, @PathVariable("checkIn") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkIn, @PathVariable("checkout") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkOut) {
        return hotelAppointmentService.appointedHotel(guests, roomId, checkIn, checkOut);
    }


    @Operation(summary = "分页查询")
    @PostMapping("/pages/rooms")
    public PageResult<RoomAndAppointmentsDto> getRoomsByPage(PageParams pageParams, @RequestBody(required = false) QueryAppointmentsDto appointmentsDto) {

        return hotelAppointmentService.getAppointmentsByPage(pageParams, appointmentsDto);
    }


}
