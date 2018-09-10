package com.netx.shopping.vo;

import com.netx.shopping.model.ordercenter.constants.OrderTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

public class AddOrderRequestDto {

    @ApiModelProperty(value = "订单类型：普通订单传NORMAL_ORDER", required = true)
    @NotNull(message = "订单类型不能为空")
    private OrderTypeEnum orderTypeEnum;

    @ApiModelProperty(value = "hash值", required = true)
    @NotBlank(message = "hash值不能为空")
    private String hash;

    @ApiModelProperty("商家id")
    @NotBlank(message = "商家id不能为空")
    private String merchantId;

    @ApiModelProperty("商品列表")
    @NotNull(message = "商品列表不能为空")
    @Size(min = 1,message = "最少加入一件商品")
    private List<ProductOrderBean> list;

    public OrderTypeEnum getOrderTypeEnum() {
        return orderTypeEnum;
    }

    public void setOrderTypeEnum(OrderTypeEnum orderTypeEnum) {
        this.orderTypeEnum = orderTypeEnum;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public List<ProductOrderBean> getList() {
        return list;
    }

    public void setList(List<ProductOrderBean> list) {
        this.list = list;
    }
}
