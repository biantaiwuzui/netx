package com.netx.shopping.vo;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

public class AddRelationshipRequestDto {

    @ApiModelProperty("商家id")
    @NotNull(message = "商家id不能为空")
    private String sellerId;

    @ApiModelProperty("需添加的客服代理的客服代码")
    @NotNull(message = "客服代码不能为空")
    private String code;

    @ApiModelProperty("添加理由")
    @NotNull(message = "理由不能为空")
    private String reason;

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
