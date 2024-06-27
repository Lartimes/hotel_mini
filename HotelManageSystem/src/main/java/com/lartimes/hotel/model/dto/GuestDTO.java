package com.lartimes.hotel.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lartimes.hotel.model.dto.GuestCardDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author itcast
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuestDTO  extends GuestCardDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 客人id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private String sex;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 工作类型（学生...）
     */
    private String workUnit;

    /**
     * 电话号码
     */
    private String phoneNumber;

}
