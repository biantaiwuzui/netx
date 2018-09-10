package com.netx.common.wz.dto.matchEvent;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 发布入场门票
 * Created by Yawn on 2018/8/1
 */
public class MatchTicketDTO {
    @ApiModelProperty(value = "有id值情况表示更新，没有的话表示插入")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @ApiModelProperty(value = "赛区ID（如果是默认设置，填写比赛Id）")
    private String matchZoneOrMatchId;


    @ApiModelProperty(value = "门票名称")
    private String ticketName;
    /**
     * 门票类型
     */
    @ApiModelProperty(value = "门票的场次ids(用逗号隔开扔进来)")
    private String venueIds;
    /**
     * 门票价钱
     */
    @ApiModelProperty(value = "门票价钱")
    private BigDecimal free;
    /**
     * 名额
     */
    @ApiModelProperty(value = "名额")
    private Integer number;
    /**
     * 门票描述
     */
    @ApiModelProperty(value = "门票说明")
    private String description;
    /**
     * 开始发售时间
     */
    @ApiModelProperty(value = "开始发售时间（不启用默认设置的时候填）")
    private Date beginTime;
    /**
     * 结束发售时间
     */
    @ApiModelProperty(value = "结束发售时间（不启用默认设置的时候填）")
    private Date endTime;
    /**
     * 顺序
     */
    @ApiModelProperty(value = "顺序非默认设置要填的")
    private Integer sort;

    @ApiModelProperty(value = "是否为默认设置(默认设置那里就是ture),")
    private boolean isDefault;
    @ApiModelProperty(value = "是否使用默认(默认设置那里直接扔false)")
    private boolean useDefalut;


    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getMatchZoneOrMatchId() {
        return matchZoneOrMatchId;
    }

    public void setMatchZoneOrMatchId(String matchZoneOrMatchId) {
        this.matchZoneOrMatchId = matchZoneOrMatchId;
    }

    public boolean isUseDefalut() {
        return useDefalut;
    }

    public void setUseDefalut(boolean useDefalut) {
        this.useDefalut = useDefalut;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public BigDecimal getFree() {
        return free;
    }

    public void setFree(BigDecimal free) {
        this.free = free;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getVenueIds() {
        return venueIds;
    }

    public void setVenueIds(String venueIds) {
        this.venueIds = venueIds;
    }
}
