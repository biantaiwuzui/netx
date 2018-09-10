package com.netx.shopping.service.merchantcenter;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.mapper.merchantcenter.MerchantPacketSetMapper;
import com.netx.shopping.model.merchantcenter.MerchantPacketSet;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p>
 * 网商-红包设置 服务实现类
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-02
 */
@Service
public class MerchantPacketSetService extends ServiceImpl<MerchantPacketSetMapper, MerchantPacketSet>{

    /**
     * 根据商家id获取首单红包
     * @param merchatId
     * @return
     */
    public BigDecimal getFirstRateByMerchatId(String merchatId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("first_rate").where("merchant_id = {0} AND deleted = {1}", merchatId, 0);
        return (BigDecimal) selectObj(wrapper);
    }


}
