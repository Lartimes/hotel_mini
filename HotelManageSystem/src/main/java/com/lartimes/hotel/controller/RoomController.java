package com.lartimes.hotel.controller;

import com.lartimes.hotel.common.ImportXlsStrategy;
import com.lartimes.hotel.common.PageParams;
import com.lartimes.hotel.common.PageResult;
import com.lartimes.hotel.common.ReadPatientExcelUtil;
import com.lartimes.hotel.model.dto.QueryRoomsDto;
import com.lartimes.hotel.model.po.Room;
import com.lartimes.hotel.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("room")
@Tag(name = "RoomController", description = "Room Controller")
public class RoomController {

    private final RoomService roomService;
    private final ReadPatientExcelUtil<Room> excelUtil;

    public RoomController(RoomService roomService, ReadPatientExcelUtil<Room> excelUtil) {
        this.roomService = roomService;
        this.excelUtil = excelUtil;
    }

    @Operation(summary = "分页查询")
    @PostMapping("/pages/emp")
    public PageResult<Room> getRoomsByPage(PageParams pageParams, @RequestBody(required = false) QueryRoomsDto roomsDto) {
        return roomService.selectRoomsByPage(pageParams, roomsDto);
    }

    @PostMapping("/readxls/rooms")
    @Operation(summary = "导入Excel")
    public List<Room> importRooms(@RequestParam("multipartFile") MultipartFile multipartFile) {
        List<Room> excelInfo = excelUtil.getExcelInfo(multipartFile, ImportXlsStrategy.ROOM);
        if(excelInfo.isEmpty()){
            return excelInfo;
        }
        excelInfo.forEach(System.out::println);
        roomService.insertList(excelInfo);
        return excelInfo;
    }


}
