package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * Create by allen on 17-9-9
 */
@ApiModel
public class WetherExistEvaluateRequestDto{

    @ApiModelProperty(value = "用户ID，不能为空")
    @NotNull
    private String userId;
    @ApiModelProperty(value = "订单ID，不能为空")
    @NotNull
    private String orderId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
