package com.lartimes.hotel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lartimes.hotel.model.po.Payment;
import org.apache.ibatis.annotations.Select;

public interface PaymentMapper extends BaseMapper<Payment> {

    @Select("""
        select count(*) from payment where payment_id = #{key} for update 
""")
    Integer selectForUpdate(String key);


}
