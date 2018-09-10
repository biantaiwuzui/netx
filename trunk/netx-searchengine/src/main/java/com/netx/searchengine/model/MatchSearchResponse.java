package com.netx.searchengine.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class MatchSearchResponse {
    /**
     * 赛事id
     */
    private String id;
    /**
     * 赛事图片
     */
    private List<String> matchImageUrl;
    /**
     * 赛事标题
     */
    private String title;
    /**
     * 副标题
     */
    private String subTitle;
    /**
     * 赛事类型
     */
    private Integer matchKind;
    /**
     * 赛事规则
     */
    private String matchRule;
    /**
     * 评分标准
     */
    private String grading;
    /**
     * 比赛介绍
     */
    private String matchIntroduction;
    /**
     * 赛事状态
     */
    private Integer matchStatus;
    /**
     * 通过时间
     */
    private Date publishTime;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 性别
     */
    private String sex;
    /**
     * 生日
     */
    private Date birthday;
    /**
     * 电话
     */
    private String mobile;
    /**
     * 分数
     */
    private BigDecimal score;
    /**
     * 信用
     */
    private Integer credit;
    /**
     * 是否登录
     */
    private Integer isLogin;
    /**
     * 等级
     */
    private Integer lv;
    /**
     * 信用综合
     */
    private Integer creditSum;
    /**
     * 商家头像
     */
    private String pictureUrl;
    /**
     * 商家id
     */
    private String merchantId;
    /**
     * 商家名称
     */
    private String organizerName;

    private String location;
    /**
     *创建者id
     */
    private String initimtorId;
    /**
     * 点击量
     */
    private Integer hit;

    private Double distance;

    private Integer regCount;

    public Integer getRegCount() {
        return regCount;
    }

    public void setRegCount(Integer regCount) {
        this.regCount = regCount;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public List<String> getMatchImageUrl() {
        return matchImageUrl;
    }

    public void setMatchImageUrl(List<String> matchImageUrl) {
        this.matchImageUrl = matchImageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public Integer getMatchKind() {
        return matchKind;
    }

    public void setMatchKind(Integer matchKind) {
        this.matchKind = matchKind;
    }

    public String getMatchRule() {
        return matchRule;
    }

    public void setMatchRule(String matchRule) {
        this.matchRule = matchRule;
    }

    public String getGrading() {
        return grading;
    }

    public void setGrading(String grading) {
        this.grading = grading;
    }

    public String getMatchIntroduction() {
        return matchIntroduction;
    }

    public void setMatchIntroduction(String matchIntroduction) {
        this.matchIntroduction = matchIntroduction;
    }

    public Integer getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(Integer matchStatus) {
        this.matchStatus = matchStatus;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
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

    public Integer getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(Integer isLogin) {
        this.isLogin = isLogin;
    }

    public Integer getLv() {
        return lv;
    }

    public void setLv(Integer lv) {
        this.lv = lv;
    }

    public Integer getCreditSum() {
        return creditSum;
    }

    public void setCreditSum(Integer creditSum) {
        this.creditSum = creditSum;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getInitimtorId() {
        return initimtorId;
    }

    public void setInitimtorId(String initimtorId) {
        this.initimtorId = initimtorId;
    }

    public Integer getHit() {
        return hit;
    }

    public void setHit(Integer hit) {
        this.hit = hit;
    }
}
