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
 * 赛制表
 * </p>
 *
 * @author Yawn
 * @since 2018-08-15
 */
@TableName("match_progress")
public class MatchProgress extends Model<MatchProgress> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 比赛ID
     */
	@TableField("match_id")
	private String matchId;
    /**
     * 赛制名称
     */
	@TableField("match_name")
	private String matchName;
    /**
     * 开始时间
     */
	@TableField(value = "begin_time", fill = FieldFill.INSERT)
	private Date beginTime;
    /**
     * 结束时间
     */
	@TableField(value = "end_time", fill = FieldFill.INSERT_UPDATE)
	private Date endTime;
    /**
     * 用来排序的
     */
	private Integer sort;


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

	public String getMatchName() {
		return matchName;
	}

	public void
	setMatchName(String matchName) {
		this.matchName = matchName;
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

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MatchProgress{" +
			"id=" + id +
			", matchId=" + matchId +
			", matchName=" + matchName +
			", beginTime=" + beginTime +
			", endTime=" + endTime +
			", sort=" + sort +
			"}";
	}
}
