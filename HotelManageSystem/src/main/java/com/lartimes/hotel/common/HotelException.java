package com.lartimes.hotel.common;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/6/19 13:07
 */
public class HotelException {
    public static void cast(String errMsg){
        throw new RuntimeException(errMsg);
    }
}
