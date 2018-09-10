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
 * 
 * </p>
 *
 * @author Yawn
 * @since 2018-08-19
 */
@TableName("match_progress_participant")
public class MatchProgressParticipant extends Model<MatchProgressParticipant> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 比赛ID
     */
	@TableField("match_id")
	private String matchId;
    /**
     * 赛区ID
     */
	@TableField("zone_id")
	private String zoneId;
    /**
     * 赛组ID
     */
	@TableField("group_id")
	private String groupId;
    /**
     * 参赛者ID
     */
	@TableField("participant_id")
	private String participantId;
    /**
     * 赛制ID
     */
	@TableField("progress_id")
	private String progressId;


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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MatchProgressParticipant{" +
			"id=" + id +
			", matchId=" + matchId +
			", zoneId=" + zoneId +
			", groupId=" + groupId +
			", participantId=" + participantId +
			", progressId=" + progressId +
			"}";
	}
}
