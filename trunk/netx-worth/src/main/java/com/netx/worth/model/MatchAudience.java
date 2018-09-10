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
 * 门票支付表
 * </p>
 *
 * @author Yawn
 * @since 2018-08-21
 */
@TableName("match_audience")
public class MatchAudience extends Model<MatchAudience> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 比赛ID
     */
	@TableField("match_id")
	private String matchId;
    /**
     * 门票的ID
     */
	@TableField("match_ticket_id")
	private String matchTicketId;
    /**
     * 场次ID
     */
	@TableField("venue_id")
	private String venueId;
    /**
     * 用户ID
     */
	@TableField("user_id")
	private String userId;
    /**
     * 是否支付
     */
	@TableField("is_pay")
	private Boolean isPay;
	@TableField("is_attend")
	private Boolean isAttend;


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

	public String getMatchTicketId() {
		return matchTicketId;
	}

	public void setMatchTicketId(String matchTicketId) {
		this.matchTicketId = matchTicketId;
	}

	public String getVenueId() {
		return venueId;
	}

	public void setVenueId(String venueId) {
		this.venueId = venueId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Boolean getPay() {
		return isPay;
	}

	public void setPay(Boolean isPay) {
		this.isPay = isPay;
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
		return "MatchAudience{" +
			"id=" + id +
			", matchId=" + matchId +
			", matchTicketId=" + matchTicketId +
			", venueId=" + venueId +
			", userId=" + userId +
			", isPay=" + isPay +
			", isAttend=" + isAttend +
			"}";
	}
}
