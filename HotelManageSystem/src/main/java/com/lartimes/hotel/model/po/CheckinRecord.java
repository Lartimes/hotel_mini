package com.lartimes.hotel.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@TableName("checkin_record")
@AllArgsConstructor
@NoArgsConstructor
public class CheckinRecord implements Serializable {

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
     * 入住日期
     */
    private LocalDate checkinDate;

    /**
     * 离店日期
     */
    private LocalDate checkoutDate;

    /**
     * 消费总金额
     */
    private Double payment;

    /**
     * 备注
     */
    private String remark;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


}
