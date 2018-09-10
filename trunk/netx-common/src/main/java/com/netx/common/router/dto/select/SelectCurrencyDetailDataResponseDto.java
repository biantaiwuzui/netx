package com.netx.common.router.dto.select;

import com.netx.common.vo.currency.TokenCurrencyBaseAndNewlyVo;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 跨模块的网币信息Response类
 * </p>
 *
 * @author 黎子安
 * @since 2017-10-13
 */
public class SelectCurrencyDetailDataResponseDto {
    @ApiModelProperty("网币详情")
    private TokenCurrencyBaseAndNewlyVo tokenCurrencyBaseAndNewlyVo;

    @ApiModelProperty("网币用户信息")
    private SelectCurrencyUserDataDto selectCurrencyUserDataDto;

    public TokenCurrencyBaseAndNewlyVo getTokenCurrencyBaseAndNewlyVo() {
        return tokenCurrencyBaseAndNewlyVo;
    }

    public void setTokenCurrencyBaseAndNewlyVo(TokenCurrencyBaseAndNewlyVo tokenCurrencyBaseAndNewlyVo) {
        this.tokenCurrencyBaseAndNewlyVo = tokenCurrencyBaseAndNewlyVo;
    }

    public SelectCurrencyUserDataDto getSelectCurrencyUserDataDto() {
        return selectCurrencyUserDataDto;
    }

    public void setSelectCurrencyUserDataDto(SelectCurrencyUserDataDto selectCurrencyUserDataDto) {
        this.selectCurrencyUserDataDto = selectCurrencyUserDataDto;
    }
}
