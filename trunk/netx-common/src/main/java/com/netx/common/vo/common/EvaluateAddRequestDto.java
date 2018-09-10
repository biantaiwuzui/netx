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
public class EvaluateAddRequestDto {
    @ApiModelProperty(notes = "评价主id,如果为回复评价,则不能为空")
    private String pId;

    @ApiModelProperty(value = "被评价人")
    @NotBlank(message = "被评价人id不能为空")
    private String toUserId;

    @ApiModelProperty(value = "事件主键id：")
    @NotBlank(message = "事件id不能为空")
    private String typeId;

    @ApiModelProperty(value = "事件名称")
    private String typeName;

    @ApiModelProperty(value = "事件类型")
    @NotNull(message = "事件类型不能为空")
    private EvaluateTypeEnum evaluateType;

    @NotBlank(message = "评价内容不能为空")
    @ApiModelProperty(required = true, value = "评价内容")
    private String content;

    @ApiModelProperty(notes = "评价分数")
    private Integer score;

    @ApiModelProperty(value = "订单ID，可以为空")
    private String  orderId;

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public EvaluateTypeEnum getEvaluateType() {
        return evaluateType;
    }

    public void setEvaluateType(EvaluateTypeEnum evaluateType) {
        this.evaluateType = evaluateType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    /*
    @ApiModelProperty(notes = "评价分数")
    private Integer score;

    @ApiModelProperty(notes = "事件id,与toUserId不能同时为空")
    private String typeId;

    @ApiModelProperty(value = "被评价人或商品,与typeId不能同时为空")
    private String toUserId;

    @ApiModelProperty(value = "被评价名称")
    private String toUserName;

    @NotBlank(message = "评价内容不能为空")
    @ApiModelProperty(required = true, value = "评价内容")
    private String content;



    @ApiModelProperty(value = "事件显示名称")
    private String typeName;

    @ApiModelProperty(value = "评论类型，商家评论：businessEvaluate，心愿评论：wishEvaluate 可以为空，如何选择其中评论特殊处理")
    private String  evaluateType;

    @ApiModelProperty(value = "订单ID，可以为空")
    private String  orderId;

    public Integer getScore() {
        return score;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getEvaluateType() {
        return evaluateType;
    }

    public void setEvaluateType(String evaluateType) {
        this.evaluateType = evaluateType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }*/
}
