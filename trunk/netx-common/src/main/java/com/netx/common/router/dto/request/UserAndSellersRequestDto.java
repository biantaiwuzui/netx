package com.netx.common.router.dto.request;

import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 请求获取商品数量、商家数量的Response类
 * </p>
 *
 * @author 黎子安
 * @since 2017-10-13
 */
public class UserAndSellersRequestDto {
    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("网币适用范围商家id集")
    private String sellerIds;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSellerIds() {
        return sellerIds;
    }

    public void setSellerIds(String sellerIds) {
        this.sellerIds = sellerIds;
    }
}