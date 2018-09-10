package com.netx.common.vo.business;

import io.swagger.annotations.ApiModelProperty;

public class GetCustomerServiceAgentRequestDto {
    @ApiModelProperty("商家id")
    private String sellerId;

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
}
