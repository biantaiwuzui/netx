package com.netx.shopping.vo;


import com.netx.shopping.model.ordercenter.constants.OrderStatusEnum;
import com.netx.shopping.model.ordercenter.constants.OrderTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@ApiModel
public class ChangeOrderStatusRequestDto {
    @ApiModelProperty("订单id")
    @NotBlank(message = "订单id不能为空")
    private String id;

    /**
     * 订单状态
     1：待付款
     2：待发货
     3：物流中
     4：退货中
     5：投诉中
     6：待评论
     7：已完成
     8：已取消
     9: 待生成
     10: 已付款
     11: 已服务
     */
    @ApiModelProperty("订单状态")
    @NotNull(message = "订单状态不能为空")
    private OrderStatusEnum statusEnum;

    @ApiModelProperty(value = "订单类型", required = true)
    @NotNull(message = "订单类型不能为空")
    private OrderTypeEnum orderTypeEnum;

    @ApiModelProperty(value = "网能id", required = true)
    private String typeId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OrderStatusEnum getStatusEnum() {
        return statusEnum;
    }

    public void setStatusEnum(OrderStatusEnum statusEnum) {
        this.statusEnum = statusEnum;
    }

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
