package com.netx.worth.model;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotations.TableField;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.annotations.Version;
import java.io.Serializable;

/**
 * <p>
 * 比赛演出次序表
 * </p>
 *
 * @author Yawn
 * @since 2018-08-19
 */
@TableName("match_appearance")
public class MatchAppearance extends Model<MatchAppearance> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 比赛ID
     */
	@TableField("match_id")
	private String matchId;
    /**
     * 比赛进程
     */
	@TableField("progress_id")
	private String progressId;
    /**
     * 赛区ID
     */
	@TableField("zone_id")
	private String zoneId;
    /**
     * 赛组ID
     */
	@TableField("group_id")
	private String groupId;
    /**
     * 分赛场ID
     */
	@TableField("venue_id")
	private String venueId;
    /**
     * 表演者ID
     */
	@TableField("performer_id")
	private String performerId;
    /**
     * 演出者名字
     */
	@TableField("performer_name")
	private String performerName;
    /**
     * 项目名称
     */
	@TableField("project_name")
	private String projectName;
    /**
     * 头像信息
     */
	@TableField("head_image_url")
	private String headImageUrl;
    /**
     * 项目介绍
     */
	@TableField("project_introduct")
	private String projectIntroduct;
    /**
     * 团队图片之类的
     */
	@TableField("appearance_images_url")
	private String appearanceImagesUrl;
    /**
     * 演出次序
     */
	@TableField("appearance_order")
	private Integer appearanceOrder;
    /**
     * 演出时间
     */
	@TableField("performance_time")
	private Date performanceTime;
    /**
     * 演出状态
     */
	@TableField("appearance_status")
	private Integer appearanceStatus;


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

	public String getProgressId() {
		return progressId;
	}

	public void setProgressId(String progressId) {
		this.progressId = progressId;
	}

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getVenueId() {
		return venueId;
	}

	public void setVenueId(String venueId) {
		this.venueId = venueId;
	}

	public String getPerformerId() {
		return performerId;
	}

	public void setPerformerId(String performerId) {
		this.performerId = performerId;
	}

	public String getPerformerName() {
		return performerName;
	}

	public void setPerformerName(String performerName) {
		this.performerName = performerName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getHeadImageUrl() {
		return headImageUrl;
	}

	public void setHeadImageUrl(String headImageUrl) {
		this.headImageUrl = headImageUrl;
	}

	public String getProjectIntroduct() {
		return projectIntroduct;
	}

	public void setProjectIntroduct(String projectIntroduct) {
		this.projectIntroduct = projectIntroduct;
	}

	public String getAppearanceImagesUrl() {
		return appearanceImagesUrl;
	}

	public void setAppearanceImagesUrl(String appearanceImagesUrl) {
		this.appearanceImagesUrl = appearanceImagesUrl;
	}

	public Integer getAppearanceOrder() {
		return appearanceOrder;
	}

	public void setAppearanceOrder(Integer appearanceOrder) {
		this.appearanceOrder = appearanceOrder;
	}

	public Date getPerformanceTime() {
		return performanceTime;
	}

	public void setPerformanceTime(Date performanceTime) {
		this.performanceTime = performanceTime;
	}

	public Integer getAppearanceStatus() {
		return appearanceStatus;
	}

	public void setAppearanceStatus(Integer appearanceStatus) {
		this.appearanceStatus = appearanceStatus;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MatchAppearance{" +
			"id=" + id +
			", matchId=" + matchId +
			", progressId=" + progressId +
			", zoneId=" + zoneId +
			", groupId=" + groupId +
			", venueId=" + venueId +
			", performerId=" + performerId +
			", performerName=" + performerName +
			", projectName=" + projectName +
			", headImageUrl=" + headImageUrl +
			", projectIntroduct=" + projectIntroduct +
			", appearanceImagesUrl=" + appearanceImagesUrl +
			", appearanceOrder=" + appearanceOrder +
			", performanceTime=" + performanceTime +
			", appearanceStatus=" + appearanceStatus +
			"}";
	}
}
