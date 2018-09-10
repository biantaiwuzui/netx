package com.netx.common.vo.business;

import io.swagger.annotations.ApiModel;

@ApiModel
public class CheckIsSendRequestDto {

    private String goodsOrderId;

    public String getGoodsOrderId() {
        return goodsOrderId;
    }

    public void setGoodsOrderId(String goodsOrderId) {
        this.goodsOrderId = goodsOrderId;
    }
}
