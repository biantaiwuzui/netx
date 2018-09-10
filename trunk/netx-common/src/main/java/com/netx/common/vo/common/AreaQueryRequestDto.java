package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Create by wongloong on 17-9-1
 */
@ApiModel
public class AreaQueryRequestDto {
    @ApiModelProperty(required = true, value = "pid表示上级目录,如果没有请填写0")
    @NotBlank
    private String pid;
    private String id;
    @ApiModelProperty(required = true, value = "层级,0为第一层")
    private Integer flag;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }
}
