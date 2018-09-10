package com.netx.shopping.service.merchantcenter;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.mapper.merchantcenter.ShippingFeeMapper;
import com.netx.shopping.model.merchantcenter.ShippingFee;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 物流费用设置 服务实现类
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-02
 */
@Service
public class ShippingFeeService extends ServiceImpl<ShippingFeeMapper, ShippingFee>{

    public ShippingFee queryByMerchantId(String merchantId){
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("merchant_id={0}",merchantId);
        return selectOne(wrapper);
    }
}
