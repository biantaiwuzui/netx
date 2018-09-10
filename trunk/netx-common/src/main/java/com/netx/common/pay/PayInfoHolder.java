package com.netx.common.pay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PayInfoHolder {
    @Value("${netx.alipay.openUrl}")
    private String openUrl;
    @Value("${netx.alipay.appPublicKey}")
    private String publicKey;
    @Value("${netx.alipay.aliPublicKey}")
    private String aliPublicKey;
    @Value("${netx.alipay.appId}")
    private String appId;
    @Value("${netx.alipay.notifyUrl}")
    private String notifyUrl;
    @Value("${netx.alipay.pId}")
    private String pId;
    @Value("${netx.alipay.appPrivateKey}")
    private String privateKey;
    @Value("${localHost.host}")
    private String host;
    @Value("${netx.wechat.appid}")
    private String wechatAppId;
    @Value("${netx.wechat.mch_id}")
    private String wechatMchId;
    @Value("${netx.wechat.api_key}")
    private String wechatApiKey;
    @Value("${netx.wechat.notifyUrl}")
    private String wechatNotifyUrl;
    @Value("${netx.wechat.order_url}")
    private String wechatOrderUrl;
    @Value("${netx.wechat.query_url}")
    private String wechatQueryOrderUrl;
    @Value("${netx.wechat.withdraw_url}")
    private String wechatWithdrawUrl;

    public String getHost() {
        return host;
    }

    public String getOpenUrl() {
        return openUrl;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getAliPublicKey() {
        return aliPublicKey;
    }

    public String getAppId() {
        return appId;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public String getpId() {
        return pId;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getWechatAppId() {
        return wechatAppId;
    }

    public String getWechatMchId() {
        return wechatMchId;
    }

    public String getWechatApiKey() {
        return wechatApiKey;
    }

    public String getWechatNotifyUrl() {
        return wechatNotifyUrl;
    }

    public String getWechatOrderUrl() {
        return wechatOrderUrl;
    }

    public String getWechatQueryOrderUrl() {
        return wechatQueryOrderUrl;
    }

    public String getWechatWithdrawUrl() {
        return wechatWithdrawUrl;
    }
}
