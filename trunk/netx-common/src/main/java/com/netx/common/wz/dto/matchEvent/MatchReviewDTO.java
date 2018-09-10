package com.netx.common.wz.dto.matchEvent;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;

/**
 * 赛事审核
 * Created by Yawn on 2018/8/1 0001.
 */
public class MatchReviewDTO {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @ApiModelProperty(value = "有id值情况表示更新，没有的话表示插入")
    private String id;
    /**
     * 比赛id
     */
    @ApiModelProperty(value = "比赛id")
    private String matchId;
    /**
     * 审核者id
     */
    @ApiModelProperty(value = "审核者id")
    private String userId;
    @ApiModelProperty(value = "单位名称")
    private String organizerName;
    @ApiModelProperty(value = "单位类型" +
            "    (0, \"主办方\"),\n" +
            "    (1, \"协办方\"),\n" +
            "    (2, \"赞助方\"),\n" +
            "    (3, \"联合举办\"),\n" +
            "    (4, \"指导单位\"),\n" +
            "    (5, \"支持方\").")
    private Integer organizerKind;
    /**
     * 是否通过审核
     */
    @ApiModelProperty(value = "是否通过审核")
    private Boolean isApprove;

    @ApiModelProperty(value = "商家id")
    private String merchantId;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getApprove() {
        return isApprove;
    }

    public void setApprove(Boolean approve) {
        isApprove = approve;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public Integer getOrganizerKind() {
        return organizerKind;
    }

    public void setOrganizerKind(Integer organizerKind) {
        this.organizerKind = organizerKind;
    }


}
