package com.netx.common.user.dto.user;

import io.swagger.annotations.ApiModelProperty;

public class OnlineUserRequestDto {
    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("在线时间条件（min）,默认一分钟前")
    private Long time;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
