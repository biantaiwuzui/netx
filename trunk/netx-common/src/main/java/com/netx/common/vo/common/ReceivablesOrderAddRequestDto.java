package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Create by allen on 17-9-17
 */
@ApiModel
public class ReceivablesOrderAddRequestDto {

    @ApiModelProperty(value = "收款用户ID",required = true)
    @NotBlank
    private String toUserId;

    @NotNull
    @ApiModelProperty(name = "支付金额,最多两位小数点", required = true)
    private BigDecimal amount;

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
