package com.netx.common.user.dto.divideManagement;

import io.swagger.annotations.ApiModelProperty;

public class SelectUserByUserNumberResponseDto {

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("lv")
    private Integer lv;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("是否是管理员")
    private Boolean adminUser;

    @ApiModelProperty("真实姓名")
    private String realName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Boolean getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(Boolean adminUser) {
        this.adminUser = adminUser;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    @Override
    public String toString() {
        return "SelectUserByUserNumberResponseDto{" +
                "userId='" + userId + '\'' +
                ", nickname='" + nickname + '\'' +
                ", lv=" + lv +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", mobile='" + mobile + '\'' +
                ", adminUser=" + adminUser +
                ", realName='" + realName + '\'' +
                '}';
    }
}
