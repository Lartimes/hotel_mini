package com.lartimes.hotel.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/6/27 10:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryEmployeeDto {

    private String name;
    private String workPosition;

}
