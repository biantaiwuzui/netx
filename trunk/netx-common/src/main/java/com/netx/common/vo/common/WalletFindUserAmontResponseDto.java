package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * Create by wongloong on 17-9-17
 */
@ApiModel
public class WalletFindUserAmontResponseDto {
    @ApiModelProperty(value = "网币",required = true)
    private BigDecimal currencyAmount;

    @ApiModelProperty(value = "红包",required = true)
    private BigDecimal redpacketAmount;

    @ApiModelProperty(value = "钱包",required = true)
    private BigDecimal walletAmount;

    public BigDecimal getCurrencyAmount() {
        return currencyAmount;
    }

    public void setCurrencyAmount(BigDecimal currencyAmount) {
        this.currencyAmount = currencyAmount;
    }

    public BigDecimal getRedpacketAmount() {
        return redpacketAmount;
    }

    public void setRedpacketAmount(BigDecimal redpacketAmount) {
        this.redpacketAmount = redpacketAmount;
    }

    public BigDecimal getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(BigDecimal walletAmount) {
        this.walletAmount = walletAmount;
    }
}
