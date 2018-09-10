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
public class EvaluateQueryRequestDto extends PageRequestDto {
    @ApiModelProperty(value = "主键", notes = "查找被评价的商品,活动主键,三者不能都为空")
    private String typeId;

    @ApiModelProperty(notes = "被评价人id")
    private String toUserId;

    @ApiModelProperty(notes = "评价人id")
    private String fromUserId;

    @ApiModelProperty(value = "评论类型")
    private EvaluateTypeEnum evaluateType;
    
    @ApiModelProperty("订单id")
    private String OrderIds;


    public String getOrderIds() {
        return OrderIds;
    }

    public void setOrderIds(String orderIds) {
        OrderIds = orderIds;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public EvaluateTypeEnum getEvaluateType() {
        return evaluateType;
    }

    public void setEvaluateType(EvaluateTypeEnum evaluateType) {
        this.evaluateType = evaluateType;
    }
}
