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
@TableName("room_stock")
public class RoomStock implements Serializable {

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
    private Integer num;

    /**
     * 坏的数量
     */
    private Integer failNum;

    /**
     * 备注
     */
    private String remark;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


}
