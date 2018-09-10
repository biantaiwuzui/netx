package com.netx.common.user.dto.article;

import com.netx.common.vo.common.PageRequestDto;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

public class SelectArticleListRequestDto extends PageRequestDto{

    @NotBlank(message = "发布者的id 不能为空")
    @ApiModelProperty("发布者的id")
    private String userId;

    @ApiModelProperty("是否查询软文")
    private Boolean isArticleType;

    public Boolean getIsArticleType() {
        return isArticleType;
    }

    public void setIsArticleType(Boolean isArticleType) {
        this.isArticleType = isArticleType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "SelectArticleListRequestDto{"
                + "userId='" + userId + '\''
                + ", isArticleType=" + isArticleType + '\''
                + ", current=" + getCurrent() + '\''
                + ", size=" + getSize() + '\''
                + '}';
    }
}
