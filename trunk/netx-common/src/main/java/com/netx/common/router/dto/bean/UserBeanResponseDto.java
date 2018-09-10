package com.netx.common.router.dto.bean;

import com.netx.common.user.model.UserPhotoImg;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 用户信息Response基类
 * </p>
 *
 * @author 黎子安
 * @since 2017-10-13
 */
public class UserBeanResponseDto {
    @ApiModelProperty("头像")
    private String headImgUrl;

    @ApiModelProperty("相册")
    private List<UserPhotoImg> imgUrls;

    @ApiModelProperty("用户id")
    private String id;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("网号")
    private String userNumber;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("是否有登录密码")
    private boolean isPWD;

    @ApiModelProperty("是否有钱包密码")
    private boolean isPayPWD;

    @ApiModelProperty("是否有管理员密码")
    private boolean isAdminPWD;

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
    @ApiModelProperty("角色:\n" +
            "     1.系统管理\n" +
            "     2.用户管理\n" +
            "     3.商家管理\n" +
            "     4.资讯管理\n" +
            "     5.财务管理\n" +
            "     6.仲裁管理")
    private String role;

    @ApiModelProperty("是否为管理员帐号")
    private Boolean isAdminUser;

    @ApiModelProperty("是否锁定，拉黑之后将其锁定")
    private Boolean isLock;

    /**
     * 通过认证的证件号，下面这5项认证信息在后台通过认证后更新
     */

    @ApiModelProperty("身份证号码")
    private String idNumber;

    @ApiModelProperty("视频信息")
    private String video;

    @ApiModelProperty("车辆信息")
    private String car;

    @ApiModelProperty("房产信息")
    private String house;

    @ApiModelProperty("学历信息")
    private String degree;

    @ApiModelProperty("文化教育概况")
    private String educationLabel;

    @ApiModelProperty("工作经历概况")
    private String professionLabel;

    @ApiModelProperty("兴趣爱好概况")
    private String interestLabel;

    @ApiModelProperty("最后登录时间")
    private Long lastLoginAt;

    @ApiModelProperty("连续登录天数")
    private Integer loginDays;

    @ApiModelProperty("礼物设置：\n" +
            "     0：不接受礼物\n" +
            "     1：接受好友礼物\n" +
            "     2：接受我关注的人的礼物")
    private Integer giftSetting;

    @ApiModelProperty("邀请设置：\n" +
            "     0：不接受邀请\n" +
            "     1：接受好友的邀请\n" +
            "     2：接受我关注的人的邀请")
    private Integer invitationSetting;

    @ApiModelProperty("总积分")
    private BigDecimal score;

    @ApiModelProperty("总信用")
    private Integer credit;

    @ApiModelProperty("总身价")
    private BigDecimal value;

    @ApiModelProperty("总收益")
    private BigDecimal income;

    @ApiModelProperty("总贡献")
    private BigDecimal contribution;

    @ApiModelProperty("用户等级")
    private Integer lv;

    @ApiModelProperty("资料完整度分值")
    private Integer userProfileScore;

    @ApiModelProperty("上次统计的资料完成度百分比")
    private BigDecimal lastCompletePercent;

    @ApiModelProperty("上次统计的总点赞数")
    private Integer lastLikes;

    @ApiModelProperty("上次统计的总关注数")
    private Integer lastWatchTo;

    @ApiModelProperty("上次统计的总被关注数")
    private Integer lastWatchFrom;

    @ApiModelProperty("当前累积的点赞次数")
    private Integer currentLikes;

    @ApiModelProperty("当前累积的关注次数")
    private Integer currentWatchTo;

    @ApiModelProperty("当前累积的被关注次数")
    private Integer currentWatchFrom;

    private Boolean isRegJMessage;

    private String jmessagePassword;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public boolean getIsPWD() {
        return isPWD;
    }

    public void setIsPWD(boolean PWD) {
        isPWD = PWD;
    }

