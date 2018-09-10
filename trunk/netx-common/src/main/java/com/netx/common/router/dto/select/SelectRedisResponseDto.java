package com.netx.common.router.dto.select;

import io.swagger.annotations.ApiModelProperty;

public class SelectRedisResponseDto {
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

    @ApiModelProperty("纬度")
    private Double lon;

    @ApiModelProperty("经度")
    private Double lat;

    @ApiModelProperty("距离")
    private Double distance;

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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}
