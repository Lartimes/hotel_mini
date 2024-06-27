package com.lartimes.hotel.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/6/21 20:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnDepositDto {
    private String key ;
    private  Double deposit ;
    private List<Integer> arr;
}
