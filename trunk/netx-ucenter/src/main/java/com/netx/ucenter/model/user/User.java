package com.netx.ucenter.model.user;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.netx.common.user.dto.common.CommonUserBaseInfoDto;

import java.io.Serializable;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author 黎子安
 * @since 2018-03-17
 */
@TableName("user")
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 网号
     */
	@TableField("user_number")
	private String userNumber;
    /**
     * 昵称
     */
	private String nickname;
	/**
	 * 姓名
	 */
	@TableField("real_name")
	private String realName;
    /**
     * 性别
     */
	private String sex;
    /**
     * 生日
     */
	private Date birthday;
    /**
     * 密码
     */
	private String password;
    /**
     * 钱包密码
     */
	@TableField("pay_password")
	private String payPassword;
    /**
     * 管理密码
     */
	@TableField("admin_password")
	private String adminPassword;
	@TableField("jmessage_password")
	private String jmessagePassword;
    /**
     * 手机号
     */
	private String mobile;
    /**
     * 角色：
            1.系统管理
            2.用户管理
            3.商家管理
            4.资讯管理
            5.财务管理
            6.仲裁管理
     */
	private String role;
    /**
     * 是否为管理员帐号：
            0：否
            1：是
     */
	@TableField("is_admin_user")
	private Boolean isAdminUser;
    /**
     * 是否锁定，拉黑之后将其锁定
     */
	@TableField("is_lock")
	private Boolean isLock;
    /**
     * 乐观锁：
            查询时先带出，更新时+1。
            更新伪代码：
            lockVersion = lockVersion +1 where lockVersion = 查询时带出的
     */
	@TableField("lock_version")
	private Integer lockVersion;
    /**
     * 通过认证的证件号，下面这5项认证信息在后台通过认证后更新
     */
	@TableField("id_number")
	private String idNumber;
    /**
     * 视频信息（根据需求设置内容）
     */
	private String video;
    /**
     * 车辆信息
     */
	private String car;
    /**
     * 房产信息
     */
	private String house;
    /**
     * 学历信息
     */
	private String degree;
    /**
     * 文化教育概况
     */
	@TableField("education_label")
	private String educationLabel;
    /**
     * 工作经历概况
     */
	@TableField("profession_label")
	private String professionLabel;
    /**
     * 兴趣爱好概况
     */
	@TableField("interest_label")
	private String interestLabel;
    /**
     * 最后登录时间
     */
	@TableField("last_login_at")
	private Date lastLoginAt;
    /**
     * 最后操作时间
     */
	@TableField("last_active_at")
	private Date lastActiveAt;
    /**
     * 连续登录天数
     */
	@TableField("login_days")
	private Integer loginDays;
    /**
     * 地址-经度
     */
	private BigDecimal lon;
    /**
     * 地址-纬度
     */
	private BigDecimal lat;
    /**
     * 礼物设置：
            0：不接受礼物
            1：接受好友礼物
            2：接受我关注的人的礼物
     */
	@TableField("gift_setting")
	private Integer giftSetting;
    /**
     * 邀请设置：
            0：不接受邀请
            1：接受好友的邀请
            2：接受我关注的人的邀请
     */
	@TableField("invitation_setting")
	private Integer invitationSetting;
    /**
     * 咨讯设置：
            0：不起任何作用
            1：仅限好友查看
     */
	@TableField("article_setting")
	private Integer articleSetting;
    /**
     * 附近设置：
0：显示我的信息
1：不显示我的信息
     */
	@TableField("nearly_setting")
	private Integer nearlySetting;
    /**
     * 语音设置：
0：关闭
1：开启
     */
	@TableField("voice_setting")
	private Integer voiceSetting;
    /**
     * 震动设置：
0：关闭
1：开启
     */
	@TableField("shock_setting")
	private Integer shockSetting;
    /**
     * 总积分
     */
	private BigDecimal score;
    /**
     * 总信用
	 *
     */
	private Integer credit;
    /**
     * 总身价
     */
	private BigDecimal value;
    /**
     * 总收益
     */
	private BigDecimal income;
    /**
     * 总贡献
     */
	private BigDecimal contribution;
    /**
     * 用户等级
     */
	private Integer lv;
    /**
     * 资料完整度分值
     */
	@TableField("user_profile_score")
	private Integer userProfileScore;
    /**
     * 上次统计的资料完成度百分比
     */
	@TableField("last_complete_percent")
	private BigDecimal lastCompletePercent;
    /**
     * 当前累积的点赞次数，需按业务要求定量清0
     */
	@TableField("current_likes")
	private Integer currentLikes;
    /**
     * 当前累积的关注次数，需按业务要求定量清0
     */
	@TableField("current_watch_to")
	private Integer currentWatchTo;
    /**
     * 当前累积的被关注次数，需按业务要求定量清0
     */
	@TableField("current_watch_from")
	private Integer currentWatchFrom;
    /**
     * 批准日期（即设为管理员的日期）
     */
	@TableField("approval_time")
	private Date approvalTime;
    /**
     * 是否注册了极光
     */
	@TableField("is_reg_jMessage")
	private Boolean isRegJMessage;
    /**
     * 是否登录
     */
	@TableField("is_login")
	private Boolean isLogin;
    /**
     * 是否后台登录
     */
	@TableField("is_login_backend")
	private Boolean isLoginBackend;
	/**
	 * 是否发行网信
	 */
	@TableField("is_publish_credit")
	private Boolean isPublishCredit;
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;
	@TableField("create_user_id")
	private String createUserId;
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;
	@TableField("update_user_id")
	private String updateUserId;
	@TableField(fill = FieldFill.INSERT)
	private Integer deleted;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserNumber() {
		return userNumber;
	}

	public void setUserNumber(String userNumber) {
		this.userNumber = userNumber;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPayPassword() {
		return payPassword;
	}

	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	public String getJmessagePassword() {
		return jmessagePassword;
	}

	public void setJmessagePassword(String jmessagePassword) {
		this.jmessagePassword = jmessagePassword;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Boolean getAdminUser() {
		return isAdminUser;
	}

	public void setAdminUser(Boolean isAdminUser) {
		this.isAdminUser = isAdminUser;
	}

	public Boolean getLock() {
		return isLock;
	}

	public void setLock(Boolean isLock) {
		this.isLock = isLock;
	}

	public Integer getLockVersion() {
		return lockVersion;
	}

	public void setLockVersion(Integer lockVersion) {
		this.lockVersion = lockVersion;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public String getCar() {
		return car;
	}

	public void setCar(String car) {
		this.car = car;
	}

	public String getHouse() {
		return house;
	}

	public void setHouse(String house) {
		this.house = house;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getEducationLabel() {
		return educationLabel;
	}

	public void setEducationLabel(String educationLabel) {
		this.educationLabel = educationLabel;
	}

	public String getProfessionLabel() {
		return professionLabel;
	}

	public void setProfessionLabel(String professionLabel) {
		this.professionLabel = professionLabel;
	}

	public String getInterestLabel() {
		return interestLabel;
	}

	public void setInterestLabel(String interestLabel) {
		this.interestLabel = interestLabel;
	}

	public Date getLastLoginAt() {
		return lastLoginAt;
	}

	public void setLastLoginAt(Date lastLoginAt) {
		this.lastLoginAt = lastLoginAt;
	}

	public Date getLastActiveAt() {
		return lastActiveAt;
	}

	public void setLastActiveAt(Date lastActiveAt) {
		this.lastActiveAt = lastActiveAt;
	}

	public Integer getLoginDays() {
		return loginDays;
	}

	public void setLoginDays(Integer loginDays) {
		this.loginDays = loginDays;
	}

	public BigDecimal getLon() {
		return lon;
	}

	public void setLon(BigDecimal lon) {
		this.lon = lon;
	}

	public BigDecimal getLat() {
		return lat;
	}

	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}

	public Integer getGiftSetting() {
		return giftSetting;
	}

	public void setGiftSetting(Integer giftSetting) {
		this.giftSetting = giftSetting;
	}

	public Integer getInvitationSetting() {
		return invitationSetting;
	}

	public void setInvitationSetting(Integer invitationSetting) {
		this.invitationSetting = invitationSetting;
	}

	public Integer getArticleSetting() {
		return articleSetting;
	}

	public void setArticleSetting(Integer articleSetting) {
		this.articleSetting = articleSetting;
	}

	public Integer getNearlySetting() {
		return nearlySetting;
	}

	public void setNearlySetting(Integer nearlySetting) {
		this.nearlySetting = nearlySetting;
	}

	public Integer getVoiceSetting() {
		return voiceSetting;
	}

	public void setVoiceSetting(Integer voiceSetting) {
		this.voiceSetting = voiceSetting;
	}

	public Integer getShockSetting() {
		return shockSetting;
	}

	public void setShockSetting(Integer shockSetting) {
		this.shockSetting = shockSetting;
	}

	public BigDecimal getScore() {
		return score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

	public Integer getCredit() {
		return credit;
	}

	public void setCredit(Integer credit) {
		this.credit = credit;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getIncome() {
		return income;
	}

	public void setIncome(BigDecimal income) {
		this.income = income;
	}

	public BigDecimal getContribution() {
		return contribution;
	}

	public void setContribution(BigDecimal contribution) {
		this.contribution = contribution;
	}

	public Integer getLv() {
		return lv;
	}

	public void setLv(Integer lv) {
		this.lv = lv;
	}

	public Integer getUserProfileScore() {
		return userProfileScore;
	}

	public void setUserProfileScore(Integer userProfileScore) {
		this.userProfileScore = userProfileScore;
	}

	public BigDecimal getLastCompletePercent() {
		return lastCompletePercent;
	}

	public void setLastCompletePercent(BigDecimal lastCompletePercent) {
		this.lastCompletePercent = lastCompletePercent;
	}

	public Integer getCurrentLikes() {
		return currentLikes;
	}

	public void setCurrentLikes(Integer currentLikes) {
		this.currentLikes = currentLikes;
	}

	public Integer getCurrentWatchTo() {
		return currentWatchTo;
	}

	public void setCurrentWatchTo(Integer currentWatchTo) {
		this.currentWatchTo = currentWatchTo;
	}

	public Integer getCurrentWatchFrom() {
		return currentWatchFrom;
	}

	public void setCurrentWatchFrom(Integer currentWatchFrom) {
		this.currentWatchFrom = currentWatchFrom;
	}

	public Date getApprovalTime() {
		return approvalTime;
	}

	public void setApprovalTime(Date approvalTime) {
		this.approvalTime = approvalTime;
	}

	public Boolean getRegJMessage() {
		return isRegJMessage;
	}

	public void setRegJMessage(Boolean isRegJMessage) {
		this.isRegJMessage = isRegJMessage;
	}

	public Boolean getLogin() {
		return isLogin;
	}

	public void setLogin(Boolean isLogin) {
		this.isLogin = isLogin;
	}

	public Boolean getLoginBackend() {
		return isLoginBackend;
	}

	public void setLoginBackend(Boolean isLoginBackend) {
		this.isLoginBackend = isLoginBackend;
	}

	public Boolean getPublishCredit() {
		return isPublishCredit;
	}

	public void setPublishCredit(Boolean isPublishCredit) {
		this.isPublishCredit = isPublishCredit;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "User{" +
				"id='" + id + '\'' +
				", userNumber='" + userNumber + '\'' +
				", nickname='" + nickname + '\'' +
				", realName='" + realName + '\'' +
				", sex='" + sex + '\'' +
				", birthday=" + birthday +
				", password='" + password + '\'' +
				", payPassword='" + payPassword + '\'' +
				", adminPassword='" + adminPassword + '\'' +
				", jmessagePassword='" + jmessagePassword + '\'' +
				", mobile='" + mobile + '\'' +
				", role='" + role + '\'' +
				", isAdminUser=" + isAdminUser +
				", isLock=" + isLock +
				", lockVersion=" + lockVersion +
				", idNumber='" + idNumber + '\'' +
				", video='" + video + '\'' +
				", car='" + car + '\'' +
				", house='" + house + '\'' +
				", degree='" + degree + '\'' +
				", educationLabel='" + educationLabel + '\'' +
				", professionLabel='" + professionLabel + '\'' +
				", interestLabel='" + interestLabel + '\'' +
				", lastLoginAt=" + lastLoginAt +
				", lastActiveAt=" + lastActiveAt +
				", loginDays=" + loginDays +
				", lon=" + lon +
				", lat=" + lat +
				", giftSetting=" + giftSetting +
				", invitationSetting=" + invitationSetting +
				", articleSetting=" + articleSetting +
				", nearlySetting=" + nearlySetting +
				", voiceSetting=" + voiceSetting +
				", shockSetting=" + shockSetting +
				", score=" + score +
				", credit=" + credit +
				", value=" + value +
				", income=" + income +
				", contribution=" + contribution +
				", lv=" + lv +
				", userProfileScore=" + userProfileScore +
				", lastCompletePercent=" + lastCompletePercent +
				", currentLikes=" + currentLikes +
				", currentWatchTo=" + currentWatchTo +
				", currentWatchFrom=" + currentWatchFrom +
				", approvalTime=" + approvalTime +
				", isRegJMessage=" + isRegJMessage +
				", isLogin=" + isLogin +
				", isLoginBackend=" + isLoginBackend +
				", isPublishCredit=" + isPublishCredit +
				", createTime=" + createTime +
				", createUserId='" + createUserId + '\'' +
				", updateTime=" + updateTime +
				", updateUserId='" + updateUserId + '\'' +
				", deleted=" + deleted +
				'}';
	}

}
