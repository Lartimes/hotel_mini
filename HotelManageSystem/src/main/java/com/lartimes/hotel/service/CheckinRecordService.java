package com.lartimes.hotel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lartimes.hotel.common.PageParams;
import com.lartimes.hotel.common.PageResult;
import com.lartimes.hotel.common.Result;
import com.lartimes.hotel.model.po.CheckinRecord;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author itcast
 * @since 2024-06-18
 */
public interface CheckinRecordService extends IService<CheckinRecord> {


    /**
     * 当天办理退房
     * @param idCard
     * @return
     */
    Result checkOut(String idCard);


    /**
     * 根据idcard 获取其之前开房记录
     * @param pageParams
     * @param map
     * @return
     */
    PageResult<CheckinRecord> getRecordsByPage(PageParams pageParams, Map<String, String> map);


}
