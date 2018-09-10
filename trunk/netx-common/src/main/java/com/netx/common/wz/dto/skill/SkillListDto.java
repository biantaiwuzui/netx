package com.netx.common.wz.dto.skill;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class SkillListDto {

    private String id;
    /**
     * 技能拥有者id
     */
    private String userId;

    /**
     *  预约者id
     */
    private String reId;
    /**
     * 技能标签，逗号分隔
     */
    private String title;
    /**
     * 水平标签，逗号分隔
     */
    private List<String> levels;
    /**
     * 描述
     */
    private String description;
    /**
     * 图片
     */
    private String images;

    /**
     * 单位
     */
    private String unit;
    /**
     * 单价
     */
    private BigDecimal amount;
    /**
     * 价格说明
     */
    private String intr;
    /**
     * 预约对象：
     1：不限制。
     2：仅限线上交易
     3：仅接受附近预约
     4：仅限女性预约
     5：仅限男性预约
     6：仅限好友预约
     */
    private Integer obj;
    /**
     * 状态：
     1：已发布
     2：已取消
     3：已结束
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
     * 报名人数
     */
    private Integer count;

    private String worthType = "Skill";

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

    public List<String> getLevels() {
        return levels;
    }

    public void setLevels(List<String> levels) {
        this.levels = levels;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReId() {
        return reId;
    }

    public void setReId(String reId) {
        this.reId = reId;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getIntr() {
        return intr;
    }

    public void setIntr(String intr) {
        this.intr = intr;
    }

    public Integer getObj() {
        return obj;
    }

    public void setObj(Integer obj) {
        this.obj = obj;
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
