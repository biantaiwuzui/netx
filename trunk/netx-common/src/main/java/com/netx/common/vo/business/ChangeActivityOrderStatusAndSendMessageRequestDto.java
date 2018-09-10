package com.netx.common.vo.business;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

public class ChangeActivityOrderStatusAndSendMessageRequestDto {

    @ApiModelProperty("订单id")
    @NotBlank(message = "订单id不能为空")
    private String orderId;

    @ApiModelProperty("开始时间戳")
    private Integer startedAt;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Integer startedAt) {
        this.startedAt = startedAt;
    }
}
