package com.netx.shopping.vo;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ProductOrderBean {

    @ApiModelProperty("商品id")
    @NotBlank(message = "商品id不能为空")
    private String productId;

    @ApiModelProperty("商品名")
    @NotBlank(message = "商品名不能为空")
    private String productName;

    @ApiModelProperty("商品库存id")
    @NotBlank(message = "库存id不能为空")
    private String skuId;

    @ApiModelProperty("商品数量")
    @NotNull(message = "商品数量不能为空")
    @Min(value = 1, message = "数量必须大于0")
    private Integer quantity;

    @ApiModelProperty("配送方式")
    @NotNull(message = "配送方式不能为空")
    private Integer deliveryWay;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getDeliveryWay() {
        return deliveryWay;
    }

    public void setDeliveryWay(Integer deliveryWay) {
        this.deliveryWay = deliveryWay;
    }
}
