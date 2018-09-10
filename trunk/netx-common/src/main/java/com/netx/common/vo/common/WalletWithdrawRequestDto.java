package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Create by wongloong on 17-9-30
 */
@ApiModel
public class WalletWithdrawRequestDto {
    @ApiModelProperty(value = "提现金额", required = true)
    @NotNull
    @Range(min=0, max=10000,message = "单笔提现金额必须大于0.01元，小于1万元")
    private BigDecimal amount;

    @ApiModelProperty(value = "提现方式,0支付宝,1微信", required = true)
    @NotNull
    private int type;

    @ApiModelProperty(value = "对应想要提现到的账号id")
    @NotBlank
    private String accountId;

    private long cent;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setCent(long cent) {
        this.cent = cent;
    }

    public long getCent() {
        return cent;
    }
}
