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
 * 商品中心-商品属性图与别名
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-09
 */
@TableName("product_property_value_detail")
public class ProductPropertyValueDetail extends Model<ProductPropertyValueDetail> {

    private static final long serialVersionUID = 1L;

	private String id;
	@TableField("product_id")
	private String productId;
	@TableField("property_id")
	private String propertyId;
	@TableField("value_id")
	private String valueId;
	@TableField("property_value")
	private Long propertyValue;
    /**
     * 1销售属性,2关键属性
     */
	@TableField("property_type")
	private Integer propertyType;
	@TableField("picture_id")
	private String pictureId;
	@TableField("big_picture_id")
	private String bigPictureId;
	private String color;
	private String alias;
    /**
     * 排序字段，数字越小排的越前
     */
	private Integer priority;
    /**
     * 记录新增的时间
     */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;
    /**
     * 记录最近修改的时间，如果为新增后未改动则保持与createTime一致
     */
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

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public String getValueId() {
		return valueId;
	}

	public void setValueId(String valueId) {
		this.valueId = valueId;
	}

	public Long getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(Long propertyValue) {
		this.propertyValue = propertyValue;
	}

	public Integer getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(Integer propertyType) {
		this.propertyType = propertyType;
	}

	public String getPictureId() {
		return pictureId;
	}

	public void setPictureId(String pictureId) {
		this.pictureId = pictureId;
	}

	public String getBigPictureId() {
		return bigPictureId;
	}

	public void setBigPictureId(String bigPictureId) {
		this.bigPictureId = bigPictureId;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
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
		return "ProductPropertyValueDetail{" +
			"id=" + id +
			", productId=" + productId +
			", propertyId=" + propertyId +
			", valueId=" + valueId +
			", propertyValue=" + propertyValue +
			", propertyType=" + propertyType +
			", pictureId=" + pictureId +
			", bigPictureId=" + bigPictureId +
			", color=" + color +
			", alias=" + alias +
			", priority=" + priority +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleted=" + deleted +
			"}";
	}
}
