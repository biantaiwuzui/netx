package com.netx.common.wz.dto.matchEvent;

import com.netx.common.user.enums.TicketTypeEnum;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 赛事报名表
 * Created by Yawn on 2018/8/1 0001.
 */
public class MatchParticipantDTO {
    @ApiModelProperty(value = "有就更新，没有就插入")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID(不用填，后端填)")
    private String userId;
    /**
     * 比赛ID
     */
    @ApiModelProperty(value = "报名者名字")
    private String userName;
    @ApiModelProperty(value = "比赛ID")
    @NotNull(message = "matchId不能为空")
    private String matchId;
    @ApiModelProperty(value = "赛区ID")
    @NotNull(message = "赛区id不能为空")
    private String zoneId;
    @ApiModelProperty(value = "赛组ID")
    @NotNull(message = "赛组id不能为空")
    private String groupId;
    /**
     * 项目名称
     */
    @ApiModelProperty(value = "项目名称")
    private String projectName;
    /**
     * 是否个人参与
     */
    @ApiModelProperty(value = "是否个人参与")
    private Boolean isTeam;
//    /**
//     * 团队ID
//     */
//    @ApiModelProperty(value = "团队ID，可不填")
//    private String teamId;
    /**
     * 生日
     */
    @ApiModelProperty(value = "出生日期")
    private Date birthday;
    /**
     * 性别
     */
    @ApiModelProperty(value = "性别")
    private String sex;
    @ApiModelProperty(value = "头像")
    private String headImagesUrl;
    /**
     * 是否监护人
     */
    @ApiModelProperty(value = "是否监护人")
    private Boolean isGuardian;
    /**
     * 各种参赛要求，填文本
     */
    @ApiModelProperty(value = "参赛要求，填JSON格式，不限长")
    private String requirementText;

    @ApiModelProperty(value = "门票类型1.FREE(免费)，2.PAY(付费)", required = true)
    @NotNull(message = "门票类型不能为空")
    private TicketTypeEnum ticketTypeEnum;

    @ApiModelProperty(value = "报名需求的里面的图片信息")
    private List<MatchRequirementDataDTO> image_message;

    @ApiModelProperty(value = "后端自己填")
    private Integer status;

    private Boolean isPass;

    public Boolean getPass() {
        return isPass;
    }

    public void setPass(Boolean pass) {
        isPass = pass;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<MatchRequirementDataDTO> getImage_message() {
        return image_message;
    }

    public void setImage_message(List<MatchRequirementDataDTO> image_message) {
        this.image_message = image_message;
    }

    public TicketTypeEnum getTicketTypeEnum() {
        return ticketTypeEnum;
    }

    public void setTicketTypeEnum(TicketTypeEnum ticketTypeEnum) {
        this.ticketTypeEnum = ticketTypeEnum;
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

    public Boolean getTeam() {
        return isTeam;
    }

    public void setTeam(Boolean team) {
        isTeam = team;
    }

//    public String getTeamId() {
//        return teamId;
//    }
//
//    public void setTeamId(String teamId) {
//        this.teamId = teamId;
//    }

    public Boolean getGuardian() {
        return isGuardian;
    }

    public void setGuardian(Boolean guardian) {
        isGuardian = guardian;
    }

    public String getRequirementText() {
        return requirementText;
    }

    public void setRequirementText(String requirementText) {
        this.requirementText = requirementText;
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
}
