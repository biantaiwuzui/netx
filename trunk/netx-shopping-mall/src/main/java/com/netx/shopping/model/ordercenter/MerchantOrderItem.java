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
 * 
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-09
 */
@TableName("merchant_order_item")
public class MerchantOrderItem extends Model<MerchantOrderItem> {

    private static final long serialVersionUID = 1L;

	private String id;
	@TableField("order_no")
	private String orderNo;
	@TableField("merchant_id")
	private String merchantId;
	@TableField("user_id")
	private String userId;
	@TableField("sku_id")
	private String skuId;
	@TableField("sku_desc")
	private String skuDesc;
	@TableField("product_name")
	private String productName;
	@TableField("product_img_url")
	private String productImgUrl;
	@TableField("unit_price")
	private Long unitPrice;
	@TableField("final_unit_price")
	private Long finalUnitPrice;
	@TableField("product_id")
	private String productId;
	@TableField("order_status")
	private String orderStatus;
	@TableField("pay_status")
	private String payStatus;
	@TableField("shipping_status")
	private String shippingStatus;
	@TableField("trade_status")
	private String tradeStatus;
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

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getSkuDesc() {
		return skuDesc;
	}

	public void setSkuDesc(String skuDesc) {
		this.skuDesc = skuDesc;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductImgUrl() {
		return productImgUrl;
	}

	public void setProductImgUrl(String productImgUrl) {
		this.productImgUrl = productImgUrl;
	}

	public Long getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Long unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Long getFinalUnitPrice() {
		return finalUnitPrice;
	}

	public void setFinalUnitPrice(Long finalUnitPrice) {
		this.finalUnitPrice = finalUnitPrice;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getShippingStatus() {
		return shippingStatus;
	}

	public void setShippingStatus(String shippingStatus) {
		this.shippingStatus = shippingStatus;
	}

	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
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
		return "MerchantOrderItem{" +
			"id=" + id +
			", orderNo=" + orderNo +
			", merchantId=" + merchantId +
			", userId=" + userId +
			", skuId=" + skuId +
			", skuDesc=" + skuDesc +
			", productName=" + productName +
			", productImgUrl=" + productImgUrl +
			", unitPrice=" + unitPrice +
			", finalUnitPrice=" + finalUnitPrice +
			", productId=" + productId +
			", orderStatus=" + orderStatus +
			", payStatus=" + payStatus +
			", shippingStatus=" + shippingStatus +
			", tradeStatus=" + tradeStatus +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleted=" + deleted +
			"}";
	}
}
