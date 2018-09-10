package com.netx.common.user.dto.photo;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class ExchangePhotoPositionRequestDto {

    @NotBlank(message = "用户id不能为空")
    @ApiModelProperty
    private String userId;

    @NotNull(message = "需要交换的照片位置值不能为空")
    @ApiModelProperty("需要交换位置的照片的id")
    private Integer beforePosition;

    @NotNull(message = "被交换的照片位置值不能为空")
    @ApiModelProperty("被交换位置的照片id")
    private Integer afterPosition;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getBeforePosition() {
        return beforePosition;
    }

    public void setBeforePosition(Integer beforePosition) {
        this.beforePosition = beforePosition;
    }

    public Integer getAfterPosition() {
        return afterPosition;
    }

    public void setAfterPosition(Integer afterPosition) {
        this.afterPosition = afterPosition;
    }
}
