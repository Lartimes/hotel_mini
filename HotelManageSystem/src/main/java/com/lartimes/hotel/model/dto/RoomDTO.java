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
public class RoomDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String roomType;

    private String roomStatus;

    private Double roomPrice;

    private String roomFloor;

    private String roomPosition;

    private Integer roomArea;

    private Integer bedNums;

    private Integer peopleNums;

    private String remark;


}
