package com.lartimes.hotel.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/6/19 14:32
 */
@Data
public class AdminUserDto  implements Serializable {
    private String name;
    private String password;
}
