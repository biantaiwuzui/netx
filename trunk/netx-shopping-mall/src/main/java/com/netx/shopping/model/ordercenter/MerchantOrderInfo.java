package com.netx.shopping.model.ordercenter;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-14
 */
@TableName("merchant_order_info")
public class MerchantOrderInfo extends Model<MerchantOrderInfo> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 下单者id
     */
	@TableField("user_id")
	private String userId;
    /**
     * 下单者名字
     */
	@TableField("user_name")
	private String userName;
    /**
     * 商家id
     */
	@TableField("merchant_id")
	private String merchantId;
    /**
     * 订单号
     */
	@TableField("order_no")
	private String orderNo;
    /**
     * 订单类型：
1：normal_order
2：demand_order
3：activity_order
     */
	@TableField("order_type")
	private String orderType;
    /**
     * 业务id
     */
	@TableField("order_type_business_id")
	private String orderTypeBusinessId;
    /**
     * 订单总费用
     */
	@TableField("order_total_fee")
	private Long orderTotalFee;
    /**
     * 商品总费用
     */
	@TableField("product_total_fee")
	private Long productTotalFee;
    /**
     * 下单时间
     */
	@TableField("order_time")
	private Date orderTime;
	@TableField("finish_time")
	private Date finishTime;
	@TableField("verify_time")
	private Date verifyTime;
    /**
     * 订单状态
     */
	@TableField("order_status")
	private String orderStatus;
	@TableField("pay_code")
	private String payCode;
	@TableField("pay_submit_time")
	private Date paySubmitTime;
	@TableField("pay_receive_time")
	private Date payReceiveTime;
	
	@ApiModelProperty(value = "订单支付状态")
	@TableField("pay_status")
	private String payStatus;
	
	@TableField("pay_out_no")
	private String payOutNo;
	@TableField("shipping_fee")
	private Long shippingFee;
	@TableField("shipping_code")
	private String shippingCode;
	@TableField("shipping_logistics_no")
	private String shippingLogisticsNo;
	@TableField("shipping_time")
	private Date shippingTime;
	@TableField("shipping_status")
	private String shippingStatus;
    /**
     * 物流详情
     */
	@TableField("shipping_logistics_details")
	private String shippingLogisticsDetails;
    /**
     * 配送方式
1：支持配送
2：不提供配送，仅限现场消费
3：提供外卖配送
     */
	@TableField("delivery_way")
	private Integer deliveryWay;
	private String remark;
    /**
     * 收货人
     */
	private String consignee;
	@TableField("full_address")
	private String fullAddress;
	@TableField("zip_code")
	private String zipCode;
	private String mobile;
    /**
     * 取消原因
     */
	@TableField("cancel_reason")
	private String cancelReason;
    /**
     * 取消时间
     */
	@TableField("cancel_time")
	private Date cancelTime;
	@TableField("is_comment")
	private Integer isComment;
    /**
     * 上一次催单时间
     */
	@TableField("remind_time")
	private Date remindTime;
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderTypeBusinessId() {
		return orderTypeBusinessId;
	}

	public void setOrderTypeBusinessId(String orderTypeBusinessId) {
		this.orderTypeBusinessId = orderTypeBusinessId;
	}

	public Long getOrderTotalFee() {
		return orderTotalFee;
	}

	public void setOrderTotalFee(Long orderTotalFee) {
		this.orderTotalFee = orderTotalFee;
	}

	public Long getProductTotalFee() {
		return productTotalFee;
	}

	public void setProductTotalFee(Long productTotalFee) {
		this.productTotalFee = productTotalFee;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public Date getVerifyTime() {
		return verifyTime;
	}

	public void setVerifyTime(Date verifyTime) {
		this.verifyTime = verifyTime;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public Date getPaySubmitTime() {
		return paySubmitTime;
	}

	public void setPaySubmitTime(Date paySubmitTime) {
		this.paySubmitTime = paySubmitTime;
	}

	public Date getPayReceiveTime() {
		return payReceiveTime;
	}

	public void setPayReceiveTime(Date payReceiveTime) {
		this.payReceiveTime = payReceiveTime;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getPayOutNo() {
		return payOutNo;
	}

	public void setPayOutNo(String payOutNo) {
		this.payOutNo = payOutNo;
	}

	public Long getShippingFee() {
		return shippingFee;
	}

	public void setShippingFee(Long shippingFee) {
		this.shippingFee = shippingFee;
	}

	public String getShippingCode() {
		return shippingCode;
	}

	public void setShippingCode(String shippingCode) {
		this.shippingCode = shippingCode;
	}

	public String getShippingLogisticsNo() {
		return shippingLogisticsNo;
	}

	public void setShippingLogisticsNo(String shippingLogisticsNo) {
		this.shippingLogisticsNo = shippingLogisticsNo;
	}

	public Date getShippingTime() {
		return shippingTime;
	}

	public void setShippingTime(Date shippingTime) {
		this.shippingTime = shippingTime;
	}

	public String getShippingStatus() {
		return shippingStatus;
	}

	public void setShippingStatus(String shippingStatus) {
		this.shippingStatus = shippingStatus;
	}

	public String getShippingLogisticsDetails() {
		return shippingLogisticsDetails;
	}

	public void setShippingLogisticsDetails(String shippingLogisticsDetails) {
		this.shippingLogisticsDetails = shippingLogisticsDetails;
	}

	public Integer getDeliveryWay() {
		return deliveryWay;
	}

	public void setDeliveryWay(Integer deliveryWay) {
		this.deliveryWay = deliveryWay;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getFullAddress() {
		return fullAddress;
	}

	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	public Date getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}

	public Integer getIsComment() {
		return isComment;
	}

	public void setIsComment(Integer isComment) {
		this.isComment = isComment;
	}

	public Date getRemindTime() {
		return remindTime;
	}

	public void setRemindTime(Date remindTime) {
		this.remindTime = remindTime;
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
		return "MerchantOrderInfo{" +
			"id=" + id +
			", userId=" + userId +
			", userName=" + userName +
			", merchantId=" + merchantId +
			", orderNo=" + orderNo +
			", orderType=" + orderType +
			", orderTypeBusinessId=" + orderTypeBusinessId +
			", orderTotalFee=" + orderTotalFee +
			", productTotalFee=" + productTotalFee +
			", orderTime=" + orderTime +
			", finishTime=" + finishTime +
			", verifyTime=" + verifyTime +
			", orderStatus=" + orderStatus +
			", payCode=" + payCode +
			", paySubmitTime=" + paySubmitTime +
			", payReceiveTime=" + payReceiveTime +
			", payStatus=" + payStatus +
			", payOutNo=" + payOutNo +
			", shippingFee=" + shippingFee +
			", shippingCode=" + shippingCode +
			", shippingLogisticsNo=" + shippingLogisticsNo +
			", shippingTime=" + shippingTime +
			", shippingStatus=" + shippingStatus +
			", shippingLogisticsDetails=" + shippingLogisticsDetails +
			", deliveryWay=" + deliveryWay +
			", remark=" + remark +
			", consignee=" + consignee +
			", fullAddress=" + fullAddress +
			", zipCode=" + zipCode +
			", mobile=" + mobile +
			", cancelReason=" + cancelReason +
			", cancelTime=" + cancelTime +
			", isComment=" + isComment +
			", remindTime=" + remindTime +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleted=" + deleted +
			"}";
	}
}
