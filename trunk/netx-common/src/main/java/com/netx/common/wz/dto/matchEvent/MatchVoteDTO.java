package com.netx.common.wz.dto.matchEvent;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Yawn on 2018/8/2 0002.
 */
public class MatchVoteDTO {
    /**
     * 比赛ID
     */
    @ApiModelProperty(value = "比赛ID")
    private String matchId;
    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID")
    private String projectId;
    /**
     * 项目介绍
     */
    @ApiModelProperty(value = "项目介绍")
    private String projectIntroduct;
    /**
     * 项目照片1
     */
    @ApiModelProperty(value = "项目照片1")
    private String projectImagesUrl;
    /**
     * 项目得分
     */
    @ApiModelProperty(value = "项目投票数")
    private Long projectVote;

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectIntroduct() {
        return projectIntroduct;
    }

    public void setProjectIntroduct(String projectIntroduct) {
        this.projectIntroduct = projectIntroduct;
    }

    public String getProjectImagesUrl() {
        return projectImagesUrl;
    }

    public void setProjectImagesUrl(String projectImagesUrl) {
        this.projectImagesUrl = projectImagesUrl;
    }

    public Long getProjectVote() {
        return projectVote;
    }

    public void setProjectVote(Long projectVote) {
        this.projectVote = projectVote;
    }
}
