package com.netx.credit.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author 梓
 * @date 2018-08-02 18:18
 */

public class CreditHoldDto {

    @ApiModelProperty("网信名称")
    private String name;

    @ApiModelProperty("发布者信用值")
    private Integer userCredit;

    @ApiModelProperty("资料完整度分值")
    private Integer userProfileScore;

    @ApiModelProperty("网信持有数量")
    private Double subscriptionNumber;

    @ApiModelProperty("上月收益")
    private Double LastMonthEarnings;

    @ApiModelProperty("累计收益")
    private Double totalEarnings;

    @ApiModelProperty("距离")
    private Double distance;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Double getSubscriptionNumber() {
        return subscriptionNumber;
    }

    public void setSubscriptionNumber(Double subscriptionNumber) {
        this.subscriptionNumber = subscriptionNumber;
    }

    public Double getLastMonthEarnings() {
        return LastMonthEarnings;
    }

    public void setLastMonthEarnings(Double lastMonthEarnings) {
        LastMonthEarnings = lastMonthEarnings;
    }

    public Double getTotalEarnings() {
        return totalEarnings;
    }

    public void setTotalEarnings(Double totalEarnings) {
        this.totalEarnings = totalEarnings;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}
