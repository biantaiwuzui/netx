package com.netx.common.user.dto.wangMing;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class AddIncomeRecordRequestDto extends AddWangMingRecordSuperRequestDto {

    @NotNull(message = "本笔收益不能为空")
    @ApiModelProperty("本笔收益，收入支出用正负号标识")
    private BigDecimal income;

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }
}
