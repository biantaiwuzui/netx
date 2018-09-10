package com.netx.common.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * @author lanyingchu
 * @date 2018/7/16 17:01
 */
@ApiModel
public class SelectSellerListByStatusAndSupportCreditDto {

    @NotNull(message = "缴费状态不能为空")
    @ApiModelProperty("缴费状态")
    private Integer payStatus;

    @ApiModelProperty("是否支持网信")
    private Boolean isHoldCredit;

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Boolean getHoldCredit() {
        return isHoldCredit;
    }

    public void setHoldCredit(Boolean holdCredit) {
        isHoldCredit = holdCredit;
    }
}
