package com.netx.common.wz.dto.meeting;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

public class ConfirmDetailDto {
    
    @ApiModelProperty(value = "活动Id")
    private String id;
    
    @ApiModelProperty(value = "活动开始时间")
    private Date startedAt;
    
    @ApiModelProperty(value = "活动结束时间")
    private Date endAt;
    
    @ApiModelProperty(value = "活动地址")
    private String address;

    @ApiModelProperty(value = "活动报名费用")
    private BigDecimal amount;
    
    @ApiModelProperty(value = "活动费用")
    private Long orderPrice;
    
    @ApiModelProperty(value = "报名总金额")
    private Long allRegisterAmount;
    
    @ApiModelProperty(value = "补足差额")
    private Long balance;
    
    @ApiModelProperty(value = "是否确认活动细节")
    private Boolean isConfirm;
    
    @ApiModelProperty(value = "订单id")
    private String orderIds;

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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(Long orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Long getAllRegisterAmount() {
        return allRegisterAmount;
    }

    public void setAllRegisterAmount(Long allRegisterAmount) {
        this.allRegisterAmount = allRegisterAmount;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Boolean getConfirm() {
        return isConfirm;
    }

    public void setConfirm(Boolean confirm) {
        isConfirm = confirm;
    }

    public String getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(String orderIds) {
        this.orderIds = orderIds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ConfirmDetailDto{" +
                "id='" + id + '\'' +
                ", startedAt=" + startedAt +
                ", endAt=" + endAt +
                ", address='" + address + '\'' +
                ", amount=" + amount +
                ", orderPrice=" + orderPrice +
                ", allRegisterAmount=" + allRegisterAmount +
                ", balance=" + balance +
                ", isConfirm=" + isConfirm +
                ", orderIds='" + orderIds + '\'' +
                '}';
    }
}
