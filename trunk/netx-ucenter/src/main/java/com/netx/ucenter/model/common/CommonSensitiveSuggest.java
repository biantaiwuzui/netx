package com.netx.ucenter.model.common;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 黎子安
 * @since 2018-03-09
 */
@TableName("common_sensitive_suggest")
public class CommonSensitiveSuggest extends Model<CommonSensitiveSuggest> {

    private static final long serialVersionUID = 1L;

	private String id;
	@TableField("suggest_user_id")
	private String suggestUserId;
	@TableField("suggest_user_name")
	private String suggestUserName;
    /**
     * 建议删除还是新增，0删除，1新增
     */
	@TableField("del_or_save")
	private Integer delOrSave;
    /**
     * 词值，多个用，号隔开
     */
	private String value;
    /**
     * 审核人id，默认0，没人审核
     */
	@TableField("audit_user_id")
	private String auditUserId;
    /**
     * 建议理由
     */
	private String reason;
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;
	@TableField("create_user_id")
	private String createUserId;
	@TableField("update_user_id")
	private String updateUserId;
	@TableField(fill = FieldFill.INSERT)
	private Integer deleted;


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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "CommonSensitiveSuggest{" +
			"id=" + id +
			", suggestUserId=" + suggestUserId +
			", suggestUserName=" + suggestUserName +
			", delOrSave=" + delOrSave +
			", value=" + value +
			", auditUserId=" + auditUserId +
			", reason=" + reason +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", createUserId=" + createUserId +
			", updateUserId=" + updateUserId +
			", deleted=" + deleted +
			"}";
	}
}
