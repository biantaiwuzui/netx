package com.netx.common.vo.currency;


import java.math.BigDecimal;

public class TradeRecordResponseVo {
    /**
     * 用户id
     */
    private String userId;
    /**
     * 交易的网信id
     */
    private String currencyId;
    /**
     * 交易金额
     */
    private BigDecimal amount;
    /**
     * 交易时间
     */
    private Integer tradeTime;
    /**
     * 交易描述
     */
    private String description;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 性别
     */
    private String sex;
    /**
     * 年龄
     */
    private Integer age;

    /**
     * 用户等级
     */
    private Integer lv;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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

    public Integer getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Integer tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getLv() {
        return lv;
    }

    public void setLv(Integer lv) {
        this.lv = lv;
    }

    @Override
    public String toString() {
        return "TradeRecordResponseVo{" +
                "userId='" + userId + '\'' +
                ", currencyId='" + currencyId + '\'' +
                ", amount=" + amount +
                ", tradeTime=" + tradeTime +
                ", description='" + description + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", lv=" + lv +
                '}';
    }
}
