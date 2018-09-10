package com.netx.common.vo.currency;

import io.swagger.annotations.ApiModelProperty;

/**
 * 网信基本统计数据vo
 * @Author hj.Mao
 */

public class TokenCurrencyBaseDataVo {

    @ApiModelProperty("用户ID")
    private String userId;

    @ApiModelProperty("已成攻发行或者持有的的网信数")
    private Long succeedCurrencyAmount;

    @ApiModelProperty("发行或者持有的网信总数")
    private Long currencyAmount;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getSucceedCurrencyAmount() {
        return succeedCurrencyAmount;
    }

    public void setSucceedCurrencyAmount(Long succeedCurrencyAmount) {
        this.succeedCurrencyAmount = succeedCurrencyAmount;
    }

    public Long getCurrencyAmount() {
        return currencyAmount;
    }

    public void setCurrencyAmount(Long currencyAmount) {
        this.currencyAmount = currencyAmount;
    }

    @Override
    public String toString() {
        return "TokenCurrencyBaseDataVo{" +
                "userId='" + userId + '\'' +
                ", succeedCurrencyAmount=" + succeedCurrencyAmount +
                ", currencyAmount=" + currencyAmount +
                '}';
    }
}
