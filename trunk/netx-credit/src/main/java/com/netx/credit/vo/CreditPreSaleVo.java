package com.netx.credit.vo;

import com.netx.credit.model.CreditDividendSetting;
import com.netx.credit.model.CreditLevelDiscountSetting;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author 梓
 * @date 2018-08-04 14:57
 */

public class CreditPreSaleVo {

    @ApiModelProperty("网信头像")
    private String creditPicture;

    @ApiModelProperty("网信名称")
    private String creditName;

    @ApiModelProperty("网信发布者信用值")
    private Integer userCredit;

    @ApiModelProperty("网信发布者信息完善度")
    private Integer userProfileScore;

    @ApiModelProperty("距离")
    private Double distance;

    @ApiModelProperty(value = "网信申购折扣")
    private Double creditLevelDiscountSettings;

    @ApiModelProperty("网信已售金额")
    private Double soldAmount;

    @ApiModelProperty("网信预售总金额")
    private Integer presaleUpperLimit;

    @ApiModelProperty("网信销售剩余百分比")
    private Integer creditPercentRemaining;

    @ApiModelProperty(value = "网信分红")
    private List<CreditDividendSetting> creditDividendSettings;

    public String getCreditPicture() {
        return creditPicture;
    }

    public void setCreditPicture(String creditPicture) {
        this.creditPicture = creditPicture;
    }

    public String getCreditName() {
        return creditName;
    }

    public void setCreditName(String creditName) {
        this.creditName = creditName;
    }

    public Integer getUserCredit() {
        return userCredit;
    }

    public void setUserCredit(Integer userCredit) {
        this.userCredit = userCredit;
    }

    public Integer getUserProfileScore() {
        return userProfileScore;
    }

    public void setUserProfileScore(Integer userProfileScore) {
        this.userProfileScore = userProfileScore;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }


    public Double getCreditLevelDiscountSettings() {
        return creditLevelDiscountSettings;
    }

    public void setCreditLevelDiscountSettings(Double creditLevelDiscountSettings) {
        this.creditLevelDiscountSettings = creditLevelDiscountSettings;
    }

    public Double getSoldAmount() {
        return soldAmount;
    }

    public void setSoldAmount(Double soldAmount) {
        this.soldAmount = soldAmount;
    }

    public Integer getPresaleUpperLimit() {
        return presaleUpperLimit;
    }

    public void setPresaleUpperLimit(Integer presaleUpperLimit) {
        this.presaleUpperLimit = presaleUpperLimit;
    }

    public Integer getCreditPercentRemaining() {
        return creditPercentRemaining;
    }

    public void setCreditPercentRemaining(Integer creditPercentRemaining) {
        this.creditPercentRemaining = creditPercentRemaining;
    }


    public List<CreditDividendSetting> getCreditDividendSettings() {
        return creditDividendSettings;
    }

    public void setCreditDividendSettings(List<CreditDividendSetting> creditDividendSettings) {
        this.creditDividendSettings = creditDividendSettings;
    }
}
