package com.netx.shopping.service.productcenter;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.netx.shopping.model.productcenter.ProductDelivery;
import com.netx.shopping.mapper.productcenter.ProductDeliveryMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 黎子安
 * @since 2018-06-08
 */
@Service
public class ProductDeliveryService extends ServiceImpl<ProductDeliveryMapper, ProductDelivery>{

    /**
     * CHEN-QIAN
     * 根据商品id查询配送方式
     * @param productId
     * @return
     */
    public List<ProductDelivery> getProductDeliveryByProductId(String productId){
        EntityWrapper<ProductDelivery> wrapper = new EntityWrapper<>();
        wrapper.where("product_id = {0} AND deleted = 0", productId);
        return this.selectList(wrapper);
    }

    public Boolean deleteByProductId(String productId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.where("product_id = {0} AND deleted = 0", productId);
        return this.delete(wrapper);
    }

    /**
     * 根据配送方式和商品id查询物流费用
     * @param productId
     * @param deliveryWay
     * @return
     */
    public Long getFeeByProductIdAndDeliveryWay(String productId, Integer deliveryWay){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("fee");
        wrapper.where("product_id = {0} AND delivery_way = {1} AND deleted = 0", productId, deliveryWay);
        return (Long)this.selectObj(wrapper);
    }
	
}
