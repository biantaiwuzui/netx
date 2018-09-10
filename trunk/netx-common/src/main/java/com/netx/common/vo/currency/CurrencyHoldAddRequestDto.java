package com.netx.common.vo.currency;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 网信持有接口接口-添加持有接口dto
 * @Author hj.Mao
 * @Since 2017-12-5
 */
@ApiModel
public class CurrencyHoldAddRequestDto {

    @ApiModelProperty("用户id")
    @NotBlank(message = "用户id不能为空")
    private String userId;

    @ApiModelProperty("网信id,如果不传，系统会随机获取一种网信进行持有操作")
    private String currencyId;

    @ApiModelProperty("网信金额(以元为单位)")
    @NotNull
    private BigDecimal amount;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }
}
