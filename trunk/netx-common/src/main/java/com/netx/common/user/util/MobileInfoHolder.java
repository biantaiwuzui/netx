package com.netx.common.user.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MobileInfoHolder {
    //官网的URL
    @Value("${netx.alidayu.url}")
    private String url;
    //成为开发者，创建应用后系统自动生成
    @Value("${netx.alidayu.appkey}")
    private String appkey;
    @Value("${netx.alidayu.secret}")
    private String secret;
    //短信签名
    @Value("${netx.alidayu.signName}")
    private String signName;

    public String getUrl() {
        return url;
    }

    public String getAppkey() {
        return appkey;
    }

    public String getSecret() {
        return secret;
    }

    public String getSignName() {
        return signName;
    }
}
