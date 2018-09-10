package com.netx.shopping.service.order;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.mapper.order.ProductOrderPutOffMapper;
import com.netx.shopping.model.order.ProductOrderPutOff;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liwei on 3/1/2018.
 */
@Service
public class ProductOrderPutOffService  extends ServiceImpl<ProductOrderPutOffMapper, ProductOrderPutOff> {

    /**
     * 获取订单延迟列表
     * @param status,orderIds
     * @return
     */
    public List<ProductOrderPutOff> getProductOrderPutOffList(int status,List<String> orderIds){
        EntityWrapper<ProductOrderPutOff> wrapperone = new EntityWrapper<>();
        wrapperone.where("status = {0}", status).in("order_id",orderIds);
        return this.selectList(wrapperone);
    }

    public List<ProductOrderPutOff> getProductOrderPutOffList(int status){
        EntityWrapper<ProductOrderPutOff> wrapperone = new EntityWrapper<>();
        wrapperone.where("status = {0} and deleted = {1}",status,0);
        return this.selectList(wrapperone);
    }

    /**
     * 获取订单延迟详情
     * @param orderId
     * @return
     */
    public ProductOrderPutOff getProductOrderPutOff(String orderId){
        EntityWrapper<ProductOrderPutOff> wrapperone = new EntityWrapper<>();
        wrapperone.where("order_id = {0} and deleted = {1}",orderId,0);
        return this.selectOne(wrapperone);
    }
}
