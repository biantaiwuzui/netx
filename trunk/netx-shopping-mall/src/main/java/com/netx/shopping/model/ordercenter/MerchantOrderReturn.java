package com.netx.shopping.model.ordercenter;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 退货
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-18
 */
@TableName("merchant_order_return")
public class MerchantOrderReturn extends Model<MerchantOrderReturn> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 下单者id
     */
	@TableField("user_id")
	private String userId;
    /**
     * 商家用户id
     */
	@TableField("merchant_user_id")
	private String merchantUserId;
    /**
     * 订单id
     */
	@TableField("order_id")
	private String orderId;
    /**
     * 申请时间
     */
	@TableField("apply_time")
	private Date applyTime;
    /**
     * 退货地址
     */
	private String address;
    /**
     * 同意时间
     */
	@TableField("agree_time")
	private Date agreeTime;
    /**
     * 仲裁结果描述
     */
	@TableField("arbitration_description")
	private String arbitrationDescription;
	@TableField("is_settled")
	private Boolean isSettled;
    /**
     * 退货物流代号
     */
	@TableField("logistics_code")
	private String logisticsCode;
    /**
     * 退货物流单号
     */
	@TableField("logistics_no")
	private String logisticsNo;
    /**
     * 确认退货时间
     */
	@TableField("confirm_time")
	private Date confirmTime;
    /**
     * 撤销退货时间
     */
	@TableField("cancel_time")
	private Date cancelTime;
    /**
     * 退货成功时间
     */
	@TableField("success_time")
	private Date successTime;
    /**
     * 状态：
1：用户申请退货
2：商家同意退货
3：用户确认退货
4：用户撤销退货
5：双方退货成功
6：商家拒收退货
7：用户退货申诉
     */
	private String status;
    /**
     * 创建时间
     */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;
    /**
     * 删除标识
     */
	@TableField(fill = FieldFill.INSERT)
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

	public String getMerchantUserId() {
		return merchantUserId;
	}

	public void setMerchantUserId(String merchantUserId) {
		this.merchantUserId = merchantUserId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getAgreeTime() {
		return agreeTime;
	}

	public void setAgreeTime(Date agreeTime) {
		this.agreeTime = agreeTime;
	}

	public String getArbitrationDescription() {
		return arbitrationDescription;
	}

	public void setArbitrationDescription(String arbitrationDescription) {
		this.arbitrationDescription = arbitrationDescription;
	}

	public Boolean getSettled() {
		return isSettled;
	}

	public void setSettled(Boolean isSettled) {
		this.isSettled = isSettled;
	}

	public String getLogisticsCode() {
		return logisticsCode;
	}

	public void setLogisticsCode(String logisticsCode) {
		this.logisticsCode = logisticsCode;
	}

	public String getLogisticsNo() {
		return logisticsNo;
	}

	public void setLogisticsNo(String logisticsNo) {
		this.logisticsNo = logisticsNo;
	}

	public Date getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}

	public Date getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}

	public Date getSuccessTime() {
		return successTime;
	}

	public void setSuccessTime(Date successTime) {
		this.successTime = successTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
		return "MerchantOrderReturn{" +
			"id=" + id +
			", userId=" + userId +
			", merchantUserId=" + merchantUserId +
			", orderId=" + orderId +
			", applyTime=" + applyTime +
			", address=" + address +
			", agreeTime=" + agreeTime +
			", arbitrationDescription=" + arbitrationDescription +
			", isSettled=" + isSettled +
			", logisticsCode=" + logisticsCode +
			", logisticsNo=" + logisticsNo +
			", confirmTime=" + confirmTime +
			", cancelTime=" + cancelTime +
			", successTime=" + successTime +
			", status=" + status +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleted=" + deleted +
			"}";
	}
}
