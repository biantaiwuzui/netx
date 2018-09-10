package com.netx.common.wz.dto.wish;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

public class WishBossListDto {

    /**
     * 心愿历史表ID
     */
    private String id;

    /**
     * 心愿使用表ID
     */
    private String wishApplyId;

    /**
     * 用户网号
     */
    private String userNumber;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 开户银行
     */
    private String depositBank;


    /**
     * 银行卡号
     */
    private String account;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 账户名称/开户人
     */
    private String accountName;

    /**
     * 申请金额/提现金额
     */
    private BigDecimal amount;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 状态
     * 0.待提现
     * 1.提现成功
     * 2.提现失败
     */
    private Integer status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWishApplyId() {
        return wishApplyId;
    }

    public void setWishApplyId(String wishApplyId) {
        this.wishApplyId = wishApplyId;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDepositBank() {
        return depositBank;
    }

    public void setDepositBank(String depositBank) {
        this.depositBank = depositBank;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
