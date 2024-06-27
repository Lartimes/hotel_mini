package com.lartimes.hotel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lartimes.hotel.model.po.Guest;
import com.lartimes.hotel.model.dto.GuestDTO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author itcast
 * @since 2024-06-18
 */
public interface GuestService extends IService<Guest> {


    List<Integer> insertGuest(GuestDTO[] guests);
}
