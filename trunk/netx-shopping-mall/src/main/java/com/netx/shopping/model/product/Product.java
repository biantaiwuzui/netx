package com.netx.shopping.model.product;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.enums.FieldFill;

import java.io.Serializable;

/**
 * <p>
 * 网商-商品表
 * </p>
 *
 * @author 李威
 * @since 2018-03-08
 */
public class Product extends Model<Product> {

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
     * 商品名称
     */
	private String name;

	/**
	 * 商品描述
	 */
	private String characteristic;
    /**
     * 供应商家
     */
	@TableField("seller_id")
	private String sellerId;
    /**
     * 商品详情
     */
	private String description;
    /**
     * 商品图片列表，多个用逗号隔开
     */
	@TableField("product_images_url")
	private String productImagesUrl;
    /**
     * 商品包装明细ID，多个用逗号隔开
     */
	@TableField("package_id")
	private String packageId;
    /**
     * 计价单位
     */
	@TableField("price_unit")
	private String priceUnit;
    /**
     * 商品基础价格
     */
	private Long price;
    /**
     * 是否配送

     */
	@TableField("is_delivery")
	private Boolean isDelivery;
    /**
     * 是否退换

     */
	@TableField("is_return")
	private Boolean isReturn;
    /**
     * 配送方式
1：支持第三方配送
2：不提供配送，仅限现场消费
3：同时提供外卖配送或现场消费
     */
	@TableField("delivery_way")
	private Integer deliveryWay;
    /**
     * 商品规格ID，多个用逗号隔开
     */
	@TableField("spec_id")
	private String specId;
    /**
     * 商品详情图片列表
     */
	@TableField("detail_images_url")
	private String detailImagesUrl;
    /**
     * 上架原因
     */
	@TableField("up_reason")
	private String upReason;
    /**
     * 下架原因
     */
	@TableField("down_reason")
	private String downReason;
    /**
     * 商品状态
            1：上架
            2：下架
     */
	private Integer status;
    /**
     * 商品上下架操作者者
     */
	@TableField("handlers_id")
	private String handlersId;
	/**
	 * 成交量
	 */
	private Long volume;
    /**
     * 访问量
     */
	@TableField("visit_num")
	private Integer visitNum;
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

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProductImagesUrl() {
		return productImagesUrl;
	}

	public void setProductImagesUrl(String productImagesUrl) {
		this.productImagesUrl = productImagesUrl;
	}

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getPriceUnit() {
		return priceUnit;
	}

	public void setPriceUnit(String priceUnit) {
		this.priceUnit = priceUnit;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
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

	public String getSpecId() {
		return specId;
	}

	public void setSpecId(String specId) {
		this.specId = specId;
	}

	public String getDetailImagesUrl() {
		return detailImagesUrl;
	}

	public void setDetailImagesUrl(String detailImagesUrl) {
		this.detailImagesUrl = detailImagesUrl;
	}

	public String getUpReason() {
		return upReason;
	}

	public void setUpReason(String upReason) {
		this.upReason = upReason;
	}

	public String getDownReason() {
		return downReason;
	}

	public void setDownReason(String downReason) {
		this.downReason = downReason;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getHandlersId() {
		return handlersId;
	}

	public void setHandlersId(String handlersId) {
		this.handlersId = handlersId;
	}

	public Long getVolume() {
		return volume;
	}

	public void setVolume(Long volume) {
		this.volume = volume;
	}

	public Integer getVisitNum() {
		return visitNum;
	}

	public void setVisitNum(Integer visitNum) {
		this.visitNum = visitNum;
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
		return "Product{" +
				"id='" + id + '\'' +
				", userId='" + userId + '\'' +
				", name='" + name + '\'' +
				", characteristic='" + characteristic + '\'' +
				", sellerId='" + sellerId + '\'' +
				", description='" + description + '\'' +
				", productImagesUrl='" + productImagesUrl + '\'' +
				", packageId='" + packageId + '\'' +
				", priceUnit='" + priceUnit + '\'' +
				", price=" + price +
				", isDelivery=" + isDelivery +
				", isReturn=" + isReturn +
				", deliveryWay=" + deliveryWay +
				", specId='" + specId + '\'' +
				", detailImagesUrl='" + detailImagesUrl + '\'' +
				", upReason='" + upReason + '\'' +
				", downReason='" + downReason + '\'' +
				", status=" + status +
				", handlersId='" + handlersId + '\'' +
				", volume=" + volume +
				", visitNum=" + visitNum +
				", createTime=" + createTime +
				", createUserId='" + createUserId + '\'' +
				", updateTime=" + updateTime +
				", updateUserId='" + updateUserId + '\'' +
				", deleted=" + deleted +
				'}';
	}
}
