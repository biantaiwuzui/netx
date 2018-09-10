package com.netx.common.wz.dto.matchEvent;

import com.netx.common.user.enums.PayTypeEnum;
import com.netx.common.user.enums.TicketTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

public class MatchTicketPayDto {
    @ApiModelProperty(value = "购票id", required = true)
    @NotNull(message = "id不能为空")
    private String payId;


    @ApiModelProperty("门票id（如果是参赛者id(matchParticipantId)）")
    @NotBlank(message = "门票id不能为空")
    private String matchTicketOrParticipantId;


    @ApiModelProperty("比赛id")
    @NotBlank(message = "比赛id不能为空")
    private String matchId;

    @ApiModelProperty(value = "支付方式：" +
            "PT_NONE：零钱支付" +
            "PT_WECHAT：微信支付" +
            "PT_ALI：支付宝支付" , required = true)
    @NotNull(message = "支付方式不能为空")
    private PayTypeEnum payType;

    @ApiModelProperty(value = "零钱实付金额")
    @NotNull(message = "零钱支付金额不能为空")
    @Min(value = 0,message = "支付金额最少0元")
    private BigDecimal payPrices;

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getMatchTicketOrParticipantId() {
        return matchTicketOrParticipantId;
    }

    public void setMatchTicketOrParticipantId(String matchTicketOrParticipantId) {
        this.matchTicketOrParticipantId = matchTicketOrParticipantId;
    }

    public PayTypeEnum getPayType() {
        return payType;
    }

    public void setPayType(PayTypeEnum payType) {
        this.payType = payType;
    }

    public BigDecimal getPayPrices() {
        return payPrices;
    }

    public void setPayPrices(BigDecimal payPrices) {
        this.payPrices = payPrices;
    }
}
