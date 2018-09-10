package com.netx.shopping.service.productcenter;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.netx.shopping.model.productcenter.Sku;
import com.netx.shopping.mapper.productcenter.SkuMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品中心-SKU 服务实现类
 * </p>
 *
 * @author CHENQIAN
 * @since 2018-05-05
 */
@Service
public class SkuService extends ServiceImpl<SkuMapper, Sku>{

    @Autowired
    private SkuMapper skuMapper;
    /**
     * 根据producId获取skuId
     * @param productId
     * @return
     */
    public List<String> getSkuIdByProductId(String productId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("id").where("product_id = {0} AND deleted = {1}", productId, 0);
        return selectObjs(wrapper);
    }

    /**
     * 根据producIds获取skuId
     * @param productIds
     * @return
     */
    public List<Sku> getSkuIdByProductIds(List<String> productIds){
        EntityWrapper<Sku> wrapper = new EntityWrapper<>();
        wrapper.in("product_id", productIds).where("deleted = {0}", 0);
        return selectList(wrapper);
    }

    /**
     * 获取最低价格
     * @param productId
     * @return
     */
    public Sku getSkuMinMarketPrice(String productId){
        EntityWrapper<Sku> wrapper = new EntityWrapper<>();
        wrapper.where("product_id = {0} AND deleted = {1}", productId, 0).orderBy("market_price", true);
        return selectOne(wrapper);
    }

    public Map<String, Object> getMaxAndMinPrice(String productId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("MIN(market_price) AS min, MAX(market_price) AS max");
        wrapper.where("product_id = {0} AND deleted = 0", productId);
        return selectMap(wrapper);
    }

    /**
     * 获取商品总销量
     * @param productId
     * @return
     */
    public Long getSumSellNums(String productId){
        return skuMapper.getSumSellNums(productId);
    }

    public Sku getSkuByIdsAndProductId(List<String> ids, String productId){
        EntityWrapper<Sku> wrapper = new EntityWrapper<>();
        wrapper.in("id", ids);
        wrapper.where("product_id = {0} AND deleted = {1}", productId, 0);
        return selectOne(wrapper);
    }
	
}
