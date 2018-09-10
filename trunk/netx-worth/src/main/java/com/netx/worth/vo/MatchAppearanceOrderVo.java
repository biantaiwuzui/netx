package com.netx.worth.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 参赛者出席顺序表
 * Created by Yawn on 2018/8/19 0019.
 */
public class MatchAppearanceOrderVo {
    @ApiModelProperty(value = "演出ID")
    private String id;
    /**
     * 比赛名字
     */
    @ApiModelProperty(value = "比赛名字")
    private String matchName;
    /**
     * 比赛进程
     */
    @ApiModelProperty(value = "比赛进程")
    private String progressName;
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
     * 分赛场名称
     */
    @ApiModelProperty(value = "分赛场名称")
    private String venueName;
    /**
     * 表演者ID
     */
    @ApiModelProperty(value = "表演者ID")
    private String performerId;
    /**
     * 演出者名字
     */
    @ApiModelProperty(value = "演出者名字")
    private String performerName;
    /**
     * 项目名称
     */
    @ApiModelProperty(value = "项目名称")
    private String projectName;
    /**
     * 头像信息
     */
    @ApiModelProperty(value = "头像信息")
    private String headImageUrl;
    /**
     * 演出次序
     */
    @ApiModelProperty(value = "演出次序")
    private Integer appearanceOrder;
    /**
     * 演出时间
     */
    @ApiModelProperty(value = "演出时间")
    private Date performanceTime;
    /**
     * 演出状态
     */
    @ApiModelProperty(value = "演出状态")
    private Integer appearanceStatus;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public String getProgressName() {
        return progressName;
    }

    public void setProgressName(String progressName) {
        this.progressName = progressName;
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

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
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
}
