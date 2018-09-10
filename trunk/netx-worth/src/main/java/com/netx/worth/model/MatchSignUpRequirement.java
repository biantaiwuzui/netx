package com.netx.worth.model;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotations.TableField;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
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
 * @since 2018-08-13
 */
@TableName("match_sign_up_requirement")
public class MatchSignUpRequirement extends Model<MatchSignUpRequirement> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 组别名称
     */
	@TableField("group_name")
	private String groupName;
	@TableField("match_id")
	private String matchId;
    /**
     * 会场ID
     */
	@TableField("venue_id")
	private String venueId;
    /**
     * 报名开始时间
     */
	@TableField(value = "begin_time", fill = FieldFill.INSERT)
	private Date beginTime;
    /**
     * 结束时间
     */
	@TableField(value = "end_time", fill = FieldFill.INSERT_UPDATE)
	private Date endTime;
    /**
     * 描述
     */
	private String decription;
    /**
     * 报名名额
     */
	private Integer quota;
    /**
     * 报名费用
     */
	private Float free;
    /**
     * 是否系统筛选
     */
	@TableField("is_auto_select")
	private Boolean isAutoSelect;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getMatchId() {
		return matchId;
	}

	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}

	public String getVenueId() {
		return venueId;
	}

	public void setVenueId(String venueId) {
		this.venueId = venueId;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getDecription() {
		return decription;
	}

	public void setDecription(String decription) {
		this.decription = decription;
	}

	public Integer getQuota() {
		return quota;
	}

	public void setQuota(Integer quota) {
		this.quota = quota;
	}

	public Float getFree() {
		return free;
	}

	public void setFree(Float free) {
		this.free = free;
	}

	public Boolean getAutoSelect() {
		return isAutoSelect;
	}

	public void setAutoSelect(Boolean isAutoSelect) {
		this.isAutoSelect = isAutoSelect;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MatchSignUpRequirement{" +
			"id=" + id +
			", groupName=" + groupName +
			", matchId=" + matchId +
			", venueId=" + venueId +
			", beginTime=" + beginTime +
			", endTime=" + endTime +
			", decription=" + decription +
			", quota=" + quota +
			", free=" + free +
			", isAutoSelect=" + isAutoSelect +
			"}";
	}
}
