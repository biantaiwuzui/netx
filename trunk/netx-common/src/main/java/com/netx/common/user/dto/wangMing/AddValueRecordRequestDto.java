package com.netx.common.user.dto.wangMing;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class AddValueRecordRequestDto extends AddWangMingRecordSuperRequestDto {

    @NotNull(message = "本笔身价不能为空")
    @ApiModelProperty("本笔身价，收入支持用正负号标识")
    private BigDecimal value;

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
