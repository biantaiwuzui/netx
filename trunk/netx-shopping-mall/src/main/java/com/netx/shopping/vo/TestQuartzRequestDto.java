package com.netx.shopping.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created By wj.liu
 * Description: 测试定时任务请求参数对象
 * Date: 2017-11-06
 */
@ApiModel
public class TestQuartzRequestDto {
    @ApiModelProperty(value = "订单id")
    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
