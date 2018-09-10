package com.netx.common.vo.business;

import com.netx.common.vo.common.PageRequestDto;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

public class GetListBySellerIdDto extends PageRequestDto{

    @ApiModelProperty("商家id")
    @NotNull(message = "商家id不能为空")
    private String sellerId;

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
}
