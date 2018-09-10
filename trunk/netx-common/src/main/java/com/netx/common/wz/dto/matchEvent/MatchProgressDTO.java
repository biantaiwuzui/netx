package com.netx.common.wz.dto.matchEvent;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 赛程
 * Created by Yawn on 2018/8/13 0013.
 */
public class MatchProgressDTO {
    @ApiModelProperty(value = "比赛ID，有就更新，没有就添加")
    private String id;
    /**
     * 比赛ID
     */
    @ApiModelProperty(value = "比赛ID")
    private String matchId;
    /**
     * 赛制名称
     */
    @ApiModelProperty(value = "赛程名称")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String matchName;
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private Date beginTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    @ApiModelProperty(value = "顺序")
    private Integer sort;

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
