package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class PunishAuthorTransferRequestDto {

    /**
     * 资讯受限id
     */
    @ApiModelProperty("资讯受限id")
    private String articleLimitedId;

    @ApiModelProperty("受限用户id")
    private String userId;


    @ApiModelProperty("受限用户网号")
    private String userNetworkNum;


    @ApiModelProperty("受限原因")
    private String reason;

    @ApiModelProperty("受限值")
    private Integer limitValue;

    @ApiModelProperty("受限类型")
    private Integer limitMeasure;

    /**
     * 发布时间
     */
    private Long ReleaseTime;
    /**
     * 操作者用户id,由前端传递值
     */
    private String operatorUserId;

    public String getArticleLimitedId() {
        return articleLimitedId;
    }

    public void setArticleLimitedId(String articleLimitedId) {
        this.articleLimitedId = articleLimitedId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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

    public Integer getLimitValue() {
        return limitValue;
    }

    public void setLimitValue(Integer limitValue) {
        this.limitValue = limitValue;
    }

    public Integer getLimitMeasure() {
        return limitMeasure;
    }

    public void setLimitMeasure(Integer limitMeasure) {
        this.limitMeasure = limitMeasure;
    }

    public Long getReleaseTime() {
        return ReleaseTime;
    }

    public void setReleaseTime(Long releaseTime) {
        ReleaseTime = releaseTime;
    }

    public String getOperatorUserId() {
        return operatorUserId;
    }

    public void setOperatorUserId(String operatorUserId) {
        this.operatorUserId = operatorUserId;
    }

    @Override
    public String toString() {
        return "PunishAuthorTransferRequestDto{" +
                "userId='" + userId + '\'' +
                ", userNetworkNum='" + userNetworkNum + '\'' +
                ", reason='" + reason + '\'' +
                ", limitValue=" + limitValue +
                ", limitMeasure=" + limitMeasure +
                ", ReleaseTime=" + ReleaseTime +
                ", operatorUserId='" + operatorUserId + '\'' +
                '}';
    }
}
