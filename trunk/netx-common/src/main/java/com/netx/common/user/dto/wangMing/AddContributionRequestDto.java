package com.netx.common.user.dto.wangMing;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class AddContributionRequestDto extends AddWangMingRecordSuperRequestDto {

    @NotNull(message = "本笔贡献不能为空")
    @ApiModelProperty("本笔贡献，收入支出用正负号标识")
    private BigDecimal contribution;

    public BigDecimal getContribution() {
        return contribution;
    }

    public void setContribution(BigDecimal contribution) {
        this.contribution = contribution;
    }
}
