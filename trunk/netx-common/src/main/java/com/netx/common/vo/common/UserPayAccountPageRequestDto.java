package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * 第三方账号分页请求dto
 */
@ApiModel
public class UserPayAccountPageRequestDto extends PageRequestDto {

    @NotBlank(message = "用户id不能为空")
    private String userId;
    @ApiModelProperty("请求的账号类型: 1.微信    2.支付宝")
    @NotNull(message = "请求的账号类型不能为空")
    @Range(min = 1 ,max = 2)
    private Integer accountType;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }
}
