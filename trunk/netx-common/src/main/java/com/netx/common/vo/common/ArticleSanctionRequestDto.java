package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel
public class ArticleSanctionRequestDto {

    @NotBlank(message = "受限名单ID不能为空")
    @ApiModelProperty("受限名单ID")
    private String articleLimitedId;

    @ApiModelProperty("解除原因")
    @NotBlank(message = "解除原因不能为空")
    private String reason;
    @ApiModelProperty("当前操作的userId")
    private String userId;

    public String getArticleLimitedId() { return articleLimitedId; }

    public void setArticleLimitedId(String articleLimitedId) { this.articleLimitedId = articleLimitedId; }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
