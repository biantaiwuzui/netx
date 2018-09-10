package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Create by wongloong on 17-9-10
 */
@ApiModel
public class SensitiveAuditListRequestDto extends PageRequestDto {

    @ApiModelProperty("审核类型, \n" +
            "0：添加 \n" +
            "1：删除 \n")
    @NotNull(message = "审核类型不能为空")
    @Max(message = "审核类型最大为1",value = 1)
    @Min(message = "审核类型最小为0",value = 0)
    private Integer delOrSave;

    @ApiModelProperty("审核状态, \n" +
            "0：未审核 \n" +
            "1：已审核 \n" +
            "2：全部")
    @NotNull(message = "审核状态不能为空")
    @Max(message = "状态值最大为2",value = 2)
    @Min(message = "状态值最小为0",value = 0)
    private Integer status;

    public Integer getDelOrSave() {
        return delOrSave;
    }

    public void setDelOrSave(Integer delOrSave) {
        this.delOrSave = delOrSave;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
