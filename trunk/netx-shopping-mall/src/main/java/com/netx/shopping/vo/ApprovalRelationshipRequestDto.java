package com.netx.shopping.vo;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

public class ApprovalRelationshipRequestDto {

    @ApiModelProperty("商家id")
    @NotNull(message = "商家id不能为空")
    private String sellerId;

    @ApiModelProperty("请求里的父商家id")
    @NotNull(message = "父商家id不能为空")
    private String pId;

    @ApiModelProperty("是否接受")
    @NotNull(message = "状态不能为空")
    private Boolean state;

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }
}
