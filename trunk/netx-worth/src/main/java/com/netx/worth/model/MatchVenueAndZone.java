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
 * 场次和赛区的关系表
 * </p>
 *
 * @author Yawn
 * @since 2018-08-17
 */
@TableName("match_venue_and_zone")
public class MatchVenueAndZone extends Model<MatchVenueAndZone> {

    private static final long serialVersionUID = 1L;

	private String id;
	@TableField("match_venue_id")
	private String matchVenueId;
	@TableField("match_zone_id")
	private String matchZoneId;
    /**
     * 用于指定添加的顺序
     */
	private Integer sort;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMatchVenueId() {
		return matchVenueId;
	}

	public void setMatchVenueId(String matchVenueId) {
		this.matchVenueId = matchVenueId;
	}

	public String getMatchZoneId() {
		return matchZoneId;
	}

	public void setMatchZoneId(String matchZoneId) {
		this.matchZoneId = matchZoneId;
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
		return "MatchVenueAndZone{" +
			"id=" + id +
			", matchVenueId=" + matchVenueId +
			", matchZoneId=" + matchZoneId +
			", sort=" + sort +
			"}";
	}
}
