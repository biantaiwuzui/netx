package com.netx.shopping.vo;

import io.swagger.annotations.ApiModelProperty;

public class GetMerchantByUserIdRequestDto {

    @ApiModelProperty("用户id")
    private String userId;
    @ApiModelProperty("1为经营商家，2为收藏商家，3为注册商家")
    private int index;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
