package com.netx.common.wz.dto.quartz;

import com.netx.common.wz.dto.quartz.base.BaseSchedulerDto;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

public class CronSchedulerDto extends BaseSchedulerDto {
    @ApiModelProperty(required = true, value = "cron表达式，详情查看官方文档：http://www.quartz-scheduler.org/documentation/quartz-2.x/tutorials/crontrigger.html")
    @NotBlank
    private String cron;

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }
}
