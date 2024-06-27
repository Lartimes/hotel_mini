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
public class CheckinInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer guestId;

    private Integer roomId;

    private LocalDate checkoutDate;

    private LocalDate checkinDate;

    private String status;

    private String isPay;

    private Integer id;


}
