package com.netx.shopping.biz.productcenter;


import ch.qos.logback.core.net.SyslogOutputStream;
import com.netx.common.user.util.VoPoConverter;
import com.netx.shopping.model.productcenter.CategoryPropertyValue;
import com.netx.shopping.model.productcenter.ProductSkuSpec;
import com.netx.shopping.model.productcenter.Sku;
import com.netx.shopping.model.productcenter.Value;
import com.netx.shopping.service.productcenter.ProductSkuSpecService;
import com.netx.shopping.service.productcenter.SkuService;
import com.netx.shopping.service.productcenter.ValueService;
import com.netx.shopping.vo.AddValueRequestDto;
import com.netx.shopping.vo.UpdateValueRequestDto;
import com.netx.utils.money.Money;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  商品中心-属性值-逻辑
 * </p>
 *
 * @author CHENQIAN
 * @since 2018-05-04
 */
@Service
public class ValueAction {

    @Autowired
    private ValueService valueService;
    @Autowired
    private CategoryPropertyValueAction categoryPropertyValueAction;
    @Autowired
    private ProductSkuSpecService productSkuSpecService;
    @Autowired
    private SkuService skuService;

    /**
     * 添加属性值
     * @param requestDto
     * @return
     */
    public Value addValue(AddValueRequestDto requestDto){
//        Value value2 = valueService.getValueByName(requestDto.getName());
//        if(value2 != null){
//            return null;
//        }
        Value value = new Value();
        value.setMerchantId(requestDto.getMerchantId());
        value.setName(requestDto.getName());
        if(StringUtils.isNotBlank(value.getId())){
            value.setDeleted(0);
        }
        valueService.insert(value);
        CategoryPropertyValue categoryPropertyValue = new CategoryPropertyValue();
        categoryPropertyValue.setValueId(value.getId());
        categoryPropertyValue.setCategoryId(requestDto.getCategoryId());
        categoryPropertyValue.setMerchantId(requestDto.getMerchantId());
        categoryPropertyValue.setPropertyId(requestDto.getPropertyId());
        categoryPropertyValueAction.add(categoryPropertyValue);
        return value;
    }

    /**
     * 修改属性值
     * @param requestDto
     * @return
     */
    @Transactional
    public Value updateValue(UpdateValueRequestDto requestDto){
        Value value = valueService.selectById(requestDto.getId());
        if(value != null){
            value.setName(requestDto.getName());
            valueService.updateById(value);
        }
        return value;
    }

    /**
     * 修改属性值-单属性值
     * @param requestDto
     * @return
     */
    @Transactional
    public Value updateValueOne(UpdateValueRequestDto requestDto){
        Value value = valueService.selectById(requestDto.getId());
        if(value != null){
            value.setName(requestDto.getName());
            valueService.updateById(value);
        }
        if(StringUtils.isNotBlank(requestDto.getSkuId())){
            Sku sku = skuService.selectById(requestDto.getSkuId());
            if (sku != null){
                sku.setMarketPrice(new Money(requestDto.getMarketPrice()).getCent());
                sku.setPrice(new Money(requestDto.getMarketPrice()).getCent());
                sku.setStorageNums(requestDto.getStorageNums());
                sku.setUnit(requestDto.getUnit());
                skuService.updateById(sku);
            }
        }
        return value;
    }

    /**
     * 删除属性值
     * @param valueId
     * @return
     */
    @Transactional
    public String deleteValue(String valueId){
        Value value = valueService.selectById(valueId);
        if(value != null){
            List<ProductSkuSpec> list = productSkuSpecService.getProductSkuSpecByValueId(valueId);
            if(list != null && list.size() > 0){
                return "属性<"+value.getName()+">在被商品使用中，不能删除！";
            }
            value.setDeleted(1);
            if(valueService.updateById(value) && categoryPropertyValueAction.deleteByValueId(valueId)){
                return null;
            }
        }
        return "属性不存在！";
    }

    /**
     * 删除属性值-单
     * @param valueId
     * @return
     */
    @Transactional
    public boolean deleteValueOne(String valueId,String skuId){
        Value value = valueService.selectById(valueId);
        if(value != null){
            value.setDeleted(1);
            valueService.updateById(value);
            categoryPropertyValueAction.deleteByValueId(valueId);
            if(StringUtils.isNotBlank(skuId)) {
                Sku sku = skuService.selectById(skuId);
                if (sku != null) {
                    sku.setDeleted(1);
                    skuService.updateById(sku);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 根据propertyId获取value
     * @param propertyId
     * @return
     */
    public List<Value> getValueByPropertyId(String propertyId){
        List<String> valueId = categoryPropertyValueAction.getValueIdByPropertyId(propertyId);
        return valueService.selectBatchIds(valueId);
    }

    /**
     * 根据商家id删除商家的属性值
     * @param merchantId
     * @return
     */
    public boolean deleteByMerchantId(String merchantId){
        List<Value> values = valueService.getValueByMerchantId(merchantId);
        for(Value value : values){
            value.setDeleted(1);
        }
        if(values != null && values.size() > 0){
            return valueService.updateBatchById(values);
        }
        return false;
    }


}
