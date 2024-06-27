package com.lartimes.hotel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lartimes.hotel.common.PageParams;
import com.lartimes.hotel.common.PageResult;
import com.lartimes.hotel.model.dto.StockDTO;
import com.lartimes.hotel.model.po.Stock;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author itcast
 * @since 2024-06-18
 */
public interface StockService extends IService<Stock> {

    PageResult<Stock> selectStocksByPage(PageParams pageParams, StockDTO stockDTO);

    void insertList(List<Stock> excelInfo);
}
