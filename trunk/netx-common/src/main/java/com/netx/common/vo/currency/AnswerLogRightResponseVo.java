package com.netx.common.vo.currency;

import java.math.BigDecimal;

public class AnswerLogRightResponseVo {

    /**
     * 答题记录id
     */
    private String answerLogId;

    /**
     * 与发行者距离
     */
    private Double distance;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 总信用
     */
    private Integer credit;

    /**
     * 总贡献
     */
    private BigDecimal contribution;

    /**
     * 年龄
     */
    private Integer age;

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public BigDecimal getContribution() {
        return contribution;
    }

    public void setContribution(BigDecimal contribution) {
        this.contribution = contribution;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAnswerLogId() {
        return answerLogId;
    }

    public void setAnswerLogId(String answerLogId) {
        this.answerLogId = answerLogId;
    }
}
