package com.netx.common.vo.business;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * Created By liwei
 * Description:获取上个月的订单交易总额
 * Date: 2017-11-09
 */
public class GetlastMonthOdersAmountResponseVo {

    @ApiModelProperty("订单交易总额")
    private BigDecimal sumTotalPrice;

    public BigDecimal getSumTotalPrice() {
        return sumTotalPrice;
    }

    public void setSumTotalPrice(BigDecimal sumTotalPrice) {
        this.sumTotalPrice = sumTotalPrice;
    }
}
