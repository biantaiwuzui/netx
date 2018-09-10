package com.netx.common.vo.currency;

/**
 * ReviewVo类,用于远程
 */
public class ReviewTokenVo {
    /**
     * 标识
     */
    private String id;
    /**
     * 网信ID
     */
    private String currencyId;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 审核状态
     1：待审核
     2：已审核
     */
    private Integer status;
    /**
     * 审核结果
     1、审核通过
     2、审核不通过
     */
    private Integer result;
    /**
     * 审核时间
     */
    private Integer audiTime;
    private Integer createTime;
    private String createUser;
    private Integer updateTime;
    private String updateUser;
    private Integer delTag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Integer getAudiTime() {
        return audiTime;
    }

    public void setAudiTime(Integer audiTime) {
        this.audiTime = audiTime;
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

    public Integer getDelTag() {
        return delTag;
    }

    public void setDelTag(Integer delTag) {
        this.delTag = delTag;
    }
}
