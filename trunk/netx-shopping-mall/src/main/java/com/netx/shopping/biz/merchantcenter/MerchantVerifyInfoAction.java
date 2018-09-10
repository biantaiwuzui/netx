package com.netx.shopping.biz.merchantcenter;


import com.netx.shopping.model.merchantcenter.MerchantVerifyInfo;
import com.netx.shopping.service.merchantcenter.MerchantVerifyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务action
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-02
 */
@Service
public class MerchantVerifyInfoAction {

    @Autowired
    private MerchantVerifyInfoService merchantVerifyInfoService;

    /**
     * 根据merchantId删除相关认证信息
     * @param merchantId
     * @return
     */
    @Transactional
    public boolean deleteByMerchantId(String merchantId){
        List<MerchantVerifyInfo> lists = merchantVerifyInfoService.getMerchantVerifyInfoByMerchantId(merchantId);
        if(lists != null && lists.size() > 0){
            for(MerchantVerifyInfo list : lists){
                list.setDeleted(1);
            }
            return merchantVerifyInfoService.updateBatchById(lists);
        }
        return false;
    }
	
}
