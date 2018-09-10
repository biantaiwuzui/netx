package com.netx.shopping.service.order;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.mapper.order.ProductReturnMapper;
import com.netx.shopping.model.order.ProductReturn;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liwei on 3/1/2018.
 */
@Service
public class ProductReturnService extends ServiceImpl<ProductReturnMapper, ProductReturn> {

    /**
     * 根据订单id获取退货信息
     * @param orderId
     * @return
     */
    public ProductReturn getProductReturnByOrderId(String orderId){
        EntityWrapper<ProductReturn> wrapper=new EntityWrapper();
        wrapper.where("order_id={0}", orderId);
        return this.selectOne(wrapper);
    }

    /**
     * 获取订单脱货列表
     * @param status,orderIds
     * @return
     */
    public List<ProductReturn> getProductReturnList(int status,List<String> orderIds){
        EntityWrapper<ProductReturn> wrapperone = new EntityWrapper<>();
        wrapperone.where("status = {0}",status).in("order_id",orderIds);
        return this.selectList(wrapperone);
    }
}
