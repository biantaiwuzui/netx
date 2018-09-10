package com.netx.shopping.biz.productcenter;


import com.netx.common.user.util.VoPoConverter;
import com.netx.shopping.model.productcenter.ProductSkuSpec;
import com.netx.shopping.model.productcenter.Property;
import com.netx.shopping.model.productcenter.Sku;
import com.netx.shopping.model.productcenter.Value;
import com.netx.shopping.service.productcenter.ProductSkuSpecService;
import com.netx.shopping.service.productcenter.PropertyService;
import com.netx.shopping.service.productcenter.SkuService;
import com.netx.shopping.service.productcenter.ValueService;
import com.netx.shopping.vo.AddProductSkuSpecRequestDto;
import com.netx.shopping.vo.GetPropertyResponseVo;
import com.netx.shopping.vo.GetValueResponseVo;
import com.netx.utils.money.Money;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  商品中心-规格表
 * </p>
 *
 * @author CHENQIAN
 * @since 2018-05-05
 */
@Service
public class ProductSkuSpecAction {

    @Autowired
    private ProductSkuSpecService productSkuSpecService;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private ValueService valueService;
    @Autowired
    private SkuService skuService;

    /**
     * 添加多个商品规格
     * @param addProductSkuSpecDtoList
     * @param skuId
     * @return
     */
    public boolean addProductSkuSpecList(List<AddProductSkuSpecRequestDto> addProductSkuSpecDtoList, String skuId){
        for(AddProductSkuSpecRequestDto addProductSkuSpecDto : addProductSkuSpecDtoList){
            addProductSkuSpecDto.setSkuId(skuId);
            addProductSkuSpec(addProductSkuSpecDto);
        }
        return true;
    }

    /**
     * 添加商品规格
     * @param addProductSkuSpecDto
     * @return
     */
    public ProductSkuSpec addProductSkuSpec(AddProductSkuSpecRequestDto addProductSkuSpecDto){
        ProductSkuSpec productSkuSpec = new ProductSkuSpec();
        VoPoConverter.copyProperties(addProductSkuSpecDto, productSkuSpec);
        if(StringUtils.isBlank(productSkuSpec.getId())){
            productSkuSpec.setDeleted(0);
        }
        return productSkuSpecService.insert(productSkuSpec)?productSkuSpec:null;
    }

    /**
     * 多属性方法
     * @param skuIds
     * @return
     */
    public List<GetPropertyResponseVo> getProperty(List<String> skuIds){
        List<GetPropertyResponseVo> response = new ArrayList<>();
        List<String> propertyIds = productSkuSpecService.getPropertyIdsBySkuIds(skuIds);
        List<Property> properties = propertyService.selectBatchIds(propertyIds);
        for(Property property : properties){
            GetPropertyResponseVo getPropertyResponseVo = new GetPropertyResponseVo();
            getPropertyResponseVo.setPropertyId(property.getId());
            getPropertyResponseVo.setPropertyName(property.getName());
            getPropertyResponseVo.setValueList(getValue(property.getId(), skuIds));
            response.add(getPropertyResponseVo);
        }
        return response;
    }

    /**
     * 根据propertyId获取属性值
     * @param propertyId
     * @return
     */
    public List<GetValueResponseVo> getValue(String propertyId, List<String> skuIds){
        List<GetValueResponseVo> response = new ArrayList<>();
        List<String> valueIds = productSkuSpecService.getValueIdByPropertyIdAndSkuIds(propertyId, skuIds);
        List<Value> values = valueService.selectBatchIds(valueIds);
        for(Value value : values){
            GetValueResponseVo getValueResponseVo = new GetValueResponseVo();
            getValueResponseVo.setValueId(value.getId());
            getValueResponseVo.setValueName(value.getName());
            response.add(getValueResponseVo);
        }
        return response;
    }

    /**
     * 单属性方法（暂时使用）
     * @param skuIds
     * @return
     */
    public List<GetPropertyResponseVo> getPropertyOne(List<String> skuIds){
        List<GetPropertyResponseVo> response = new ArrayList<>();
        if(skuIds != null && skuIds.size() > 0){
            String propertyId = productSkuSpecService.getPropertyIdBySkuId(skuIds.get(0));
            if (propertyId != null) {
                Property property = propertyService.selectById(propertyId);
                if (property != null) {
                    GetPropertyResponseVo getPropertyResponseVo = new GetPropertyResponseVo();
                    getPropertyResponseVo.setPropertyId(property.getId());
                    getPropertyResponseVo.setPropertyName(property.getName());
                    getPropertyResponseVo.setValueList(getValueOne(skuIds));
                    response.add(getPropertyResponseVo);
                }
            }
        }
        return response;
    }

    /**
     * 单属性方法（暂时使用）
     * 根据skuIds获取属性值(暂时使用)
     * @param skuIds
     * @return
     */
    public List<GetValueResponseVo> getValueOne(List<String> skuIds){
        List<GetValueResponseVo> response = new ArrayList<>();
        for(String skuId : skuIds){
            GetValueResponseVo getValueResponseVo = new GetValueResponseVo();
            Sku sku = skuService.selectById(skuId);
            if(sku != null){
                VoPoConverter.copyProperties(sku, getValueResponseVo);
                getValueResponseVo.setSkuId(skuId);
                getValueResponseVo.setPrice(new BigDecimal(Money.getMoneyString(sku.getPrice())));
                getValueResponseVo.setMarketPrice(new BigDecimal(Money.getMoneyString(sku.getMarketPrice())));
            }
            String valueId = productSkuSpecService.getValueIdBySkuId(skuId);
            Value value = valueService.selectById(valueId);
            if(value != null){
                getValueResponseVo.setValueId(value.getId());
                getValueResponseVo.setValueName(value.getName());
            }
            response.add(getValueResponseVo);
        }
        return response;
    }

    /**
     * 获取skuIds
     * @param skuIds
     * @param valueId
     * @return
     */
    public List<String> getSkuIds(List<String> skuIds, String valueId){
        return productSkuSpecService.getSkuIds(skuIds, valueId);
    }

    /**
     * 根据skuIds删除规格关联表
     * @param skuIds
     * @return
     */
    public boolean delete(List<String> skuIds){
        List<ProductSkuSpec> productSkuSpecs = productSkuSpecService.getProductSkuSpecBySkuIds(skuIds);
        for(ProductSkuSpec productSkuSpec : productSkuSpecs){
            productSkuSpec.setDeleted(1);
        }
        if(productSkuSpecs != null && productSkuSpecs.size() > 0) {
            return productSkuSpecService.updateBatchById(productSkuSpecs);
        }
        return false;
    }

    /**
     * 获取属性值名
     * @param skuId
     * @return
     */
    public String getValueNames(String skuId){
        List<String> valueId = productSkuSpecService.getValueIdsBySkuId(skuId);
        return valueService.getNamesById(valueId);
    }

	
}
