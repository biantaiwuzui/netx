package com.netx.shopping.vo;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

public class DelCartProductRequestDto {

    @ApiModelProperty("商家id")
    @NotBlank(message = "商家id不能为空")
    private String merchantId;

    @ApiModelProperty("购物车的中的清单id")
    private String cartItemId;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(String cartItemId) {
        this.cartItemId = cartItemId;
    }
}
