package com.netx.fuse.biz.shoppingmall.marketing;

import com.netx.common.common.enums.MessageTypeEnum;
import com.netx.common.common.enums.PushMessageDocTypeEnum;
import com.netx.fuse.biz.shoppingmall.business.SellerRegisterFuseAction;
import com.netx.fuse.proxy.MessagePushProxy;
import com.netx.shopping.biz.business.SellerRegisterAction;
import com.netx.shopping.biz.marketing.AddCustomeragentAction;
import com.netx.shopping.model.business.Seller;
import com.netx.shopping.model.marketing.SellerAddCustomeragent;
import com.netx.shopping.service.business.SellerService;
import com.netx.shopping.service.marketing.AddCustomeragentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddCustomeragentFuseAction {

    @Autowired
    SellerService sellerService;

    @Autowired
    SellerRegisterAction sellerRegisterAction;

    @Autowired
    AddCustomeragentService addCustomeragentService;

    @Autowired
    MessagePushProxy messagePushProxy;

    @Autowired
    AddCustomeragentAction addCustomeragentAction;

    @Autowired
    SellerRegisterFuseAction sellerRegisterFuseAction;

    public Integer addCustomeragentByCode(String sellerId,String code,String reason) {
        Seller seller = sellerService.selectById(sellerId);
        if(seller == null){
            return 4;
        }
        Seller toSeller = sellerRegisterAction.checkCustomerCode(code);
        if(toSeller==null){
            return 0;
        }
        if(toSeller.getReferralCode()!=null){
            return 3;
        }
        SellerAddCustomeragent customeragent = new SellerAddCustomeragent();
        customeragent.setReason(reason);
        customeragent.setSellerId(sellerId);
        customeragent.setToSellerId(toSeller.getId());
        customeragent.setCreateUserId(sellerId);
        if(addCustomeragentService.insert(customeragent)){
            //添加极光推送,推送失败返回其他值
            messagePushProxy.messagePushJump(MessageTypeEnum.PRODUCT_TYPE,addCustomeragentAction.createAlertMsg("{0}（当前用户的注册的商家的名称）申请成为你的推荐人，理由是：{1}",new String[]{seller.getName(),reason}),"申请新增客服通知",toSeller.getUserId(), PushMessageDocTypeEnum.ADDCUSTOMERAGENT,sellerId+","+toSeller.getId());
            return 1;
        }
        return 2;
    }

    public Integer isAgrentCustomeragentRequest(String pSellerId,String sellerId,Boolean type) throws Exception{
        Seller pSeller = sellerService.selectById(pSellerId);
        if(pSeller == null){
            return 2;
        }
        Seller seller = sellerService.selectById(sellerId);
        if(seller == null){
            return 3;
        }
        if(seller.getPayStatus()!=0){
            return 4;
        }
        if(addCustomeragentService.getAddCustomeragentCount(pSellerId,sellerId)==0){
            return 0;
        }
        addCustomeragentService.updateState(type?1:2,pSellerId,sellerId);
        if(type){
            seller.setReferralCode(pSeller.getCustomerCode());
            pSeller.setSecondNum(pSeller.getSecondNum()+1);
            seller.setGroupNo(pSeller.getSecondNum());
            sellerService.updateById(seller);
            sellerRegisterAction.addBusinessNum(pSeller.getId(),addCustomeragentAction.getCity(pSeller.getCityCode(),pSeller.getProvinceCode()));
            sellerRegisterFuseAction.buildRelationship(seller,pSeller);
        }
        //极光推送
        messagePushProxy.messagePushJump(MessageTypeEnum.PRODUCT_TYPE,addCustomeragentAction.createAlertMsg("{0}（当前用户的注册的商家的名称）"+(type?"同意":"拒绝")+"你成为其客服代表的推荐人",new String[]{seller.getName()}),"申请新增客服通知",pSeller.getUserId(), PushMessageDocTypeEnum.ADDCUSTOMERAGENT,pSellerId+","+sellerId);
        return 1;
    }
}
