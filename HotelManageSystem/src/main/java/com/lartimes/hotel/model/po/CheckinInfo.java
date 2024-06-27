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
@TableName("checkin_info")
public class CheckinInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 客人id
     */
    private Integer guestId;

    /**
     * 客房id
     */
    private Integer roomId;

    /**
     * 离店日期
     */
    private LocalDate checkoutDate;

    /**
     * 入住日期
     */
    private LocalDate checkinDate;

    /**
     * 入住状态
     */
    private String status;

    /**
     * 是否已支付
     */
    private String isPay;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


}
