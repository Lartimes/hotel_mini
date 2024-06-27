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
@TableName("stock")
public class Stock implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 物品id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 名字
     */
    private String name;

    /**
     * 价格
     */
    private Double price;

    /**
     * 总数量
     */
    private Integer totalNum;

    /**
     * 进价
     */
    private Double orginPrice;

    /**
     * 正在使用数量
     */
    private Integer usingNum;


}
