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
 * 参赛团队
 * </p>
 *
 * @author Yawn
 * @since 2018-08-15
 */
@TableName("match_team")
public class MatchTeam extends Model<MatchTeam> {

    private static final long serialVersionUID = 1L;

	private String id;
	@TableField("match_id")
	private String matchId;
    /**
     * 团队
     */
	@TableField("team_name")
	private String teamName;
    /**
     * 团队口号
     */
	@TableField("team_slogan")
	private String teamSlogan;
    /**
     * 团队简介
     */
	@TableField("team_introduction")
	private String teamIntroduction;
    /**
     * 团队图标
     */
	@TableField("team_image_url")
	private String teamImageUrl;
    /**
     * 是否通过比赛审核
     */
	@TableField("team_leader")
	private String teamLeader;
	@TableField("is_pass")
	private Boolean isPass;


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

	public Boolean getPass() {
		return isPass;
	}

	public void setPass(Boolean isPass) {
		this.isPass = isPass;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MatchTeam{" +
			"id=" + id +
			", matchId=" + matchId +
			", teamName=" + teamName +
			", teamSlogan=" + teamSlogan +
			", teamIntroduction=" + teamIntroduction +
			", teamImageUrl=" + teamImageUrl +
			", teamLeader=" + teamLeader +
			", isPass=" + isPass +
			"}";
	}
}
