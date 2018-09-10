package com.netx.ucenter.model.common;

import java.io.Serializable;

import java.math.BigDecimal;
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
@TableName("common_examine_finance")
public class CommonExamineFinance extends Model<CommonExamineFinance> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	private String id;
    /**
     * 银行利息
     */
	private BigDecimal interest;
    /**
     * 核算差额
     */
	@TableField("check_difference")
	private BigDecimal checkDifference;
    /**
     * 调整账户原因
     */
	@TableField("adjust_account_reason")
	private String adjustAccountReason;
    /**
     * 提现金额
     */
	@TableField("extract_money")
	private BigDecimal extractMoney;
    /**
     * 提现金额原因
     */
	@TableField("extract_money_reason")
	private String extractMoneyReason;
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
    /**
     * 审核状态 1审核通过 2不通过 3 提交审核
     */
	private Integer status;
    /**
     * 审核理由
     */
	private String reason;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getInterest() {
		return interest;
	}

	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	public BigDecimal getCheckDifference() {
		return checkDifference;
	}

	public void setCheckDifference(BigDecimal checkDifference) {
		this.checkDifference = checkDifference;
	}

	public String getAdjustAccountReason() {
		return adjustAccountReason;
	}

	public void setAdjustAccountReason(String adjustAccountReason) {
		this.adjustAccountReason = adjustAccountReason;
	}

	public BigDecimal getExtractMoney() {
		return extractMoney;
	}

	public void setExtractMoney(BigDecimal extractMoney) {
		this.extractMoney = extractMoney;
	}

	public String getExtractMoneyReason() {
		return extractMoneyReason;
	}

	public void setExtractMoneyReason(String extractMoneyReason) {
		this.extractMoneyReason = extractMoneyReason;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "CommonExamineFinance{" +
			"id=" + id +
			", interest=" + interest +
			", checkDifference=" + checkDifference +
			", adjustAccountReason=" + adjustAccountReason +
			", extractMoney=" + extractMoney +
			", extractMoneyReason=" + extractMoneyReason +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", createUserId=" + createUserId +
			", updateUserId=" + updateUserId +
			", deleted=" + deleted +
			", status=" + status +
			", reason=" + reason +
			"}";
	}
}
