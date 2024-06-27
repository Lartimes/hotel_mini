package com.lartimes.hotel.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("hotel_appointment")
public class HotelAppointment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 客房id
     */
    private Integer roomId;

    /**
     * 客人id
     */
    private Integer guestId;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 入住日期
     */
    private LocalDate checkInDate;

    /**
     * 离店日期
     */
    private LocalDate checkOutDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 押金
     */
    private Double deposit;



}
