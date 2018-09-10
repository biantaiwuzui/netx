package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Create by wongloong on 17-9-11
 */
@ApiModel
public class WzCommonCostSettingAddDto {
    @ApiModelProperty("费用id")
    private String id;
    @ApiModelProperty("活动、需求、技能、邀约、赠送分成")
    @NotNull
    private BigDecimal sharedFee;
    @ApiModelProperty("提现手续费")
    @NotNull
    private BigDecimal withdrawFee;
    @ApiModelProperty("注册商家管理费")
    @NotNull
    private BigDecimal shopManagerFee;
    @ApiModelProperty(required = true, value = "注册商家管理费有效期,0无限时间,其他，输入年数")
    private int shopManagerFeeLimitDate;
    @ApiModelProperty(required = true, value = "网币资金利息(月)")
    private BigDecimal currencyInst;
    @ApiModelProperty("网币发行保证金")
    @NotNull
    private BigDecimal currencyIssueFee;
    /**
     * 网币竞购系数
     */
    @ApiModelProperty("网币竞购系数")
    private BigDecimal currencyFundsInterest;
    /**
     * 网币排除认购费用
     */
    @ApiModelProperty("网币排队认购费用")
    private BigDecimal currencySubscribeFee;
    @ApiModelProperty("广告类图文、音视的点击费用")
    @NotNull
    private BigDecimal clickFee;
    @ApiModelProperty("违规图文、音视的点击费用")
    private BigDecimal violationClickFee;
    @ApiModelProperty("图文、音视的发布押金")
    private BigDecimal picAndVoicePublishDeposit;
    @ApiModelProperty("心愿资金管理费")
    private BigDecimal wishCapitalManageFee;
    @NotNull
    @ApiModelProperty("商品销售收入分成")
    private BigDecimal salersharedFee;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getSharedFee() {
        return sharedFee;
    }

    public void setSharedFee(BigDecimal sharedFee) {
        this.sharedFee = sharedFee;
    }

    public BigDecimal getWithdrawFee() {
        return withdrawFee;
    }

    public void setWithdrawFee(BigDecimal withdrawFee) {
        this.withdrawFee = withdrawFee;
    }

    public BigDecimal getShopManagerFee() {
        return shopManagerFee;
    }

    public void setShopManagerFee(BigDecimal shopManagerFee) {
        this.shopManagerFee = shopManagerFee;
    }

    public BigDecimal getCurrencyIssueFee() {
        return currencyIssueFee;
    }

    public void setCurrencyIssueFee(BigDecimal currencyIssueFee) {
        this.currencyIssueFee = currencyIssueFee;
    }

    public BigDecimal getClickFee() {
        return clickFee;
    }

    public void setClickFee(BigDecimal clickFee) {
        this.clickFee = clickFee;
    }

    public BigDecimal getSalersharedFee() {
        return salersharedFee;
    }

    public void setSalersharedFee(BigDecimal salersharedFee) {
        this.salersharedFee = salersharedFee;
    }

    public int getShopManagerFeeLimitDate() {
        return shopManagerFeeLimitDate;
    }

    public void setShopManagerFeeLimitDate(int shopManagerFeeLimitDate) {
        this.shopManagerFeeLimitDate = shopManagerFeeLimitDate;
    }

    public BigDecimal getCurrencyInst() {
        return currencyInst;
    }

    public void setCurrencyInst(BigDecimal currencyInst) {
        this.currencyInst = currencyInst;
    }

    public BigDecimal getViolationClickFee() {
        return violationClickFee;
    }

    public void setViolationClickFee(BigDecimal violationClickFee) {
        this.violationClickFee = violationClickFee;
    }

    public BigDecimal getPicAndVoicePublishDeposit() {
        return picAndVoicePublishDeposit;
    }

    public void setPicAndVoicePublishDeposit(BigDecimal picAndVoicePublishDeposit) {
        this.picAndVoicePublishDeposit = picAndVoicePublishDeposit;
    }

    public BigDecimal getCurrencyFundsInterest() {
        return currencyFundsInterest;
    }

    public void setCurrencyFundsInterest(BigDecimal currencyFundsInterest) {
        this.currencyFundsInterest = currencyFundsInterest;
    }

    public BigDecimal getCurrencySubscribeFee() {
        return currencySubscribeFee;
    }

    public void setCurrencySubscribeFee(BigDecimal currencySubscribeFee) {
        this.currencySubscribeFee = currencySubscribeFee;
    }

    public BigDecimal getWishCapitalManageFee() {
        return wishCapitalManageFee;
    }

    public void setWishCapitalManageFee(BigDecimal wishCapitalManageFee) {
        this.wishCapitalManageFee = wishCapitalManageFee;
    }
}
