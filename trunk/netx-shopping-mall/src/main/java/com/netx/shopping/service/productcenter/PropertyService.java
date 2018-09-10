package com.netx.shopping.service.productcenter;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.netx.shopping.model.productcenter.Property;
import com.netx.shopping.mapper.productcenter.PropertyMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品中心-属性 服务实现类
 * </p>
 *
 * @author CHENQIAN
 * @since 2018-05-04
 */
@Service
public class PropertyService extends ServiceImpl<PropertyMapper, Property> {

    /**
     * 根据merchantId获取属性列表
     * @param merchantId
     * @return
     */
    public List<Property> getPropertyByMerchantId(String merchantId){
        EntityWrapper<Property> wrapper = new EntityWrapper<>();
        wrapper.where("merchant_id = {0} AND deleted = {1}", merchantId, 0);
        return selectList(wrapper);
    }

    /**
     * 根据merchantId获取属性id
     * @param merchantId
     * @return
     */
    public List<String> getPropertyIdByMerchantId(String merchantId){
        EntityWrapper wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("id");
        wrapper.where("merchant_id = {0} AND deleted = {1}", merchantId, 0);
        return selectObjs(wrapper);
    }

}
