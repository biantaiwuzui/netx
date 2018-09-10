package com.netx.worth.model;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotations.TableField;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.Version;
import java.io.Serializable;

/**
 * <p>
 * 结算流水表 每次结算时插入
 * </p>
 *
 * @author lcx
 * @since 2018-03-08
 */
@TableName("settlement_log")
public class SettlementLog extends Model<SettlementLog> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 结算表Id
     */
	@TableField("settlement_id")
	private String settlementId;
    /**
     * 结算信用表ID
     */
	@TableField("settlement_credit_id")
	private String settlementCreditId;
    /**
     * 结算金额表ID
     */
	@TableField("settlement_amount_id")
	private String settlementAmountId;
	@TableField("user_id")
	private String userId;
    /**
     * 信用，+-表示
     */
	private Integer credit;
    /**
     * 金额，+-表示
     */
	private Long amount;
    /**
     * 结算前总信用
     */
	@TableField("last_credit")
	private Integer lastCredit;
    /**
     * 结算前总金额
     */
	@TableField("last_amount")
	private Long lastAmount;
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

	public String getSettlementId() {
		return settlementId;
	}

	public void setSettlementId(String settlementId) {
		this.settlementId = settlementId;
	}

	public String getSettlementCreditId() {
		return settlementCreditId;
	}

	public void setSettlementCreditId(String settlementCreditId) {
		this.settlementCreditId = settlementCreditId;
	}

	public String getSettlementAmountId() {
		return settlementAmountId;
	}

	public void setSettlementAmountId(String settlementAmountId) {
		this.settlementAmountId = settlementAmountId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getCredit() {
		return credit;
	}

	public void setCredit(Integer credit) {
		this.credit = credit;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Integer getLastCredit() {
		return lastCredit;
	}

	public void setLastCredit(Integer lastCredit) {
		this.lastCredit = lastCredit;
	}

	public Long getLastAmount() {
		return lastAmount;
	}

	public void setLastAmount(Long lastAmount) {
		this.lastAmount = lastAmount;
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
		return "SettlementLog{" +
			"id=" + id +
			", settlementId=" + settlementId +
			", settlementCreditId=" + settlementCreditId +
			", settlementAmountId=" + settlementAmountId +
			", userId=" + userId +
			", credit=" + credit +
			", amount=" + amount +
			", lastCredit=" + lastCredit +
			", lastAmount=" + lastAmount +
			", createTime=" + createTime +
			", createUserId=" + createUserId +
			", updateTime=" + updateTime +
			", updateUserId=" + updateUserId +
			", deleted=" + deleted +
			"}";
	}
}
