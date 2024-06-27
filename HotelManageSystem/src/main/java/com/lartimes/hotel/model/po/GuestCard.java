package com.lartimes.hotel.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author itcast
 */
@Data
@Setter
@Getter
@TableName("guest_card")
public class GuestCard implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 客人id
     */
    private Integer id;

    /**
     * 身份证
     */
    private String idcard;

    /**
     *  身份状态
     */
    private String status;


}
