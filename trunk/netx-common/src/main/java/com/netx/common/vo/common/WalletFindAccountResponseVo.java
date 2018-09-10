package com.netx.common.vo.common;

import java.util.Map;

/**
 * Create by wongloong on 17-9-17
 */
public class WalletFindAccountResponseVo {

    private String userId;

    /**
     * 微信提现账户
     */
    private String wechatAccount;

    /**
     * 阿里提现账户
     */
    private String aliAccount;

    /**
     * 使用过的支付宝账号
     */
    private Map<String,String> usedAliAccount;
    /**
     * 使用过的微信账号
     */
    private Map<String,String> usedWechatAccount;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWechatAccount() {
        return wechatAccount;
    }

    public void setWechatAccount(String wechatAccount) {
        this.wechatAccount = wechatAccount;
    }

    public String getAliAccount() {
        return aliAccount;
    }

    public void setAliAccount(String aliAccount) {
        this.aliAccount = aliAccount;
    }

    public Map<String, String> getUsedAliAccount() {
        return usedAliAccount;
    }

    public void setUsedAliAccount(Map<String, String> usedAliAccount) {
        this.usedAliAccount = usedAliAccount;
    }

    public Map<String, String> getUsedWechatAccount() {
        return usedWechatAccount;
    }

    public void setUsedWechatAccount(Map<String, String> usedWechatAccount) {
        this.usedWechatAccount = usedWechatAccount;
    }
}
