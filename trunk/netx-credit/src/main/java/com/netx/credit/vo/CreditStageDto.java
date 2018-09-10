package com.netx.credit.vo;


import com.netx.credit.model.constants.CreditStageNameSetting;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

public class CreditStageDto {

    @ApiModelProperty("用户等级名称")
    private List<CreditStageNameSetting> stageName;

    @ApiModelProperty("认购开始日期")
    private Date startDate;

    @ApiModelProperty("认购结束日期")
    private Date endDate;

    @ApiModelProperty("申购比列")
    private double subscriptionRatio;


    public List<CreditStageNameSetting> getStageName() {
        return stageName;
    }

    public void setStageName(List<CreditStageNameSetting> stageName) {
        this.stageName = stageName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getSubscriptionRatio() {
        return subscriptionRatio;
    }

    public void setSubscriptionRatio(double subscriptionRatio) {
        this.subscriptionRatio = subscriptionRatio;
    }
}
