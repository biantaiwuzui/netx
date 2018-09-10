package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Create by wongloong on 17-9-17
 */
@ApiModel
public class WalletModifyWechatAccountRequestDto {
    @ApiModelProperty(value = "微信提现账号",required = true)
    @NotBlank
    private String accountId;
//    @ApiModelProperty(value = "短信验证码",required = true)
//    @NotBlank
//    private String code;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }


//    public String getCode() {
//        return code;
//    }
//
//    public void setCode(String code) {
//        this.code = code;
//    }
}
