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
 * 赛组表
 * </p>
 *
 * @author Yawn
 * @since 2018-08-18
 */
@TableName("match_group")
public class MatchGroup extends Model<MatchGroup> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 比赛ID
     */
	@TableField("match_id")
	private String matchId;
    /**
     * 赛组名称
     */
	@TableField("match_group_name")
	private String matchGroupName;
    /**
     * 排序
     */
	private Integer sort;
	private Integer quota;
	private Float free;
	@TableField("is_auto_select")
	private Boolean isAutoSelect;


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

	public String getMatchGroupName() {
		return matchGroupName;
	}

	public void setMatchGroupName(String matchGroupName) {
		this.matchGroupName = matchGroupName;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
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
		return "MatchGroup{" +
			"id=" + id +
			", matchId=" + matchId +
			", matchGroupName=" + matchGroupName +
			", sort=" + sort +
			", quota=" + quota +
			", free=" + free +
			", isAutoSelect=" + isAutoSelect +
			"}";
	}
}
