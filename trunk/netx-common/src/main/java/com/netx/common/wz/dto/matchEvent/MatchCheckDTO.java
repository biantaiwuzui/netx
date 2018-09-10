package com.netx.common.wz.dto.matchEvent;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;

public class MatchCheckDTO {
    @Min(message = "最小为1", value = 1)
    @ApiModelProperty(value = "页数(从1开始传)")
    private Integer page;
    @ApiModelProperty(value = "比赛Id")
    private String matchId;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }
}
