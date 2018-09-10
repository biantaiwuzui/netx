package com.netx.common.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@ApiModel
public class DeleteBusinessManageRequestDto {
    @ApiModelProperty("主管id")
    @NotBlank(message = "主管id不能为空")
    private String id;

    @ApiModelProperty("商家id")
    private String merchantId;

    @ApiModelProperty("人员类型： \n" +
            "0.收银员 \n" +
            "1.主管 \n" +
            "2.法人 \n" +
            "3.注册者 \n" +
            "4.特权行使人 \n" +
            "5.收银人员")
    @NotNull(message = "人员类型不能为空")
    private Integer merchantUserType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
