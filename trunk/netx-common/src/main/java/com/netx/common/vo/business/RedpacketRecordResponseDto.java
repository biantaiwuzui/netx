package com.netx.common.vo.business;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

public class RedpacketRecordResponseDto {
    /**
     * 第几个红包
     */
    private Integer redpacketNO;

    /**
     * 红包总数
     */
    private Integer redpacketAllNum;
    /**
     * 最高金额
     */
    private BigDecimal maxAmount;
    /**
     * 最低金额
     */
    private BigDecimal minAmount;
    /**
     * 发放时间
     */
    private String sendTime;
    /**
     *抢红包人数
     */
    private Integer redpacketNum;
    /**
     * 红包总额
     */
    private BigDecimal allAmount;

    /**
     * 用户抢红包情况
     */
    private String robSituation;

    /**
     * 红包金额
     */
    private BigDecimal amount;
    List<RedpacketRecordDto> userRedpacketRecord;

    public Integer getRedpacketNO() {
        return redpacketNO;
    }

    public void setRedpacketNO(Integer redpacketNO) {
        this.redpacketNO = redpacketNO;
    }

    public Integer getRedpacketAllNum() {
        return redpacketAllNum;
    }

    public void setRedpacketAllNum(Integer redpacketAllNum) {
        this.redpacketAllNum = redpacketAllNum;
    }

    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public BigDecimal getAllAmount() {
        return allAmount;
    }

    public void setAllAmount(BigDecimal allAmount) {
        this.allAmount = allAmount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public List<RedpacketRecordDto> getUserRedpacketRecord() {
        return userRedpacketRecord;
    }

    public void setUserRedpacketRecord(List<RedpacketRecordDto> userRedpacketRecord) {
        this.userRedpacketRecord = userRedpacketRecord;
    }

    public Integer getRedpacketNum() {
        return redpacketNum;
    }

    public void setRedpacketNum(Integer redpacketNum) {
        this.redpacketNum = redpacketNum;
    }

    public String getRobSituation() {
        return robSituation;
    }

    public void setRobSituation(String robSituation) {
        this.robSituation = robSituation;
    }
}
