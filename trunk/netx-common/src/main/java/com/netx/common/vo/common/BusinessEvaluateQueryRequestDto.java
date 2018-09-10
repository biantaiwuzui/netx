package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * Create by allen on 17-9-9
 */
@ApiModel
public class BusinessEvaluateQueryRequestDto extends PageRequestDto {

    @ApiModelProperty(value = "商家id，不能为空")
    @NotNull
    private String businessId;

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
}
