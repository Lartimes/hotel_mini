package com.lartimes.hotel.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 
 * </p>
 *
 * @author itcast
 */
@Data
public class CheckinRecordDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer guestId;

    private Integer roomId;

    private LocalDate checkinDate;

    private LocalDate checkoutDate;

    private Double payment;

    private String remark;

    private Integer id;


}
