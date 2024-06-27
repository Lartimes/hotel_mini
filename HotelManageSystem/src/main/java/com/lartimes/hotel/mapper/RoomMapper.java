package com.lartimes.hotel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lartimes.hotel.model.po.Room;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RoomMapper extends BaseMapper<Room> {


    void changeStatus(@Param("pk") int pk, @Param("status") String status);
}
