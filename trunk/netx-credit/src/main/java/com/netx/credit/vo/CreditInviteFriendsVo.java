package com.netx.credit.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * 邀请好友列表
 */
public class CreditInviteFriendsVo {

    @ApiModelProperty("被邀请好友头像")
    private String userPhoto;

    @ApiModelProperty("邀请内购好友昵称")
    private String nickName;

    @ApiModelProperty("邀请内购好友性别")
    private String sex;

    @ApiModelProperty("邀请内购好友年龄")
    private Integer age;

    @ApiModelProperty("邀请内购好友信用值")
    private Integer credit;

    @ApiModelProperty("邀请内购好友状态")
    private Integer status;

    @ApiModelProperty("邀请内购好友认购金额")
    private Double amount;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }
}
