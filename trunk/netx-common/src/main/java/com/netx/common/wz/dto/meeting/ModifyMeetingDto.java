package com.netx.common.wz.dto.meeting;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class ModifyMeetingDto {
    @ApiModelProperty(value = "活动ID")
    private String id;

    @ApiModelProperty(value = "主发起人ID")
    private String userId;

    @ApiModelProperty(value = "活动地址")
    private String address;

    @ApiModelProperty(value = "订单列表")
    private String orderIds;

    @ApiModelProperty(value = "订单总金额")
    private BigDecimal orderPrice;

    @ApiModelProperty(value = "经度")
    private BigDecimal lon;

    @ApiModelProperty(value = "纬度")
    private BigDecimal lat;

    @ApiModelProperty(value = "活动图片URL")
    private String pic;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(String orderIds) {
        this.orderIds = orderIds;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
