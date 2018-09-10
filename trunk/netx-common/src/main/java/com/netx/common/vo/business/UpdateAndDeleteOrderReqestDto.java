package com.netx.common.vo.business;

import io.swagger.annotations.ApiModelProperty;

public class UpdateAndDeleteOrderReqestDto {

    @ApiModelProperty(required = true, value = "订单id")
    private String orderId;

    @ApiModelProperty(required = true, value = "商品数量")
    private Integer quanty;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getQuanty() {
        return quanty;
    }

    public void setQuanty(Integer quanty) {
        this.quanty = quanty;
    }
}
