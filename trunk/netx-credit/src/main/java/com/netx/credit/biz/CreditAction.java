package com.netx.credit.biz;

import com.alibaba.fastjson.JSON;
import com.netx.common.user.util.VoPoConverter;
import com.netx.credit.vo.PublishCreditRequestDto;
import com.netx.credit.model.Credit;
import com.netx.credit.service.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class CreditAction {

    @Autowired
    private CreditService creditService;

    // 发行网信
    public String publishCredit(PublishCreditRequestDto dto,String userId, String cashierId) {
        Credit credit = new Credit();
        Date date = new Date();
        credit.setPictureUrl(dto.getPictureUrl());
        credit.setName(dto.getName());
        credit.setMerchantId(dto.getMerchantId());
        credit.setScope(JSON.toJSONString(dto.getCreditScope()));
        credit.setDividendSetting(JSON.toJSONString(dto.getCreditDividendSettings()));
        credit.setPresaleUpperLimit(dto.getPresaleUpperLimit());
        credit.setDescription(dto.getDescription());
        credit.setUserId(userId);
        credit.setIssueNumber(dto.getIssueNumber());
        credit.setCashierId(cashierId);
        credit.setEnsureDeal(dto.getEnsureDeal());
        credit.setInnerPurchaseDate(new Date(dto.getInnerPurchaseDate()));
        credit.setSubscriptionDiscount(JSON.toJSONString(dto.getCreditLevelDiscountSettings()));
        credit.setCreditStatus(0);
        credit.setDeleted(0);
        credit.setCreateTime(date);
        credit.setUpdateTime(date);
        return creditService.insert(credit)?credit.getId():null;
    }


    /**
     * 编辑网信
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> editCredit(PublishCreditRequestDto requestDto){
        Map<String, String> result = new HashMap<>();
        Credit credit = creditService.selectById(requestDto.getCreditId());
        if (credit.getId() == null) {
            result.put("creditIdNotExist", "检验网信id是否正确");
            return result;
        }
        VoPoConverter.copyProperties(requestDto, credit);
//        credit.setPictureUrl(JSON.toJSONString(requestDto.getPictureUrl()));
        credit.setScope(JSON.toJSONString(requestDto.getCreditScope()));
        credit.setDividendSetting(JSON.toJSONString(requestDto.getCreditDividendSettings()));
        credit.setInnerPurchaseDate(new Date(requestDto.getInnerPurchaseDate()));
        credit.setSubscriptionDiscount(JSON.toJSONString(requestDto.getCreditLevelDiscountSettings()));
        credit.setUpdateTime(new Date());
        if (creditService.updateById(credit)) {
            result.put("creditId", requestDto.getCreditId());
            return result;
        } else {
            result.put("editError", "修改网信资料失败");
            return result;
        }
    }
    /**
     * 判断是否可以预售网信
     */
    public boolean isCanPublishCredit(PublishCreditRequestDto requestDto, String userId){
        if (creditService.isCanPublishCredit(requestDto, userId) == null) {
            return true;
        } else {
            return true;
        }
    }

    public String selectUserIdByCreditId(String creditId) {
        Credit credit  = creditService.selectById(creditId);
        if (credit == null) {
            return null;
        }
        return credit.getUserId();
    }

    /**
     * 发行者取消所申请的网信
     */
    public Boolean deleteCredit(String creditId) {
        Credit credit = creditService.selectById(creditId);
        credit.setCreditStatus(2);
        credit.setDeleted(1);
        return creditService.updateById(credit);
    }


}
