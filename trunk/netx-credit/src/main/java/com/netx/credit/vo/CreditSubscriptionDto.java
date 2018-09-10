package com.netx.credit.vo;

/**
 * @author lanyingchu
 * @date 2018/8/1 14:06
 */
public class CreditSubscriptionDto {
    // 认购的网信id
    private String creditId;
    // 商家内购者所在商家id
    private String merchantId;
    // 认购者id
    private String userId;
    // 认购等级
    private String creditStageId;
    // 认购额
    private double subscriptionNumber;
    // 认购状态 0.未响应，1.同意并完成付款，2.拒绝参加内购，3.非内购人员
    private int status;
    // 身份（按CreditSubscriptionTypeEnum枚举类型确认）
    private String type;

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreditStageId() {
        return creditStageId;
    }

    public void setCreditStageId(String creditStageId) {
        this.creditStageId = creditStageId;
    }

    public double getSubscriptionNumber() {
        return subscriptionNumber;
    }

    public void setSubscriptionNumber(double subscriptionNumber) {
        this.subscriptionNumber = subscriptionNumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
