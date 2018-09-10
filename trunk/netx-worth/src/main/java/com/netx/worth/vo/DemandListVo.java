package com.netx.worth.vo;

import com.netx.worth.model.Demand;

import java.math.BigDecimal;
import java.util.Date;

public class DemandListVo extends Demand{
    /**
     * 订单消费
     */
    private BigDecimal orderPriceBig;
    /**
     * 报酬，根据isEachWage判断是总报酬还是单位报酬
     */
    private BigDecimal wageBig;
    /**
     * 已经托管的保证金
     */
    private BigDecimal bailBig;

    public BigDecimal getOrderPriceBig() {
        return orderPriceBig;
    }

    public void setOrderPriceBig(BigDecimal orderPriceBig) {
        this.orderPriceBig = orderPriceBig;
    }

    public BigDecimal getWageBig() {
        return wageBig;
    }

    public void setWageBig(BigDecimal wageBig) {
        this.wageBig = wageBig;
    }

    public BigDecimal getBailBig() {
        return bailBig;
    }

    public void setBailBig(BigDecimal bailBig) {
        this.bailBig = bailBig;
    }
}
