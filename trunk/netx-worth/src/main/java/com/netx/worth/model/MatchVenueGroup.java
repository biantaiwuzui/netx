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
 * 
 * </p>
 *
 * @author Yawn
 * @since 2018-08-26
 */
@TableName("match_venue_group")
public class MatchVenueGroup extends Model<MatchVenueGroup> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 场次ID
     */
	@TableField("venue_id")
	private String venueId;
    /**
     * 赛组ID
     */
	@TableField("group_id")
	private String groupId;
    /**
     * 赛程ID
     */
	@TableField("progress_id")
	private String progressId;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVenueId() {
		return venueId;
	}

	public void setVenueId(String venueId) {
		this.venueId = venueId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getProgressId() {
		return progressId;
	}

	public void setProgressId(String progressId) {
		this.progressId = progressId;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MatchVenueGroup{" +
			"id=" + id +
			", venueId=" + venueId +
			", groupId=" + groupId +
			", progressId=" + progressId +
			"}";
	}
}
