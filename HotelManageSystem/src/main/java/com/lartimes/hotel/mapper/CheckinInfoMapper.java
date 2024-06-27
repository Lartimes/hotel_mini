package com.lartimes.hotel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lartimes.hotel.model.po.CheckinInfo;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

public interface CheckinInfoMapper extends BaseMapper<CheckinInfo> {

    void updateDate(@Param("roomId") Integer roomId,
                    @Param("from")LocalDate from,
                    @Param("to")LocalDate to);

}
