package com.netx.worth.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 返回参赛用户信息
 * Created by Yawn on 2018/8/18 0018.
 */
public class MatchParticipantVo {

    @ApiModelProperty(value = "参赛者ID")
    private String participantId;
    /**
     * 参赛者名称
     */
    @ApiModelProperty(value = "参赛者名称")
    private String userName;
    /**
     * 出席码
     */
    @ApiModelProperty(value = "出席码")
    private String attendCode;
    /**
     * 比赛名字
     */
    @ApiModelProperty(value = "比赛名字")
    private String matchName;
    /**
     * 赛区名字
     */
    @ApiModelProperty(value = "赛区名字")
    private String zoneName;
    /**
     * 赛组名字
     */
    @ApiModelProperty(value = "赛组名字")
    private String groupName;
    /**
     * 是否支付报名费
     */
    @ApiModelProperty(value = "是否支付报名费")
    private Boolean isPay;
    /**
     * 是否通过比赛审核
     */
    @ApiModelProperty(value = "是否通过比赛审核")
    private Boolean isPass;
    /**
     * 是否在场
     */
    @ApiModelProperty(value = "是否在场")
    private Boolean isSpot;

    /**
     * 参赛者头像
     */
    @ApiModelProperty(value = "参赛者头像")
    private String imageUrl;

    @ApiModelProperty(value = "是否监护人")
    private Boolean isGuardian;

    @ApiModelProperty(value = "是否团队")
    private Boolean isTeam;

    @ApiModelProperty(value = "出席地址")
    private String address;
    @ApiModelProperty(value = "开始时间")
    private Date beginTime;
    private String groupId;
    @ApiModelProperty(value = "报名的各种要求")
    private String requirementText;
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getRequirementText() {
        return requirementText;
    }

    public void setRequirementText(String requirementText) {
        this.requirementText = requirementText;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAttendCode() {
        return attendCode;
    }

    public void setAttendCode(String attendCode) {
        this.attendCode = attendCode;
    }

    public Boolean getPay() {
        return isPay;
    }

    public void setPay(Boolean pay) {
        isPay = pay;
    }

    public Boolean getPass() {
        return isPass;
    }

    public void setPass(Boolean pass) {
        isPass = pass;
    }

    public Boolean getSpot() {
        return isSpot;
    }

    public void setSpot(Boolean spot) {
        isSpot = spot;
    }

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Boolean getGuardian() {
        return isGuardian;
    }

    public void setGuardian(Boolean guardian) {
        isGuardian = guardian;
    }

    public Boolean getTeam() {
        return isTeam;
    }

    public void setTeam(Boolean team) {
        isTeam = team;
    }
}
