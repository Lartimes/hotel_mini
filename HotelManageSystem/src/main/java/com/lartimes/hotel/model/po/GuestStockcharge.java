package com.lartimes.hotel.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author itcast
 */
@Data
@TableName("guest_stockcharge")
@AllArgsConstructor
@NoArgsConstructor
public class GuestStockcharge implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 客房id
     */
    private Integer roomId;

    /**
     * 物品id
     */
    private Integer stockId;

    /**
     * 消费数量
     */
    private Integer nums;

    /**
     * 坏的数量
     */
    private Integer failCount;

    /**
     * 是否支付
     */
    private String isPay;

    /**
     * 客人id
     */
    private Integer guestId;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


}
