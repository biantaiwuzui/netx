package com.netx.common.user.model;

import io.swagger.annotations.ApiModelProperty;

/**
 * 存储到redis用户类
 *
 * @author 黎子安
 * @date 2017/10/23
 */
public class RedisUser {


    @ApiModelProperty("用户id")
    private String id;

    @ApiModelProperty("用户网号")
    private String userNumber;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("用户手机号")
    private String mobile;

    @ApiModelProperty("用户权限")
    private String userRole;

    @ApiModelProperty("性别")
    private String sex;

    /**
     * 最后操作时间
     */
    @ApiModelProperty("最后操作时间")
    private Long lastActiveAt;

    /**
     * 后台是否登录
     */
    @ApiModelProperty("后台是否登录")
    private int isLoginBackend;

    private Long expiredAt;

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public Long getLastActiveAt() {
        return lastActiveAt;
    }

    public void setLastActiveAt(Long lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }

    public int getIsLoginBackend() {
        return isLoginBackend;
    }

    public void setIsLoginBackend(int isLoginBackend) {
        this.isLoginBackend = isLoginBackend;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Long getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Long expiredAt) {
        this.expiredAt = expiredAt;
    }

    @Override
    public String toString() {
        return "RedisUser{" +
                "id='" + id + '\'' +
                ", userNumber='" + userNumber + '\'' +
                ", nickName='" + nickName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", userRole='" + userRole + '\'' +
                ", sex='" + sex + '\'' +
                ", lastActiveAt=" + lastActiveAt +
                ", isLoginBackend=" + isLoginBackend +
                ", expiredAt=" + expiredAt +
                '}';
    }
}
