package com.netx.common.user.dto.article;

import com.netx.common.user.dto.common.CommonListByGeohashDto;
import com.netx.common.user.dto.common.CommonListDto;
import com.netx.common.user.enums.ArticleTypeEnum;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 根据距离、时间分页查询咨讯列表
 * @author 李卓
 */
public class SelectArticleListByDistanceAndTimeRequestDto {

    //@NotBlank(message = "使用者的 userId 不能为空")
    @ApiModelProperty("使用者的 userId")
    private String userId;

    @Valid
    @NotNull(message = "分页信息不能为空")
    @ApiModelProperty("分页信息")
    private CommonListDto commonListDto;

    @Valid
    @NotNull(message = "位置信息不能为空")
    @ApiModelProperty("位置信息")
    private CommonListByGeohashDto commonListByGeohashDto;

    @NotNull(message = "咨讯类型不能为空")
    @ApiModelProperty("咨讯类型，分别是：‘①图文’，‘②音视’")
    private ArticleTypeEnum articleTypeEnum;

    public ArticleTypeEnum getArticleTypeEnum() {
        return articleTypeEnum;
    }

    public void setArticleTypeEnum(ArticleTypeEnum articleTypeEnum) {
        this.articleTypeEnum = articleTypeEnum;
    }

    public CommonListDto getCommonListDto() {
        return commonListDto;
    }

    public void setCommonListDto(CommonListDto commonListDto) {
        this.commonListDto = commonListDto;
    }

    public CommonListByGeohashDto getCommonListByGeohashDto() {
        return commonListByGeohashDto;
    }

    public void setCommonListByGeohashDto(CommonListByGeohashDto commonListByGeohashDto) {
        this.commonListByGeohashDto = commonListByGeohashDto;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
