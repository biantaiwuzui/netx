package com.netx.common.wz.dto.wish;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

public class WishHistoryDto {
    @ApiModelProperty(value = "心愿历史记录表ID")
    @NotBlank(message = "心愿历史记录表ID不能为空")
    private String id;
}
