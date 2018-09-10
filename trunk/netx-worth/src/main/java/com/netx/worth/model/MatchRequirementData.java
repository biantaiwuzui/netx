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
 * 需要上传的资料
 * </p>
 *
 * @author Yawn
 * @since 2018-08-17
 */
@TableName("match_requirement_data")
public class MatchRequirementData extends Model<MatchRequirementData> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 用户id
     */
	@TableField("match_id")
	private String matchId;
    /**
     * 赛区ID
     */
	@TableField("zone_id")
	private String zoneId;
    /**
     * 比赛id
     */
	@TableField("group_id")
	private String groupId;
    /**
     * 要求ID
     */
	@TableField("requirement_id")
	private String requirementId;
    /**
     * 用户ID
     */
	@TableField("user_id")
	private String userId;
    /**
     * 要求名称
     */
	@TableField("requirement_title")
	private String requirementTitle;
    /**
     * 资料介绍
     */
	private String introduction;
    /**
     * 资料图片
     */
	@TableField("images_url")
	private String imagesUrl;

	@TableField("is_child_or_team")
	private boolean isChildOrTeam;

	public boolean isChildOrTeam() {
		return isChildOrTeam;
	}

	public void setChildOrTeam(boolean childOrTeam) {
		isChildOrTeam = childOrTeam;
	}

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

	public String getRequirementId() {
		return requirementId;
	}

	public void setRequirementId(String requirementId) {
		this.requirementId = requirementId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRequirementTitle() {
		return requirementTitle;
	}

	public void setRequirementTitle(String requirementTitle) {
		this.requirementTitle = requirementTitle;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getImagesUrl() {
		return imagesUrl;
	}

	public void setImagesUrl(String imagesUrl) {
		this.imagesUrl = imagesUrl;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MatchRequirementData{" +
			"id=" + id +
			", matchId=" + matchId +
			", zoneId=" + zoneId +
			", groupId=" + groupId +
			", requirementId=" + requirementId +
			", userId=" + userId +
			", requirementTitle=" + requirementTitle +
			", introduction=" + introduction +
			", imagesUrl=" + imagesUrl +
			"}";
	}
}
