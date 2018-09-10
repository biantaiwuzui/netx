package com.netx.fuse.biz.shoppingmall.marketingcenter;

import com.netx.common.common.enums.MessageTypeEnum;
import com.netx.common.common.enums.PushMessageDocTypeEnum;
import com.netx.fuse.biz.shoppingmall.merchantcenter.MerchantRegisterFuseAction;
import com.netx.shopping.biz.marketingcenter.MerchantAddCustomeragentAction;
import com.netx.shopping.biz.merchantcenter.MerchantRegisterAction;
import com.netx.shopping.model.marketingcenter.MerchantAddCustomeragent;
import com.netx.shopping.model.merchantcenter.Merchant;
import com.netx.shopping.service.marketingcenter.MerchantAddCustomeragentService;
import com.netx.shopping.service.merchantcenter.MerchantService;
import com.netx.ucenter.biz.common.WzCommonImHistoryAction;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@Service
public class MerchantAddCustomeragentFuseAction {

    @Autowired
    MerchantService merchantService;
    @Autowired
    MerchantAddCustomeragentService merchantAddCustomeragentService;
    @Autowired
    WzCommonImHistoryAction wzCommonImHistoryAction;
    @Autowired
    MerchantAddCustomeragentAction merchantAddCustomeragentAction;
    @Autowired
    MerchantRegisterAction merchantRegisterAction;
    @Autowired
    MerchantRegisterFuseAction merchantRegisterFuseAction;

    @Transactional(rollbackFor = Exception.class)
    public Integer addCustomeragentByCode(String merchantId, String code, String reason,String userId) throws Exception {
        Merchant merchant = merchantService.selectById(merchantId);
        if(merchant == null){
            return 4;
        }
        Merchant referralMerchant = merchantService.getMerchantByCustomerServiceCode(code);
        if(referralMerchant == null){
            return 0;
        }
        if(StringUtils.isNotBlank(referralMerchant.getReferralServiceCode())){
            return 3;
        }
        MerchantAddCustomeragent customeragent = new MerchantAddCustomeragent();
        customeragent.setReason(reason);
        customeragent.setMerchantId(merchantId);
        customeragent.setState(0);
        customeragent.setToMerchantId(referralMerchant.getId());
        if(merchantAddCustomeragentService.insert(customeragent)){
            Map<String,Object> param = new HashMap<>();
            param.put("merchantId",merchantId);
            param.put("toMerchantId",referralMerchant.getId());
            wzCommonImHistoryAction.add(userId,referralMerchant.getUserId(),createAlertMsg("\"{0}\"商家申请成为你的推荐人，理由是：{1}",new String[]{merchant.getName(),reason}),merchantId,MessageTypeEnum.PRODUCT_TYPE,PushMessageDocTypeEnum.ADDCUSTOMERAGENT,param);
            return 1;
        }
        return 2;
    }

    @Transactional(rollbackFor = Exception.class)
    public Integer isAgrentCustomeragentRequest(String userId,String parentMerchantId, String merchantId, Boolean type) throws Exception {
        Merchant parentMerchant = merchantService.selectById(parentMerchantId);
        if(parentMerchant == null){
            return 2;
        }
        Merchant merchant = merchantService.selectById(merchantId);
        if(merchant == null){
            return 3;
        }
        if(merchant.getPayStatus() != 0){
            return 4;
        }
        if(merchantAddCustomeragentService.getAddCustomeragentCount(parentMerchantId, merchantId) == 0){
            return 0;
        }
        merchantAddCustomeragentService.updateState(type?1:2, parentMerchantId, merchantId);
        if(type){
            merchantRegisterAction.createBusinessNum(parentMerchant.getId(), merchantRegisterAction.getCity(parentMerchant.getCityCode(),parentMerchant.getProvinceCode()));
            merchantRegisterFuseAction.buildRelationship(merchant, parentMerchant);
        }
        wzCommonImHistoryAction.add(userId,parentMerchant.getUserId(),createAlertMsg("\"{0}\"商家"+(type?"同意":"拒绝")+"你成为其客服代表的推荐人",new String[]{merchant.getName()}),merchantId,MessageTypeEnum.PRODUCT_TYPE,PushMessageDocTypeEnum.DEAL_CUSTOMERAGENT,null);
        return 1;
    }

    public String createAlertMsg(String patter,Object object){
        return new MessageFormat(patter).format(object);
    }

    public Integer queryState(String merchantId, String toMerchantId){
        return merchantAddCustomeragentService.queryState(merchantId, toMerchantId);
    }

}
