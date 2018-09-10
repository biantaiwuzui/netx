package com.netx.common.vo.common;

import com.netx.common.common.enums.EvaluateTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Create by wongloong on 17-9-9
 */
@ApiModel
public class MyEvaluateQueryRequestDto extends PageRequestDto {

    @ApiModelProperty(notes = "被评价人id")
    //@NotBlank(message = "评价人不能为空")
    private String toUserId;

    @ApiModelProperty(value = "评论类型")
    private EvaluateTypeEnum evaluateType;

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public EvaluateTypeEnum getEvaluateType() {
        return evaluateType;
    }

    public void setEvaluateType(EvaluateTypeEnum evaluateType) {
        this.evaluateType = evaluateType;
    }
}
