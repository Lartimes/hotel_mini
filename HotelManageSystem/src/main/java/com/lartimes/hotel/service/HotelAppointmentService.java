package com.lartimes.hotel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lartimes.hotel.common.PageParams;
import com.lartimes.hotel.common.PageResult;
import com.lartimes.hotel.model.dto.GuestDTO;
import com.lartimes.hotel.model.dto.QueryAppointmentsDto;
import com.lartimes.hotel.model.dto.RoomAndAppointmentsDto;
import com.lartimes.hotel.model.po.HotelAppointment;

import java.time.LocalDate;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author itcast
 * @since 2024-06-18
 */
public interface HotelAppointmentService extends IService<HotelAppointment> {

    /**
     * 进行预约
     *
     * @param guests
     * @param roomId
     * @return
     */
    Object appointedHotel(GuestDTO[] guests, Integer roomId , LocalDate checkIn , LocalDate checkOut);

    /**
     * 获取精准该区间的房间是否存在
     * @param id
     * @param checkInDate
     * @param checkOutDate
     * @return
     */
    HotelAppointment getAccurateCheckInHotel(Integer id , LocalDate  checkInDate, LocalDate checkOutDate);

    /**
     * 更改入住期限
     * @param roomId
     * @param from
     * @param to
     */
    void updateDate(Integer roomId, LocalDate from, LocalDate to);

    /**
     * 分页查询预约信息
     * @param pageParams
     * @param appointmentsDto
     * @return
     */
    PageResult<RoomAndAppointmentsDto> getAppointmentsByPage(PageParams pageParams, QueryAppointmentsDto appointmentsDto);
}
