package com.netx.common.vo.currency;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * Created By wj.liu
 * Description: 获取用户网信金额返回对象
 * Date: 2017-11-08
 */
@ApiModel
public class GetUserCurrencyAmountVo {

    @ApiModelProperty(value = "某个网信金额")
    private BigDecimal currencyAmount;

    @ApiModelProperty(value = "网信总额，包括自己发行的未兑付完的和持有的")
    private BigDecimal allAmount;

    public BigDecimal getCurrencyAmount() {
        return currencyAmount;
    }

    public void setCurrencyAmount(BigDecimal currencyAmount) {
        this.currencyAmount = currencyAmount;
    }

    public BigDecimal getAllAmount() {
        return allAmount;
    }

    public void setAllAmount(BigDecimal allAmount) {
        this.allAmount = allAmount;
    }
}
