package com.netx.shopping.vo;

public class QueryManagerResponseDto {

    private String id;
    /**
     * 商家注册者用户ID
     */
    private String userId;
    /**
     * 商家管理人员类型：法人代表，收银人员，业务主管
     */
    private String merchantUserType;
    /**
     * 收银人员姓名
     */
    private String userName;
    /**
     * 收银人手机号
     */
    private String userPhone;
    /**
     * 收银人网号
     */
    private String userNetworkNum;

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

    public String getMerchantUserType() {
        return merchantUserType;
    }

    public void setMerchantUserType(String merchantUserType) {
        this.merchantUserType = merchantUserType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserNetworkNum() {
        return userNetworkNum;
    }

    public void setUserNetworkNum(String userNetworkNum) {
        this.userNetworkNum = userNetworkNum;
    }
}
