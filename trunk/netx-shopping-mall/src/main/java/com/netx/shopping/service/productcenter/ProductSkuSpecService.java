package com.netx.shopping.service.productcenter;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.netx.shopping.model.productcenter.ProductSkuSpec;
import com.netx.shopping.mapper.productcenter.ProductSkuSpecMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  规格-服务实现类
 * </p>
 *
 * @author CHENQIAN
 * @since 2018-05-05
 */
@Service
public class ProductSkuSpecService extends ServiceImpl<ProductSkuSpecMapper, ProductSkuSpec> {

    /**
     * 根据skuIds获取propertyIds
     * @param skuIds
     * @return
     */
    public List<String> getPropertyIdsBySkuIds(List<String> skuIds){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("property_id").where("deleted = {0}",0);
        wrapper.in("sku_id", skuIds);
        return selectObjs(wrapper);
    }

    /**
     * 根据skuId获取propertyId
     * @param skuId
     * @return
     */
    public String getPropertyIdBySkuId(String skuId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("property_id").where("deleted = {0} AND sku_id = {1}",0,skuId);
        return (String)selectObj(wrapper);
    }

    /**
     * 根据skuId获取valueIds
     * @param skuId
     * @return
     */
    public List<String> getValueIdsBySkuId(String skuId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("value_id").where("sku_id = {0} AND deleted = {1}",skuId, 0);
        return selectObjs(wrapper);
    }

    /**
     * 根据skuId获取valueIds
     * @param skuId
     * @return
     */
    public String getValueIdBySkuId(String skuId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("value_id").where("sku_id = {0} AND deleted = {1}",skuId, 0);
        return (String)selectObj(wrapper);
    }

    /**
     * 根据propertyId和skuIds获取valueIds
     * @param propertyId
     * @param skuIds
     * @return
     */
    public List<String> getValueIdByPropertyIdAndSkuIds(String propertyId, List<String> skuIds){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("value_id").in("sku_id", skuIds);
        wrapper.where("property_id = {0} AND deleted = {1}",propertyId, 0);
        return selectObjs(wrapper);
    }

    public List<String> getSkuIds(List<String> skuIds, String valueId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("sku_id");
        if(skuIds != null && skuIds.size() > 0){
            wrapper.in("sku_id", skuIds);
        }
        wrapper.where("value_id = {0} AND deleted = {1}", valueId, 0);
        return selectObjs(wrapper);
    }

    /**
     * 根据skuIds获取规格
     * @param skuIds
     * @return
     */
    public List<ProductSkuSpec> getProductSkuSpecBySkuIds(List<String> skuIds){
        EntityWrapper<ProductSkuSpec> wrapper = new EntityWrapper<>();
        wrapper.in("sku_id", skuIds).where("deleted = {0}", 0);
        return selectList(wrapper);
    }

    /**
     * 根据valueId获取规格
     * @param valueId
     * @return
     */
    public List<ProductSkuSpec> getProductSkuSpecByValueId(String valueId){
        EntityWrapper<ProductSkuSpec> wrapper = new EntityWrapper<>();
        wrapper.where("value_id = {0} AND deleted = 0", valueId);
        return selectList(wrapper);
    }
}
