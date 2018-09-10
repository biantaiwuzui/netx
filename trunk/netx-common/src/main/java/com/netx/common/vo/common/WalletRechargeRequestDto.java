package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import sun.awt.SunHints;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Create by wongloong on 17-9-28
 */
@ApiModel
public class WalletRechargeRequestDto {
    @NotNull
    @ApiModelProperty(name = "支付金额,支付宝：最多两位小数点，微信以分为单位，整数", required = true)
    @Range(min=0, max=100000,message = "单笔充值金额必须大于0元，小于10万元")
    private BigDecimal payAmount;

    @ApiModelProperty(name = "账号昵称", required = true)
    private String nickName;

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "WalletRechargeRequestDto{" + "payAmount=" + payAmount + ", nickName='" + nickName + '\'' + '}';
    }
}
