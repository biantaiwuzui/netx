package com.netx.credit.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.credit.mapper.CreditSubscriptionMapper;
import com.netx.credit.model.Credit;
import com.netx.credit.model.CreditSubscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lanyingchu
 * @date 2018/7/6 15:16
 */
@Service
public class CreditSubscriptionService extends ServiceImpl<CreditSubscriptionMapper, CreditSubscription> {

    @Autowired
    CreditSubscriptionMapper creditSubscriptionMapper;
    /**
     * 根据网信id把相同的网信id的认购金额全部加起来
     */
    public Double countCreditMoney(String creditId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect ( "sum(subscription_number)" ).where ( "credit_id = {0}",creditId );
        return (Double) selectObj ( wrapper );
    }

    // 网信详情 - 根据 creditId 获取所有 内购好友id,认购状态，认购金额
    public List<CreditSubscription> getInnerFriendIdByCreditId(String creditId, String type) {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("user_id as userId, status, subscription_number as subscriptionNumber");
        wrapper.where("credit_id = {0} and type = {1}", creditId, type);
        return selectList(wrapper);
    }


    // 网信详情 - 根据 creditId 获取所有参与内购的商家人员的认购信息
    public List<CreditSubscription> getInnerMerchantManagerInfoByCreditId(String creditId, String merchantId) {
        EntityWrapper<CreditSubscription> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("user_id as userId, status,type, subscription_number as subscriptionNumber");
        wrapper.where("credit_id = {0} and merchant_id = {1}", creditId, merchantId);
        return selectList(wrapper);
    }
    // 网信详情 - 根据 creditId 获取所有参与内购的商家id
    public List<String> getInnerMerchangIdByCreditId(String creditId) {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("DISTINCT merchant_id");
        wrapper.where("credit_Id = {0}", creditId);
        return selectObjs(wrapper);
    }

    // 网信认购 - 获取认购详情
    public CreditSubscription getMerchantInnerCreditInfo(String userId, String type, String creditId, String merchantId) {
        EntityWrapper<CreditSubscription> wrapper = new EntityWrapper<>();
        if (merchantId == null) {
            wrapper.where("user_id = {0} and type = {1} and credit_id = {2}", userId, type, creditId);
            wrapper.isNull("merchant_id");
        } else {
            wrapper.where("user_id = {0} and type = {1} and credit_id = {2} and merchant_id = {3}", userId, type, creditId, merchantId);
        }
        return selectOne(wrapper);
    }

    //网信首页 - 根据UserId 获取 用户持有的网信
    public List<CreditSubscription> getCreditHoldByuserId(String userId){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.where("user_id = {0} and status = 1",userId);
        return selectList(wrapper);
    }


    /*用户是否持有网信**/
    public List<CreditSubscription> isHoldCredit(String userId, Integer status){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.where("user_id = {0} and status = {1} ",userId,status);
        return  this.selectList(wrapper);
    }

    // 编辑网信 - 获取该网信的所有受邀好友id
    public List<String> getInnerFriendIds(String creditId, String type) {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("user_id");
        wrapper.where("credit_id = {0} and type = {1}", creditId, type);
        return selectObjs(wrapper);
    }

    // 网信开放认购详情 - 获取持有人列表 - 获取所有认购了该网信的用户信息
    public List<CreditSubscription> getHolderListByCreditId(String creditId) {
        EntityWrapper<CreditSubscription> wrapper = new EntityWrapper<>();
        wrapper.where("credit_id = {0} and status = {1}", creditId, 1);
        return selectList(wrapper);
    }

    // 网信详情 - 判断用户身份
    public String selectUserType(String creditId, String userId) {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("id");
        wrapper.where("credit_id = {0} and user_id = {1} and credit_stage_id = {2}", creditId, userId, "CSN_TOP");
        return (String)selectObj(wrapper);
    }

    //网信详情 - 判断用户认购状态
    public Integer selectUserStatus(String creditId, String userId) {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("status");
        wrapper.where("credit_id = {0} and user_id = {1} ", creditId, userId);
        return (int)selectObj(wrapper);
    }


}
