package com.netx.common.wz.dto.wish;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
public class WishWithdrawalDto {
    @ApiModelProperty(value = "心愿历史表ID")
    @NotBlank(message = "心愿历史表ID不能为空")
    private String id;

    @ApiModelProperty(value = "心愿使用表ID")
    @NotBlank(message = "心愿使用表ID不能为空")
    private String wishApplyId;

    @ApiModelProperty(value = "推荐意见")
    private String reason;

    @ApiModelProperty(value = "操作者id")
    @NotBlank(message = "操作者不能为空")
    private String userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWishApplyId() {
        return wishApplyId;
    }

    public void setWishApplyId(String wishApplyId) {
        this.wishApplyId = wishApplyId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "WishWithdrawalDto{" +
                "id='" + id + '\'' +
                ", wishApplyId='" + wishApplyId + '\'' +
                ", reason='" + reason + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}