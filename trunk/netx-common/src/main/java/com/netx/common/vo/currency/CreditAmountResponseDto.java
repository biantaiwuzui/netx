package com.netx.common.vo.currency;

import java.math.BigDecimal;

public class CreditAmountResponseDto {

    private BigDecimal applyAmount;

    private BigDecimal holdAmount;

    private BigDecimal price;

    private BigDecimal tradeAmount;

    private BigDecimal releaseAmount;

    private String newlyTime;

    private String description;

    public BigDecimal getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(BigDecimal applyAmount) {
        this.applyAmount = applyAmount;
    }

    public BigDecimal getHoldAmount() {
        return holdAmount;
    }

    public void setHoldAmount(BigDecimal holdAmount) {
        this.holdAmount = holdAmount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(BigDecimal tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public String getNewlyTime() {
        return newlyTime;
    }

    public void setNewlyTime(String newlyTime) {
        this.newlyTime = newlyTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getReleaseAmount() {
        return releaseAmount;
    }

    public void setReleaseAmount(BigDecimal releaseAmount) {
        this.releaseAmount = releaseAmount;
    }
}
