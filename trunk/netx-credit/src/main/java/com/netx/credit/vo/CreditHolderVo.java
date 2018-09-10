package com.netx.credit.vo;

/**
 * @author lanyingchu
 * @date 2018/8/4 9:20
 */

/**
 * 网信持有人信息
 */
public class CreditHolderVo {


    /**
     * 用户头像
     */
    private String headPhoto;
    /**
     * 用户昵称
     */
    private String name;
    /**
     * 用户信用值
     */
    private int credit;
    /**
     * 认购日期
     */
    private Long subscriptionDate;
    /**
     * 认购金额
     */
    private Double subscriptionNumber;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public Long getSubscriptionDate() {
        return subscriptionDate;
    }

    public void setSubscriptionDate(Long subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }

    public Double getSubscriptionNumber() {
        return subscriptionNumber;
    }

    public void setSubscriptionNumber(Double subscriptionNumber) {
        this.subscriptionNumber = subscriptionNumber;
    }

    public String getHeadPhoto() {
        return headPhoto;
    }

    public void setHeadPhoto(String headPhoto) {
        this.headPhoto = headPhoto;
    }
}
