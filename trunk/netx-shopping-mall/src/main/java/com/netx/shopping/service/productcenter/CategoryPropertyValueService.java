package com.netx.shopping.service.productcenter;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.netx.shopping.model.productcenter.CategoryPropertyValue;
import com.netx.shopping.mapper.productcenter.CategoryPropertyValueMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品中心-类目属性属性值映射 服务实现类
 * </p>
 *
 * @author CHENQIAN
 * @since 2018-05-04
 */
@Service
public class CategoryPropertyValueService extends ServiceImpl<CategoryPropertyValueMapper, CategoryPropertyValue> {


    public CategoryPropertyValue getByCategoryIdAndMerchantIdAndPropertyIdAndValueId(CategoryPropertyValue categoryPropertyValue){
        EntityWrapper<CategoryPropertyValue> wrapper = new EntityWrapper<>();
        wrapper.where("category_id = {0}", categoryPropertyValue.getCategoryId());
        wrapper.and("merchant_id = {0}", categoryPropertyValue.getMerchantId());
        wrapper.and("property_id = {0}", categoryPropertyValue.getPropertyId());
        wrapper.and("value_id = {0}", categoryPropertyValue.getValueId());
        wrapper.and("deleted = {0}", 0);
        return selectOne(wrapper);
    }

    public CategoryPropertyValue getByCategoryIdAndMerchantIdAndPropertyId(CategoryPropertyValue categoryPropertyValue){
        EntityWrapper<CategoryPropertyValue> wrapper = new EntityWrapper<>();
        wrapper.where("category_id = {0}", categoryPropertyValue.getCategoryId());
        wrapper.and("merchant_id = {0}", categoryPropertyValue.getMerchantId());
        wrapper.and("property_id = {0}", categoryPropertyValue.getPropertyId());
        wrapper.and("deleted = {0}", 0);
        wrapper.orderBy("create_time", false);
        return selectOne(wrapper);
    }

    /**
     * 根据propertyId获取ValueId
     * @param propertyId
     * @return
     */
    public List<String> getValueIdByPropertyId(String propertyId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("value_id").where("property_id = {0} AND deleted = {1}",propertyId, 0);
        wrapper.orderBy("priority", true);
        return selectObjs(wrapper);
    }

    /**
     * 根据商家id或获取类目属性属性值映射
     * @param merchantId
     * @return
     */
    public List<CategoryPropertyValue> getCategoryPropertyValueByMerchantId(String merchantId){
        EntityWrapper<CategoryPropertyValue> wrapper = new EntityWrapper<>();
        wrapper.where("merchant_id = {0} AND deleted = {1}", merchantId, 0);
        return selectList(wrapper);
    }

    /**
     * 根据属性值id或获取类目属性属性值映射
     * @param valueId
     * @return
     */
    public CategoryPropertyValue getCategoryPropertyValueByValueId(String valueId){
        EntityWrapper<CategoryPropertyValue> wrapper = new EntityWrapper<>();
        wrapper.where("value_id = {0} AND deleted = {1}", valueId, 0);
        return selectOne(wrapper);
    }

}
