package com.netx.common.vo.business;

import java.math.BigDecimal;
import java.sql.Time;

public class LuckyMoneyResponseDto {
    private String id;
    /**
     * 发送时间
     */
    private Time sendTime;
    /**
     * 发送人数比例
     */
    private BigDecimal sendPeople;
    /**
     * 发送金额比例
     */
    private BigDecimal sendCount;

    private Long createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Time getSendTime() {
        return sendTime;
    }

    public void setSendTime(Time sendTime) {
        this.sendTime = sendTime;
    }

    public BigDecimal getSendPeople() {
        return sendPeople;
    }

    public void setSendPeople(BigDecimal sendPeople) {
        this.sendPeople = sendPeople;
    }

    public BigDecimal getSendCount() {
        return sendCount;
    }

    public void setSendCount(BigDecimal sendCount) {
        this.sendCount = sendCount;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
