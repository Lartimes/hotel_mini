package com.lartimes.hotel.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("guest")
public class Guest implements Serializable {

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
