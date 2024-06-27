package com.lartimes.hotel.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/6/19 10:36
 */
@Data
@TableName("admin_user")
public class AdminUser {
    @TableId(value = "id" , type = IdType.AUTO)
    private Integer id;
    private String name;
    private String password;
    private Integer empId;

    public String getAccountName() {
        return this.getName();
    }



}
