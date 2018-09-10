package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * Create by wongloong on 17-9-10
 */
@ApiModel
public class SensitiveAuditListRequest extends PageRequestDto {
    @NotNull
    @ApiModelProperty("审核类型,0添加1删除")
    private Integer delOrSave;

    public Integer getDelOrSave() {
        return delOrSave;
    }

    public void setDelOrSave(Integer delOrSave) {
        this.delOrSave = delOrSave;
    }
}
