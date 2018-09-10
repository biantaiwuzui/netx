package com.netx.shopping.vo;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class GetMerchantManagerListRequestDto {

    @ApiModelProperty("商家注册者id")
    @NotBlank(message = "商家注册者id不能为空")
    private String userId;

    @ApiModelProperty("商家id")
    private String merchantId;

    @ApiModelProperty("人员类型： \n" +
            "0.查询收银员列表 \n" +
            "1.查询主管列表 \n" +
            "2.查询法人列表 \n" +
            "3.查询注册者列表 \n" +
            "4.查询特权行使人列表 \n" +
            "5.查询收银人员列表")
    @NotNull(message = "人员类型不能为空")
    private Integer merchantUserType;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getMerchantUserType() {
        return merchantUserType;
    }

    public void setMerchantUserType(Integer merchantUserType) {
        this.merchantUserType = merchantUserType;
    }
}
