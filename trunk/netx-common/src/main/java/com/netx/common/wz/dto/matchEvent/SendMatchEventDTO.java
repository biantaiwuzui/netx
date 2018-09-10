package com.netx.common.wz.dto.matchEvent;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 发起比赛
 * Created by Yawn on 2018/8/1 0001.
 */
public class SendMatchEventDTO {
    @ApiModelProperty(value = "有id就更新，没有就是插入")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    /**
     * 发起者ID
     */
    @ApiModelProperty(value = "发起者ID，填用户ID")
    private String initiatorId;
    /**
     * 标题
     */
    @ApiModelProperty(value = "赛事标题")
    private String title;
    /**
     * 比赛类型
     */
    @ApiModelProperty(value = "比赛类型")
    private String matchKind;
    /**
     * 副标题
     */
    @ApiModelProperty(value = "子标题")
    private String subTitle;
    /**
     * 比赛规则
     */

    @ApiModelProperty(value = "比赛规则")
    private String matchRule;
    /**
     * 评分标准
     */
    @ApiModelProperty(value = "评分标准")
    private String grading;
    /**
     * 比赛介绍_图片
     */
    @ApiModelProperty(value = "赛事介绍")
    private String matchIntroduction;
    /**
     * 赛事介绍_比赛图文详情
     */
    @ApiModelProperty(value = "图文详情")
    private String matchImageUrl;
    /**
     * 比赛状态
     */
    @ApiModelProperty(value = "赛事状态" +
            "    (0, \"已保存\"),\n" +
            "    (1, \"待审核\"),\n" +
            "    (2, \"审核拒绝\"),\n" +
            "    (3, \"审核通过\"),\n" +
            "    (4, \"已启动\"),\n" +
            "    (5, \"报名中\"),\n" +
            "    (6, \"售票中\"),\n" +
            "    (7, \"开始前\"),\n" +
            "    (8, \"进行中\"),\n" +
            "    (9, \"赛事结束\");")
    private Integer matchStatus;
    /**
     * 是否通过审核
     */
    @ApiModelProperty(value = "是否通过审核")
    private Boolean isApproved;
    private Date passTime;
    private Date updateTime;

    private String reason;

    private Double lon;

    private Double lat;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
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

    public String getInitiatorId() {
        return initiatorId;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }



    public String getMatchRule() {
        return matchRule;
    }

    public String getGrading() {
        return grading;
    }

    public String getMatchIntroduction() {
        return matchIntroduction;
    }

    public String getMatchImageUrl() {
        return matchImageUrl;
    }

    public Integer getMatchStatus() {
        return matchStatus;
    }

    public Boolean getApproved() {
        return isApproved;
    }

    public void setInitiatorId(String initiatorId) {
        this.initiatorId = initiatorId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public void setMatchRule(String matchRule) {
        this.matchRule = matchRule;
    }

    public void setGrading(String grading) {
        this.grading = grading;
    }

    public void setMatchIntroduction(String matchIntroduction) {
        this.matchIntroduction = matchIntroduction;
    }

    public void setMatchImageUrl(String matchImageUrl) {
        this.matchImageUrl = matchImageUrl;
    }

    public void setMatchStatus(Integer matchStatus) {
        this.matchStatus = matchStatus;
    }

    public void setApproved(Boolean approved) {
        isApproved = approved;
    }

    public String getMatchKind() {
        return matchKind;
    }

    public void setMatchKind(String matchKind) {
        this.matchKind = matchKind;
    }
}
