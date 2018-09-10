package com.netx.common.vo.currency;

import io.swagger.annotations.ApiModelProperty;

public class TokenCurrencyBaseAndNewlyVo {

    @ApiModelProperty("网信详情")
    private TokenCurrencyVo tokenCurrencyVo;

    @ApiModelProperty("网信数量信息")
    private TokenCurrencyBaseDataVo tokenCurrencyBaseDataVo;

    public TokenCurrencyVo getTokenCurrencyVo() {
        return tokenCurrencyVo;
    }

    public void setTokenCurrencyVo(TokenCurrencyVo tokenCurrencyVo) {
        this.tokenCurrencyVo = tokenCurrencyVo;
    }

    public TokenCurrencyBaseDataVo getTokenCurrencyBaseDataVo() {
        return tokenCurrencyBaseDataVo;
    }

    public void setTokenCurrencyBaseDataVo(TokenCurrencyBaseDataVo tokenCurrencyBaseDataVo) {
        this.tokenCurrencyBaseDataVo = tokenCurrencyBaseDataVo;
    }

    @Override
    public String toString() {
        return "TokenCurrencyBaseAndNewlyVo{" +
                "tokenCurrencyVo=" + tokenCurrencyVo +
                ", tokenCurrencyBaseDataVo=" + tokenCurrencyBaseDataVo +
                '}';
    }
}
