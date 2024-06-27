package com.lartimes.hotel.controller;

import com.lartimes.hotel.common.ImportXlsStrategy;
import com.lartimes.hotel.common.PageParams;
import com.lartimes.hotel.common.PageResult;
import com.lartimes.hotel.common.ReadPatientExcelUtil;
import com.lartimes.hotel.model.dto.StockDTO;
import com.lartimes.hotel.model.po.Stock;
import com.lartimes.hotel.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("stock")
@Tag(name = "StockController", description = "stock Controller")
public class StockController {

    private final StockService stockService;
    private final ReadPatientExcelUtil<Stock> excelUtil;

//    物品


    public StockController(StockService stockService, ReadPatientExcelUtil<Stock> excelUtil) {
        this.stockService = stockService;
        this.excelUtil = excelUtil;
    }

    @Operation(summary = "分页查询")
    @PostMapping("/pages/stocks")
    public PageResult<Stock> getRoomsByPage(PageParams pageParams, @RequestBody(required = false) StockDTO stockDTO) {
        return stockService.selectStocksByPage(pageParams, stockDTO);
    }

    @PostMapping("/readxls/stocks")
    public List<Stock> importRooms(@RequestParam("multipartFile") MultipartFile multipartFile) {
        List<Stock> excelInfo = excelUtil.getExcelInfo(multipartFile, ImportXlsStrategy.STOCK);
        if(excelInfo.isEmpty()){
            return excelInfo;
        }
        excelInfo.forEach(System.out::println);
        stockService.insertList(excelInfo);
        return excelInfo;
    }
}
