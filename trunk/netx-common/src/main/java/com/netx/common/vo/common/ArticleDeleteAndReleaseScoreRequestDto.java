package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

@ApiModel
public class ArticleDeleteAndReleaseScoreRequestDto {

    @ApiModelProperty("资讯ID数组")
    @NotEmpty(message = "资讯ID数组不能为空")
    private List<String> articleIds;

    @NotBlank(message = "审核人Id不能为空")
    @ApiModelProperty("审核人id")
    private String auditUserId;

    public List<String> getArticleIds() {
        return articleIds;
    }

    public void setArticleIds(List<String> articleIds) { this.articleIds = articleIds; }

    public String getAuditUserId() { return auditUserId; }

    public void setAuditUserId(String auditUserId) { this.auditUserId = auditUserId; }
}
