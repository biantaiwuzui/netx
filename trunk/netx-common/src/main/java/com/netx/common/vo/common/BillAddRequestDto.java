package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Create by wongloong on 17-9-17
 */
@ApiModel
public class BillAddRequestDto {
    @ApiModelProperty(value = "收款人id", required = true)
    private String toUserId;

    @ApiModelProperty(value = "流水类型:0平台，1经营,涉及到平台或者经营的需填,反之不用，详情看原型400")
    private Integer type;

    /**
     * 金额
     */
    @ApiModelProperty(value = "金额", required = true)
    @NotNull
    private BigDecimal amount;
    /**
     * 描述
     */
    @ApiModelProperty(value = "描述", required = true)
    @NotBlank
    private String description;
    /**
     * 交易方式，0支付宝,1微信，2网币，3零钱,4红包
     */
    @ApiModelProperty(value = "交易方式:0支付宝,1微信，2网币，3零钱,4红包", required = true)
    private Integer payChannel;
    @ApiModelProperty(value = "网币id,当payChannel=3时必填")
    private String currencyId;

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(Integer payChannel) {
        this.payChannel = payChannel;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
