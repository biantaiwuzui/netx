package com.netx.shopping.service.business;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.mapper.business.SellerPacketSetMapper;
import com.netx.shopping.model.business.SellerPacketSet;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by liwei on 3/1/2018.
 */
@Service
public class PacketSetService extends ServiceImpl<SellerPacketSetMapper, SellerPacketSet> {

    public BigDecimal getFirstRate(String sellerId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("first_rate AS firstRate").where("seller_id = {0} AND deleted = 0", sellerId);
        return (BigDecimal) this.selectObj(wrapper);
    }

}
