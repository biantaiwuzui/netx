package com.netx.common.vo.common;

import com.netx.common.user.dto.common.CommonListDto;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

public class GetKidTagsRequestDto extends CommonListDto {
    @ApiModelProperty(value = "父标签id")
    @NotNull(message = "父id不能为空")
    private String pid;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
