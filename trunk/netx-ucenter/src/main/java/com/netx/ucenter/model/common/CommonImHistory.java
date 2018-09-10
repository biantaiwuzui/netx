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
 * @since 2018-06-12
 */
@TableName("common_im_history")
public class CommonImHistory extends Model<CommonImHistory> {

    private static final long serialVersionUID = 1L;

    /**
     * 记录id
     */
	private String id;
    /**
     * 发送者id
     */
	@TableField("from_user_id")
	private String fromUserId;
    /**
     * 接收者Id
     */
	@TableField("to_user_id")
	private String toUserId;
    /**
     * 消息
     */
	@TableField("message_payload")
	private String messagePayload;
    /**
     * 网值类型：
网能：Activity
网友：User
网商：Product
网信：Credit
     */
	private String type;
    /**
     * 事件id
     */
	@TableField("type_id")
	private String typeId;
	@TableField("push_params")
	private String pushParams;
	@TableField("doc_type")
	private String docType;
	@TableField("send_time")
	private Date sendTime;
    /**
     * 是否已读
     */
	@TableField("is_read")
	private Boolean isRead;
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;
	@TableField("create_user_id")
	private String createUserId;
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;
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

	public String getMessagePayload() {
		return messagePayload;
	}

	public void setMessagePayload(String messagePayload) {
		this.messagePayload = messagePayload;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getPushParams() {
		return pushParams;
	}

	public void setPushParams(String pushParams) {
		this.pushParams = pushParams;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Boolean getRead() {
		return isRead;
	}

	public void setRead(Boolean isRead) {
		this.isRead = isRead;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
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
		return "CommonImHistory{" +
			"id=" + id +
			", fromUserId=" + fromUserId +
			", toUserId=" + toUserId +
			", messagePayload=" + messagePayload +
			", type=" + type +
			", typeId=" + typeId +
			", pushParams=" + pushParams +
			", docType=" + docType +
			", sendTime=" + sendTime +
			", isRead=" + isRead +
			", createTime=" + createTime +
			", createUserId=" + createUserId +
			", updateTime=" + updateTime +
			", updateUserId=" + updateUserId +
			", deleted=" + deleted +
			"}";
	}
}
