package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * Create by wongloong on 17-9-17
 */
@ApiModel
public class BillQueryRequestDto extends PageRequestDto {
    @ApiModelProperty("查询时间起,不传以默认以1970年01月1日 0分0秒开始")
    private Long startTime = 0l;
    @ApiModelProperty("查询时间止，不传以当前提交的时间戳为截止")
    private Long endTime = System.currentTimeMillis();

    @ApiModelProperty(required = true,notes = "流水类型:0平台，1经营")
    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }


    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

}
