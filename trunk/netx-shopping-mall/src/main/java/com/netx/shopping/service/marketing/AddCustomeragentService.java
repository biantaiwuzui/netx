package com.netx.shopping.service.marketing;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.mapper.marketing.SellerAddCustomeragentMapper;
import com.netx.shopping.model.marketing.SellerAddCustomeragent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liwei on 3/1/2018.
 */
@Service
public class AddCustomeragentService extends ServiceImpl<SellerAddCustomeragentMapper, SellerAddCustomeragent> {

    @Autowired
    SellerAddCustomeragentMapper addCustomeragentMapper;

    /**
     * 获取数量
     * @param sellerId,sellerId
     * @return
     */
    public int getAddCustomeragentCount(String sellerId,String toSellerId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.where("seller_id = {0} and to_seller_id = {1} and state = 0",sellerId,toSellerId);
        return this.selectCount(wrapper);
    }

    public Boolean updateState(Integer state,String pid,String sellerId){
        return addCustomeragentMapper.updateState(state,pid,sellerId);
    }
}
