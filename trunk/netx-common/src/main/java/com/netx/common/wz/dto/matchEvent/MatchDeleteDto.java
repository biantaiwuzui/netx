package com.netx.common.wz.dto.matchEvent;

import io.swagger.annotations.ApiModelProperty;

public class MatchDeleteDto {
    @ApiModelProperty(value = "要删除的赛事相关的东西的id")
    private String deleteId;
    @ApiModelProperty(value = "赛事id")
    private String matchId;
    @ApiModelProperty(value = "这个前端不用填")
    private String userId;

    public String getDeleteId() {
        return deleteId;
    }

    public void setDeleteId(String deleteId) {
        this.deleteId = deleteId;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
