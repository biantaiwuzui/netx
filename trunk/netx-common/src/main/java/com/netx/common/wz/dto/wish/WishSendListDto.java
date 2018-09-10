package com.netx.common.wz.dto.wish;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class WishSendListDto {

    @ApiModelProperty("申请时间")
    private Date createtime;

    @ApiModelProperty("心愿id")
    private String id;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("等级")
    private Integer lv;

    @ApiModelProperty("头像")
    private String headImgUrl;

    @ApiModelProperty("发布者信用")
    private Integer credit;

    @ApiModelProperty("距离")
    private Double distance;

    @ApiModelProperty("标签")
    private String wishLabels;

    @ApiModelProperty("目标金额")
    private BigDecimal amount;

    @ApiModelProperty("已筹金额")
    private BigDecimal currentAmount;

    @ApiModelProperty("已使用金额")
    private BigDecimal currentApplyAmount;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("心愿图片")
    private String WishImagesUrl;

    @ApiModelProperty("心愿图片第二张")
    private String WishImagesTwoUrl;




    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 截至时间
     */
    private Date expiredAt;

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Integer getLv() {
        return lv;
    }

    public void setLv(Integer lv) {
        this.lv = lv;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getWishLabels() {
        return wishLabels;
    }

    public void setWishLabels(String wishLabels) {
        this.wishLabels = wishLabels;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(BigDecimal currentAmount) {
        this.currentAmount = currentAmount;
    }

    public BigDecimal getCurrentApplyAmount() {
        return currentApplyAmount;
    }

    public void setCurrentApplyAmount(BigDecimal currentApplyAmount) {
        this.currentApplyAmount = currentApplyAmount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Date expiredAt) {
        this.expiredAt = expiredAt;
    }

    public String getWishImagesUrl() {
        return WishImagesUrl;
    }

    public void setWishImagesUrl(String wishImagesUrl) {
        WishImagesUrl = wishImagesUrl;
    }

    public String getWishImagesTwoUrl() {
        return WishImagesTwoUrl;
    }

    public void setWishImagesTwoUrl(String wishImagesTwoUrl) {
        WishImagesTwoUrl = wishImagesTwoUrl;
    }
}