    public boolean getIsPayPWD() {
        return isPayPWD;
    }

    public void setIsPayPWD(boolean payPWD) {
        isPayPWD = payPWD;
    }

    public boolean getIsAdminPWD() {
        return isAdminPWD;
    }

    public void setIsAdminPWD(boolean adminPWD) {
        isAdminPWD = adminPWD;
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

    public Boolean getIsAdminUser() {
        return isAdminUser;
    }

    public void setIsAdminUser(Boolean isAdminUser) {
        this.isAdminUser = isAdminUser;
    }

    public Boolean getIsLock() {
        return isLock;
    }

    public void setIsLock(Boolean lock) {
        isLock = lock;
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

    public Long getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(Long lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public Integer getLoginDays() {
        return loginDays;
    }

    public void setLoginDays(Integer loginDays) {
        this.loginDays = loginDays;
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

    public Integer getLastLikes() {
        return lastLikes;
    }

    public void setLastLikes(Integer lastLikes) {
        this.lastLikes = lastLikes;
    }

    public Integer getLastWatchTo() {
        return lastWatchTo;
    }

    public void setLastWatchTo(Integer lastWatchTo) {
        this.lastWatchTo = lastWatchTo;
    }

    public Integer getLastWatchFrom() {
        return lastWatchFrom;
    }

    public void setLastWatchFrom(Integer lastWatchFrom) {
        this.lastWatchFrom = lastWatchFrom;
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

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public List<UserPhotoImg> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(List<UserPhotoImg> imgUrls) {
        this.imgUrls = imgUrls;
    }

    public Boolean getRegJMessage() {
        return isRegJMessage;
    }

    public void setRegJMessage(Boolean regJMessage) {
        isRegJMessage = regJMessage;
    }

    public String getJmessagePassword() {
        return jmessagePassword;
    }

    public void setJmessagePassword(String jmessagePassword) {
        this.jmessagePassword = jmessagePassword;
    }

    @Override
    public String toString() {
        return "UserBeanResponseDto{" +
                "headImgUrl='" + headImgUrl + '\'' +
                ", imgUrls=" + imgUrls +
                ", id='" + id + '\'' +
                ", sex='" + sex + '\'' +
                ", userNumber='" + userNumber + '\'' +
                ", nickname='" + nickname + '\'' +
                ", age=" + age +
                ", isPWD=" + isPWD +
                ", isPayPWD=" + isPayPWD +
                ", isAdminPWD=" + isAdminPWD +
                ", mobile='" + mobile + '\'' +
                ", role='" + role + '\'' +
                ", isAdminUser=" + isAdminUser +
                ", isLock=" + isLock +
                ", idNumber='" + idNumber + '\'' +
                ", video='" + video + '\'' +
                ", car='" + car + '\'' +
                ", house='" + house + '\'' +
                ", degree='" + degree + '\'' +
                ", educationLabel='" + educationLabel + '\'' +
                ", professionLabel='" + professionLabel + '\'' +
                ", interestLabel='" + interestLabel + '\'' +
                ", lastLoginAt=" + lastLoginAt +
                ", loginDays=" + loginDays +
                ", giftSetting=" + giftSetting +
                ", invitationSetting=" + invitationSetting +
                ", score=" + score +
                ", credit=" + credit +
                ", value=" + value +
                ", income=" + income +
                ", contribution=" + contribution +
                ", lv=" + lv +
                ", userProfileScore=" + userProfileScore +
                ", lastCompletePercent=" + lastCompletePercent +
                ", lastLikes=" + lastLikes +
                ", lastWatchTo=" + lastWatchTo +
                ", lastWatchFrom=" + lastWatchFrom +
                ", currentLikes=" + currentLikes +
                ", currentWatchTo=" + currentWatchTo +
                ", currentWatchFrom=" + currentWatchFrom +
                ", isRegJMessage=" + isRegJMessage +
                ", jmessagePassword='" + jmessagePassword + '\'' +
                '}';
    }
}
