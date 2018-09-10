package com.netx.worth.vo;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

public class MeetingConflictDto {

    @ApiModelProperty(value = "活动主题")
    private String title;

    @ApiModelProperty(value = "活动开始时间")
    private Date startedAt;

    @ApiModelProperty(value = "活动那个结束时间")
    private Date endAt;

    @ApiModelProperty(value = "报名费用")
    private BigDecimal amount;

    @ApiModelProperty(value = "活动地址")
    private String address;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "MeetingConflictDto{" +
                "title='" + title + '\'' +
                ", startedAt=" + startedAt +
                ", endAt=" + endAt +
                ", amount=" + amount +
                ", address='" + address + '\'' +
                '}';
    }
}
