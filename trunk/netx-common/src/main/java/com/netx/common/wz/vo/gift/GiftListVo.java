package com.netx.common.wz.vo.gift;

//import com.sy.worth.rpc.model.user;

import java.math.BigDecimal;

public class GiftListVo {

    private String id;
    private String fromUserId;
    private Integer sendAt;
    private Integer giftType;
    private String relatableId;
    private BigDecimal amount;
    private String info;
    private String description;
    private Boolean isAnonymity;
    private Integer status;
    private Integer createTime;
    private String createUser;
    private Integer updateTime;
    private String updateUser;
    private Boolean delTag;
//    private user fromUser;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Integer getSendAt() {
        return sendAt;
    }

    public void setSendAt(Integer sendAt) {
        this.sendAt = sendAt;
    }

    public Integer getGiftType() {
        return giftType;
    }

    public void setGiftType(Integer giftType) {
        this.giftType = giftType;
    }

    public String getRelatableId() {
        return relatableId;
    }

    public void setRelatableId(String relatableId) {
        this.relatableId = relatableId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getAnonymity() {
        return isAnonymity;
    }

    public void setAnonymity(Boolean anonymity) {
        isAnonymity = anonymity;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Boolean getDelTag() {
        return delTag;
    }

    public void setDelTag(Boolean delTag) {
        this.delTag = delTag;
    }

//    public user getFromUser() {
//        return fromUser;
//    }
//
//    public void setFromUser(user fromUser) {
//        this.fromUser = fromUser;
//    }
}
