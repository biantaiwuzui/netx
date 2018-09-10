package com.netx.common.vo.business;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

/**
 * 网商每个月订单交易额vo类
 * @Authro hj.Mao
 * @Date 2017-11-21
 */
public class GetEveryMonthOrderAmountResponseDto {

    @ApiModelProperty("网商的每个月交易额的集合")
    private List<BigDecimal> sumTotalPrices;

    public List<BigDecimal> getSumTotalPrices() {
        return sumTotalPrices;
    }

    public void setSumTotalPrices(List<BigDecimal> sumTotalPrices) {
        this.sumTotalPrices = sumTotalPrices;
    }
}
