package com.netx.shopping.service.order;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.mapper.order.ProductOrderMapper;
import com.netx.shopping.model.order.ProductOrder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by liwei on 3/5/2018.
 */
@Service
public class OrderAutoCloseService extends ServiceImpl<ProductOrderMapper, ProductOrder> {

    public List<ProductOrder> getProductOrderList(Date expireTime){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.where("status=2").and("pay_time>{0}", expireTime);
        return this.selectList(wrapper);
    }


}
