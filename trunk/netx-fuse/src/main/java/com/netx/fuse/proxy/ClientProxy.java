package com.netx.fuse.proxy;

import com.netx.common.vo.business.GetSellerManageIdAndMoneyPhoneVo;
import com.netx.fuse.client.shoppingmall.BusinessManageClientAction;
import com.netx.fuse.client.shoppingmall.SellerClientAction;
import com.netx.fuse.client.ucenter.SensitiveClientAction;
import com.netx.fuse.client.worth.*;
import com.netx.shopping.model.business.Seller;
import com.netx.shopping.model.business.SellerManage;
import com.netx.shopping.model.merchantcenter.MerchantManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 远程调用工具类
 *
 */

@Component
public class ClientProxy {
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    SellerClientAction sellerClient;
    @Autowired
    BusinessManageClientAction businessManageClient;
    @Autowired
    SensitiveClientAction sensitiveClient;
    @Autowired
    CleanClientAction cleanClient;
    @Autowired
    DemandClientAction demandClient;
    @Autowired
    WishClientAction wishClient;
    @Autowired
    SkillClientAction skillClient;
    @Autowired
    MeetingClientAction meetingClient;
    @Autowired
    InvitationClientAction invitationClient;
    @Autowired
    SellerClientAction sellerClientAction;

    public String getSellerLegalMobile(String merchantId) {
        String mobile = null;
        MerchantManager verify = sellerClientAction.getMerchantManagerByMerchantId(merchantId, 2);
        if (verify != null) {
            mobile = verify.getUserPhone();
        }
        return mobile;
    }

//    public GetSellerManageIdAndMoneyPhoneVo getSellerManageIdAndMoneyPhone(String sellerId) {
//        GetSellerManageIdAndMoneyPhoneVo getSellerManageIdAndMoneyPhoneVo = new GetSellerManageIdAndMoneyPhoneVo();
//        Seller result = sellerClient.getSellerById(sellerId);
//        if (result != null) {
//            getSellerManageIdAndMoneyPhoneVo.setMoneyPhone(result.getMoneyPhone());
//            getSellerManageIdAndMoneyPhoneVo.setManageId(result.getManageId());
//        }
//        return getSellerManageIdAndMoneyPhoneVo;
//    }

    public String getMange(String manageId) {
        SellerManage result = businessManageClient.getManage(manageId);
        String manageUserId = null;
        if (result != null) {
            manageUserId = result.getUserId();
        }
        return manageUserId;
    }

    /**
     * 清除网值中关于这个用户所有数据
     * */
    public boolean clean(String userId) {
        //TODO 网值数据清除
        /*Result result=cleanClient.clean(userId);
        if (result.getCode() == 0) {
            logger.info("远程调用成功");
            return (boolean) result.getObject();
        }
        logger.error("远程调用失败");*/
        return false;
    }

    public Boolean checkNetx(String userId){
        return checkDemand(userId) && checkWish(userId) && checkSkill(userId) && checkMeeting(userId) && checkInvitation(userId);
    }

    public Boolean checkDemand(String userId){
        //TODO 需求检查
        /*Result result = demandClient.checkHasUnComplete(userId);
        if(result.getCode()==0){
            logger.info("远程调用成功");
            return (Boolean) result.getObject();
        }
        logger.error("远程调用失败");*/
        return false;
    }

    public Boolean checkWish(String userId){
        //TODO 心愿检查
        /*Result result = wishClient.checkHasUnComplete(userId);
        if(result.getCode()==0){
            logger.info("远程调用成功");
            return (Boolean) result.getObject();
        }
        logger.error("远程调用失败");*/
        return false;
    }

    public Boolean checkSkill(String userId){
        //TODO 技能检查
        /*Result result = skillClient.checkHasUnComplete(userId);
        if(result.getCode()==0){
            logger.info("远程调用成功");
            return (Boolean) result.getObject();
        }
        logger.error("远程调用失败");*/
        return false;
    }

    public Boolean checkMeeting(String userId){
        //TODO 活动检查
        /*Result result = meetingClient.checkHasUnComplete(userId);
        if(result.getCode()==0){
            logger.info("远程调用成功");
            return (Boolean) result.getObject();
        }
        logger.error("远程调用失败");*/
        return false;
    }

    public Boolean checkInvitation(String userId){
        //TODO 邀请检查
        /*Result result = invitationClient.checkHasUnComplete(userId);
        if(result.getCode()==0){
            logger.info("远程调用成功");
            return (Boolean) result.getObject();
        }
        logger.error("远程调用失败");*/
        return false;
    }
}
