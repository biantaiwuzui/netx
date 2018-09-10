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
 * 网商-商品订单表
 * </p>
 *
 * @author 李威
 * @since 2018-03-08
 */
@TableName("product_order")
public class ProductOrder extends Model<ProductOrder> {

    private static final long serialVersionUID = 1L;

    /**
     * 标识ID
     */
	private String id;
    /**
     * 用户ID
     */
	@TableField("user_id")
	private String userId;
    /**
     * 商家ID
     */
	@TableField("seller_id")
	private String sellerId;
    /**
     * 订单编号
     */
	@TableField("order_num")
	private String orderNum;
    /**
     * 活动id 是活动里生成的订单需传活动id
     */
	@TableField("activity_id")
	private String activityId;
    /**
     * 心愿id 是心愿里生成的订单需传心愿id
     */
	@TableField("demane_id")
	private String demaneId;
    /**
     * 订单金额
     */
	@TableField("total_price")
	private Long totalPrice;
    /**
     * 支付金额
     */
	@TableField("pay_price")
	private Long payPrice;
    /**
     * 订单备注
     */
	private String remark;
    /**
     * 下单时间
     */
	@TableField("order_time")
	private Date orderTime;
    /**
     * 配送方式
1：第三方配送
2：不提供配送，现场消费
3：外卖配送
     */
	@TableField("delivery_way")
	private Integer deliveryWay;
    /**
     * 支付时间
     */
	@TableField("pay_time")
	private Date payTime;
    /**
     * 发货时间
     */
	@TableField("send_time")
	private Date sendTime;
    /**
     * 收货地址
     */
	private String address;
    /**
     * 订单状态
            1：待付款
            2：待发货
            3：物流中
            4：退货中
            5：投诉中
            6：待评论
            7：已完成
            8：已取消
            9 : 待生成
          10：已付款
          11：已服务
          12 : 延迟中
     */
	private Integer status;
    /**
     * 支付方式
    1、网币支付
    2、零钱支付
    3、平台垫付
    4、网币+零钱支付
     */
	@TableField("pay_way")
	private Integer payWay;
    /**
     * 网币id
     */
	@TableField("credit_id")
	private String creditId;
    /**
     * 网币金额
     */
	@TableField("net_credit")
	private Long netCredit;
    /**
     * 物流公司标签
     */
	@TableField("logistics_code")
	private String logisticsCode;
    /**
     * 物流单号
     */
	@TableField("logistics_num")
	private String logisticsNum;
    /**
     * 物流状态
            1：物流中
            2：已完成
     */
	@TableField("logistics_status")
	private Integer logisticsStatus;
    /**
     * 物流详情或配送详情
     */
	@TableField("logistcs_details")
	private String logistcsDetails;
    /**
     * 投诉ID
     */
	@TableField("arbitration_id")
	private String arbitrationId;
    /**
     * 是否延迟收货 0：否     1：是   2 : 定时器执行后的值 
     */
	@TableField("is_put_off_order")
	private Integer isPutOffOrder;
    /**
     * 上一次催单时间
     */
	@TableField("remind_time")
	private Date remindTime;
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

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getDemaneId() {
		return demaneId;
	}

	public void setDemaneId(String demaneId) {
		this.demaneId = demaneId;
	}

	public Long getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Long totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Long getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(Long payPrice) {
		this.payPrice = payPrice;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Integer getDeliveryWay() {
		return deliveryWay;
	}

	public void setDeliveryWay(Integer deliveryWay) {
		this.deliveryWay = deliveryWay;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getPayWay() {
		return payWay;
	}

	public void setPayWay(Integer payWay) {
		this.payWay = payWay;
	}

	public String getCreditId() {
		return creditId;
	}

	public void setCreditId(String creditId) {
		this.creditId = creditId;
	}

	public Long getNetCredit() {
		return netCredit;
	}

	public void setNetCredit(Long netCredit) {
		this.netCredit = netCredit;
	}

	public String getLogisticsCode() {
		return logisticsCode;
	}

	public void setLogisticsCode(String logisticsCode) {
		this.logisticsCode = logisticsCode;
	}

	public String getLogisticsNum() {
		return logisticsNum;
	}

	public void setLogisticsNum(String logisticsNum) {
		this.logisticsNum = logisticsNum;
	}

	public Integer getLogisticsStatus() {
		return logisticsStatus;
	}

	public void setLogisticsStatus(Integer logisticsStatus) {
		this.logisticsStatus = logisticsStatus;
	}

	public String getLogistcsDetails() {
		return logistcsDetails;
	}

	public void setLogistcsDetails(String logistcsDetails) {
		this.logistcsDetails = logistcsDetails;
	}

	public String getArbitrationId() {
		return arbitrationId;
	}

	public void setArbitrationId(String arbitrationId) {
		this.arbitrationId = arbitrationId;
	}

	public Integer getIsPutOffOrder() {
		return isPutOffOrder;
	}

	public void setIsPutOffOrder(Integer isPutOffOrder) {
		this.isPutOffOrder = isPutOffOrder;
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
		return "ProductOrder{" +
			"id=" + id +
			", userId=" + userId +
			", sellerId=" + sellerId +
			", orderNum=" + orderNum +
			", activityId=" + activityId +
			", demaneId=" + demaneId +
			", totalPrice=" + totalPrice +
			", payPrice=" + payPrice +
			", remark=" + remark +
			", orderTime=" + orderTime +
			", deliveryWay=" + deliveryWay +
			", payTime=" + payTime +
			", sendTime=" + sendTime +
			", address=" + address +
			", status=" + status +
			", payWay=" + payWay +
			", creditId=" + creditId +
			", netCredit=" + netCredit +
			", logisticsCode=" + logisticsCode +
			", logisticsNum=" + logisticsNum +
			", logisticsStatus=" + logisticsStatus +
			", logistcsDetails=" + logistcsDetails +
			", arbitrationId=" + arbitrationId +
			", isPutOffOrder=" + isPutOffOrder +
			", remindTime=" + remindTime +
			", createTime=" + createTime +
			", createUserId=" + createUserId +
			", updateTime=" + updateTime +
			", updateUserId=" + updateUserId +
			", deleted=" + deleted +
			"}";
	}
}
