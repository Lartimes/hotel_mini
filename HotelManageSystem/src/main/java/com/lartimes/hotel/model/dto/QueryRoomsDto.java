package com.lartimes.hotel.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/6/27 11:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryRoomsDto {

    private String roomStatus;
    private String roomType;
    private Double roomPrice;
    private Integer roomArea;
    private Integer peopleNums;
    private Integer bedNums;
}
