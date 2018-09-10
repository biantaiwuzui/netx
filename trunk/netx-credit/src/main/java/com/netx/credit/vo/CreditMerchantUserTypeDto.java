package com.netx.credit.vo;

import io.swagger.annotations.ApiModelProperty;

public class CreditMerchantUserTypeDto {

    @ApiModelProperty("商家类型 1.法人代表 2. 业务主管 3. 收银人员")
    private String merchantType;

    @ApiModelProperty("用户名称")
    private String realName;

    @ApiModelProperty("认购状态")
    private Integer status;

    @ApiModelProperty("认购金额")
    private Double amount;


    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}

