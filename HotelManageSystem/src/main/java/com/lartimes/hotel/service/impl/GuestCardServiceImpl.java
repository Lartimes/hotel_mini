package com.lartimes.hotel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lartimes.hotel.mapper.GuestCardMapper;
import com.lartimes.hotel.model.po.GuestCard;
import com.lartimes.hotel.service.GuestCardService;
import org.springframework.stereotype.Service;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/6/27 17:22
 */
@Service
public class GuestCardServiceImpl extends ServiceImpl<GuestCardMapper, GuestCard> implements GuestCardService {
}
