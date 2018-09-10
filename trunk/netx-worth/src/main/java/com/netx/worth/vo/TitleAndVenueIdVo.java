package com.netx.worth.vo;

import io.swagger.annotations.ApiModelProperty;

public class TitleAndVenueIdVo {
    @ApiModelProperty(value = "场次的标题")
    private String title;
    @ApiModelProperty(value = "场次的id")
    private String matchVenueId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMatchVenueId() {
        return matchVenueId;
    }

    public void setMatchVenueId(String matchVenueId) {
        this.matchVenueId = matchVenueId;
    }
}
