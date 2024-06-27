package com.lartimes.hotel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lartimes.hotel.common.PageParams;
import com.lartimes.hotel.common.PageResult;
import com.lartimes.hotel.model.dto.QueryRoomsDto;
import com.lartimes.hotel.model.po.Room;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author itcast
 * @since 2024-06-18
 */
public interface RoomService extends IService<Room> {

    /**
     * 修改房间状态
     * @param pk roomId
     * @param status 状态
     */
    void modifyStatus(int pk, String status);


    /**
     * 对所有room 进行分组
     * @return
     */
    List<List<Integer>> groupByHash();


    /**
     * 分页查询Room
     * @param pageParams
     * @param roomsDto
     * @return
     */
    PageResult<Room> selectRoomsByPage(PageParams pageParams, QueryRoomsDto roomsDto);


    /**
     * 进行更新和插入
     * @param excelInfo
     */
    void insertList(List<Room> excelInfo);

}
