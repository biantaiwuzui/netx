package com.netx.common.user.dto.userManagement;

import com.netx.common.user.dto.common.CommonListDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@ApiModel("根据网号分页查询用户")
public class SelectUserInSystemBlacklistRequest extends CommonListDto{

    //网号这里使用@NotNull，是因为网号可以为空串
    @ApiModelProperty(value = "网号")
    @NotNull(message = "网号不能为空，但可以为空串，空串代表查询所有网号")
    private String userNumber;

    @NotNull(message = "操作类型不能为空")
    @ApiModelProperty(value = "操作类型（白名单：0， 黑名单：1）")
    private Integer operateType;

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public Integer getOperateType() {
        return operateType;
    }

    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }
}
