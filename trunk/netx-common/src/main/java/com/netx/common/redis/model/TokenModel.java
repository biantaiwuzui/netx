package com.netx.common.redis.model;

import io.swagger.annotations.ApiModelProperty;

/**
 * Token 的 Model 类，可以增加字段提高安全性，例如时间戳、url 签名
 * @author 黎子安
 * @date 2017/9/20.
 */
public class TokenModel {

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private String userId;
    /**
     * 用户token
     */
    @ApiModelProperty("用户token")
    private String token;
    public TokenModel (String userId, String token) {
        this.userId = userId;
        this.token = token;
    }
    public TokenModel (){}
    public String getUserId () {
        return userId;
    }
    public void setUserId (String userId) {
        this.userId = userId;
    }
    public String getToken () {
        return token;
    }
    public void setToken (String token) {
        this.token = token;
    }
}