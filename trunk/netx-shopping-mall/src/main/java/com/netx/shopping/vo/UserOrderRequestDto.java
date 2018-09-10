package com.netx.shopping.vo;

import com.netx.common.vo.common.PageRequestDto;
import com.netx.shopping.model.ordercenter.constants.OrderQueryEnum;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

public class UserOrderRequestDto extends PageRequestDto {

    @ApiModelProperty("查询状态")
    @NotNull(message = "查询状态不能为空")
    private OrderQueryEnum orderQueryEnum;

    @ApiModelProperty("查询类型：查询售出的false，购买的true")
    @NotNull(message = "查询类型不能为空")
    private Boolean isQueryBuy;

    public OrderQueryEnum getOrderQueryEnum() {
        return orderQueryEnum;
    }

    public void setOrderQueryEnum(OrderQueryEnum orderQueryEnum) {
        this.orderQueryEnum = orderQueryEnum;
    }

    public Boolean getIsQueryBuy() {
        return isQueryBuy;
    }

    public void setIsQueryBuy(Boolean isQueryBuy) {
        this.isQueryBuy = isQueryBuy;
    }
}
