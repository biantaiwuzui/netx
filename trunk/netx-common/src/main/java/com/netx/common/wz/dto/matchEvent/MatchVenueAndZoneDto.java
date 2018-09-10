package com.netx.common.wz.dto.matchEvent;


import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

public class MatchVenueAndZoneDto {
    @ApiModelProperty(value = "场次id(没有就不要传)")
    private String matchVenueId;
    @ApiModelProperty(value = "赛区id")
    @NotBlank(message = "赛区id不能为空")
    private String matchZoneId;

    public String getMatchVenueId() {
        return matchVenueId;
    }

    public void setMatchVenueId(String matchVenueId) {
        this.matchVenueId = matchVenueId;
    }

    public String getMatchZoneId() {
        return matchZoneId;
    }

    public void setMatchZoneId(String matchZoneId) {
        this.matchZoneId = matchZoneId;
    }
}
