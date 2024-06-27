package com.lartimes.hotel.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * <p>
 * 
 * </p>
 *
 * @author itcast
 */
@Data
@TableName("room")
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 客房id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 客房类型
     */
    private String roomType;

    /**
     * 客房状态
     */
    private String roomStatus;

    /**
     * 客房价格
     */
    private Double roomPrice;

    /**
     * 客房几层
     */
    private String roomFloor;

    /**
     * 位置
     */
    private String roomPosition;

    /**
     * 面积
     */
    private Integer roomArea;

    /**
     * 床数量
     */
    private Integer bedNums;

    /**
     * 最多人数
     */
    private Integer peopleNums;

    /**
     * 备注
     */
    private String remark;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Room room = (Room) o;
        if (!Objects.equals(roomType, room.roomType)) return false;
        if (!Objects.equals(roomPrice, room.roomPrice)) return false;
        if (!Objects.equals(roomPosition, room.roomPosition)) return false;
        if (!Objects.equals(roomArea, room.roomArea)) return false;
        if (!Objects.equals(bedNums, room.bedNums)) return false;
        return Objects.equals(peopleNums, room.peopleNums);
    }

    @Override
    public int hashCode() {
        int result = roomType != null ? roomType.hashCode() : 0;
        result = 31 * result + (roomPrice != null ? roomPrice.hashCode() : 0);
        result = 31 * result + (roomPosition != null ? roomPosition.hashCode() : 0);
        result = 31 * result + (roomArea != null ? roomArea.hashCode() : 0);
        result = 31 * result + (bedNums != null ? bedNums.hashCode() : 0);
        result = 31 * result + (peopleNums != null ? peopleNums.hashCode() : 0);
        return result;
    }
}
