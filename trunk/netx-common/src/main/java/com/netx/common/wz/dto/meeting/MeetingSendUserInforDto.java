package com.netx.common.wz.dto.meeting;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

public class MeetingSendUserInforDto {
    @ApiModelProperty("用户id")
    private String id;

    @ApiModelProperty("用户网号")
    private String userNumber;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("出生年月日")
    private Date birthday;

    @ApiModelProperty("年龄")
    private int age;

    @ApiModelProperty("等级")
    private String lv;

    @ApiModelProperty("头像")
    private String headImgUrl;

    @ApiModelProperty("个性标签（以‘,’隔开）：性格")
    private String tag;

    @ApiModelProperty("信用")
    private Integer credit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getLv() {
        return lv;
    }

    public void setLv(String lv) {
        this.lv = lv;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    @Override
    public String toString() {
        return "MeetingSendUserInforDto{" +
                "id='" + id + '\'' +
                ", userNumber='" + userNumber + '\'' +
                ", nickName='" + nickName + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday=" + birthday +
                ", age=" + age +
                ", lv='" + lv + '\'' +
                ", headImgUrl='" + headImgUrl + '\'' +
                ", tag='" + tag + '\'' +
                ", credit=" + credit +
                '}';
    }
}
