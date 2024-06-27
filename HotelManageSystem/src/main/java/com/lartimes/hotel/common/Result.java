package com.lartimes.hotel.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/6/19 10:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private int code;
    private String result;
    private T t;

    public Result(int ok, String s) {
        this.code = ok;
        this.result = s;
    }
}
