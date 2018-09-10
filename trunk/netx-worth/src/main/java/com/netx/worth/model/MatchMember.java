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
 * 比赛工作人员
 * </p>
 *
 * @author Yawn
 * @since 2018-08-15
 */
@TableName("match_member")
public class MatchMember extends Model<MatchMember> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 比赛id
     */
	@TableField("match_id")
	private String matchId;
    /**
     * 用户编号
     */
	@TableField("user_id")
	private String userId;
    /**
     * 用户称呼
     */
	@TableField("user_call")
	private String userCall;
    /**
     * 工作人员类型：领导、嘉宾、会场工作人员
     */
	private String kind;
    /**
     * 是否接受邀请
     */
	@TableField("is_accept")
	private Boolean isAccept;
    /**
     * 是否在场
     */
	@TableField("is_spot")
	private Boolean isSpot;
    /**
     * 是否为网值用户
     */
	@TableField("is_net_user")
	private Boolean isNetUser;
    /**
     * 邀请码
     */
	@TableField("active_code")
	private String activeCode;


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

	public String getUserCall() {
		return userCall;
	}

	public void setUserCall(String userCall) {
		this.userCall = userCall;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public Boolean getAccept() {
		return isAccept;
	}

	public void setAccept(Boolean isAccept) {
		this.isAccept = isAccept;
	}

	public Boolean getSpot() {
		return isSpot;
	}

	public void setSpot(Boolean isSpot) {
		this.isSpot = isSpot;
	}

	public Boolean getNetUser() {
		return isNetUser;
	}

	public void setNetUser(Boolean isNetUser) {
		this.isNetUser = isNetUser;
	}

	public String getActiveCode() {
		return activeCode;
	}

	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MatchMember{" +
			"id=" + id +
			", matchId=" + matchId +
			", userId=" + userId +
			", userCall=" + userCall +
			", kind=" + kind +
			", isAccept=" + isAccept +
			", isSpot=" + isSpot +
			", isNetUser=" + isNetUser +
			", activeCode=" + activeCode +
			"}";
	}
}
