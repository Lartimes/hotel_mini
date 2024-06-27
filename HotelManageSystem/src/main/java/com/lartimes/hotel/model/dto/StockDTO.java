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
public class StockDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private Double price;

    private Integer totalNum;

    private Double orginPrice;

    private Integer usingNum;


}
