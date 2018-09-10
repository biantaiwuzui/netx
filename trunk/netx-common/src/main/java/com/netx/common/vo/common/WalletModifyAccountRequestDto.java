package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Create by wongloong on 17-9-17
 */
@ApiModel
public class WalletModifyAccountRequestDto {
    @ApiModelProperty(value = "支付宝提现账号",required = true)
    @NotBlank
    private String aliAccountId;
    @ApiModelProperty(value = "微信提现账号",required = true)
    @NotBlank
    private String wechatAccountId;
//    @ApiModelProperty(value = "钱包密码",required = true)
//    @NotBlank
//    private String walletPassword;

    public String getAliAccountId() {
        return aliAccountId;
    }

    public void setAliAccountId(String aliAccountId) {
        this.aliAccountId = aliAccountId;
    }

    public String getWechatAccountId() {
        return wechatAccountId;
    }

    public void setWechatAccountId(String wechatAccountId) {
        this.wechatAccountId = wechatAccountId;
    }

//    public String getWalletPassword() {
//        return walletPassword;
//    }
//
//    public void setWalletPassword(String walletPassword) {
//        this.walletPassword = walletPassword;
//    }
}
