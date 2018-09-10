package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Time;

/**
 * Create by wongloong on 17-9-11
 */
@ApiModel
public class WzCommonLuckyMoneyAddDto {

    @ApiModelProperty("发送红包时间")
    @NotNull
    private Time sendTime;
    @ApiModelProperty("发送人数比例")
    @NotNull
    private BigDecimal sendPeople;
    @ApiModelProperty("发送比例")
    @NotNull
    private BigDecimal sendCount;

    public Time getSendTime() {
        return sendTime;
    }

    public void setSendTime(Time sendTime) {
        this.sendTime = sendTime;
    }

    public BigDecimal getSendPeople() {
        return sendPeople;
    }

    public void setSendPeople(BigDecimal sendPeople) {
        this.sendPeople = sendPeople;
    }

    public BigDecimal getSendCount() {
        return sendCount;
    }

    public void setSendCount(BigDecimal sendCount) {
        this.sendCount = sendCount;
    }
}
