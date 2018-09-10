package com.netx.common.user.dto.userManagement;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class SelectUserVerifyResponse extends UserVerifyBeanResponse {

    @ApiModelProperty("昵称")
    private String nickName;
    @ApiModelProperty("网号")
    private String userNumber;
    @ApiModelProperty("等级")
    private Integer lv;
    @ApiModelProperty("性别")
    private String sex;
    @ApiModelProperty("年龄")
    private Integer age;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public Integer getLv() {
        return lv;
    }

    public void setLv(Integer lv) {
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

    @Override
    public String toString() {
        return "SelectUserVerifyResponse{" +
                "nickName='" + nickName + '\'' +
                ", userNumber='" + userNumber + '\'' +
                ", lv=" + lv +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", videoResource=" + super.getVideoResource() +
                ", idNumber='" + super.getIdNumber() + '\'' +
                ", idCardResource=" + super.getIdCardResource() +
                ", carResource=" + super.getCarResource() +
                ", houseResource=" + super.getHouseResource() +
                ", degreeResource=" + super.getDegreeResource() +
                '}';
    }
}
