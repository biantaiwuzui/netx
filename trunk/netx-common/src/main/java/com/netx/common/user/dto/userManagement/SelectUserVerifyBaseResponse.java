package com.netx.common.user.dto.userManagement;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;

@ApiModel("用户认证请求基数")
public class SelectUserVerifyBaseResponse {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 等级
     */
    private Integer lv;

    /**
     * 性别
     */
    private String sex;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 网号
     */
    private String userNumber;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 认证请求发出时间
     */
    private Long createTime;

    /**
     * 处理人
     */
    private String admin;

    /**
     * 认证状态
     */
    private Integer status;

    /**
     * 不通过原因
     */
    private String reason;

    /**
     * 认证类型
     */
    private Integer verifyType;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(Integer verifyType) {
        this.verifyType = verifyType;
    }

    @Override
    public String toString() {
        return "SelectUserVerifyBaseResponse{" +
                "userId='" + userId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", lv=" + lv +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", userNumber='" + userNumber + '\'' +
                ", mobile='" + mobile + '\'' +
                ", createTime=" + createTime +
                ", admin='" + admin + '\'' +
                ", status=" + status +
                ", reason='" + reason + '\'' +
                ", verifyType=" + verifyType +
                '}';
    }
}
