package com.lartimes.hotel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lartimes.hotel.model.po.HotelAppointment;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

public interface HotelAppointmentMapper extends BaseMapper<HotelAppointment> {


    void updateLivingDate(@Param("roomId") Integer roomId,
                          @Param("from")LocalDate from,
                          @Param("to")LocalDate to);
}
