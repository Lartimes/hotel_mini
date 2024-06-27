package com.lartimes.hotel.model.dto;

import com.lartimes.hotel.model.po.HotelAppointment;
import com.lartimes.hotel.model.po.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/6/27 16:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomAndAppointmentsDto {
    private List<Room> rooms;
    private List<HotelAppointment> appointments;
}
