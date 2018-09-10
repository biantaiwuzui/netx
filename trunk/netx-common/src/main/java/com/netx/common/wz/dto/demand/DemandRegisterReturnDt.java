package com.netx.common.wz.dto.demand;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 申请者的部分信息
 */
public class DemandRegisterReturnDt {

    private static final long serialVersionUID = 1L;

    private String id;
    /**
     * 主表ID
     */
    private String demandId;
    /**
     * 报名人
     */
    private String userId;
    /**
     * 建议开始时间
     */
    private Date startAt;
    /**
     * 建议结束时间
     */
    private Date endAt;
    /**
     * 建议时间要求：只有大概范围，如：50天内、仅限周末等，具体的时间待申请成功后再与发布者协商确定
     */
    private String about;
    /**
     * 描述
     */
    private String description;
    private String unit;
    /**
     * 希望的报酬
     */
    private BigDecimal wage;
    /**
     * 地址
     */
    private String address;
    /**
     * 经度
     */
    private BigDecimal lon;
    /**
     * 纬度
     */
    private BigDecimal lat;
    /**
     * 距离
     */
    private Double distance;
    /**
     * 商家消费
     */
    private BigDecimal order_price;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDemandId() {
        return demandId;
    }

    public void setDemandId(String demandId) {
        this.demandId = demandId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public Date getEndAt() {
        return endAt;
    }

    public void setEndAt(Date endAt) {
        this.endAt = endAt;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getWage() {
        return wage;
    }

    public void setWage(BigDecimal wage) {
        this.wage = wage;
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

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public BigDecimal getOrder_price() {
        return order_price;
    }

    public void setOrder_price(BigDecimal order_price) {
        this.order_price = order_price;
    }
}
