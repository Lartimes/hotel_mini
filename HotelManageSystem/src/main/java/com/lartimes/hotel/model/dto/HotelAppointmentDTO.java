package com.lartimes.hotel.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 
 * </p>
 *
 * @author itcast
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelAppointmentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer roomId;

    private Integer guestId;

    private Integer id;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private String remark;

    private Double deposit;


}
