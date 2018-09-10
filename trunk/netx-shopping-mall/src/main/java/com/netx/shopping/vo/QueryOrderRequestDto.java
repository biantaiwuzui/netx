package com.netx.shopping.vo;

import com.netx.shopping.model.ordercenter.constants.OrderStatusEnum;
import io.swagger.annotations.ApiModelProperty;

public class QueryOrderRequestDto {

    @ApiModelProperty("商家id")
    private String merchantId;

    @ApiModelProperty("状态")
    private OrderStatusEnum orderStatusEnum;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public OrderStatusEnum getOrderStatusEnum() {
        return orderStatusEnum;
    }

    public void setOrderStatusEnum(OrderStatusEnum orderStatusEnum) {
        this.orderStatusEnum = orderStatusEnum;
    }
}
