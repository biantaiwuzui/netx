package com.netx.shopping.model.productcenter;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 网商-商品表
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-11
 */
@TableName("product")
public class Product extends Model<Product> {

    private static final long serialVersionUID = 1L;

    /**
     * 标识ID
     */
	private String id;
    /**
     * 供应商家
     */
	@TableField("merchant_id")
	private String merchantId;
    /**
     * 商品名称
     */
	private String name;
    /**
     * 商品描述
     */
	private String characteristic;
    /**
     * 商品描述
     */
	private String description;
    /**
     * 是否配送
     */
	@TableField("is_delivery")
	private Boolean isDelivery;
    /**
     * 是否支持退换

     */
	@TableField("is_return")
	private Boolean isReturn;

	/**
	 * 配送方式
	 1：支持配送
	 2：不提供配送，仅限现场消费
	 3：提供外卖配送
	 */
	@TableField("delivery_way")
	private Integer deliveryWay;
    /**
     * 商品状态
            1：上架
            2：下架
     */
	@TableField("online_status")
	private Integer onlineStatus;
    /**
     * 访问量
     */
	@TableField("visit_count")
	private Integer visitCount;

	@TableField("shipping_fee_id")
	private String shippingFeeId;

	@TableField("shipping_fee")
	private Long shippingFee;

	/**
	 * 发布者userId
	 */
	@TableField("publisher_user_id")
	private String publisherUserId;

    /**
     * 创建时间
     */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;
    /**
     * 更新时间
     */
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

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCharacteristic() {
		return characteristic;
	}

	public void setCharacteristic(String characteristic) {
		this.characteristic = characteristic;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getDelivery() {
		return isDelivery;
	}

	public void setDelivery(Boolean isDelivery) {
		this.isDelivery = isDelivery;
	}

	public Boolean getReturn() {
		return isReturn;
	}

	public void setReturn(Boolean isReturn) {
		this.isReturn = isReturn;
	}

	public Integer getDeliveryWay() {
		return deliveryWay;
	}

	public void setDeliveryWay(Integer deliveryWay) {
		this.deliveryWay = deliveryWay;
	}

	public Integer getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(Integer onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	public Integer getVisitCount() {
		return visitCount;
	}

	public void setVisitCount(Integer visitCount) {
		this.visitCount = visitCount;
	}

	public String getShippingFeeId() {
		return shippingFeeId;
	}

	public void setShippingFeeId(String shippingFeeId) {
		this.shippingFeeId = shippingFeeId;
	}

	public Long getShippingFee() {
		return shippingFee;
	}

	public void setShippingFee(Long shippingFee) {
		this.shippingFee = shippingFee;
	}

	public String getPublisherUserId() {
		return publisherUserId;
	}

	public void setPublisherUserId(String publisherUserId) {
		this.publisherUserId = publisherUserId;
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


}
