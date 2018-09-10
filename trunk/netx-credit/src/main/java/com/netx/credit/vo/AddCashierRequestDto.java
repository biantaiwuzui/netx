package com.netx.credit.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author lanyingchu
 * @date 2018/7/11 19:43
 */
public class AddCashierRequestDto {

    @ApiModelProperty("标识id")
    private String id;

    @ApiModelProperty("商家id")
    private String merchantId;

    @ApiModelProperty("商家注册者用户id")
    private String merchantUserId;

    @ApiModelProperty("收银人员网号")
    private String cashierNetworkNum;

    @ApiModelProperty("收银人员真实姓名")
    private String cashierName;

    @ApiModelProperty("收银人员手机号码")
    private String cashierPhone;

    @ApiModelProperty("收银人员身份证号")
    private String cashierIdNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantUserId() {
        return merchantUserId;
    }

    public void setMerchantUserId(String merchantUserId) {
        this.merchantUserId = merchantUserId;
    }

    public String getCashierNetworkNum() {
        return cashierNetworkNum;
    }

    public void setCashierNetworkNum(String cashierNetworkNum) {
        this.cashierNetworkNum = cashierNetworkNum;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    public String getCashierPhone() {
        return cashierPhone;
    }

    public void setCashierPhone(String cashierPhone) {
        this.cashierPhone = cashierPhone;
    }

    public String getCashierIdNumber() {
        return cashierIdNumber;
    }

    public void setCashierIdNumber(String cashierIdNumber) {
        this.cashierIdNumber = cashierIdNumber;
    }

    @Override
    public String toString() {
        return "AddCashierRequestDto{" +
                "id='" + id + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", merchantUserId='" + merchantUserId + '\'' +
                ", cashierNetworkNum='" + cashierNetworkNum + '\'' +
                ", cashierName='" + cashierName + '\'' +
                ", cashierPhone='" + cashierPhone + '\'' +
                ", cashierIdNumber='" + cashierIdNumber + '\'' +
                '}';
    }
}
