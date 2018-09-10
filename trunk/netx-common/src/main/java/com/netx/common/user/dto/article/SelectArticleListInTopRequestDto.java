package com.netx.common.user.dto.article;

import com.netx.common.user.dto.common.CommonListDto;
import com.netx.common.user.enums.ArticleTopTypeEnum;
import com.netx.common.user.enums.ArticleTypeEnum;
import com.netx.common.vo.common.PageRequestDto;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 查询置顶咨讯列表
 * @author 李卓
 */
public class SelectArticleListInTopRequestDto extends PageRequestDto {

    /*@Valid
    @NotNull(message = "位置信息不能为空")
    @ApiModelProperty("位置信息")
    private CommonListByGeohashDto commonListByGeohashDto;*/

    @NotNull
    @ApiModelProperty("置顶类型枚举<br>" +
            " 1：列表置顶<br>" +
            " 2：分类置顶<br>")
    private ArticleTopTypeEnum articleTopTypeEnum;

    @ApiModelProperty("是否查询软文")
    private Boolean isArticleType;

    public Boolean getIsArticleType() {
        return isArticleType;
    }

    public void setIsArticleType(Boolean isArticleType) {
        this.isArticleType = isArticleType;
    }

    public ArticleTopTypeEnum getArticleTopTypeEnum() {
        return articleTopTypeEnum;
    }

    public void setArticleTopTypeEnum(ArticleTopTypeEnum articleTopTypeEnum) {
        this.articleTopTypeEnum = articleTopTypeEnum;
    }

    @Override
    public String toString() {
        return "SelectArticleListInTopRequestDto{"
                + " articleTopTypeEnum=" + articleTopTypeEnum + '\''
                + ", isArticleType=" + isArticleType + '\''
                + ", current=" + getCurrent() + '\''
                + ", size=" + getSize() + '\''
                + '}';
    }
}
