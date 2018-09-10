package com.netx.common.vo.currency;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 用于子安模块合并的dto
 */
@ApiModel
public class GetCurrencyTokenRequestDto {

    @ApiModelProperty("用户id")
    @NotBlank(message = "用户id不能为空")
    private String userId;
    @ApiModelProperty("判断是否为持有网信信息:\n false---发行的网信详情\ntrue------持有的网信信息 ")
    private Boolean isHold;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getIsHold() { return isHold; }

    public void setIsHold(Boolean isHold) { this.isHold = isHold; }

    @Override
    public String toString() {
        return "GetCurrencyTokenRequestDto{" +
                "userId='" + userId + '\'' +
                ", isHold=" + isHold +
                '}';
    }
}
