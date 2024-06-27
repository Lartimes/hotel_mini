package com.lartimes.hotel;

import com.lartimes.hotel.common.ImportXlsStrategy;
import com.lartimes.hotel.common.ReadPatientExcelUtil;
import com.lartimes.hotel.model.po.Room;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/6/27 17:16
 */
@RestController
public class TestController {


    private final ReadPatientExcelUtil<Room> excelUtil;

    public TestController(ReadPatientExcelUtil<Room> excelUtil) {
        this.excelUtil = excelUtil;
    }

    @PostMapping("/readxls/rooms")
    public List<Room> importRooms(@RequestParam("multipartFile") MultipartFile multipartFile) {
        List<Room> excelInfo = excelUtil.getExcelInfo(multipartFile, ImportXlsStrategy.ROOM);
        excelInfo.forEach(System.out::println);
        return excelInfo;
    }
}
