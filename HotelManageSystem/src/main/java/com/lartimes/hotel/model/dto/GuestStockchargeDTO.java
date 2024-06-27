package com.lartimes.hotel.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author itcast
 */
@Data
public class GuestStockchargeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer roomId;

    private Integer stockId;

    private Integer nums;

    private Integer failCount;

    private String isPay;

    private Integer guestId;

    private Integer id;


}
