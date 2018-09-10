package com.netx.shopping.biz.marketingcenter;


import com.netx.shopping.service.marketingcenter.MerchantRecordingHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p>
 * 网商-提成记录表 前端控制器
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-22
 */
@Service
public class MerchantRecordingHistoryAction {

    @Autowired
    private MerchantRecordingHistoryService merchantRecordingHistoryService;

    public BigDecimal getDirect(String merchantId, Integer type){
        BigDecimal decimal = merchantRecordingHistoryService.getDecimal(merchantId, type);
        return decimal==null?new BigDecimal(0):decimal;
    }

    public BigDecimal getCommission(String merchantId){
        return getDirect(merchantId,0).add(getDirect(merchantId,1));
    }
	
}
