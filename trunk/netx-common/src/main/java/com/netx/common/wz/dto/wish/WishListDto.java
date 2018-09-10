package com.netx.common.wz.dto.wish;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class WishListDto {

    private String id;
    private String userId;
    /**
     * 主题
     */
    private String title;
    /**
     * 标签，逗号分隔
     */
    private List<String> wishLabels;
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
    private List<String> refereeIds;
    /**
     * 描述
     */
    private String description;
    /**
     * 图片
     */
    private String images;

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

    /**
     * 发布者昵称
     */
    private String nickname;

    /**
     * 发布者性别
     */
    private String sex;

    /**
     * 发布者生日
     */
    private Date birthday;

    /**
     * 发布者手机
     */
    private String mobile;

    /**
     * 发布者总积分
     */
    private BigDecimal score;

    /**
     * 发布者信用
     */
    private Integer credit;

    /**
     * 创建时间
     */
    private Date publishTime;

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
    private Boolean isHoldCredit;

    /**
     * 人数
     */
    private Integer count;

    private String worthType = "Wish";

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

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

    public List<String> getWishLabels() {
        return wishLabels;
    }

    public void setWishLabels(List<String> wishLabels) {
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

    public Date getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Date expiredAt) {
        this.expiredAt = expiredAt;
    }

    public List<String> getRefereeIds() {
        return refereeIds;
    }

    public void setRefereeIds(List<String> refereeIds) {
        this.refereeIds = refereeIds;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getWorthType() {
        return worthType;
    }

    public void setWorthType(String worthType) {
        this.worthType = worthType;
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

    public Boolean getHoldCredit() {
        return isHoldCredit;
    }

    public void setHoldCredit(Boolean holdCredit) {
        isHoldCredit = holdCredit;
    }
}
