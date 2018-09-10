package com.netx.common.user.dto.user;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class ScreenUserRequestDto {
    @ApiModelProperty("用户id集")
    private List<String> userIds;

    @ApiModelProperty("在线时间条件（min）,默认一分钟前")
    private Long time;

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "ScreenUserRequestDto{" +
                "userIds=" + userIds +
                ", time=" + time +
                '}';
    }
}
