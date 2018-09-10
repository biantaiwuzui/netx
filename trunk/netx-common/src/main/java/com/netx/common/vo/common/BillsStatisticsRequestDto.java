package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class BillsStatisticsRequestDto {
    @ApiModelProperty(required = true, notes = "用户id")
    private String userId;
    @ApiModelProperty("网币id")
    private String currencyId;
    @ApiModelProperty("流水类型:0平台1经营")
    private Integer type;

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
