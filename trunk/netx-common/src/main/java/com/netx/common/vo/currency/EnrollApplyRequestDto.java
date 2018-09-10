package com.netx.common.vo.currency;

import com.netx.common.vo.common.FrozenAddRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * 报名申购dto
 */
@ApiModel
public class EnrollApplyRequestDto   extends FrozenAddRequestDto{
    @ApiModelProperty("报名费用")
    private BigDecimal enrollAmount;

    public BigDecimal getEnrollAmount() {
        return enrollAmount;
    }

    public void setEnrollAmount(BigDecimal enrollAmount) {
        this.enrollAmount = enrollAmount;
    }

}
