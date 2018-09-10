package com.netx.common.vo.currency;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * @Author hj.Mao
 * @Date 2017.12.19
 */
@ApiModel
public class BooleanRecommendApproveRequestDto {

    @ApiModelProperty("发行者用户id")
    @NotBlank(message = "发行者用户id不能为空")
    private  String releaseUserId;

    @ApiModelProperty("选择的好友id列表")
    @NotEmpty(message = "选择的好友id列表不能为空")
    private  List<String> selectRecommendUserIds;

    public String getReleaseUserId() {
        return releaseUserId;
    }

    public void setReleaseUserId(String releaseUserId) {
        this.releaseUserId = releaseUserId;
    }

    public List<String> getSelectRecommendUserIds() {
        return selectRecommendUserIds;
    }

    public void setSelectRecommendUserIds(List<String> selectRecommendUserIds) {
        this.selectRecommendUserIds = selectRecommendUserIds;
    }

    @Override
    public String toString() {
        return "BooleanRecommendApproveRequestDto{" +
                "releaseUserId='" + releaseUserId + '\'' +
                ", selectRecommendUserIds=" + selectRecommendUserIds +
                '}';
    }
}
