package com.netx.common.vo.currency;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * 网信收银人员支付弹框vo类
 * @Author hj.Mao
 * @Date 2017-12-27
 */
public class BackBuyInfoResponseDto  extends HoldListResponseVo{

    @ApiModelProperty("应付回购费用")
    private BigDecimal payAmount;

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    @Override
    public String toString() {
        return "BackBuyInfoResponseDto{" +
                "payAmount=" + payAmount +
                '}';
    }
}
