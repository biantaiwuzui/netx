package com.netx.credit.biz;

import com.netx.credit.model.CreditSubscription;
import com.netx.credit.service.CreditSubscriptionService;
import com.netx.credit.vo.CreditSubscriptionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author lanyingchu
 * @date 2018/8/1 12:50
 */

@Service
public class CreditSubscriptionAction {

    @Autowired
    private CreditSubscriptionService creditSubscriptionService;

    /**
     * 添加认购信息
     */
    @Transactional(rollbackFor = Exception.class)
    public String add(CreditSubscriptionDto dto, String message){
        CreditSubscription creditSubscription = new CreditSubscription();
        Date date = new Date();
        creditSubscription.setCreditId(dto.getCreditId());
        creditSubscription.setMerchantId(dto.getMerchantId());
        creditSubscription.setUserId(dto.getUserId());
        creditSubscription.setCreditStageId(dto.getCreditStageId());
        creditSubscription.setType(dto.getType());
        creditSubscription.setSubscriptionNumber(dto.getSubscriptionNumber());
        creditSubscription.setStatus(dto.getStatus());
        creditSubscription.setMessagePyload(message);
        creditSubscription.setSendTime(date);
        creditSubscription.setCreateTime(date);
        creditSubscription.setUpdateTime(date);
        return creditSubscriptionService.insert(creditSubscription)?creditSubscription.getId():null;
    }

    /**
     * 修改认购信息
     */
    public void editSubscription(CreditSubscriptionDto dto) {
        CreditSubscription creditSubscription = creditSubscriptionService.getMerchantInnerCreditInfo(dto.getUserId(), dto.getType(), dto.getCreditId(), dto.getMerchantId());
        // 修改认购额和认购状态
        creditSubscription.setStatus(dto.getStatus());
        creditSubscription.setSubscriptionNumber(creditSubscription.getSubscriptionNumber() + dto.getSubscriptionNumber());
        creditSubscription.setUpdateTime(new Date());
        // 更新该认购信息
        creditSubscriptionService.updateById(creditSubscription);
    }

}
