package com.lartimes.hotel.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author itcast
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuestCardDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String idcard;

    private String status;


}
