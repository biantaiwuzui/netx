package com.netx.common.wz.dto.matchEvent;

import io.swagger.annotations.ApiModelProperty;

public class MatchProgressParticipantDTO {

    /**
     * 比赛ID
     */
    @ApiModelProperty("比赛ID")
    private String matchId;
    /**
     * 赛区ID
     */
    @ApiModelProperty("赛区ID")
    private String zoneId;
    /**
     * 赛组ID
     */
    @ApiModelProperty("赛组ID")
    private String groupId;
    /**
     * 参赛者ID
     */
    @ApiModelProperty("参赛者ID")
    private String participantId;
    /**
     * 赛制ID
     */
    @ApiModelProperty("赛制ID")
    private String progressId;


    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public String getProgressId() {
        return progressId;
    }

    public void setProgressId(String progressId) {
        this.progressId = progressId;
    }
}
