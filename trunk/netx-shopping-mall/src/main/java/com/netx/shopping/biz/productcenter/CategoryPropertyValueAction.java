package com.netx.shopping.biz.productcenter;


import com.netx.shopping.model.productcenter.CategoryPropertyValue;
import com.netx.shopping.service.productcenter.CategoryPropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * 商品中心-类目属性属性值映射
 * </p>
 *
 * @author CHENQIAN
 * @since 2018-05-04
 */
@Service
public class CategoryPropertyValueAction {

    @Autowired
    private CategoryPropertyValueService categoryPropertyValueService;

    /**
     * 添加类目属性属性值映射
     * @param request
     * @return
     */
    public CategoryPropertyValue add(CategoryPropertyValue request){
        CategoryPropertyValue categoryPropertyValue = categoryPropertyValueService.getByCategoryIdAndMerchantIdAndPropertyIdAndValueId(request);
        if(categoryPropertyValue == null){
            categoryPropertyValue = categoryPropertyValueService.getByCategoryIdAndMerchantIdAndPropertyId(request);
            if(categoryPropertyValue == null){
                request.setPriority(0);
            }else {
                request.setPriority(categoryPropertyValue.getPriority() + 1);
            }
            categoryPropertyValueService.insert(request);
            return request;
        }
        return null;
    }

    /**
     * 删除类目属性属性值映射
     * @param merchantId
     * @return
     */
    public boolean deleteByMerchantId(String merchantId){
        List<CategoryPropertyValue> categoryPropertyValues = categoryPropertyValueService.getCategoryPropertyValueByMerchantId(merchantId);
        for(CategoryPropertyValue categoryPropertyValue : categoryPropertyValues){
            categoryPropertyValue.setDeleted(1);
        }
        if(categoryPropertyValues != null && categoryPropertyValues.size() > 0){
            return categoryPropertyValueService.updateBatchById(categoryPropertyValues);
        }
        return false;
    }

    /**
     * 删除类目属性属性值映射
     * @param valueId
     * @return
     */
    public boolean deleteByValueId(String valueId){
        CategoryPropertyValue categoryPropertyValue = categoryPropertyValueService.getCategoryPropertyValueByValueId(valueId);
        if(categoryPropertyValue != null){
            categoryPropertyValue.setDeleted(1);
            return categoryPropertyValueService.updateById(categoryPropertyValue);
        }
        return false;
    }


    /**
     * 根据propertyId获取ValueId
     * @param propertyId
     * @return
     */
    public List<String> getValueIdByPropertyId(String propertyId){
        return categoryPropertyValueService.getValueIdByPropertyId(propertyId);
    }
	
}
