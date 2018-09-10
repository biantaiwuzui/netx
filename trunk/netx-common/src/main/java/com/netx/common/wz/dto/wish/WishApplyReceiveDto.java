package com.netx.common.wz.dto.wish;

import java.math.BigDecimal;

public class WishApplyReceiveDto {
    /**业务ID*/
    private String id;

    /**用户ID*/
    private String userId;

    /**心愿表ID*/
    private String wishId;

    /**申请金额*/
    private BigDecimal amount;

    /**描述*/
    private String description;

    /**凭据*/
    private String pic;

    /**使用类型*/
    private Integer applyType;

    /**收款者网号*/
    private String applyInfo;

    /**收款者昵称*/
    private String nickName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "WishApplyReceiveDto{" +
                "id='" + id + '\'' +
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
