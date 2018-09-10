package com.netx.common.wz.vo.invitation;

//import com.sy.worth.rpc.model.user;

import java.math.BigDecimal;
import java.util.Date;

public class InvitationListVo {

    private String id;
    /**
     * 邀请人
     */
    private String fromUserId;
    /**
     * 对象
     */
    private String toUserId;
    /**
     * 邀请标题（主题）
     */
    private String title;
    /**
     * 地址
     */
    private String address;
    /**
     * 开始时间
     */
    private Integer startAt;
    /**
     * 结束时间
     */
    private Date endAt;
    /**
     * 报酬
     */
    private BigDecimal amout;
    /**
     * 报酬类型：
     * 1：现金
     * 2：网币
     */
    private Integer payType;
    /**
     * 关联主键，没有就是0
     */
    private String relatableId;
    /**
     * 描述
     */
    private String description;
    /**
     * 是否匿名
     */
    private Boolean isAnonymity;
    /**
     * 邀请码
     */
    private Integer code;
    /**
     * 状态：
     * 1：发出邀请
     * 2：待确认的邀请
     * 3：接受邀请
     * 4：拒绝邀请
     */
    private Integer status;

    private Integer createTime;

//    private user user;
//
//    public user getUser() {
//        return user;
//    }
//
//    public void setUser(user user) {
//        this.user = user;
//    }

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

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getStartAt() {
        return startAt;
    }

    public void setStartAt(Integer startAt) {
        this.startAt = startAt;
    }

    public Date getEndAt() {
        return endAt;
    }

    public void setEndAt(Date endAt) {
        this.endAt = endAt;
    }

    public BigDecimal getAmout() {
        return amout;
    }

    public void setAmout(BigDecimal amout) {
        this.amout = amout;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getRelatableId() {
        return relatableId;
    }

    public void setRelatableId(String relatableId) {
        this.relatableId = relatableId;
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

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
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
}
