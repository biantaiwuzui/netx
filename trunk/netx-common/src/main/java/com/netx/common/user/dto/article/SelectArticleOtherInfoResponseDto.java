package com.netx.common.user.dto.article;

import io.swagger.annotations.ApiModelProperty;

public class SelectArticleOtherInfoResponseDto {
    @ApiModelProperty("点赞数量")
    private int likesNumber;

    @ApiModelProperty("评论数量")
    private int commentNumber;

    @ApiModelProperty("赠送数量")
    private int giftNumber;

    @ApiModelProperty("邀请数量")
    private int invitationNumber;

    public SelectArticleOtherInfoResponseDto() {
        likesNumber = 0;
        commentNumber = 0;
        giftNumber = 0;
        invitationNumber = 0;
    }

    public int getLikesNumber() {
        return likesNumber;
    }

    public void setLikesNumber(int likesNumber) {
        this.likesNumber = likesNumber;
    }

    public int getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(int commentNumber) {
        this.commentNumber = commentNumber;
    }

    public int getGiftNumber() {
        return giftNumber;
    }

    public void setGiftNumber(int giftNumber) {
        this.giftNumber = giftNumber;
    }

    public int getInvitationNumber() {
        return invitationNumber;
    }

    public void setInvitationNumber(int invitationNumber) {
        this.invitationNumber = invitationNumber;
    }
}
