package com.lartimes.hotel.service;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/6/22 9:39
 */
public interface RoomScheduledService {

    /**
     * 定时更新rooms / group
     */
    public void groupByHash();


    /**
     * 每天11:30 / 11 进行房间到期检测
     * 发送通知给Guest ,  MQ 12点进行检测
     */
    void checkLeavingRooms();

}
