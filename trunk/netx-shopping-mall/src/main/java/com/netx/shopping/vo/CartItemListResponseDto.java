package com.netx.shopping.vo;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class CartItemListResponseDto extends OrderProductBean{

    @ApiModelProperty("购物id")
    private String cartId;
    @ApiModelProperty("清单id【cartItemId】")
    private String id;

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
