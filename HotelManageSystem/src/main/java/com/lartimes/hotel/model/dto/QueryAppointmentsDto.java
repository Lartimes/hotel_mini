package com.lartimes.hotel.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/6/27 12:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryAppointmentsDto {

    private String idCard;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
