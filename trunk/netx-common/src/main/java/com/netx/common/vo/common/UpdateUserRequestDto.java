package com.netx.common.vo.common;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.Date;

public class UpdateUserRequestDto {

    @ApiModelProperty("用户id")
    @NotBlank(message = "用户id不能为空")
    private String id;
    /**
     * 网号
     */
    @ApiModelProperty("网号")
    private String userNumber;
    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    private String nickname;
    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    private String realName;
    /**
     * 性别
     */
    @ApiModelProperty("性别")
    private String sex;
    /**
     * 生日
     */
    @ApiModelProperty("生日")
    private Date birthday;
    /**
     * 盐
     */
    @ApiModelProperty("盐")
    private String salt;
    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String password;
    /**
     * 钱包密码
     */
    @ApiModelProperty("钱包密码")
    private String payPassword;
    /**
     * 管理密码
     */
    @ApiModelProperty("管理密码")
    private String adminPassword;
    @ApiModelProperty("jmessage_password")
    private String jmessagePassword;
    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
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
    @ApiModelProperty("角色")
    private String role;
    /**
     * 是否为管理员帐号：
     0：否
     1：是
     */
    @ApiModelProperty("是否为管理员帐号")
    private Boolean isAdminUser;
    /**
     * 是否锁定，拉黑之后将其锁定
     */
    @ApiModelProperty("是否锁定，拉黑之后将其锁定")
    private Boolean isLock;
    /**
     * 乐观锁：
     查询时先带出，更新时+1。
     更新伪代码：
     lockVersion = lockVersion +1 where lockVersion = 查询时带出的
     */
    @ApiModelProperty("乐观锁")
    private Integer lockVersion;
    /**
     * 通过认证的证件号，下面这5项认证信息在后台通过认证后更新
     */
    @ApiModelProperty("通过认证的证件号，下面这5项认证信息在后台通过认证后更新")
    private String idNumber;
    /**
     * 视频信息（根据需求设置内容）
     */
    @ApiModelProperty("视频信息（根据需求设置内容）")
    private String video;
    /**
     * 车辆信息
     */
    @ApiModelProperty("车辆信息")
    private String car;
    /**
     * 房产信息
     */
    @ApiModelProperty("房产信息")
    private String house;
    /**
     * 学历信息
     */
    @ApiModelProperty("学历信息")
    private String degree;
    /**
     * 文化教育概况
     */
    @ApiModelProperty("文化教育概况")
    private String educationLabel;
    /**
     * 工作经历概况
     */
    @ApiModelProperty("工作经历概况")
    private String professionLabel;
    /**
     * 兴趣爱好概况
     */
    @ApiModelProperty("兴趣爱好概况")
    private String interestLabel;
    /**
     * 最后登录时间
     */
    @ApiModelProperty("最后登录时间")
    private Date lastLoginAt;
    /**
     * 最后操作时间
     */
    @ApiModelProperty("最后操作时间")
    private Date lastActiveAt;
    /**
     * 连续登录天数
     */
    @ApiModelProperty("连续登录天数")
    private Integer loginDays;
    /**
     * 地址-经度
     */
    @ApiModelProperty("地址-经度")
    private BigDecimal lon;
    /**
     * 地址-纬度
     */
    @ApiModelProperty("地址-纬度")
    private BigDecimal lat;
    /**
     * 礼物设置：
     0：不接受礼物
     1：接受好友礼物
     2：接受我关注的人的礼物
     */
    @ApiModelProperty("礼物设置")
    private Integer giftSetting;
    /**
     * 邀请设置：
     0：不接受邀请
     1：接受好友的邀请
     2：接受我关注的人的邀请
     */
    @ApiModelProperty("邀请设置")
    private Integer invitationSetting;
    /**
     * 咨讯设置：
     0：不起任何作用
     1：仅限好友查看
     */
    @ApiModelProperty("咨讯设置")
    private Integer articleSetting;
    /**
     * 附近设置：
     0：显示我的信息
     1：不显示我的信息
     */
    @ApiModelProperty("附近设置")
    private Integer nearlySetting;
    /**
     * 语音设置：
     0：关闭
     1：开启
     */
    @ApiModelProperty("语音设置")
    private Integer voiceSetting;
    /**
     * 震动设置：
     0：关闭
     1：开启
     */
    @ApiModelProperty("震动设置")
    private Integer shockSetting;
    /**
     * 总积分
     */
    @ApiModelProperty("总积分")
    private BigDecimal score;
    /**
     * 总信用
     */
    @ApiModelProperty("总信用")
    private Integer credit;
    /**
     * 总身价
     */
    @ApiModelProperty("总身价")
    private BigDecimal value;
    /**
     * 总收益
     */
    @ApiModelProperty("总收益")
    private BigDecimal income;
    /**
     * 总贡献
     */
    @ApiModelProperty("总贡献")
    private BigDecimal contribution;
    /**
     * 用户等级
     */
    @ApiModelProperty("用户等级")
    private Integer lv;
    /**
     * 资料完整度分值
     */
    @ApiModelProperty("资料完整度分值")
    private Integer userProfileScore;
    /**
     * 上次统计的资料完成度百分比
     */
    @ApiModelProperty("上次统计的资料完成度百分比")
    private BigDecimal lastCompletePercent;
    /**
     * 当前累积的点赞次数，需按业务要求定量清0
     */
    @ApiModelProperty("当前累积的点赞次数，需按业务要求定量清0")
    private Integer currentLikes;
    /**
     * 当前累积的关注次数，需按业务要求定量清0
     */
    @ApiModelProperty("当前累积的关注次数，需按业务要求定量清0")
    private Integer currentWatchTo;
    /**
     * 当前累积的被关注次数，需按业务要求定量清0
     */
    @ApiModelProperty("当前累积的被关注次数，需按业务要求定量清0")
    private Integer currentWatchFrom;
    /**
     * 批准日期（即设为管理员的日期）
     */
    @ApiModelProperty("批准日期（即设为管理员的日期）")
    private Date approvalTime;
    /**
     * 是否注册了极光
     */
    @ApiModelProperty("是否注册了极光")
    private Boolean isRegJMessage;
    /**
     * 是否登录
     */
    @ApiModelProperty("是否登录")
    private Boolean isLogin;
    /**
     * 是否后台登录
     */
    @ApiModelProperty("是否后台登录")
    private Boolean isLoginBackend;

    @ApiModelProperty("create_user_id")
    private String createUserId;
    @ApiModelProperty("update_user_id")
    private String updateUserId;
    @ApiModelProperty()
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

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
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

    public void setAdminUser(Boolean adminUser) {
        isAdminUser = adminUser;
    }

    public Boolean getLock() {
        return isLock;
    }

    public void setLock(Boolean lock) {
        isLock = lock;
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

    public void setRegJMessage(Boolean regJMessage) {
        isRegJMessage = regJMessage;
    }

    public Boolean getLogin() {
        return isLogin;
    }

    public void setLogin(Boolean login) {
        isLogin = login;
    }

    public Boolean getLoginBackend() {
        return isLoginBackend;
    }

    public void setLoginBackend(Boolean loginBackend) {
        isLoginBackend = loginBackend;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
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
}
