package com.netx.worth.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

public class MatchGroupAndTimeVo {
    @ApiModelProperty("赛组名字")
    private String matchGroupName;
    @ApiModelProperty("赛组ID")
    private String groupId;
    @ApiModelProperty("结束时间")
    private Date endTime;
    @ApiModelProperty("开始时间")
    private Date startTime;
    @ApiModelProperty("ID")
    private String id;
    private boolean isDefault;

    private boolean isZoneTime;
    @ApiModelProperty(value = "报名费用")
    private int matchGroupFree;
    @ApiModelProperty(value = "参赛名额")
    private int matchGroupQuota;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public boolean isZoneTime() {
        return isZoneTime;
    }

    public void setZoneTime(boolean zoneTime) {
        isZoneTime = zoneTime;
    }

    public String getMatchGroupName() {
        return matchGroupName;
    }

    public void setMatchGroupName(String matchGroupName) {
        this.matchGroupName = matchGroupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public int getMatchGroupFree() {
        return matchGroupFree;
    }

    public void setMatchGroupFree(int matchGroupFree) {
        this.matchGroupFree = matchGroupFree;
    }

    public int getMatchGroupQuota() {
        return matchGroupQuota;
    }

    public void setMatchGroupQuota(int matchGroupQuota) {
        this.matchGroupQuota = matchGroupQuota;
    }
}
