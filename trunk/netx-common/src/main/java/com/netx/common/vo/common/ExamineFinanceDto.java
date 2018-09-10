package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@ApiModel
public class ExamineFinanceDto {

    @ApiModelProperty(value = "银行利息", required = true)
    @NotNull
    private BigDecimal interest;
    @ApiModelProperty(value = "差额", required = true)
    @NotNull
    private BigDecimal checkDifference;
    @ApiModelProperty(value = "依据以及原因", required = true)
    private String adjustAccountReason;
    @ApiModelProperty(value = "提现金额", required = true)
    @NotNull
    private BigDecimal extractMoney;
    @ApiModelProperty(value = "提现原因", required = true)
    private String extractMoneyReason;
    @ApiModelProperty(value = "用户id", required = true)
    @NotNull
    private String createUser;

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public BigDecimal getCheckDifference() {
        return checkDifference;
    }

    public void setCheckDifference(BigDecimal checkDifference) {
        this.checkDifference = checkDifference;
    }

    public String getAdjustAccountReason() {
        return adjustAccountReason;
    }

    public void setAdjustAccountReason(String adjustAccountReason) {
        this.adjustAccountReason = adjustAccountReason;
    }

    public BigDecimal getExtractMoney() {
        return extractMoney;
    }

    public void setExtractMoney(BigDecimal extractMoney) {
        this.extractMoney = extractMoney;
    }

    public String getExtractMoneyReason() {
        return extractMoneyReason;
    }

    public void setExtractMoneyReason(String extractMoneyReason) {
        this.extractMoneyReason = extractMoneyReason;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
}
