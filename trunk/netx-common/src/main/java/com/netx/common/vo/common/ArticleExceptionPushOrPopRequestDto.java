package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel
public class ArticleExceptionPushOrPopRequestDto {

    @NotBlank(message = "资讯的ID不能为空")
    @ApiModelProperty("资讯的ID")
    public String  articleId;

    @NotBlank(message = "列入异常资讯的原因不能为空")
    @ApiModelProperty("列入异常的理由")
    public String reason;

    @ApiModelProperty("变更的状态")
    private int status;

    @ApiModelProperty("资讯的图片")
    private String pic;

    @ApiModelProperty("资讯文字的字数")
    private int length;
    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleIds) {
        this.articleId = articleIds;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}