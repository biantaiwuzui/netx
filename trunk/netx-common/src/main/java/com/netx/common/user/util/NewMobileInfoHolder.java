package com.netx.common.user.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NewMobileInfoHolder {
    //官网的URL
    @Value("${netx.alidayu.url}")
    private String url;
    //成为开发者，创建应用后系统自动生成
    @Value("${netx.alidayu.newappkey}")
    private String appkey;
    @Value("${netx.alidayu.newsecret}")
    private String secret;
    //短信签名
    @Value("${netx.alidayu.signName}")
    private String signName;
    @Value("${netx.alidayu.domain}")
    private String domain;
    @Value("${netx.alidayu.product}")
    private String product;

    public String getDomain() {
        return domain;
    }

    public String getProduct() {
        return product;
    }

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
