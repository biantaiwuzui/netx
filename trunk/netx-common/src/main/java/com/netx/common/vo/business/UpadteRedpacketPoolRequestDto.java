package com.netx.common.vo.business;

import java.math.BigDecimal;

public class UpadteRedpacketPoolRequestDto {
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 来源
     */
    private int source;
    /**
     * 方式，1支入，2支出
     */
    private int way;
    /**
     * 来源id
     */
    private String sourceId;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getWay() {
        return way;
    }

    public void setWay(int way) {
        this.way = way;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }
}
