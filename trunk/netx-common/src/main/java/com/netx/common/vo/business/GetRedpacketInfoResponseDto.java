package com.netx.common.vo.business;

import java.math.BigDecimal;

public class GetRedpacketInfoResponseDto {
    /**
     * 第几个红包
     */
    private Integer redpacketNO;

    /**
     * 红包总数
     */
    private Integer redpacketAllNum;

    /**
     * 红包发放id
     */
    private String id;

    /**
     * 上个红包发放id
     */
    private String lastRedpacketSendId;

    /**
     * 红包总额
     */
    private BigDecimal allAmount;

    /**
     * 红包金额
     */
    private BigDecimal amount;

    /**
     * 红包发放时间
     */
    private Long sendTime;

    /**
     * 红包金额发放给用户时间
     */
    private Long sendMoneyTime;

    /**
     * 已枪红包金额
     */
    private BigDecimal robAmount;

    /**
     * 用户抢红包状态 1未抢 2抢到了 3.用户没抢到红包
     */
    private Integer userRobStatus;

    /**
     * 红包是否抢完 0.还未能抢红包 1.红包未抢完 2.红包时间已过
     */
    private Integer robFinishStatus;

    private BigDecimal userRobAmount;

    public BigDecimal getUserRobAmount() {
        return userRobAmount;
    }

    public void setUserRobAmount(BigDecimal userRobAmount) {
        this.userRobAmount = userRobAmount;
    }

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastRedpacketSendId() {
        return lastRedpacketSendId;
    }

    public void setLastRedpacketSendId(String lastRedpacketSendId) {
        this.lastRedpacketSendId = lastRedpacketSendId;
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

    public Long getSendTime() {
        return sendTime;
    }

    public void setSendTime(Long sendTime) {
        this.sendTime = sendTime;
    }

    public BigDecimal getRobAmount() {
        return robAmount;
    }

    public void setRobAmount(BigDecimal robAmount) {
        this.robAmount = robAmount;
    }

    public Long getSendMoneyTime() {
        return sendMoneyTime;
    }

    public void setSendMoneyTime(Long sendMoneyTime) {
        this.sendMoneyTime = sendMoneyTime;
    }

    public Integer getUserRobStatus() {
        return userRobStatus;
    }

    public void setUserRobStatus(Integer userRobStatus) {
        this.userRobStatus = userRobStatus;
    }

    public Integer getRobFinishStatus() {
        return robFinishStatus;
    }

    public void setRobFinishStatus(Integer robFinishStatus) {
        this.robFinishStatus = robFinishStatus;
    }
}
