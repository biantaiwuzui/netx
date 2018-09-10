package com.netx.worth.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * 赛事简单描述VO
 * Created by Yawn on 2018/9/3 0003.
 */
public class MatchEventSimpleVo {
    //比赛ID
    @ApiModelProperty(value = "比赛ID")
    private String matchId;
    //    赛事名称
    @ApiModelProperty(value = "赛事名称")
    private String title;
    //    赛事子标题
    @ApiModelProperty(value = "赛事子标题")
    private String subTitle;
    //    赛事状态
    @ApiModelProperty(value = "赛事状态")
    private Integer matchStatus;
    //    赛事照片
    @ApiModelProperty(value = "赛事照片")
    private String matchImageUrl;
    //    赛事类型
    @ApiModelProperty(value = "赛事类型")
    private Integer kind;
    //    赛事组织方名字
    @ApiModelProperty(value = "赛事组织方名字")
    private String organizer;
    //     赛事报名人数
    @ApiModelProperty(value = "赛事报名人数")
    private int participantSum;
    //    距离开始还有多少天
    @ApiModelProperty(value = "距离开始还有多少天")
    private int toBeginDay;
    //    距离
    @ApiModelProperty(value = "距离,后端逻辑还没写，先不要显示")
    private float distance;
    //    组织者名字
    @ApiModelProperty(value = "组织者名字")
    private String organizerName;
    //    组织者年龄
    @ApiModelProperty(value = "组织者年龄")
    private int organizerAge;
    //    性别
    @ApiModelProperty(value = "性别")
    private String organizerSex;
    //    信用
    @ApiModelProperty(value = "信用")
    private int organizerCredit;

    @ApiModelProperty(value = "正在进行的个数")
    private Integer matchingCount;
    @ApiModelProperty(value = "总数")
    private Integer matchCount;
    @ApiModelProperty("类型")
    private String worthType;

    public String getWorthType() {
        return worthType;
    }

    public void setWorthType(String worthType) {
        this.worthType = worthType;
    }

    public Integer getMatchingCount() {
        return matchingCount;
    }

    public void setMatchingCount(Integer matchingCount) {
        this.matchingCount = matchingCount;
    }

    public Integer getMatchCount() {
        return matchCount;
    }

    public void setMatchCount(Integer matchCount) {
        this.matchCount = matchCount;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
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

    public Integer getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(Integer matchStatus) {
        this.matchStatus = matchStatus;
    }

    public String getMatchImageUrl() {
        return matchImageUrl;
    }

    public void setMatchImageUrl(String matchImageUrl) {
        this.matchImageUrl = matchImageUrl;
    }

    public Integer getKind() {
        return kind;
    }

    public void setKind(Integer kind) {
        this.kind = kind;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public int getParticipantSum() {
        return participantSum;
    }

    public void setParticipantSum(int participantSum) {
        this.participantSum = participantSum;
    }

    public int getToBeginDay() {
        return toBeginDay;
    }

    public void setToBeginDay(int toBeginDay) {
        this.toBeginDay = toBeginDay;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public int getOrganizerAge() {
        return organizerAge;
    }

    public void setOrganizerAge(int organizerAge) {
        this.organizerAge = organizerAge;
    }

    public String getOrganizerSex() {
        return organizerSex;
    }

    public void setOrganizerSex(String organizerSex) {
        this.organizerSex = organizerSex;
    }

    public int getOrganizerCredit() {
        return organizerCredit;
    }

    public void setOrganizerCredit(int organizerCredit) {
        this.organizerCredit = organizerCredit;
    }
}
