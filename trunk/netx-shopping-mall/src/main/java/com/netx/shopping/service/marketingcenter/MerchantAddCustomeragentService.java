package com.netx.shopping.service.marketingcenter;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.netx.shopping.model.marketingcenter.MerchantAddCustomeragent;
import com.netx.shopping.mapper.marketingcenter.MerchantAddCustomeragentMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 网商-添加客服代理请求表 服务实现类
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-22
 */
@Service
public class MerchantAddCustomeragentService extends ServiceImpl<MerchantAddCustomeragentMapper, MerchantAddCustomeragent> {

    @Autowired
    private MerchantAddCustomeragentMapper merchantAddCustomeragentMapper;

    /**
     * 获取数量
     * @param merchantId
     * @param toMerchantId
     * @return
     */
    public int getAddCustomeragentCount(String merchantId, String toMerchantId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.where("merchant_id = {0} AND to_merchant_id = {1} AND state = 0 AND deleted = 0", merchantId, toMerchantId);
        return this.selectCount(wrapper);
    }

    public Boolean updateState(Integer state, String pid, String merchantId){
        return merchantAddCustomeragentMapper.updateState(state, pid, merchantId);
    }

    public Integer queryState(String merchantId, String toMerchantId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("state");
        wrapper.where("merchant_id = {0} AND to_merchant_id = {1} AND deleted = 0", merchantId, toMerchantId);
        Integer state = (Integer) selectObj(wrapper);
        if(state == null){
            state = 3;
        }
        return state;
    }
	
}
