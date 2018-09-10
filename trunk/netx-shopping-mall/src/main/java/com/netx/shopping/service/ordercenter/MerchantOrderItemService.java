package com.netx.shopping.service.ordercenter;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.netx.shopping.model.ordercenter.MerchantOrderItem;
import com.netx.shopping.mapper.ordercenter.MerchantOrderItemMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author CHENQIAN
 * @since 2018-05-04
 */
@Service
public class MerchantOrderItemService extends ServiceImpl<MerchantOrderItemMapper, MerchantOrderItem> {

    public boolean deleteByOrderNo(List<String> orderNo){
        Wrapper wrapper = new EntityWrapper();
        wrapper.in("order_no",orderNo);
        return delete(wrapper);
    }

    public List<Map<String,Object>> queryByOrderNo(String orderNo){
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("order_no={0}",orderNo);
        String sql = "count(*) as quantity,product_id as productId,sku_id as skuId," +
                "sku_desc as value,product_img_url as productPic,unit_price as unitPrice," +
                "final_unit_price as finalUnitPrice, product_name as name";
        wrapper.setSqlSelect(sql);
        wrapper.groupBy("productId,skuId,value,productPic,unitPrice,finalUnitPrice,name");
        return selectMaps(wrapper);
    }

    /**
     * 修改订单详情
     * @param productId
     * @param skuId
     * @param orderNo
     * @param merchantOrderItem
     * @return
     */
    public boolean updateOrderItem(String productId,String skuId,String orderNo,MerchantOrderItem merchantOrderItem){
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("product_id={0} and sku_id={1} and order_no={2}",productId,skuId,orderNo);
        return update(merchantOrderItem,wrapper);
    }

    public boolean updateOrderItem(String orderNo,MerchantOrderItem merchantOrderItem){
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("order_no={0}",orderNo);
        return update(merchantOrderItem,wrapper);
    }

    public boolean updateOrderItem(List<String> orderNo,MerchantOrderItem merchantOrderItem){
        Wrapper wrapper = new EntityWrapper();
        wrapper.in("order_no",orderNo);
        return update(merchantOrderItem,wrapper);
    }
    
    public List<MerchantOrderItem> selectWZItem(List<String> order){
        EntityWrapper<MerchantOrderItem> wrapper = new EntityWrapper<>();
        wrapper.in("order_no",order);
        return selectList(wrapper);
    }
    
}
