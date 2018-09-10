package com.netx.shopping.model.order;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;

import java.io.Serializable;

/**
 * <p>
 * 网商-商品退货表
 * </p>
 *
 * @author 李威
 * @since 2018-03-08
 */
@TableName("product_return")
public class ProductReturn extends Model<ProductReturn> {

    private static final long serialVersionUID = 1L;

    /**
     * 标识id
     */
	private String id;
    /**
     * 用户id
     */
	@TableField("user_id")
	private String userId;
    /**
     * 商家用户id
     */
	@TableField("seller_userId")
	private String sellerUserId;
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
     * 退货物流名称
     */
	@TableField("logistics_name")
	private String logisticsName;
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
     * 双方退货成功时间
     */
	@TableField("success_time")
	private Date successTime;
    /**
     * 状态
            1：用户申请退货
            2：商家同意退货
            3：用户确认退货
            4：用户撤销退货
            5：双方退货成功
            6：商家拒收退货
            7：用户退货申诉
     */
	private Integer status;
    /**
     * 创建时间
     */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;
    /**
     * 创建人
     */
	@TableField("create_user_id")
	private String createUserId;
    /**
     * 更新时间
     */
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;
    /**
     * 更新人
     */
	@TableField("update_user_id")
	private String updateUserId;
    /**
     * 删除标识
     */
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

	public String getSellerUserId() {
		return sellerUserId;
	}

	public void setSellerUserId(String sellerUserId) {
		this.sellerUserId = sellerUserId;
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

	public String getLogisticsName() {
		return logisticsName;
	}

	public void setLogisticsName(String logisticsName) {
		this.logisticsName = logisticsName;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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
		return "ProductReturn{" +
			"id=" + id +
			", userId=" + userId +
			", sellerUserId=" + sellerUserId +
			", orderId=" + orderId +
			", applyTime=" + applyTime +
			", address=" + address +
			", agreeTime=" + agreeTime +
			", arbitrationDescription=" + arbitrationDescription +
			", isSettled=" + isSettled +
			", logisticsName=" + logisticsName +
			", logisticsNo=" + logisticsNo +
			", confirmTime=" + confirmTime +
			", cancelTime=" + cancelTime +
			", successTime=" + successTime +
			", status=" + status +
			", createTime=" + createTime +
			", createUserId=" + createUserId +
			", updateTime=" + updateTime +
			", updateUserId=" + updateUserId +
			", deleted=" + deleted +
			"}";
	}
}
