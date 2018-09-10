package com.netx.common.wz.dto.meeting;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class RegMeetingDto {

    @ApiModelProperty(value = "聚会表ID")
    @NotBlank(message = "聚会表ID不能为空")
    private String meetingId;

    @ApiModelProperty(value = "报名人ID")
//    @NotBlank(message = "报名人ID不能为空")
    private String userId;

    @ApiModelProperty(value = "邀请的好友id，以英文逗号分隔")
    private String friends;

    @ApiModelProperty(value = "立即报名传0，隐身报名传1")
    @NotNull(message = "是否隐身不能为空")
    private Boolean isAnonymity;

    @ApiModelProperty(value = "报名数量")
    @NotNull(message = "报名数量不能为空")
    @Min(value = 1, message = "至少是1")
    private Integer amount;

    @ApiModelProperty(value = "报名总费用（单价*数量），取主表的单价字段值计算传过来")
    @NotNull(message = "费用不能为空")
    private BigDecimal fee;
    

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFriends() {
        return friends;
    }

    public void setFriends(String friends) {
        this.friends = friends;
    }

    public Boolean getAnonymity() {
        return isAnonymity;
    }

    public void setAnonymity(Boolean anonymity) {
        isAnonymity = anonymity;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }
}
