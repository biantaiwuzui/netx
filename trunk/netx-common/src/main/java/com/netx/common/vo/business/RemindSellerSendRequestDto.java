package com.netx.common.vo.business;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created By liwei
 * Description: 提醒商家发货
 * Date: 2017-11-03
 */
public class RemindSellerSendRequestDto {

    @ApiModelProperty("订单id")
    @NotBlank(message = "订单id不能为空")
    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
