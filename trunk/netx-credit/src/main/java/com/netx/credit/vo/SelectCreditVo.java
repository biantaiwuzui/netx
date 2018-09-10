package com.netx.credit.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author lanyingchu
 * @date 2018/7/30 11:20
 */
@ApiModel("我认购的网信")
public class SelectCreditVo {

    @ApiModelProperty("网信名称")
    private String creditName;

    @ApiModelProperty("用户信用值")
    private int creidtValue;

    @ApiModelProperty("与发布网信地址的距离")
    private double distance;

    @ApiModelProperty("持有数量")
    private double holdNumber;

    @ApiModelProperty("信用值排名")
    private double creditValueRanking;

    @ApiModelProperty("是否拥有网信,0表示未认购任何网信")
    private int status;

    public String getCreditName() {
        return creditName;
    }

    public void setCreditName(String creditName) {
        this.creditName = creditName;
    }

    public int getCreidtValue() {
        return creidtValue;
    }

    public void setCreidtValue(int creidtValue) {
        this.creidtValue = creidtValue;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getHoldNumber() {
        return holdNumber;
    }

    public void setHoldNumber(double holdNumber) {
        this.holdNumber = holdNumber;
    }

    public double getCreditValueRanking() {
        return creditValueRanking;
    }

    public void setCreditValueRanking(double creditValueRanking) {
        this.creditValueRanking = creditValueRanking;
    }

    public int isStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
