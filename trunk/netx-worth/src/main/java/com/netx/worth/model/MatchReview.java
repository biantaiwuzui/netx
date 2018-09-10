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
 * 比赛审核人
 * </p>
 *
 * @author Yawn
 * @since 2018-08-15
 */
@TableName("match_review")
public class MatchReview extends Model<MatchReview> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	private String id;
    /**
     * 比赛id
     */
	@TableField("match_id")
	private String matchId;
    /**
     * 审核者id
     */
	@TableField("user_id")
	private String userId;
	@TableField("organizer_name")
	private String organizerName;
	@TableField("organizer_kind")
	private Integer organizerKind;
	@TableField("is_accept")
	private Boolean isAccept;
	@TableField("is_approve")
	private Boolean isApprove;
	@TableField("merchant_id")
	private String marchantId;

	public String getMarchantId() {
		return marchantId;
	}

	public void setMarchantId(String marchantId) {
		this.marchantId = marchantId;
	}

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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrganizerName() {
		return organizerName;
	}

	public void setOrganizerName(String organizerName) {
		this.organizerName = organizerName;
	}

	public Integer getOrganizerKind() {
		return organizerKind;
	}

	public void setOrganizerKind(Integer organizerKind) {
		this.organizerKind = organizerKind;
	}

	public Boolean getAccept() {
		return isAccept;
	}

	public void setAccept(Boolean isAccept) {
		this.isAccept = isAccept;
	}

	public Boolean getApprove() {
		return isApprove;
	}

	public void setApprove(Boolean isApprove) {
		this.isApprove = isApprove;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MatchReview{" +
			"id=" + id +
			", matchId=" + matchId +
			", userId=" + userId +
			", organizerName=" + organizerName +
			", organizerKind=" + organizerKind +
			", isAccept=" + isAccept +
			", isApprove=" + isApprove +
			"}";
	}
}
