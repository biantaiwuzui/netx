package com.netx.ucenter.vo.router;

import io.swagger.annotations.ApiModelProperty;

public class SearchUserInfoRequestDto {

    @ApiModelProperty("距离(单位:km)")
    private Double redaius;

    @ApiModelProperty("在线(单位:min)")
    private Long online;

    @ApiModelProperty("性别")
    private String sex;

    public Double getRedaius() {
        return redaius;
    }

    public void setRedaius(Double redaius) {
        this.redaius = redaius;
    }

    public Long getOnline() {
        return online;
    }

    public void setOnline(Long online) {
        this.online = online;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
