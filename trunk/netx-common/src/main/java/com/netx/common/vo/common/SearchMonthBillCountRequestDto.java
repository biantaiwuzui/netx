package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * 查找月交易额dto
 * @Author hj.Mao
 */
@ApiModel
public class SearchMonthBillCountRequestDto {

    @ApiModelProperty("用户ID List集合")
    @NotEmpty(message = "用户ID集合不能为空")
    List<String> recommendUserIds;

    public List<String> getRecommendUserIds() {
        return recommendUserIds;
    }

    public void setRecommendUserIds(List<String> recommendUserIds) {
        this.recommendUserIds = recommendUserIds;
    }
}
