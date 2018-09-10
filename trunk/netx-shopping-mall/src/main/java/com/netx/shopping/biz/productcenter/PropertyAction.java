package com.netx.shopping.biz.productcenter;


import com.netx.common.user.util.VoPoConverter;
import com.netx.shopping.biz.merchantcenter.ShippingFeeAction;
import com.netx.shopping.model.merchantcenter.ShippingFee;
import com.netx.shopping.model.productcenter.Property;
import com.netx.shopping.service.productcenter.PropertyService;
import com.netx.shopping.vo.AddPropertyRequestDto;
import com.netx.shopping.vo.RegisterMerchantResponseVo;
import com.netx.utils.money.Money;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 商品中心-属性
 * </p>
 *
 * @author CHENQIAN
 * @since 2018-05-04
 */
@Service
public class PropertyAction {

    @Autowired
    private PropertyService propertyService;
    @Autowired
    private ShippingFeeAction shippingFeeAction;

    /**
     * 添加/编辑属性
     * @param request
     * @return
     */
    public Property addProperty(AddPropertyRequestDto request){
        Property property = new Property();
        VoPoConverter.copyProperties(request, property);
        if(StringUtils.isNotBlank(property.getId())){
            property.setDeleted(0);
        }
        return propertyService.insertOrUpdate(property)?property:null;
    }

    /**
     * 根据merchantId获取属性列表
     * @param merchantId
     * @return
     */
    public List<Property> getPropertByMerchantId(String merchantId){
        return propertyService.getPropertyByMerchantId(merchantId);
    }

    /**
     * 根据merchantId获取属性列表
     * @param merchantId
     * @return
     */
    public RegisterMerchantResponseVo getPropertIdByMerchantId(String merchantId){
        RegisterMerchantResponseVo responseVo = new RegisterMerchantResponseVo();
        List<String> propertIds = propertyService.getPropertyIdByMerchantId(merchantId);
        if(propertIds != null && propertIds.size() > 0){
            responseVo.setPropertIds(propertIds);
        }
        ShippingFee shippingFee = shippingFeeAction.getShippingFeeByMerchantId(merchantId);
        if(shippingFee != null){
            responseVo.setShippingFeeId(shippingFee.getId());
            responseVo.setShippingFee(new BigDecimal(Money.getMoneyString(shippingFee.getFee())));
        }
        return responseVo;
    }

    /**
     * 根据商家id删除属性
     * @param merchantId
     * @return
     */
    public boolean deleteByMerchantId(String merchantId){
        List<Property> properties = propertyService.getPropertyByMerchantId(merchantId);
        for(Property property : properties){
            property.setDeleted(1);
        }
        if(properties != null && properties.size() > 0){
            return propertyService.updateBatchById(properties);
        }
        return false;
    }
}
