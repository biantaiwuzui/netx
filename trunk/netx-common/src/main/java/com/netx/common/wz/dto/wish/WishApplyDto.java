package com.netx.common.wz.dto.wish;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class WishApplyDto {

    @ApiModelProperty(value = "用户ID")
    @NotBlank(message = "用户ID不能为空")
    private String userId;

    @ApiModelProperty(value = "心愿表ID")
    @NotBlank(message = "心愿表ID不能为空")
    private String wishId;

    @ApiModelProperty(value = " 申请金额")
    @NotNull(message = "心愿表ID不能为空")
    private BigDecimal amount;

    @ApiModelProperty(value = "描述")
    @NotBlank(message = "描述不能为空")
    private String description;

    @ApiModelProperty(value = "凭据")
    @NotBlank(message = "凭据不能为空")
    private String pic;

    @ApiModelProperty(value = "使用类型：\n" +
            "     0：自提。\n" +
            "     1：给平台网友。\n" +
            "     2：给第三方。")
    @NotNull(message = "心愿表ID不能为空")
    private Integer applyType;
    /**
     * 使用信息，如本平台的就填网号，如其他，以json形式填
     */
    @ApiModelProperty(value = "收款者网号")
    @NotBlank(message = "使用信息不能为空")
    private String applyInfo;

    @ApiModelProperty(value = "收款者昵称")
    private String nickName;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWishId() {
        return wishId;
    }

    public void setWishId(String wishId) {
        this.wishId = wishId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public Integer getApplyType() {
        return applyType;
    }

    public void setApplyType(Integer applyType) {
        this.applyType = applyType;
    }

    public String getApplyInfo() {
        return applyInfo;
    }

    public void setApplyInfo(String applyInfo) {
        this.applyInfo = applyInfo;
    }



    @Override
    public String toString() {
        return "WishApplyDto{" +
                ", userId='" + userId + '\'' +
                ", wishId='" + wishId + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", pic='" + pic + '\'' +
                ", applyType=" + applyType +
                ", applyInfo='" + applyInfo + '\'' +
                ", nickName='" + nickName + '\'' +
                '}';
    }
}
