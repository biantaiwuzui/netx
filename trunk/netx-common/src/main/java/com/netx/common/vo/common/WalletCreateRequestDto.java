package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Create by wongloong on 17-9-17
 * 创建钱包账号
 */
@ApiModel
public class WalletCreateRequestDto {
    @Deprecated
    @ApiModelProperty(value = "钱包密码", required = true)
    private String walletPassword;
    @Deprecated
    @ApiModelProperty(value = "短信验证码", required = true)
    private String code;

    public String getWalletPassword() {
        return walletPassword;
    }

    public void setWalletPassword(String walletPassword) {
        this.walletPassword = walletPassword;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
