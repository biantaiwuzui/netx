package com.netx.common.vo.common;

import com.netx.common.common.enums.LimitEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ApiModel
public class PunishAuthorRequestDto {

    @ApiModelProperty("受限ID")
    private String articleLimitedId;

    @ApiModelProperty("用户ID")
    @NotBlank(message = "用户ID不能为空")
    private String userId;

    @NotBlank(message = "作者网号不能为空")
    @ApiModelProperty("作者网号")
    private String userNetworkNum;

    @NotBlank(message = "处分理由不能为空")
    @ApiModelProperty("处分理由")
    private String reason;

    @NotNull(message = "限制措施不能为空")
    @ApiModelProperty("限制措施")
    private LimitEnum limitEnum;

    @NotNull(message = "对应的限制的值不能为空")
    @Min(value = 0)
    @ApiModelProperty("对应的限制的值")
    private Integer limitValue;

    @ApiModelProperty("减少信用值参数")
    private Integer param1;

    @ApiModelProperty("每个月允许发布的资讯数量参数")
    private Integer param2;

    @ApiModelProperty("每天允许发布的资讯数量参数")
    private Integer param3;

    @ApiModelProperty("禁止发布资讯天数参数")
    private Integer param4;

    @ApiModelProperty("发布资讯时付费参数")
    private Integer param5;

    /**
     * 操作者昵称,由前端传递值
     */
    @ApiModelProperty("操作者用户id,由前端直接传递值")
    private String operatorUserId;


    public String getOperatorUserId() { return operatorUserId; }

    public void setOperatorUserId(String operatorUserId) { this.operatorUserId = operatorUserId; }

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId;}


    public String getUserNetworkNum() {
        return userNetworkNum;
    }

    public void setUserNetworkNum(String userNetworkNum) {
        this.userNetworkNum = userNetworkNum;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LimitEnum getLimitEnum() {
        return limitEnum;
    }

    public void setLimitEnum(LimitEnum limitEnum) {
        this.limitEnum = limitEnum;
    }

    public Integer getLimitValue() {
        return limitValue;
    }

    public void setLimitValue(Integer limitValue) {
        this.limitValue = limitValue;
    }

    public Integer getParam1() {
        return param1;
    }

    public void setParam1(Integer param1) {
        this.param1 = param1;
    }

    public Integer getParam2() {
        return param2;
    }

    public void setParam2(Integer param2) {
        this.param2 = param2;
    }

    public Integer getParam3() {
        return param3;
    }

    public void setParam3(Integer param3) {
        this.param3 = param3;
    }

    public Integer getParam4() {
        return param4;
    }

    public void setParam4(Integer param4) {
        this.param4 = param4;
    }

    public Integer getParam5() {
        return param5;
    }

    public void setParam5(Integer param5) {
        this.param5 = param5;
    }

    public String getArticleLimitedId() {
        return articleLimitedId;
    }

    public void setArticleLimitedId(String articleLimitedId) {
        this.articleLimitedId = articleLimitedId;
    }

    @Override
    public String toString() {
        return "PunishAuthorRequestDto{" +
                "articleLimitedId='" + articleLimitedId + '\'' +
                ", userId='" + userId + '\'' +
                ", userNetworkNum='" + userNetworkNum + '\'' +
                ", reason='" + reason + '\'' +
                ", limitEnum=" + limitEnum +
                ", limitValue=" + limitValue +
                ", param1=" + param1 +
                ", param2=" + param2 +
                ", param3=" + param3 +
                ", param4=" + param4 +
                ", param5=" + param5 +
                ", operatorUserId='" + operatorUserId + '\'' +
                '}';
    }
}
