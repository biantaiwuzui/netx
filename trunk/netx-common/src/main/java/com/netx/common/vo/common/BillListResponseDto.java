package com.netx.common.vo.common;

import java.math.BigDecimal;
import java.util.Date;

public class BillListResponseDto {
    private String id;
    /**
     * 钱包用户id
     */
    private String userId;
    /**
     * 交易类型，0支出，1收入
     */
    private Integer tradeType;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 描述
     */
    private String description;
    /**
     * 第三方单号
     */
    private String thirdBillId;

    private Date createTime;

    private Date updateTime;

    private String createUserId;

    private String updateUserId;

    private Integer deleted;
    /**
     * 对方userId
     */
    private String bak1;
    /**
     * 网币id
     */
    private String bak2;

    private String bak3;

    private String bak4;

    private String bak5;
    /**
     * 是否到帐,第三方支付用
     */
    private Integer toAccount;
    /**
     * 交易方式，0支付宝,1微信，2网币，3零钱
     */
    private Integer payChannel;
    /**
     * 流水类型,0平台，1经营
     */
    private Integer type;

    private String nickname;

    private String sex;

    private Integer age;

    private Integer lv;

    private Integer credit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getTradeType() {
        return tradeType;
    }

    public void setTradeType(Integer tradeType) {
        this.tradeType = tradeType;
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

    public String getThirdBillId() {
        return thirdBillId;
    }

    public void setThirdBillId(String thirdBillId) {
        this.thirdBillId = thirdBillId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateUser() {
        return createUserId;
    }

    public void setCreateUser(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getUpdateUser() {
        return updateUserId;
    }

    public void setUpdateUser(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Integer getDelTag() {
        return deleted;
    }

    public void setDelTag(Integer deleted) {
        this.deleted = deleted;
    }

    public String getBak1() {
        return bak1;
    }

    public void setBak1(String bak1) {
        this.bak1 = bak1;
    }

    public String getBak2() {
        return bak2;
    }

    public void setBak2(String bak2) {
        this.bak2 = bak2;
    }

    public String getBak3() {
        return bak3;
    }

    public void setBak3(String bak3) {
        this.bak3 = bak3;
    }

    public String getBak4() {
        return bak4;
    }

    public void setBak4(String bak4) {
        this.bak4 = bak4;
    }

    public String getBak5() {
        return bak5;
    }

    public void setBak5(String bak5) {
        this.bak5 = bak5;
    }

    public Integer getToAccount() {
        return toAccount;
    }

    public void setToAccount(Integer toAccount) {
        this.toAccount = toAccount;
    }

    public Integer getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(Integer payChannel) {
        this.payChannel = payChannel;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Integer getLv() {
        return lv;
    }

    public void setLv(Integer lv) {
        this.lv = lv;
    }


    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }
}
