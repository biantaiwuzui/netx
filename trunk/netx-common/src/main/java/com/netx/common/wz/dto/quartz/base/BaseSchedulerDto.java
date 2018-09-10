package com.netx.common.wz.dto.quartz.base;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Map;

public class BaseSchedulerDto {
    @ApiModelProperty(required = true, value = "job包下的类名，如com.sy.worth.job.***Job")
    @NotBlank
    private String targetClass;

    @ApiModelProperty(required = true, value = "任务名称，可使用业务主键uuid，若无，则可写如：批量取消用户冻结 等")
    @NotBlank
    private String name;

    @ApiModelProperty(required = true, value = "组名称，可使用业务Model名称等")
    @NotBlank
    private String group;

    @ApiModelProperty(value = "参数，key为参数名 value为参数值，key必须为String，value必须为String|Boolean|Float|Double|Integer|Long中的一个")
    private Map<String, Object> jobData;

    public String getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(String targetClass) {
        this.targetClass = targetClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getJobData() {
        return jobData;
    }

    public void setJobData(Map<String, Object> jobData) {
        this.jobData = jobData;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
