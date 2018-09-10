package com.netx.common.vo.common;

import io.swagger.annotations.ApiModelProperty;

public class GetUserArticleRequestDto{
    @ApiModelProperty("用户id")
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
