package com.lartimes.hotel.service.impl;

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lartimes.hotel.common.PageParams;
import com.lartimes.hotel.common.PageResult;
import com.lartimes.hotel.mapper.RoomMapper;
import com.lartimes.hotel.model.dto.QueryRoomsDto;
import com.lartimes.hotel.model.po.Room;
import com.lartimes.hotel.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 */
@Slf4j
@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements RoomService {
    private final RoomMapper roomMapper;
    private final SqlSessionFactory sqlSessionFactory;

    public RoomServiceImpl(RoomMapper roomMapper, SqlSessionFactory sqlSessionFactory) {
        this.roomMapper = roomMapper;
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public void modifyStatus(int pk, String status) {
        roomMapper.changeStatus(pk, status);
    }

    @Override
    public List<List<Integer>> groupByHash() {
        List<Room> rooms = roomMapper.selectList(new LambdaQueryWrapper<Room>().eq(Room::getRoomStatus, "空闲"));
        HashMap<Integer, List<Integer>> map = new HashMap<>();
        rooms.forEach(room -> {
            int key = Math.abs(room.hashCode());
            if (map.containsKey(key)) {
                List<Integer> list = new ArrayList<>(map.get(key).stream().toList());
                list.add(room.getId());
                map.put(key, list);
            } else {
                map.put(key, List.of(room.getId()));
            }
        });
        return map.values().stream().toList();
    }

    @Override
    public PageResult<Room> selectRoomsByPage(PageParams pageParams, QueryRoomsDto roomsDto) {
        IPage<Room> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        Wrapper<Room> query = new LambdaQueryWrapper<Room>().eq(StringUtils.isNotBlank(roomsDto.getRoomStatus()), Room::getRoomStatus, roomsDto.getRoomStatus()).eq(StringUtils.isNotBlank(roomsDto.getRoomType()), Room::getRoomType, roomsDto.getRoomType()).gt(roomsDto.getBedNums() != null, Room::getBedNums, roomsDto.getBedNums()).gt(roomsDto.getPeopleNums() != null, Room::getPeopleNums, roomsDto.getPeopleNums()).gt(roomsDto.getRoomArea() != null, Room::getRoomArea, roomsDto.getRoomArea()).gt(roomsDto.getRoomPrice() != null, Room::getRoomPrice, roomsDto.getRoomPrice());
        IPage<Room> roomPage = roomMapper.selectPage(page, query);
        return new PageResult<>(roomPage.getRecords(), roomPage.getTotal(), pageParams.getPageNo(), pageParams.getPageSize());
    }

    @Transactional
    @Override
    public void insertList(List<Room> excelInfo) {
        ArrayList<Room> insertingRooms = new ArrayList<>();
        excelInfo.forEach(room -> {
            Integer id = room.getId();
            Room oldRoom = roomMapper.selectById(id);
            if (oldRoom != null) {
                roomMapper.updateById(room);
            } else {
                insertingRooms.add(room);
            }
        });
        MybatisBatch<Room> mybatisBatch = new MybatisBatch<>(sqlSessionFactory, insertingRooms);
        MybatisBatch.Method<Room> method = new MybatisBatch.Method<>(RoomMapper.class);
        mybatisBatch.execute(method.insert());
    }
}
