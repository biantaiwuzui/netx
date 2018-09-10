package com.netx.common.vo.common;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 费用设置查询返回对象dto
 * hj.Mao
 * 2017-11-15
 */
public class CostSettingResponseDto implements Serializable {
    /**
     * 活动、需求、技能、邀约、赠送分成
     */
    @ApiModelProperty("活动、需求、技能、邀约、赠送分成")
    private BigDecimal sharedFee;
    /**
     * 提现手续费
     */
    @ApiModelProperty("提现手续费")
    private BigDecimal withdrawFee;
    /**
     * 注册商家管理费
     */
    @ApiModelProperty("注册商家管理费")
    private BigDecimal shopManagerFee;
    /**
     * 注册商家管理费有效期
     */
    @ApiModelProperty("注册商家管理费有效期")
    private int shopManagerFeeLimitDate;
    /**
     * 网币发行费
     */
    @ApiModelProperty("网币发行费")
    private BigDecimal currencyIssueFee;
    /**
     * 网币竞购系数
     */
    @ApiModelProperty("网币竞购系数")
    private BigDecimal currencyFundsInterest;
    /**
     * 网币报名认购费用
     */
    @ApiModelProperty("网币报名认购费用")
    private BigDecimal currencySubscribeFee;
    /**
     * 网币资金利息
     */
    @ApiModelProperty("网币资金利息")
    private BigDecimal currencyInst;

    /**
     * 图文、音视的发布押金
     */
    @ApiModelProperty("图文、音视的发布押金")
    private BigDecimal picAndVoicePublishDeposit;

    /**
     * 图文、音视的点击费用
     */
    @ApiModelProperty("图文、音视的点击费用")
    private BigDecimal clickFee;
    /**
     * 违规图文、音视的点击费用
     */
    @ApiModelProperty("违规图文、音视的点击费用")
    private BigDecimal violationClickFee;

    /**
     * 心愿资金管理费
     */
    @ApiModelProperty("心愿资金管理费")
    private BigDecimal wishCapitalManageFee;
    /**
     * 商品销售收入分成
     */
    @ApiModelProperty("商品销售收入分成")
    private BigDecimal salersharedFee;

    public BigDecimal getSharedFee() { return sharedFee; }

    public void setSharedFee(BigDecimal sharedFee) { this.sharedFee = sharedFee; }

    public BigDecimal getWithdrawFee() { return withdrawFee; }

    public void setWithdrawFee(BigDecimal withdrawFee) { this.withdrawFee = withdrawFee; }

    public BigDecimal getShopManagerFee() { return shopManagerFee; }

    public void setShopManagerFee(BigDecimal shopManagerFee) { this.shopManagerFee = shopManagerFee; }

    public int getShopManagerFeeLimitDate() { return shopManagerFeeLimitDate; }

    public void setShopManagerFeeLimitDate(int shopManagerFeeLimitDate) { this.shopManagerFeeLimitDate = shopManagerFeeLimitDate; }

    public BigDecimal getCurrencyIssueFee() { return currencyIssueFee; }

    public void setCurrencyIssueFee(BigDecimal currencyIssueFee) { this.currencyIssueFee = currencyIssueFee; }

    public BigDecimal getCurrencyFundsInterest() { return currencyFundsInterest; }

    public void setCurrencyFundsInterest(BigDecimal currencyFundsInterest) { this.currencyFundsInterest = currencyFundsInterest; }

    public BigDecimal getCurrencySubscribeFee() { return currencySubscribeFee; }

    public void setCurrencySubscribeFee(BigDecimal currencySubscribeFee) { this.currencySubscribeFee = currencySubscribeFee; }

    public BigDecimal getCurrencyInst() { return currencyInst; }

    public void setCurrencyInst(BigDecimal currencyInst) { this.currencyInst = currencyInst; }

    public BigDecimal getPicAndVoicePublishDeposit() { return picAndVoicePublishDeposit; }

    public void setPicAndVoicePublishDeposit(BigDecimal picAndVoicePublishDeposit) { this.picAndVoicePublishDeposit = picAndVoicePublishDeposit; }

    public BigDecimal getClickFee() { return clickFee; }

    public void setClickFee(BigDecimal clickFee) { this.clickFee = clickFee; }

    public BigDecimal getViolationClickFee() { return violationClickFee; }

    public void setViolationClickFee(BigDecimal violationClickFee) { this.violationClickFee = violationClickFee; }

    public BigDecimal getWishCapitalManageFee() { return wishCapitalManageFee; }

    public void setWishCapitalManageFee(BigDecimal wishCapitalManageFee) { this.wishCapitalManageFee = wishCapitalManageFee;}

    public BigDecimal getSalersharedFee() { return salersharedFee; }

    public void setSalersharedFee(BigDecimal salersharedFee) { this.salersharedFee = salersharedFee; }

    @Override
    public String toString() {
        return "CostSettingResponseDto{" +
                "sharedFee=" + sharedFee +
                ", withdrawFee=" + withdrawFee +
                ", shopManagerFee=" + shopManagerFee +
                ", shopManagerFeeLimitDate=" + shopManagerFeeLimitDate +
                ", currencyIssueFee=" + currencyIssueFee +
                ", currencyFundsInterest=" + currencyFundsInterest +
                ", currencySubscribeFee=" + currencySubscribeFee +
                ", currencyInst=" + currencyInst +
                ", picAndVoicePublishDeposit=" + picAndVoicePublishDeposit +
                ", clickFee=" + clickFee +
                ", violationClickFee=" + violationClickFee +
                ", wishCapitalManageFee=" + wishCapitalManageFee +
                ", salersharedFee=" + salersharedFee +
                '}';
    }
}
