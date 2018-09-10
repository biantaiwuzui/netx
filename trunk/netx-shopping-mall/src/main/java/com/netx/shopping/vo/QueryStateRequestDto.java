package com.netx.shopping.vo;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

public class QueryStateRequestDto {

    @ApiModelProperty("商家id")
    @NotBlank(message = "商家id不能为空")
    private String merchantId;

    @ApiModelProperty("请求里的父商家id")
    @NotBlank(message = "请求里的父商家id不能为空")
    private String parentMerchant;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getParentMerchant() {
        return parentMerchant;
    }

    public void setParentMerchant(String parentMerchant) {
        this.parentMerchant = parentMerchant;
    }
}
