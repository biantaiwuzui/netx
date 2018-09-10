package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@ApiModel
public class BillStatisticsRequestDto {
    @ApiModelProperty(required = true, notes = "用户id")
    private String userId;
    @NotNull
    @ApiModelProperty(required = true, notes = "查询类型,0为支出,1为收入")
    private Integer queryType;
    @ApiModelProperty("查询时间起")
    private Long startTime;
    @ApiModelProperty("查询时间止")
    private Long endTime;
    @ApiModelProperty("交易方式，0支付宝,1微信，2网币，3零钱")
    private Integer payChannel;
    @ApiModelProperty("网币id")
    private String currencyId;
    @ApiModelProperty("流水类型 0.平台 1.经营")
    private Integer type;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getQueryType() {
        return queryType;
    }

    public void setQueryType(Integer queryType) {
        this.queryType = queryType;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Integer getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(Integer payChannel) {
        this.payChannel = payChannel;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
