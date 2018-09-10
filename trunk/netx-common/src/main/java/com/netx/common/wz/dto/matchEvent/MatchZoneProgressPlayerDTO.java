package com.netx.common.wz.dto.matchEvent;

import io.swagger.annotations.ApiModelProperty;

/**
 * 用来获取该赛区不同赛制的入选人
 */
public class MatchZoneProgressPlayerDTO {
    @ApiModelProperty(value = "赛区id")
    private String zoneId;
    @ApiModelProperty(value = "赛制id")
    private String progressId;

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getProgressId() {
        return progressId;
    }

    public void setProgressId(String progressId) {
        this.progressId = progressId;
    }
}
