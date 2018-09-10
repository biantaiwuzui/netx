package com.netx.common.user.dto.integration;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel("综合信息的概况（教育、工作、兴趣）")
public class EditUserLabelRequest {

    @ApiModelProperty("用户id")
    private String userId;

    @NotBlank(message = "概况不能为空")
    @ApiModelProperty("教育或工作或兴趣的概况")
    private String label;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
