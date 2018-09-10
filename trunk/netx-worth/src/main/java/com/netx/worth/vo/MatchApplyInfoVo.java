package com.netx.worth.vo;

import java.util.Date;

public class MatchApplyInfoVo {
    private String matchGroupName;
    private Date endTime;
    private Date startTime;
    private String zoneName;
    private boolean isDefulat;
    private boolean isZoneTime;

    public Date getStartTime() {
        return startTime;
    }

    public boolean isZoneTime() {
        return isZoneTime;
    }

    public void setZoneTime(boolean zoneTime) {
        isZoneTime = zoneTime;
    }

    public boolean isDefulat() {
        return isDefulat;
    }

    public void setDefulat(boolean defulat) {
        isDefulat = defulat;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getMatchGroupName() {
        return matchGroupName;
    }

    public void setMatchGroupName(String matchGroupName) {
        this.matchGroupName = matchGroupName;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }


}
