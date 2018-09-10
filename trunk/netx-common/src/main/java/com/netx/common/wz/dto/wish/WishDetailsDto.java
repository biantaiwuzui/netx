package com.netx.common.wz.dto.wish;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 心愿详情
 */
public class
WishDetailsDto {

    private String id;
    private String userId;
    /**
     * 主题
     */
    private String title;
    /**
     * 标签，逗号分隔
     */
    private String wishLabel;
    /**
     * 希望筹集的金额
     */
    private BigDecimal amount;
    /**
     * 当前筹集数
     */
    private BigDecimal currentAmount;
    /**
     * 当前已使用金额
     */
    private BigDecimal currentApplyAmount;
    /**
     * 截至时间
     */
    private Date expiredAt;
    /**
     * 推荐人，逗号分隔
     */
    private String refereeIds;
    /**
     * 描述
     */
    private String description;
    /**
     * 图片
     */
    private String wishImagesUrl;
    /**
     * 图片
     */
    private String wishImagesTwoUrl;
    /**
     * 状态：
     1：已发布
     2：已取消
     3：已关闭，即推荐人数不足50%
     4：推荐成功
     5：已失败，即筹款目标未达成
     6：筹集目标达成，即心愿发起成功
     7：已完成，即金额使用完毕
     */
    private Integer status;
    /**
     * 距离
     */
    private Double distance;

    private Integer credit;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 发布者等级
     */
    private Integer lv;

    /**
     * 发布者的年龄
     */
    private Integer age;

    /**
     * 支持网信
     */
    private Boolean isHoldcredit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getWishLabel() {
        return wishLabel;
    }

    public void setWishLabel(String wishLabel) {
        this.wishLabel = wishLabel;
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

    public Date getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Date expiredAt) {
        this.expiredAt = expiredAt;
    }

    public String getRefereeIds() {
        return refereeIds;
    }

    public void setRefereeIds(String refereeIds) {
        this.refereeIds = refereeIds;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWishImagesUrl() {
        return wishImagesUrl;
    }

    public void setWishImagesUrl(String wishImagesUrl) {
        this.wishImagesUrl = wishImagesUrl;
    }

    public String getWishImagesTwoUrl() {
        return wishImagesTwoUrl;
    }

    public void setWishImagesTwoUrl(String wishImagesTwoUrl) {
        this.wishImagesTwoUrl = wishImagesTwoUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getLv() {
        return lv;
    }

    public void setLv(Integer lv) {
        this.lv = lv;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getHoldcredit() {
        return isHoldcredit;
    }

    public void setHoldcredit(Boolean holdcredit) {
        isHoldcredit = holdcredit;
    }
}