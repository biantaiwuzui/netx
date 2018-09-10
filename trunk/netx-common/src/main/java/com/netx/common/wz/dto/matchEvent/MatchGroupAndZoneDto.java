package com.netx.common.wz.dto.matchEvent;

import com.baomidou.mybatisplus.enums.FieldFill;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

public class MatchGroupAndZoneDto {
    private String id;
    /**
     * 赛区id
     */
    @ApiModelProperty("赛区id")
    private String matchZoneId;
    /**
     * 赛组id
     */
    @ApiModelProperty("赛组id")
    private String matchGroupId;
    /**
     * 开始时间
     */
    @ApiModelProperty("赛组id")
    private Date startTime;
    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private Date endTime;
    @ApiModelProperty("是否默认（在表示使用该区的默认时间的话，就是两个都填true，如果使用外面的时间就将这里传false" +
            ",在表示各组的默认时间时，如果使用外面的时间，就传true）")
    private boolean isDefault;

    @ApiModelProperty("是否是赛区的设置")
    private boolean isZoneTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isZoneTime() {
        return isZoneTime;
    }

    public void setZoneTime(boolean zoneTime) {
        isZoneTime = zoneTime;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getMatchZoneId() {
        return matchZoneId;
    }

    public void setMatchZoneId(String matchZoneId) {
        this.matchZoneId = matchZoneId;
    }

    public String getMatchGroupId() {
        return matchGroupId;
    }

    public void setMatchGroupId(String matchGroupId) {
        this.matchGroupId = matchGroupId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
