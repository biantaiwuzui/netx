package com.netx.shopping.vo;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class AddManagerRequestDto {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("商家注册者用户id")
    private String userId;

    @ApiModelProperty("商家管理人员类型：0.收银人员，1.业务主管，2.法人代表，3.注册者，4.特权行使人，5.客服人员")
    @NotNull(message = "商家管理人员类型不能为空")
    private Integer merchantUserType;

    @ApiModelProperty("姓名")
    @NotBlank(message = "姓名不能为空")
    private String userName;

    @ApiModelProperty("手机号")
    @NotBlank(message = "手机号不能为空")
    private String userPhone;

    @ApiModelProperty("网号")
    @NotBlank(message = "网号不能为空")
    private String userNetworkNum;

    @ApiModelProperty("商家id")
    private String merchantId;

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

    public Integer getMerchantUserType() {
        return merchantUserType;
    }

    public void setMerchantUserType(Integer merchantUserType) {
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

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }
}
