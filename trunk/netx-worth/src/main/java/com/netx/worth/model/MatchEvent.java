package com.netx.worth.model;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.annotations.Version;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 比赛
 * </p>
 *
 * @author Yawn
 * @since 2018-08-18
 */
@TableName("match_event")
public class MatchEvent extends Model<MatchEvent> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
	private String id;
    /**
     * 发起者ID
     */
	@TableField("initiator_id")
	private String initiatorId;
    /**
     * 标题
     */
	private String title;
    /**
     * 副标题
     */
	@TableField("sub_title")
	private String subTitle;
    /**
     * 比赛形式_不限/个人/团队/
     */
	@TableField("match_kind")
	private String matchKind;
    /**
     * 比赛规则
     */
	@TableField("match_rule")
	private String matchRule;
    /**
     * 评分标准
     */
	private String grading;
    /**
     * 比赛介绍_图片
     */
	@TableField("match_introduction")
	private String matchIntroduction;
    /**
     * 赛事介绍_比赛图文详情
     */
	@TableField("match_image_url")
	private String matchImageUrl;
    /**
     * 比赛状态
     */
	@TableField("match_status")
	private Integer matchStatus;
    /**
     * 是否通过审核
     */
	@TableField("is_approved")
	private Boolean isApproved;


	/**
	 * 是否通过审核
	 */
	@TableField("pass_time")
	private Date passTime;
	/**
	 * 是否通过审核
	 */
	@TableField("update_time")
	private Date updateTime;

	/**
	 * 不通过理由
	 */
	private String reason;

	private Double lat;

	private Double lon;

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Date getPassTime() {
		return passTime;
	}

	public void setPassTime(Date passTime) {
		this.passTime = passTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInitiatorId() {
		return initiatorId;
	}

	public void setInitiatorId(String initiatorId) {
		this.initiatorId = initiatorId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getMatchKind() {
		return matchKind;
	}

	public void setMatchKind(String matchKind) {
		this.matchKind = matchKind;
	}

	public String getMatchRule() {
		return matchRule;
	}

	public void setMatchRule(String matchRule) {
		this.matchRule = matchRule;
	}

	public String getGrading() {
		return grading;
	}

	public void setGrading(String grading) {
		this.grading = grading;
	}

	public String getMatchIntroduction() {
		return matchIntroduction;
	}

	public void setMatchIntroduction(String matchIntroduction) {
		this.matchIntroduction = matchIntroduction;
	}

	public String getMatchImageUrl() {
		return matchImageUrl;
	}

	public void setMatchImageUrl(String matchImageUrl) {
		this.matchImageUrl = matchImageUrl;
	}

	public Integer getMatchStatus() {
		return matchStatus;
	}

	public void setMatchStatus(Integer matchStatus) {
		this.matchStatus = matchStatus;
	}

	public Boolean getApproved() {
		return isApproved;
	}

	public void setApproved(Boolean isApproved) {
		this.isApproved = isApproved;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MatchEvent{" +
			"id=" + id +
			", initiatorId=" + initiatorId +
			", title=" + title +
			", subTitle=" + subTitle +
			", matchKind=" + matchKind +
			", matchRule=" + matchRule +
			", grading=" + grading +
			", matchIntroduction=" + matchIntroduction +
			", matchImageUrl=" + matchImageUrl +
			", matchStatus=" + matchStatus +
			", isApproved=" + isApproved +
			"}";
	}
}
