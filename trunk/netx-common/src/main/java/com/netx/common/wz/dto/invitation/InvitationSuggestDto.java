package com.netx.common.wz.dto.invitation;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

public class InvitationSuggestDto {

    @ApiModelProperty(value = "邀请ID")
    @NotBlank(message = "邀请ID不能为空")
    private String invitationId;

    @ApiModelProperty(value = "被邀请人ID")
    @NotBlank(message = "被邀请人ID不能为空")
    private String userId;


    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "开始时间")
    private Long startAt;

    @ApiModelProperty(value = "结束时间")
    private Long endAt;

    @ApiModelProperty(value = "其他要求")
    private String suggestion;

    public String getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(String invitationId) {
        this.invitationId = invitationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getStartAt() {
        return startAt;
    }

    public void setStartAt(Long startAt) {
        this.startAt = startAt;
    }

    public Long getEndAt() {
        return endAt;
    }

    public void setEndAt(Long endAt) {
        this.endAt = endAt;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }
}
