package com.netx.shopping.vo;

import com.baomidou.mybatisplus.annotations.TableField;

/**
 * Created By liwei
 * Description: 网商收银人员列表返回结果
 * Date: 2018-01-23
 */
public class BusinessCashierListResponseVo {
    /**
     * 标识ID
     */
    private String id;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 收银人员姓名
     */
    private String moneyName;
    /**
     * 收银人手机号
     */
    private String moneyPhone;
    /**
     * 收银人网号
     */
    private String moneyNetworkNum;

    /**
     * 商家id
     */
    @TableField("seller_id")
    private String sellerId;
    /**
     * 是否当前商家适用中： 0：不是   1：是
     */
    @TableField("is_current")
    private Integer isCurrent;

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

    public String getMoneyName() {
        return moneyName;
    }

    public void setMoneyName(String moneyName) {
        this.moneyName = moneyName;
    }

    public String getMoneyPhone() {
        return moneyPhone;
    }

    public void setMoneyPhone(String moneyPhone) {
        this.moneyPhone = moneyPhone;
    }

    public String getMoneyNetworkNum() {
        return moneyNetworkNum;
    }

    public void setMoneyNetworkNum(String moneyNetworkNum) {
        this.moneyNetworkNum = moneyNetworkNum;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public Integer getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(Integer isCurrent) {
        this.isCurrent = isCurrent;
    }
}
