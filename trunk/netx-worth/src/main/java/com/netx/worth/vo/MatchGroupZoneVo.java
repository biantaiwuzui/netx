package com.netx.worth.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 根据赛区查找赛组
 * Created by Yawn on 2018/8/27 0027.
 */
public class MatchGroupZoneVo {
    @ApiModelProperty(value = "赛组ID")
    private String groupId;
    /**
     * 赛组名称
     */
    @ApiModelProperty(value = "赛区名称")
    private String matchZoneName;
    @ApiModelProperty(value = "赛组名称")
    private String matchGroupName;
    @ApiModelProperty("参赛名额")
    private Integer quota;
    @ApiModelProperty(value = "报名人数")
    private Integer participantSum;
    @ApiModelProperty("已通过")
    private Integer passNumber;
    private Date startTime;
    private Date endTime;
    private String zoneId;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getMatchZoneName() {
        return matchZoneName;
    }

    public void setMatchZoneName(String matchZoneName) {
        this.matchZoneName = matchZoneName;
    }

    public String getMatchGroupName() {
        return matchGroupName;
    }

    public void setMatchGroupName(String matchGroupName) {
        this.matchGroupName = matchGroupName;
    }

    public Integer getQuota() {
        return quota;
    }

    public void setQuota(Integer quota) {
        this.quota = quota;
    }

    public Integer getParticipantSum() {
        return participantSum;
    }

    public void setParticipantSum(Integer participantSum) {
        this.participantSum = participantSum;
    }

    public Integer getPassNumber() {
        return passNumber;
    }

    public void setPassNumber(Integer passNumber) {
        this.passNumber = passNumber;
    }
}
