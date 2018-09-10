package com.netx.common.wz.dto.quartz;

import com.netx.common.wz.dto.quartz.base.BaseSchedulerDto;
import io.swagger.annotations.ApiModelProperty;

public class SimpleSchedulerDto extends BaseSchedulerDto {

    @ApiModelProperty(value = "开始时间的毫秒时间戳，若不填代表立即开始")
    private Long startAt;
    @ApiModelProperty(value = "结束时间的毫秒时间戳，若此处有值，代表到这个时候会强制停止，无论有没有做完")
    private Long endAt;

    @ApiModelProperty(value = "每几秒执行一次，这个值必须和repeatCount或repeatForever=true同时设置才会生效")
    private Integer intervalInSeconds;

    @ApiModelProperty(value = "重复几次")
    private Integer repeatCount;

    @ApiModelProperty(value = "是否无限重复")
    private Boolean repeatForever;

    public Long getStartAt() {
        return startAt;
    }

    public void setStartAt(Long startAt) {
        this.startAt = startAt;
    }

    public Long getEndAt() {
        return endAt;
    }

    public void setEndAt(Long endAt) {
        this.endAt = endAt;
    }

    public Integer getIntervalInSeconds() {
        return intervalInSeconds;
    }

    public void setIntervalInSeconds(Integer intervalInSeconds) {
        this.intervalInSeconds = intervalInSeconds;
    }

    public Integer getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(Integer repeatCount) {
        this.repeatCount = repeatCount;
    }

    public Boolean getRepeatForever() {
        return repeatForever;
    }

    public void setRepeatForever(Boolean repeatForever) {
        this.repeatForever = repeatForever;
    }
}
