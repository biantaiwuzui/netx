package com.netx.shopping.vo;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class MerchantManageFeeRequestDto {

    @ApiModelProperty(value = "用户id", required = true)
    private String userId;

    @ApiModelProperty(value = "商家id", required = true)
    @NotBlank(message = "商家id不能为空")
    private String merchantId;

    @ApiModelProperty(value = "缴费有效期,0：终生;其他传年数", required = true)
    @NotNull(message = "缴费有效期不能为空")
    @Min(value = 0,message = "有效期不能小于0")
    private Integer effectiveTime;

    @ApiModelProperty("特权行使人网号")
    @NotBlank(message = "特权行使人网号不能为空")
    private String privilegeNetworkNum;

    @ApiModelProperty("引荐人的客服代码")
    private String referralServiceCode;

    @ApiModelProperty("费用描述")
    @NotBlank(message = "费用描述不能为空")
    private String description;

    @ApiModelProperty("费用")
    @NotNull(message = "费用不能为空")
    private BigDecimal amount;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(Integer effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public String getPrivilegeNetworkNum() {
        return privilegeNetworkNum;
    }

    public void setPrivilegeNetworkNum(String privilegeNetworkNum) {
        this.privilegeNetworkNum = privilegeNetworkNum;
    }

    public String getReferralServiceCode() {
        return referralServiceCode;
    }

    public void setReferralServiceCode(String referralServiceCode) {
        this.referralServiceCode = referralServiceCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
