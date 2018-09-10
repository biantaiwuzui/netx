package com.netx.shopping.vo;

import io.swagger.annotations.ApiModelProperty;

public class DelectAddressRequestDto {
    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty(value = "地址")
    private String value;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
