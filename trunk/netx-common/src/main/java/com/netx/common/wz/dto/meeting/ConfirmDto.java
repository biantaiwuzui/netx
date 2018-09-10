package com.netx.common.wz.dto.meeting;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;

import java.util.Date;

public class ConfirmDto {
    
    @ApiModelProperty(value = "活动Id")
    private String id;

    @ApiModelProperty(value = "活动开始时间")
    private Date startedAt;

    @ApiModelProperty(value = "活动结束时间")
    private Date endAt;

    @ApiModelProperty(value = "活动地址")
    private String address;

    @ApiModelProperty(value = "订单id")
    private String orderIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public Date getEndAt() {
        return endAt;
    }

    public void setEndAt(Date endAt) {
        this.endAt = endAt;
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
}
