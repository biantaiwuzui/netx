package com.netx.ucenter.vo.request;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * boss
 * 修改用户信息
 *  @author baixingtian
 *  @since 2018-6-04
 */
public class EditUserRequestDto {

    @NotBlank(message = "改用户id为空")
    @ApiModelProperty(value = "User表id")
    private String id;

    @NotBlank(message = "昵称不能改为为空")
    @ApiModelProperty(value = "User昵称")
    private String nickname;

    @NotBlank(message = "网号不能改为为空")
    @ApiModelProperty(value = "User网号")
    private String userNumber;

   // @NotBlank(message = "真实姓名不能改为为空")
    @ApiModelProperty(value = "User真实姓名")
    private String realName;

    @ApiModelProperty(value = "User性别")
    private Integer sex;

    @ApiModelProperty(value = "User是否登录")
    private Integer login;
    /**
     * 是否为管理员帐号：
     0：否
     1：是
     */
    @ApiModelProperty(value = "User是否是管理")
    private Integer adminUser;

    @NotBlank(message = "电话号码不能改为为空")
    @ApiModelProperty(value = "User电话号码")
    private String mobile;

    /**
     * 角色：
     1.系统管理
     2.用户管理
     3.商家管理
     4.资讯管理
     5.财务管理
     6.仲裁管理
     */
    @ApiModelProperty(value = "User角色")
    private String role;

    @ApiModelProperty(value = "User教育概述")
    private String educationLabel;

    @ApiModelProperty(value = "User工作概述")
    private String professionLabel;

    @ApiModelProperty(value = "User兴趣概述")
    private String interestLabel;

    @ApiModelProperty(value = "User等级")
    private Integer lv;


    public Integer getLogin() {
        return login;
    }

    public void setLogin(Integer login) {
        this.login = login;
    }

    public Integer getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(Integer adminUser) {
        this.adminUser = adminUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEducationLabel() {
        return educationLabel;
    }

    public void setEducationLabel(String educationLabel) {
        this.educationLabel = educationLabel;
    }

    public String getProfessionLabel() {
        return professionLabel;
    }

    public void setProfessionLabel(String professionLabel) {
        this.professionLabel = professionLabel;
    }

    public String getInterestLabel() {
        return interestLabel;
    }

    public void setInterestLabel(String interestLabel) {
        this.interestLabel = interestLabel;
    }

    public Integer getLv() {
        return lv;
    }

    public void setLv(Integer lv) {
        this.lv = lv;
    }
}
