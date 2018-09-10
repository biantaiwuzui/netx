package com.netx.common.user.dto.article;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class SelectArticleResponseDto {

    @ApiModelProperty("咨讯id")
    private String id;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("用户昵称")
    private String nickname;

    @ApiModelProperty("用户昵称")
    private Integer age;

    @ApiModelProperty("用户昵称")
    private String sex;

    @ApiModelProperty("用户信用")
    private Integer credit;

    @ApiModelProperty("用户头像")
    private String headImg;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("图片")
    private String pic;

    @ApiModelProperty("附件（音视独有）")
    private String atta;

    @ApiModelProperty("发布的位置")
    private String location;

    @ApiModelProperty("发布时间")
    private Long createTime;

    @ApiModelProperty("经度")
    private BigDecimal lon;

    @ApiModelProperty("维度")
    private BigDecimal lat;

    @ApiModelProperty("‘读’的数量")
    private Long hits;

    @ApiModelProperty("点赞数量")
    private int likesNumber;

    @ApiModelProperty("评论数量")
    private int commentNumber;

    @ApiModelProperty("赠送数量")
    private int giftNumber;

    @ApiModelProperty("邀请数量")
    private int invitationNumber;

    @ApiModelProperty("是否匿名")
    private boolean isAnonymity;

    @ApiModelProperty("图文状态")
    private Integer statusCode;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public Long getHits() {
        return hits;
    }

    public void setHits(Long hits) {
        this.hits = hits;
    }

    public BigDecimal getLon() {
        return lon;
    }

    public void setLon(BigDecimal lon) {
        this.lon = lon;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public boolean isAnonymity() {
        return isAnonymity;
    }

    public void setAnonymity(boolean anonymity) {
        isAnonymity = anonymity;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
}
