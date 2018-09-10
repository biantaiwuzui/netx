package com.netx.common.wz.dto.wish;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class WishExamineDto {

    @ApiModelProperty(value = "申请金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "申请凭据")
    private String pic;

//    @ApiModelProperty(value = "开户银行")
//    private String depositBank;
//
//    @ApiModelProperty(value = "银行账号")
//    private String account;

//    @ApiModelProperty(value = "联系电话")
//    private String mobile;
//
//    @ApiModelProperty(value = "账户名称")
//    private String accountName;

    @ApiModelProperty("用户网号")
    private String userNumber;

    @ApiModelProperty("昵称")
    private String nickname;
    
    @ApiModelProperty("审批Id")
    private String ApplyId;


    public String getApplyId() {
        return ApplyId;
    }

    public void setApplyId(String applyId) {
        ApplyId = applyId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
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


    @Override
    public String toString() {
        return "WishExamineDto{" +
                "amount=" + amount +
                ", pic='" + pic + '\'' +
                ", userNumber='" + userNumber + '\'' +
                ", nickname='" + nickname + '\'' +
                ", ApplyId='" + ApplyId + '\'' +
                '}';
    }
}
