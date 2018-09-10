package com.netx.worth.model;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotations.TableField;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.Version;
import java.io.Serializable;

/**
 * <p>
 * 网值结算表
 * </p>
 *
 * @author lcx
 * @since 2018-03-08
 */
public class Settlement extends Model<Settlement> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 关联类型，值为Model名
     */
	@TableField("relatable_type")
	private String relatableType;
    /**
     * 关联ID
     */
	@TableField("relatable_id")
	private String relatableId;
    /**
     * 描述
     */
	private String description;
    /**
     * 是否能结算，是指该项是否能够进入结算流程
     */
	@TableField("is_can")
	private Boolean isCan;
    /**
     * 过期时间，过了这个时间就会结算
     */
	@TableField("expired_at")
	private Date expiredAt;
    /**
     * 是否完成结算
     */
	@TableField("is_finish")
	private Boolean isFinish;
    /**
     * 结算时间
     */
	@TableField("finish_at")
	private Date finishAt;
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;
	@TableField("create_user_id")
	private String createUserId;
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;
	@TableField("update_user_id")
	private String updateUserId;
	@TableField(fill = FieldFill.INSERT)
    @TableLogic
	private Integer deleted;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRelatableType() {
		return relatableType;
	}

	public void setRelatableType(String relatableType) {
		this.relatableType = relatableType;
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

	public Boolean getCan() {
		return isCan;
	}

	public void setCan(Boolean isCan) {
		this.isCan = isCan;
	}

	public Date getExpiredAt() {
		return expiredAt;
	}

	public void setExpiredAt(Date expiredAt) {
		this.expiredAt = expiredAt;
	}

	public Boolean getFinish() {
		return isFinish;
	}

	public void setFinish(Boolean isFinish) {
		this.isFinish = isFinish;
	}

	public Date getFinishAt() {
		return finishAt;
	}

	public void setFinishAt(Date finishAt) {
		this.finishAt = finishAt;
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
		return "Settlement{" +
			"id=" + id +
			", relatableType=" + relatableType +
			", relatableId=" + relatableId +
			", description=" + description +
			", isCan=" + isCan +
			", expiredAt=" + expiredAt +
			", isFinish=" + isFinish +
			", finishAt=" + finishAt +
			", createTime=" + createTime +
			", createUserId=" + createUserId +
			", updateTime=" + updateTime +
			", updateUserId=" + updateUserId +
			", deleted=" + deleted +
			"}";
	}
}
