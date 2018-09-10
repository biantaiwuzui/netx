package com.netx.common.wz.dto.quartz;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel
public class DeleteSchedulerDto {

    @ApiModelProperty("name")
    @NotBlank
    private String name;

    @ApiModelProperty("group")
    @NotBlank
    private String group;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
