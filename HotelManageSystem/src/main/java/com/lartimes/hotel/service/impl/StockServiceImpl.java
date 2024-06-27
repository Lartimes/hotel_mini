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
import com.lartimes.hotel.mapper.StockMapper;
import com.lartimes.hotel.model.dto.StockDTO;
import com.lartimes.hotel.model.po.Stock;
import com.lartimes.hotel.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements StockService {


    private final StockMapper stockMapper;
    private final SqlSessionFactory sqlSessionFactory;

    public StockServiceImpl(StockMapper stockMapper, SqlSessionFactory sqlSessionFactory) {
        this.stockMapper = stockMapper;
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public PageResult<Stock> selectStocksByPage(PageParams pageParams, StockDTO stockDTO) {
        IPage<Stock> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        Wrapper<Stock> query = new LambdaQueryWrapper<Stock>().like(StringUtils.isNotBlank(stockDTO.getName()), Stock::getName, stockDTO.getName()).gt(stockDTO.getPrice() != null, Stock::getPrice, stockDTO.getPrice());
        IPage<Stock> stocks = stockMapper.selectPage(page, query);
        return new PageResult<Stock>(stocks.getRecords(), stocks.getTotal(), pageParams.getPageNo(), pageParams.getPageSize());

    }

    @Transactional
    @Override
    public void insertList(List<Stock> excelInfo) {
        ArrayList<Stock> insertingStocks = new ArrayList<>();
        excelInfo.forEach(stock -> {
            Integer id = stock.getId();
            Stock oldStock = stockMapper.selectById(id);
            if (oldStock != null) {
                stockMapper.updateById(stock);
            } else {
                insertingStocks.add(stock);
            }
        });
        MybatisBatch<Stock> mybatisBatch = new MybatisBatch<>(sqlSessionFactory, insertingStocks);
        MybatisBatch.Method<Stock> method = new MybatisBatch.Method<>(StockMapper.class);
        mybatisBatch.execute(method.insert());
    }
}
