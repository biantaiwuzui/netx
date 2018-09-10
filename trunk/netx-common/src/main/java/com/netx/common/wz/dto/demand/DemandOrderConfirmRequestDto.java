package com.netx.common.wz.dto.demand;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

public class DemandOrderConfirmRequestDto {

    @ApiModelProperty(value = "需求单ID")
    @NotBlank(message = "需求单ID不能为空")
    private String demandId;

    @ApiModelProperty(value = "开始时间")
    @NotNull(message = "开始时间不能为空")
    private Date startAt;

    @ApiModelProperty(value = "结束时间")
    @NotNull(message = "结束时间不能为空")
    private Date endAt;

    @ApiModelProperty(value = "时间单位")
    private String unit;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "经度")
    @NotNull(message = "经度不能为空")
    @Range(min = -180,max = 180,message = "经度输入不在范围内")
    private BigDecimal lon;

    @ApiModelProperty(value = "纬度")
    @NotNull(message = "纬度不能为空")
    @Range(min = -90,max = 90,message = "纬度输入不在范围内")
    private BigDecimal lat;

    @ApiModelProperty(value = "订单ID，逗号分隔")
    private String orderIds;

    @ApiModelProperty(value = "订单消费")
    private BigDecimal orderPriceBig;

    @ApiModelProperty(value = "报酬，根据isEachWage判断是总报酬还是单位报酬")
    private BigDecimal wage;

    @ApiModelProperty(value = "是否为单位报酬，即：以上报酬是否为每个入选者的单位报酬")
    private Boolean isEachWage;

    @ApiModelProperty(value = "托管保证金")
    private BigDecimal bailBig=BigDecimal.ZERO;

    @ApiModelProperty(value = "是否支付（托管）")
    private Boolean isPay;

    public String getDemandId() {
        return demandId;
    }

    public void setDemandId(String demandId) {
        this.demandId = demandId;
    }

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Long startAt) {
        if(startAt!=null){
            this.startAt = new Date(startAt);
        }
    }

    public Date getEndAt() {
        return endAt;
    }

    public void setEndAt(Long endAt) {
        if(endAt!=null){
            this.endAt = new Date(endAt);
        }
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

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

    public String getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(String orderIds) {
        this.orderIds = orderIds;
    }

    public BigDecimal getOrderPriceBig() {
        return orderPriceBig;
    }

    public void setOrderPriceBig(BigDecimal orderPriceBig) {
        this.orderPriceBig = orderPriceBig;
    }

    public BigDecimal getBailBig() {
        return bailBig;
    }

    public void setBailBig(BigDecimal bailBig) {
        if(bailBig!=null){
            this.bailBig = bailBig;
        }
    }

    public BigDecimal getWage() {
        return wage;
    }

    public void setWage(BigDecimal wage) {
        this.wage = wage;
    }

    public Boolean getEachWage() {
        return isEachWage;
    }

    public void setEachWage(Boolean eachWage) {
        isEachWage = eachWage;
    }

    public Boolean getPay() {
        return isPay;
    }

    public void setPay(Boolean pay) {
        isPay = pay;
    }

}
