package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 仲裁接受处理的参数dto
 * create by haojun
 * 2017/9/29
 */

@ApiModel
public class ArbitrationAcceptHandleRequestDto {

    @ApiModelProperty("裁决人ID，必填")
    @NotBlank(message = "操作者用户ID不能为空")
    private String opUserId;

    @ApiModelProperty("仲裁列ID，必填")
    @NotBlank(message = "仲裁列ID不能为空")
    private String id;

    @ApiModelProperty("仲裁类型")
    @NotNull(message = "仲裁类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "\r\n扣除投诉人信用值前的复选框" +
            "\r\n 0----不扣\n" +
            " \r\n1-----扣\n" +
            "如果这个值为1，那fromUserCreditPoint，fromUserCreditPotinReason要有值")
    @Range(min = 0,max = 1)
    private Integer fromUserCreditRefund;

    @ApiModelProperty("扣除投诉者信用值")
    private BigDecimal fromUserCreditPoint;

    @ApiModelProperty("扣除投诉者信用的理由")
    private String fromUserCreditPointReason;

    @ApiModelProperty("扣除投诉人信用值前的复选框\n+" +
            "       0----不扣\n" +
            "       1-----扣"+
            "如果这个值为1，那fromUserCreditPoint，fromUserCreditPotinReason要有值"
    )
    @Range(min = 0,max = 1)
    private Integer toUserCreditRefund;

    @ApiModelProperty("扣除被投诉者信用值")
    private BigDecimal toUserCreditPoint;


    @ApiModelProperty("扣除被投诉者信用值")
    private String toUserCreditPointReason;

    @ApiModelProperty("退款单选框：true-----同意退款，false------不同意退款\n")
    private Boolean refundRadioButton;

    @ApiModelProperty("退款仲裁同意或者不同意的理由")
    private String refundArbitrateReason;

    @ApiModelProperty("退货单选框：true-----同意退货，false------不同意退货\n")
    private Boolean returnRadioButton;

    @ApiModelProperty("退货仲裁同意或者不同意的理由")
    private String returnArbitrateReason;

    @ApiModelProperty("扣除发行者冻结的金额：就是说退款:1.------是,:0.-------否")
    private Integer substractReleaseFrozenMoneyRefund;

    @ApiModelProperty("选中扣除发行者冻结的金额的原因")
    private String substractReleaseFrozenMoneyReason;

    public String getOpUserId() {
        return opUserId;
    }

    public void setOpUserId(String opUserId) {
        this.opUserId = opUserId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getFromUserCreditRefund() {
        return fromUserCreditRefund;
    }

    public void setFromUserCreditRefund(Integer fromUserCreditRefund) {
        this.fromUserCreditRefund = fromUserCreditRefund;
    }

    public BigDecimal getFromUserCreditPoint() {
        return fromUserCreditPoint;
    }

    public void setFromUserCreditPoint(BigDecimal fromUserCreditPoint) {
        this.fromUserCreditPoint = fromUserCreditPoint;
    }

    public String getFromUserCreditPointReason() {
        return fromUserCreditPointReason;
    }

    public void setFromUserCreditPointReason(String fromUserCreditPointReason) {
        this.fromUserCreditPointReason = fromUserCreditPointReason;
    }

    public Integer getToUserCreditRefund() {
        return toUserCreditRefund;
    }

    public void setToUserCreditRefund(Integer toUserCreditRefund) {
        this.toUserCreditRefund = toUserCreditRefund;
    }

    public BigDecimal getToUserCreditPoint() {
        return toUserCreditPoint;
    }

    public void setToUserCreditPoint(BigDecimal toUserCreditPoint) {
        this.toUserCreditPoint = toUserCreditPoint;
    }

    public String getToUserCreditPointReason() {
        return toUserCreditPointReason;
    }

    public void setToUserCreditPointReason(String toUserCreditPointReason) {
        this.toUserCreditPointReason = toUserCreditPointReason;
    }

    public Boolean getRefundRadioButton() {
        return refundRadioButton;
    }

    public void setRefundRadioButton(Boolean refundRadioButton) {
        this.refundRadioButton = refundRadioButton;
    }

    public String getRefundArbitrateReason() {
        return refundArbitrateReason;
    }

    public void setRefundArbitrateReason(String refundArbitrateReason) {
        this.refundArbitrateReason = refundArbitrateReason;
    }

    public Boolean getReturnRadioButton() {
        return returnRadioButton;
    }

    public void setReturnRadioButton(Boolean returnRadioButton) {
        this.returnRadioButton = returnRadioButton;
    }

    public String getReturnArbitrateReason() {
        return returnArbitrateReason;
    }

    public void setReturnArbitrateReason(String returnArbitrateReason) {
        this.returnArbitrateReason = returnArbitrateReason;
    }

    public Integer getSubstractReleaseFrozenMoneyRefund() {
        return substractReleaseFrozenMoneyRefund;
    }

    public void setSubstractReleaseFrozenMoneyRefund(Integer substractReleaseFrozenMoneyRefund) {
        this.substractReleaseFrozenMoneyRefund = substractReleaseFrozenMoneyRefund;
    }

    public String getSubstractReleaseFrozenMoneyReason() {
        return substractReleaseFrozenMoneyReason;
    }

    public void setSubstractReleaseFrozenMoneyReason(String substractReleaseFrozenMoneyReason) {
        this.substractReleaseFrozenMoneyReason = substractReleaseFrozenMoneyReason;
    }

    @Override
    public String toString() {
        return "ArbitrationAcceptHandleRequestDto{" +
                "opUserId='" + opUserId + '\'' +
                ", id='" + id + '\'' +
                ", type=" + type +
                ", fromUserCreditRefund=" + fromUserCreditRefund +
                ", fromUserCreditPoint=" + fromUserCreditPoint +
                ", fromUserCreditPointReason='" + fromUserCreditPointReason + '\'' +
                ", toUserCreditRefund=" + toUserCreditRefund +
                ", toUserCreditPoint=" + toUserCreditPoint +
                ", toUserCreditPointReason='" + toUserCreditPointReason + '\'' +
                ", refundRadioButton=" + refundRadioButton +
                ", refundArbitrateReason='" + refundArbitrateReason + '\'' +
                ", returnRadioButton=" + returnRadioButton +
                ", returnArbitrateReason='" + returnArbitrateReason + '\'' +
                ", substractReleaseFrozenMoneyRefund=" + substractReleaseFrozenMoneyRefund +
                ", substractReleaseFrozenMoneyReason='" + substractReleaseFrozenMoneyReason + '\'' +
                '}';
    }
}
