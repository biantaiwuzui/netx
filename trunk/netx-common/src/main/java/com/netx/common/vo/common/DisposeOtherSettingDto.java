package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Create by wongloong on 17-9-8
 */
@ApiModel
public class DisposeOtherSettingDto {
    @ApiModelProperty(required = true, name = "审核人id")
    @NotBlank
    private String disposeUserId;
    @ApiModelProperty(required = true, name = "设置id")
    @NotBlank
    private String id;
    @ApiModelProperty(required = true, name = "审核意见", notes = "0:拒绝,1通过")
    @NotNull
    private Integer canUse;

    public String getDisposeUserId() {
        return disposeUserId;
    }

    public void setDisposeUserId(String disposeUserId) {
        this.disposeUserId = disposeUserId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCanUse() {
        return canUse;
    }

    public void setCanUse(Integer canUse) {
        this.canUse = canUse;
    }
}
