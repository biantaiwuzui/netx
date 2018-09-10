package com.netx.common.wz.dto.matchEvent;

import io.swagger.annotations.ApiModelProperty;

public class MatchGroupAndVenueDto {
    @ApiModelProperty(value = "场次id")
    private String matchVenueId;
    @ApiModelProperty(value = "赛组id")
    private String matchGroupId;

    public String getMatchVenueId() {
        return matchVenueId;
    }

    public void setMatchVenueId(String matchVenueId) {
        this.matchVenueId = matchVenueId;
    }

    public String getMatchGroupId() {
        return matchGroupId;
    }

    public void setMatchGroupId(String matchGroupId) {
        this.matchGroupId = matchGroupId;
    }
}
