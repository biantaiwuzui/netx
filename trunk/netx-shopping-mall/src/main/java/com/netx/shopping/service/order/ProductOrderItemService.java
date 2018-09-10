package com.netx.shopping.service.order;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.mapper.order.ProductOrderItemMapper;
import com.netx.shopping.model.order.ProductOrderItem;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by liwei on 3/1/2018.
 */
@Service
public class ProductOrderItemService extends ServiceImpl<ProductOrderItemMapper, ProductOrderItem> {

    /**
     * 获取商品成交的订单ids
     * @param goodsId
     * @return
     */
    public List<String> getProductOrderIds(String goodsId){
        EntityWrapper wrapper=new EntityWrapper();
        wrapper.setSqlSelect("order_id").where("product_id={0} and deleted=0",goodsId);
        return this.selectObjs(wrapper);
    }

    /**
     * 获取商品订单详细列表
     * @param orderId
     * @return
     */
    public List<ProductOrderItem> getProductOrderItemListByOrderId(String orderId){
        EntityWrapper<ProductOrderItem> wrapper=new EntityWrapper<>();
        wrapper.where("order_id={0}",orderId);
        return this.selectList(wrapper);
    }

    /**
     * 根据商品id获取订单id集
     * @param productId
     * @return
     */
    public List<String> getProductOrderIdByProductId(String productId){
        EntityWrapper wrapper=new EntityWrapper<>();
        wrapper.setSqlSelect("order_id").where("product_id = {0} and deleted = {1}",productId,0);
        return this.selectObjs(wrapper);
    }

    public ProductOrderItem getProductOrderItemByOrderId(String orderId){
        EntityWrapper<ProductOrderItem> wrapper=new EntityWrapper<>();
        wrapper.where("order_id={0} AND deleted = 0",orderId);
        return this.selectOne(wrapper);
    }

    /**
     * 根据订单id和商品id获取订单详情
     * @param
     * @return
     */
    public ProductOrderItem getProductOrderItemByOrderIdAndProductId(String orderId, String goodsId){
        EntityWrapper<ProductOrderItem> wrapper = new EntityWrapper<>();
        wrapper.where("order_id={0}", orderId).andNew(" product_id={0}", goodsId);
        return this.selectOne(wrapper);
    }

    public ProductOrderItem getProductOrderItem(List<String> orderId,String productId,String speId){
        EntityWrapper<ProductOrderItem> wrapper = new EntityWrapper<>();
        wrapper.where("spe_id = {0} and product_id = {1}",speId,productId).in("order_id",orderId);
        return this.selectOne(wrapper);
    }

    /**
     * 根据订单id获取商品Id
     * @param
     * @return
     */
    public String  getProductId(String orderId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("product_id").where("id={0}",orderId);
       return (String) this.selectObj(wrapper);
    }

    /**
     * 获取某商品订单详细列表
     * @param
     * @return
     */
    public List<ProductOrderItem> getProductOrderItemList(String productId, Date startTime, Date endTime){
        EntityWrapper<ProductOrderItem> wrapper=new EntityWrapper<>();
        wrapper.where("product_id={0}",productId).between("create_time",startTime,endTime);
        return this.selectList(wrapper);
    }


    /**
     * 根据订单id删除
     * @param
     * @return
     */
    public boolean deleteByOrderId(String orderId){
        EntityWrapper<ProductOrderItem> wrapper = new EntityWrapper<>();
        wrapper.where("order_id = {0}",orderId);
        return this.delete(wrapper);
    }

}
