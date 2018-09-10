package com.netx.common.vo.business;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@Api
public class SearchGoodsRequestDto {

    @NotNull
    @ApiModelProperty("搜索条件，1白名单，2黑名单")
    private int condition;

    @NotNull
    @ApiModelProperty("商家名称")
    private String sellerName;

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }
}
