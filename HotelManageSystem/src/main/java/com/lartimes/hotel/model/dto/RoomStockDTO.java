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
public class RoomStockDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer roomId;

    private Integer stockId;

    private Integer num;

    private String status;

    private Integer failNum;

    private String remark;


}
