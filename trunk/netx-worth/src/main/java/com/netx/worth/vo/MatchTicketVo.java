package com.netx.worth.vo;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class MatchTicketVo {
    /**
     * id
     */
    private String id;
    /**
     * 赛区Id
     */
    private String matchZoneId;

    private String ticketName;
    /**
     * 门票价钱
     */
    private BigDecimal free;
    /**
     * 名额
     */
    private Integer number;
    /**
     * 门票描述
     */
    private String description;
    /**
     * 开始发售时间
     */
    private Date beginTime;
    /**
     * 结束发售时间
     */
    private Date endTime;

    private Integer sort;

    private boolean usedefault;

    private List<KindAndVenceVo> kindAndVenceVoList;

    public boolean isUsedefault() {
        return usedefault;
    }

    public void setUsedefault(boolean usedefault) {
        this.usedefault = usedefault;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }


    public List<KindAndVenceVo> getKindAndVenceVoList() {
        return kindAndVenceVoList;
    }

    public void setKindAndVenceVoList(List<KindAndVenceVo> kindAndVenceVoList) {
        this.kindAndVenceVoList = kindAndVenceVoList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMatchZoneId() {
        return matchZoneId;
    }

    public void setMatchZoneId(String matchZoneId) {
        this.matchZoneId = matchZoneId;
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
}
