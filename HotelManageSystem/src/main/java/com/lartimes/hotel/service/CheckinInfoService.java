package com.lartimes.hotel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lartimes.hotel.common.Result;
import com.lartimes.hotel.model.po.CheckinInfo;

import java.time.LocalDate;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author itcast
 * @since 2024-06-18
 */
public interface CheckinInfoService extends IService<CheckinInfo> {

    /**
     * 办理入住
     * @param data
     * @return
     */
    Result hotelCheckIn(Map<String, String> data);


    /**
     * 广播通知guests
     *
     * @return
     */
    Result broadCastToGuests();


    /**
     * 修改入住时间
     * @param from
     * @param to
     * @return
     */
    Result modifyLivingDate(Integer roomId, LocalDate from , LocalDate to);

    CheckinInfo getNowLivingRoom(Integer id);


}
