package com.netx.common.vo.business;

import com.netx.common.vo.common.PageRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ApiModel
public class SearchSelleRequestDto  extends PageRequestDto{

    @NotNull
    @ApiModelProperty("搜索条件，1白名单，2黑名单")
    private int condition;

    @ApiModelProperty("经度")
    private BigDecimal lon;

    @ApiModelProperty("纬度")
    private BigDecimal lat;

    @ApiModelProperty("成交金额1")
    private BigDecimal amount1;

    @ApiModelProperty("成交金额2")
    private BigDecimal amount2;

    @ApiModelProperty("商品数量1")
    private Integer num1;

    @ApiModelProperty("商品数量2")
    private Integer num2;

    @ApiModelProperty("缴费状态，0已缴费，1待缴费,2待续费")
    private int payStatus;

    @ApiModelProperty("注册日期1")
    private String date1;

    @ApiModelProperty("注册日期2")
    private String date2;

    @ApiModelProperty("是否发行网币,0未发行，1发行")
    private int isIssue;

    @ApiModelProperty("搜索范围(1~10公里)")
    private Double distance;

    public BigDecimal getLon() {
        return lon;
    }

    public void setLon(BigDecimal lon) {
        this.lon = lon;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getAmount1() {
        return amount1;
    }

    public void setAmount1(BigDecimal amount1) {
        this.amount1 = amount1;
    }

    public BigDecimal getAmount2() {
        return amount2;
    }

    public void setAmount2(BigDecimal amount2) {
        this.amount2 = amount2;
    }

    public Integer getNum1() {
        return num1;
    }

    public void setNum1(Integer num1) {
        this.num1 = num1;
    }

    public Integer getNum2() {
        return num2;
    }

    public void setNum2(Integer num2) {
        this.num2 = num2;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public String getDate1() {
        return date1;
    }

    public void setDate1(String date1) {
        this.date1 = date1;
    }

    public String getDate2() {
        return date2;
    }

    public void setDate2(String date2) {
        this.date2 = date2;
    }

    public int getIsIssue() {
        return isIssue;
    }

    public void setIsIssue(int isIssue) {
        this.isIssue = isIssue;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}
