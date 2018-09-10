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
 * 默认报名时间表
 * </p>
 *
 * @author Yawn
 * @since 2018-08-17
 */
@TableName("match_apply_default_time")
public class MatchApplyDefaultTime extends Model<MatchApplyDefaultTime> {

    private static final long serialVersionUID = 1L;

	private String id;
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
     * 比赛id
     */
	@TableField("match_id")
	private String matchId;
    /**
     * 类型
     */
	private Integer type;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getMatchId() {
		return matchId;
	}

	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MatchApplyDefaultTime{" +
			"id=" + id +
			", startTime=" + startTime +
			", endTime=" + endTime +
			", matchId=" + matchId +
			", type=" + type +
			"}";
	}
}
