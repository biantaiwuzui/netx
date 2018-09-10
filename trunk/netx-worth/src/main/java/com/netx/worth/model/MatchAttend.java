package com.netx.worth.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

/**
 * <p>
 * 出席表
 * </p>
 *
 * @author Yawn
 * @since 2018-07-31
 */
@TableName("match_attend")
public class MatchAttend extends Model<MatchAttend> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	private String id;
    /**
     * 出席_用户id
     */
	@TableField("user_id")
	private String userId;
    /**
     * 出席_角色
     */
	private String role;
    /**
     * 比赛ID
     */
	@TableField("match_id")
	private String matchId;
    /**
     * 确认出席
     */
	@TableField("is_attend")
	private Boolean isAttend;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getMatchId() {
		return matchId;
	}

	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}

	public Boolean getAttend() {
		return isAttend;
	}

	public void setAttend(Boolean isAttend) {
		this.isAttend = isAttend;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MatchAttend{" +
			"id=" + id +
			", userId=" + userId +
			", role=" + role +
			", matchId=" + matchId +
			", isAttend=" + isAttend +
			"}";
	}
}
