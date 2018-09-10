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
 * 退款表
 * </p>
 *
 * @author lcx
 * @since 2018-03-08
 */
public class Refund extends Model<Refund> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 申请人
     */
	@TableField("user_id")
	private String userId;
    /**
     * 描述
     */
	private String description;
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
     * 退款金额
     */
	private Long amount;
    /**
     * 状态：
            0：已申请退款
            1：已同意同款
            2：已拒绝退款
            3：超期自动同意退款
     */
	private Integer status;
    /**
     * 通过或拒绝人ID
     */
	@TableField("operate_user_id")
	private String operateUserId;
    /**
     * 申请退款时支付的费用：
            即选择：解冻部分费用并退回给我，其余部分支付给Ta
     */
	private Long bail;
    /**
     * 支付方式：0：网币 1：现金
     */
	@TableField("pay_way")
	private Integer payWay;
    /**
     * 过期时间，过期后将自动退款
     */
	@TableField("expired_at")
	private Date expiredAt;
    /**
     * 定时任务是否已处理
     */
	@TableField("is_process")
	private Boolean isProcess;
    /**
     * 处理时间
     */
	@TableField("process_at")
	private Date processAt;

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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getOperateUserId() {
		return operateUserId;
	}

	public void setOperateUserId(String operateUserId) {
		this.operateUserId = operateUserId;
	}

	public Long getBail() {
		return bail;
	}

	public void setBail(Long bail) {
		this.bail = bail;
	}

	public Integer getPayWay() {
		return payWay;
	}

	public void setPayWay(Integer payWay) {
		this.payWay = payWay;
	}

	public Date getExpiredAt() {
		return expiredAt;
	}

	public void setExpiredAt(Date expiredAt) {
		this.expiredAt = expiredAt;
	}

	public Boolean getProcess() {
		return isProcess;
	}

	public void setProcess(Boolean isProcess) {
		this.isProcess = isProcess;
	}

	public Date getProcessAt() {
		return processAt;
	}

	public void setProcessAt(Date processAt) {
		this.processAt = processAt;
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
		return "Refund{" +
			"id=" + id +
			", userId=" + userId +
			", description=" + description +
			", relatableType=" + relatableType +
			", relatableId=" + relatableId +
			", amount=" + amount +
			", status=" + status +
			", operateUserId=" + operateUserId +
			", bail=" + bail +
			", payWay=" + payWay +
			", expiredAt=" + expiredAt +
			", isProcess=" + isProcess +
			", processAt=" + processAt +
			", createTime=" + createTime +
			", createUserId=" + createUserId +
			", updateTime=" + updateTime +
			", updateUserId=" + updateUserId +
			", deleted=" + deleted +
			"}";
	}
}
