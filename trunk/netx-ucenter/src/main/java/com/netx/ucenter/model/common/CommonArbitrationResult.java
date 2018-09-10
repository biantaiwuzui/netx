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
@TableName("common_arbitration_result")
public class CommonArbitrationResult extends Model<CommonArbitrationResult> {

    private static final long serialVersionUID = 1L;

	private String id;
	@TableField("from_user_credit_point")
	private BigDecimal fromUserCreditPoint;
    /**
     * 扣除投诉人信用值得理由
     */
	@TableField("from_user_credit_point_reason")
	private String fromUserCreditPointReason;
	@TableField("to_user_credit_point")
	private BigDecimal toUserCreditPoint;
    /**
     * 扣除被投诉人的信用值理由
     */
	@TableField("to_user_credit_point_reason")
	private String toUserCreditPointReason;
    /**
     *  同意退款
     */
	@TableField("refund_radio_button")
	private Integer refundRadioButton;
    /**
     * 不同意退款
     */
	@TableField("refund_arbitrate_reason")
	private Integer refundArbitrateReason;
    /**
     * 同意退货
     */
	@TableField("return_radio_button")
	private Integer returnRadioButton;
    /**
     * 同意退货的理由
     */
	@TableField("return_arbitrate_reason")
	private String returnArbitrateReason;
    /**
     * 扣除发行者冻结的金额：就是说退款:0.否      1.是
     */
	@TableField("substract_release_frozen_money_refund")
	private Integer substractReleaseFrozenMoneyRefund;
    /**
     * 选中扣除发行者冻结的金额的原因
     */
	@TableField("substract_release_frozen_money_reason")
	private String substractReleaseFrozenMoneyReason;
    /**
     * 通用仲裁管理员仲裁的答案:0-----不同意,1-----同意
     */
	@TableField("op_common_answer")
	private Integer opCommonAnswer;
    /**
     * 通用仲裁管理员同意的辩解
     */
	@TableField("op_common_reason")
	private String opCommonReason;
    /**
     * 如果是拒绝受理,这就是理由
     */
	private String descriptions;
    /**
     * 仲裁的状态:1.等待申诉,2.受理中,3.对方已申诉,等待处理,4.已裁决,拒绝受理
     */
	@TableField("status_code")
	private Integer statusCode;
    /**
     * 扣除发行者冻结的金额：就是说退款:0.否      1.是
     */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;
    /**
     * 操作者的ID
     */
	@TableField("op_user_id")
	private String opUserId;
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

	public BigDecimal getFromUserCreditPoint() {
		return fromUserCreditPoint;
	}

	public void setFromUserCreditPoint(BigDecimal fromUserCreditPoint) {
		this.fromUserCreditPoint = fromUserCreditPoint;
	}

	public String getFromUserCreditPointReason() {
		return fromUserCreditPointReason;
	}

	public void setFromUserCreditPointReason(String fromUserCreditPointReason) {
		this.fromUserCreditPointReason = fromUserCreditPointReason;
	}

	public BigDecimal getToUserCreditPoint() {
		return toUserCreditPoint;
	}

	public void setToUserCreditPoint(BigDecimal toUserCreditPoint) {
		this.toUserCreditPoint = toUserCreditPoint;
	}

	public String getToUserCreditPointReason() {
		return toUserCreditPointReason;
	}

	public void setToUserCreditPointReason(String toUserCreditPointReason) {
		this.toUserCreditPointReason = toUserCreditPointReason;
	}

	public Integer getRefundRadioButton() {
		return refundRadioButton;
	}

	public void setRefundRadioButton(Integer refundRadioButton) {
		this.refundRadioButton = refundRadioButton;
	}

	public Integer getRefundArbitrateReason() {
		return refundArbitrateReason;
	}

	public void setRefundArbitrateReason(Integer refundArbitrateReason) {
		this.refundArbitrateReason = refundArbitrateReason;
	}

	public Integer getReturnRadioButton() {
		return returnRadioButton;
	}

	public void setReturnRadioButton(Integer returnRadioButton) {
		this.returnRadioButton = returnRadioButton;
	}

	public String getReturnArbitrateReason() {
		return returnArbitrateReason;
	}

	public void setReturnArbitrateReason(String returnArbitrateReason) {
		this.returnArbitrateReason = returnArbitrateReason;
	}

	public Integer getSubstractReleaseFrozenMoneyRefund() {
		return substractReleaseFrozenMoneyRefund;
	}

	public void setSubstractReleaseFrozenMoneyRefund(Integer substractReleaseFrozenMoneyRefund) {
		this.substractReleaseFrozenMoneyRefund = substractReleaseFrozenMoneyRefund;
	}

	public String getSubstractReleaseFrozenMoneyReason() {
		return substractReleaseFrozenMoneyReason;
	}

	public void setSubstractReleaseFrozenMoneyReason(String substractReleaseFrozenMoneyReason) {
		this.substractReleaseFrozenMoneyReason = substractReleaseFrozenMoneyReason;
	}

	public Integer getOpCommonAnswer() {
		return opCommonAnswer;
	}

	public void setOpCommonAnswer(Integer opCommonAnswer) {
		this.opCommonAnswer = opCommonAnswer;
	}

	public String getOpCommonReason() {
		return opCommonReason;
	}

	public void setOpCommonReason(String opCommonReason) {
		this.opCommonReason = opCommonReason;
	}

	public String getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getOpUserId() {
		return opUserId;
	}

	public void setOpUserId(String opUserId) {
		this.opUserId = opUserId;
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
		return "CommonArbitrationResult{" +
			"id=" + id +
			", fromUserCreditPoint=" + fromUserCreditPoint +
			", fromUserCreditPointReason=" + fromUserCreditPointReason +
			", toUserCreditPoint=" + toUserCreditPoint +
			", toUserCreditPointReason=" + toUserCreditPointReason +
			", refundRadioButton=" + refundRadioButton +
			", refundArbitrateReason=" + refundArbitrateReason +
			", returnRadioButton=" + returnRadioButton +
			", returnArbitrateReason=" + returnArbitrateReason +
			", substractReleaseFrozenMoneyRefund=" + substractReleaseFrozenMoneyRefund +
			", substractReleaseFrozenMoneyReason=" + substractReleaseFrozenMoneyReason +
			", opCommonAnswer=" + opCommonAnswer +
			", opCommonReason=" + opCommonReason +
			", descriptions=" + descriptions +
			", statusCode=" + statusCode +
			", createTime=" + createTime +
			", opUserId=" + opUserId +
			", updateTime=" + updateTime +
			", createUserId=" + createUserId +
			", updateUserId=" + updateUserId +
			", deleted=" + deleted +
			"}";
	}
}
