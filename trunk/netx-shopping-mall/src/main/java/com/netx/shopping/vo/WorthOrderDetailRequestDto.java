package com.netx.shopping.vo;

import com.netx.shopping.model.ordercenter.constants.OrderTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class WorthOrderDetailRequestDto {

    @ApiModelProperty("订单类型")
    @NotNull(message = "订单类型不能为空")
    private OrderTypeEnum orderTypeEnum;

    @ApiModelProperty("事件id")
    @NotBlank(message = "事件id不能为空")
    private String typeId;

    public OrderTypeEnum getOrderTypeEnum() {
        return orderTypeEnum;
    }

    public void setOrderTypeEnum(OrderTypeEnum orderTypeEnum) {
        this.orderTypeEnum = orderTypeEnum;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
}
