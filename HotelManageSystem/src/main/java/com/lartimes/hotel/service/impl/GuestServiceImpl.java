package com.lartimes.hotel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lartimes.hotel.mapper.GuestCardMapper;
import com.lartimes.hotel.mapper.GuestMapper;
import com.lartimes.hotel.model.po.Guest;
import com.lartimes.hotel.model.po.GuestCard;
import com.lartimes.hotel.model.dto.GuestDTO;
import com.lartimes.hotel.service.GuestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
public class GuestServiceImpl extends ServiceImpl<GuestMapper, Guest> implements GuestService {

    private final GuestMapper guestMapper;

    private final GuestCardMapper guestCardMapper;

    public GuestServiceImpl(GuestMapper guestMapper, GuestCardMapper guestCardMapper) {
        this.guestMapper = guestMapper;
        this.guestCardMapper = guestCardMapper;
    }

    @Transactional
    @Override
    public List<Integer> insertGuest(GuestDTO[] guests) {
        ArrayList<Integer> arr = new ArrayList<>(guests.length);
        for (GuestDTO guest : guests) {
            String idcard = guest.getIdcard();
            GuestCard guestCard = guestCardMapper.selectOne(
                    new LambdaQueryWrapper<GuestCard>()
                            .eq(idcard != null,
                                    GuestCard::getIdcard, idcard));
            Guest tmp = new Guest();
            BeanUtils.copyProperties(guest , tmp);
            if (guestCard!= null) {
                tmp.setId(guestCard.getId());
                guestMapper.updateById(tmp);
                arr.add(guestCard.getId());
                continue;
            }
            int pk = guestMapper.insert(tmp);
            GuestCard card = new GuestCard();
            card.setId(pk);
            arr.add(pk);
            card.setIdcard(idcard);
            guestCardMapper.insert(card);
        }
        return arr;
    }

}
