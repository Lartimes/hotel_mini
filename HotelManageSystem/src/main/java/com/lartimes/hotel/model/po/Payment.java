package com.lartimes.hotel.model.po;

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
@TableName("payment")
@AllArgsConstructor
@NoArgsConstructor
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer payId;

    /**
     * 客人id
     */
    private Integer guestId;

    /**
     * 消费日期
     */
    private LocalDate paymentDate;

    /**
     * 消费金额
     */
    private Double paymentCost;

    /**
     * 状态
     */
    private String status;

    /**
     * 消费方式
     */
    private String payMethod;

    /**
     * 备注
     */
    private String remark;

    /**
     * 消费类型
     */
    private String paymentType;


    private String paymentId;

}
