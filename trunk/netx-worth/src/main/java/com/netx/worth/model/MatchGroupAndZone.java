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
 * 赛组和赛区表
 * </p>
 *
 * @author Yawn
 * @since 2018-08-20
 */
@TableName("match_group_and_zone")
public class MatchGroupAndZone extends Model<MatchGroupAndZone> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 赛区id
     */
	@TableField("match_zone_id")
	private String matchZoneId;
    /**
     * 赛组id
     */
	@TableField("match_group_id")
	private String matchGroupId;
    /**
     * 开始时间
     */
	@TableField("start_time")
	private Date startTime;
    /**
     * 结束时间
     */
	@TableField(value = "end_time", fill = FieldFill.INSERT_UPDATE)
	private Date endTime;
    /**
     * 0表示是默认时间 1表示不是默认时间
     */
	@TableField("is_default")
	private Boolean isDefault;
    /**
     * 是否为赛区时间
     */
	@TableField("is_zone_time")
	private Boolean isZoneTime;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMatchZoneId() {
		return matchZoneId;
	}

	public void setMatchZoneId(String matchZoneId) {
		this.matchZoneId = matchZoneId;
	}

	public String getMatchGroupId() {
		return matchGroupId;
	}

	public void setMatchGroupId(String matchGroupId) {
		this.matchGroupId = matchGroupId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Boolean getDefault() {
		return isDefault;
	}

	public void setDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	public Boolean getZoneTime() {
		return isZoneTime;
	}

	public void setZoneTime(Boolean isZoneTime) {
		this.isZoneTime = isZoneTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MatchGroupAndZone{" +
			"id=" + id +
			", matchZoneId=" + matchZoneId +
			", matchGroupId=" + matchGroupId +
			", startTime=" + startTime +
			", endTime=" + endTime +
			", isDefault=" + isDefault +
			", isZoneTime=" + isZoneTime +
			"}";
	}
}
