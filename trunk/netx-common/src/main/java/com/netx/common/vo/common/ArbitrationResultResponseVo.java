package com.netx.common.vo.common;

/**
 * 仲裁结果vo类
 * hj.Mao
 * 2017-11-10
 */

import io.swagger.annotations.ApiModelProperty;

public class ArbitrationResultResponseVo {

    @ApiModelProperty("主键ID")
    private String id;

    @ApiModelProperty("管理员仲裁退款结果：true-----同意退款，false------不同意退款\n")
    private Boolean refundRadioButton;

    @ApiModelProperty("退款仲裁同意或者不同意的理由")
    private String refundArbitrateReason;

    @ApiModelProperty("管理员仲裁退货结果：true-----同意退货，false------不同意退货\n")
    private Boolean returnRadioButton;

    @ApiModelProperty("退货仲裁同意或者不同意的理由")
    private String returnArbitrateReason;

    @ApiModelProperty("通用仲裁管理员仲裁的答案 0-----不同意 1-----同意")
    private Integer opCommonAnswer;

    @ApiModelProperty("通用仲裁管理员同意的辩解")
    private String opCommonReason;

    @ApiModelProperty("拒绝受理的理由")
    private String descriptions;

    @ApiModelProperty("仲裁的状态")
    private Integer statusCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Integer getOpCommonAnswer() {
        return opCommonAnswer;
    }

    public void setOpCommonAnswer(Integer opCommonAnswer) {
        this.opCommonAnswer = opCommonAnswer;
    }

    public String getOpCommonReason() {
        return opCommonReason;
    }

    public void setOpCommonReason(String opCommonReason) {
        this.opCommonReason = opCommonReason;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return "ArbitrationResultResponseVo{" +
                "id='" + id + '\'' +
                ", refundRadioButton=" + refundRadioButton +
                ", refundArbitrateReason='" + refundArbitrateReason + '\'' +
                ", returnRadioButton=" + returnRadioButton +
                ", returnArbitrateReason='" + returnArbitrateReason + '\'' +
                ", opCommonAnswer=" + opCommonAnswer +
                ", opCommonReason='" + opCommonReason + '\'' +
                ", descriptions='" + descriptions + '\'' +
                ", statusCode=" + statusCode +
                '}';
    }
}
