package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * Create by wongloong on 17-9-9
 */
@ApiModel
public class SuggestionReplyRequestDto {
    @NotNull
    @ApiModelProperty(required = true, name = "回复id")
    private String id;
    @NotNull
    @ApiModelProperty(required = true, name = "回复内容")
    private String replyContent;
    @NotNull
    @ApiModelProperty(required = true, name = "回复人id")
    private String replyUserId;

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public String getReplyUserId() {
        return replyUserId;
    }

    public void setReplyUserId(String replyUserId) {
        this.replyUserId = replyUserId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
