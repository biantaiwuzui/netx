package com.netx.common.wz.dto.matchEvent;

import io.swagger.annotations.ApiModelProperty;

/**
 * 参赛团队
 * Created by Yawn on 2018/8/1 0001.
 */
public class MatchTeamDTO {
    /**
     * 比赛ID
     */
    @ApiModelProperty(value = "比赛ID")
    private String matchId;
    /**
     * 团队名称
     */
    @ApiModelProperty(value = "团队名称")
    private String teamName;
    /**
     * 团队口号
     */
    @ApiModelProperty(value = "团队口号")
    private String teamSlogan;
    /**
     * 团队简介
     */
    @ApiModelProperty(value = "团队简介")
    private String teamIntroduction;
    /**
     * 队长
     */
    @ApiModelProperty(value = "队长")
    private String teamLeader;
    /**
     * 团队图标
     */
    @ApiModelProperty(value = "团队图标")
    private String teamImageUrl;

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamSlogan() {
        return teamSlogan;
    }

    public void setTeamSlogan(String teamSlogan) {
        this.teamSlogan = teamSlogan;
    }

    public String getTeamIntroduction() {
        return teamIntroduction;
    }

    public void setTeamIntroduction(String teamIntroduction) {
        this.teamIntroduction = teamIntroduction;
    }

    public String getTeamImageUrl() {
        return teamImageUrl;
    }

    public void setTeamImageUrl(String teamImageUrl) {
        this.teamImageUrl = teamImageUrl;
    }

    public String getTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(String teamLeader) {
        this.teamLeader = teamLeader;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }
}
