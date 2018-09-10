package com.netx.searchengine.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class WorthSearchResponse {
    /**
     * 网能id
     */
    private String id;

    /**
     * 网能类型
     */
    private String worthType;

    /**
     * 网能标题
     */
    private String title;

    /**
     * 网能详情
     */
    private String detail;

    /**
     * 点击量
     */
    private Integer hit;

    /**
     * 发布时间
     */
    private Date publishTime;

    /**
     * 结束时间
     */
    private Date endTime;

    private BigDecimal amount;

    private Integer num;

    /**
     * 成交数
     */
    private Integer count;

    /**
     * 成交总额
     */
    private BigDecimal total;

    /**
     * 网能图片
     */
    private String images;

    /**
     * 单位
     */
    private String unit;

    /**
     * 是否支持网信
     */
    private Boolean isHoldCredit;

    /**
     * 经度
     */
    private BigDecimal lon;

    /**
     * 纬度
     */
    private BigDecimal lat;

    /**
     * 距离
     */
    private Double distance;

    /**
     * 发布者id
     */
    private String userId;

    /**
     * 发布者头像
     */
    private String headImg;

    /**
     * 发布者生日
     */
    private Date birthday;

    /**
     * 发布者昵称
     */
    private String nickname;

    /**
     * 发布者性别
     */
    private String sex;

    /**
     * 发布者等级
     */
    private Integer lv;

    /**
     * 是否在线
     */
    private Boolean isLogin;

    /**
     * 信用
     */
    private Integer credit;

    /**
     * 最后登录时间
     */
    private Date lastLoginAt;

    /**
     * 最后操作时间
     */
    private Date lastActiveAt;

    /**
     * 发布状态
     */
    private Integer status;

    private Integer meetingType;
    /**
     * 赛事图片
     */
    private List<String> matchImageUrl;
    /**
     * 赛事副标题
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
     * 赛事
     */
    private String grading;
    /**
     * 赛事
     */
    private String matchIntroduction;
    /**
     * 赛事状态
     */
    private Integer matchStatus;
    /**
     * 赛事商家名字
     */
    private String organizerName;
    /**
     * 赛事商家id
     */
    private String initimtorId;
    /**
     * 报名数
     */
    private Integer regCount;

    public List<String> getMatchImageUrl() {
        return matchImageUrl;
    }

    public void setMatchImageUrl(List<String> matchImageUrl) {
        this.matchImageUrl = matchImageUrl;
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

    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public String getInitimtorId() {
        return initimtorId;
    }

    public void setInitimtorId(String initimtorId) {
        this.initimtorId = initimtorId;
    }

    public Integer getRegCount() {
        return regCount;
    }

    public void setRegCount(Integer regCount) {
        this.regCount = regCount;
    }

    public Integer getMeetingType() {
        return meetingType;
    }

    public void setMeetingType(Integer meetingType) {
        this.meetingType = meetingType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWorthType() {
        return worthType;
    }

    public void setWorthType(String worthType) {
        this.worthType = worthType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getHit() {
        return hit;
    }

    public void setHit(Integer hit) {
        this.hit = hit;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Boolean getHoldCredit() {
        return isHoldCredit;
    }

    public void setHoldCredit(Boolean holdCredit) {
        isHoldCredit = holdCredit;
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

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getLv() {
        return lv;
    }

    public void setLv(Integer lv) {
        this.lv = lv;
    }

    public Boolean getLogin() {
        return isLogin;
    }

    public void setLogin(Boolean login) {
        isLogin = login;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Date getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(Date lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public Date getLastActiveAt() {
        return lastActiveAt;
    }

    public void setLastActiveAt(Date lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
