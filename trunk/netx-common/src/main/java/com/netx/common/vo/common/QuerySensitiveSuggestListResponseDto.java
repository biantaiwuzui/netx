package com.netx.common.vo.common;

import java.util.Date;
import java.util.List;

public class QuerySensitiveSuggestListResponseDto {

    /**
     * 标识id
     */
    private String id;
    /**
     * 建议人userId
     */
    private String suggestUserId;
    /**
     * 建议人昵称
     */
    private String suggestUserName;
    /**
     * 建议删除还是新增，0删除，1新增
     */
    private Integer delOrSave;
    /**
     * 词值，多个用，号隔开
     */
    private List<String> values;
    /**
     * 审核人id，默认0，没人审核
     */
    private String auditUserId;
    /**
     * 建议理由
     */
    private String reason;
    /**
     * 创建时间
     */
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSuggestUserId() {
        return suggestUserId;
    }

    public void setSuggestUserId(String suggestUserId) {
        this.suggestUserId = suggestUserId;
    }

    public String getSuggestUserName() {
        return suggestUserName;
    }

    public void setSuggestUserName(String suggestUserName) {
        this.suggestUserName = suggestUserName;
    }

    public Integer getDelOrSave() {
        return delOrSave;
    }

    public void setDelOrSave(Integer delOrSave) {
        this.delOrSave = delOrSave;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public String getAuditUserId() {
        return auditUserId;
    }

    public void setAuditUserId(String auditUserId) {
        this.auditUserId = auditUserId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
