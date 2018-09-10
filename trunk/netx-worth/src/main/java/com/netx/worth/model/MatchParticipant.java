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
 * 参赛报名
 * </p>
 *
 * @author Yawn
 * @since 2018-08-18
 */
@TableName("match_participant")
public class MatchParticipant extends Model<MatchParticipant> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	private String id;
    /**
     * 用户ID
     */
	@TableField("user_id")
	private String userId;
    /**
     * 比赛ID
     */
	@TableField("match_id")
	private String matchId;
	@TableField("zone_id")
	private String zoneId;
	@TableField("group_id")
	private String groupId;
    /**
     * 项目名称
     */
	@TableField("project_name")
	private String projectName;
    /**
     * 用户名
     */
	@TableField("user_name")
	private String userName;
    /**
     * 生日
     */
	private Date birthday;
    /**
     * 性别
     */
	private String sex;
	@TableField("head_images_url")
	private String headImagesUrl;
    /**
     * 提交的要求，JSON格式提交
     */
	@TableField("requirement_text")
	private String requirementText;
    /**
     * 是否个人参与
     */
	@TableField("is_team")
	private Boolean isTeam;
    /**
     * 团队ID
     */
	@TableField("team_id")
	private String teamId;
    /**
     * 是否支付报名费
     */
	@TableField("is_pay")
	private Boolean isPay;
    /**
     * 是否通过比赛审核
     */
	@TableField("is_pass")
	private Boolean isPass;
    /**
     * 是否监护人
     */
	@TableField("is_guardian")
	private Boolean isGuardian;
    /**
     * 是否在场
     */
	@TableField("is_spot")
	private Boolean isSpot;
	/**
	 * 状态
	 * 0表示失败
	 * 1表示正在进行
	 * 2表示成功
	 */
	private Integer status;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getHeadImagesUrl() {
		return headImagesUrl;
	}

	public void setHeadImagesUrl(String headImagesUrl) {
		this.headImagesUrl = headImagesUrl;
	}

	public String getRequirementText() {
		return requirementText;
	}

	public void setRequirementText(String requirementText) {
		this.requirementText = requirementText;
	}

	public Boolean getTeam() {
		return isTeam;
	}

	public void setTeam(Boolean isTeam) {
		this.isTeam = isTeam;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public Boolean getPay() {
		return isPay;
	}

	public void setPay(Boolean isPay) {
		this.isPay = isPay;
	}

	public Boolean getPass() {
		return isPass;
	}

	public void setPass(Boolean isPass) {
		this.isPass = isPass;
	}

	public Boolean getGuardian() {
		return isGuardian;
	}

	public void setGuardian(Boolean isGuardian) {
		this.isGuardian = isGuardian;
	}

	public Boolean getSpot() {
		return isSpot;
	}

	public void setSpot(Boolean isSpot) {
		this.isSpot = isSpot;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MatchParticipant{" +
			"id=" + id +
			", userId=" + userId +
			", matchId=" + matchId +
			", zoneId=" + zoneId +
			", groupId=" + groupId +
			", projectName=" + projectName +
			", userName=" + userName +
			", birthday=" + birthday +
			", sex=" + sex +
			", headImagesUrl=" + headImagesUrl +
			", requirementText=" + requirementText +
			", isTeam=" + isTeam +
			", teamId=" + teamId +
			", isPay=" + isPay +
			", isPass=" + isPass +
			", isGuardian=" + isGuardian +
			", isSpot=" + isSpot +
			"}";
	}
}
