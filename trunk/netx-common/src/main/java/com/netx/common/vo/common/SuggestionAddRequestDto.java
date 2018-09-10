package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * Create by wongloong on 17-9-9
 */
@ApiModel("添加意见")
public class SuggestionAddRequestDto {
    @NotNull
    @ApiModelProperty(notes = "内容", required = true)
    private String content;
    @NotNull
    @ApiModelProperty(notes = "提交者id", required = true)
    private String userId;
    @NotNull
    @ApiModelProperty(notes = "提交者昵称", required = true)
    private String userName;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
