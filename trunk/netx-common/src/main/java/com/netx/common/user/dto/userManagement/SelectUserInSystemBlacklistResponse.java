package com.netx.common.user.dto.userManagement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("黑名单/白名单里的用户信息")
public class SelectUserInSystemBlacklistResponse {

    @ApiModelProperty("用户id")
    private String id;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("lv等级")
    private String lv;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("信用")
    private Integer credit;

    @ApiModelProperty("网号")
    private String userNumber;

    @ApiModelProperty("拉黑或释放原因")
    private String reason;

    @ApiModelProperty("操作者昵称")
    private String operateUserNickname;

    public String getId() {
        return id;
    }

    public void setId(String userId) {
        this.id = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLv() {
        return lv;
    }

    public void setLv(String lv) {
        this.lv = lv;
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

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getOperateUserNickname() {
        return operateUserNickname;
    }

    public void setOperateUserNickname(String operateUserNickname) {
        this.operateUserNickname = operateUserNickname;
    }

    @Override
    public String toString() {
        return "SelectUserInSystemBlacklistResponse{" +
                "userId='" + id + '\'' +
                ", nickName='" + nickName + '\'' +
                ", lv='" + lv + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", credit=" + credit +
                ", userNumber='" + userNumber + '\'' +
                ", reason='" + reason + '\'' +
                ", operateUserNickname='" + operateUserNickname + '\'' +
                '}';
    }
}
