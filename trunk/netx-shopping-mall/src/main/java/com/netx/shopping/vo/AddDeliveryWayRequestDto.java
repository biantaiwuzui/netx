package com.netx.shopping.vo;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class AddDeliveryWayRequestDto {

    @ApiModelProperty("配送方式id，发布商品不传，编辑商品必传")
    private String id;

    @ApiModelProperty("配送方式\n" +
            "1：支持第三方配送\n" +
            "2：不提供配送，仅限现场消费\n" +
            "3：同时提供外卖配送")
    @NotNull(message = "配送方式不能为空")
    private Integer deliveryWay;

    @ApiModelProperty("配送费用")
    private BigDecimal shoppingFee;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getDeliveryWay() {
        return deliveryWay;
    }

    public void setDeliveryWay(Integer deliveryWay) {
        this.deliveryWay = deliveryWay;
    }

    public BigDecimal getShoppingFee() {
        return shoppingFee;
    }

    public void setShoppingFee(BigDecimal shoppingFee) {
        this.shoppingFee = shoppingFee;
    }
}
