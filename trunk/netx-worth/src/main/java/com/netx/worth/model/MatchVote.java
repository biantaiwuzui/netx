package com.netx.worth.model;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.annotations.Version;
import java.io.Serializable;

/**
 * <p>
 * 投票
 * </p>
 *
 * @author Yawn
 * @since 2018-08-15
 */
@TableName("match_vote")
public class MatchVote extends Model<MatchVote> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 比赛ID
     */
	@TableField("match_id")
	private String matchId;
    /**
     * 项目ID
     */
	@TableField("project_id")
	private String projectId;
    /**
     * 项目介绍
     */
	@TableField("project_introduct")
	private String projectIntroduct;
    /**
     * 项目照片1
     */
	@TableField("project_images_url")
	private String projectImagesUrl;
    /**
     * 项目得分
     */
	@TableField("project_vote")
	private Long projectVote;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MatchVote{" +
			"id=" + id +
			", matchId=" + matchId +
			", projectId=" + projectId +
			", projectIntroduct=" + projectIntroduct +
			", projectImagesUrl=" + projectImagesUrl +
			", projectVote=" + projectVote +
			"}";
	}
}
