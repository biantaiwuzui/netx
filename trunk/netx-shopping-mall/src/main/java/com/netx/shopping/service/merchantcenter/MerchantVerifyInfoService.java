package com.netx.shopping.service.merchantcenter;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.netx.shopping.model.merchantcenter.MerchantVerifyInfo;
import com.netx.shopping.mapper.merchantcenter.MerchantVerifyInfoMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.shopping.model.merchantcenter.constants.MechantVerifyTypeEnum;
import com.netx.shopping.model.merchantcenter.constants.MerchantManagerEnum;
import com.netx.shopping.model.merchantcenter.constants.MerchantVerifyStatusEnum;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-02
 */
@Service
public class MerchantVerifyInfoService extends ServiceImpl<MerchantVerifyInfoMapper, MerchantVerifyInfo>{

    /**
     * 根据商家id查询相关认证信息
     * @param merchantId
     * @return
     */
    public List<MerchantVerifyInfo> getMerchantVerifyInfoByMerchantId(String merchantId){
        EntityWrapper<MerchantVerifyInfo> wrapper = new EntityWrapper<>();
        wrapper.where("merchant_id = {0} AND deleted = {1}", merchantId, 0);
        return this.selectList(wrapper);
    }

    /**
     * 根据商家id获取法人身份证
     * @param merchantId
     * @param mechantVerifyTypeEnum
     * @return
     */
    public String getIdCardByMerchantId(String merchantId, MechantVerifyTypeEnum mechantVerifyTypeEnum){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("verify_info");
        wrapper.where("merchant_id = {0} AND verify_type = {1} AND deleted = 0", merchantId, mechantVerifyTypeEnum.getName());
        return (String)this.selectObj(wrapper);
    }
	
}
