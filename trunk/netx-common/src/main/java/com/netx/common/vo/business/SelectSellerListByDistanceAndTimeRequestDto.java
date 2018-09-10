package com.netx.common.vo.business;

import com.netx.common.user.dto.common.CommonListByGeohashDto;
import com.netx.common.user.dto.common.CommonListDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 根据距离查询网商的列表
 * @Author 浩俊
 */
@ApiModel
public class SelectSellerListByDistanceAndTimeRequestDto {

    @Valid
    @NotNull(message = "分页信息不能为空")
    @ApiModelProperty("分页信息")
    private CommonListDto commonListDto;

    @Valid
    @NotNull(message = "位置信息不能为空")
    @ApiModelProperty("位置信息")
    private CommonListByGeohashDto commonListByGeohashDto;

    /**
     * 商家类别ID
     */
    @NotNull(message = "商家类别ID不能为空")
    @ApiModelProperty("商家类别ID,必填")
    private String categoryId;

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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
