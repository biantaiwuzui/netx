package com.netx.common.user.dto.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@ApiModel("图文信息")
public class SelectArticleDetailResponseDto {
    @ApiModelProperty("图文id")
    private String id;

    @ApiModelProperty("发布时间")
    private Date createTime;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("内容（图文独有）")
    private String content;

    @ApiModelProperty("附件")
    private String atta;

    @ApiModelProperty("图片url，多张图片以逗号隔开")
    private String pic;

    @ApiModelProperty("软文类型:<br>" +
            "0：免费图文<br>" +
            "1：软文【广告】" )
    private Integer advertorialType;

    private Integer statusCode;

    private boolean isCollect;

    private boolean isLike;

    @ApiModelProperty("点赞数量")
    private int likesNumber;

    @ApiModelProperty("评论数量")
    private int commentNumber;

    @ApiModelProperty("赠送数量")
    private int giftNumber;

    @ApiModelProperty("邀请数量")
    private int invitationNumber;

    @ApiModelProperty("收藏数量")
    private int collectNumber;

    @ApiModelProperty("关联的活动")
    private List<Object> objects;

    @ApiModelProperty("关联的活动类型")
    private String worthType;

    public List<Object> getObjects() {
        return objects;
    }

    public void setObjects(List<Object> objects) {
        this.objects = objects;
    }

    public String getWorthType() {
        return worthType;
    }

    public void setWorthType(String worthType) {
        this.worthType = worthType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getAtta() {
        return atta;
    }

    public void setAtta(String atta) {
        this.atta = atta;
    }

    public Integer getAdvertorialType() {
        return advertorialType;
    }

    public void setAdvertorialType(Integer advertorialType) {
        this.advertorialType = advertorialType;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isCollect() {
        return isCollect;
    }

    public void setIsCollect(boolean isCollect) {
        this.isCollect = isCollect;
    }

    public boolean getIsLike() {
        return isLike;
    }

    public void setIsLike(boolean isLike) {
        this.isLike = isLike;
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

    public int getCollectNumber() {
        return collectNumber;
    }

    public void setCollectNumber(int collectNumber) {
        this.collectNumber = collectNumber;
    }
}
