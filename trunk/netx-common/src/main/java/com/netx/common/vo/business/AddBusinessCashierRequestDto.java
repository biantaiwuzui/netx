package com.netx.common.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created By liwei
 * Description: 添加或修改网商收银人员请求参数
 * Date: 2018-01-23
 */
@ApiModel
public class AddBusinessCashierRequestDto {
    @ApiModelProperty("收银id")
    private String id;

    @ApiModelProperty("用户id")
    private String userId;


    @ApiModelProperty("收银人员姓名")
    @NotBlank(message = "收银人员姓名不能为空")
    private String moneyName;

    @ApiModelProperty("收银人手机号")
    @NotBlank(message = "收银人手机号不能为空")
    private String moneyPhone;

    @ApiModelProperty("收银人网号")
    @NotBlank(message = "收银人网号不能为空")
    private String moneyNetworkNum;

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
}
