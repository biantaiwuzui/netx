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
 * 商品中心-类目属性映射
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-09
 */
@TableName("category_property")
public class CategoryProperty extends Model<CategoryProperty> {

    private static final long serialVersionUID = 1L;

	private String id;
	@TableField("category_id")
	private String categoryId;
	@TableField("merchant_id")
	private String merchantId;
	@TableField("property_id")
	private String propertyId;
    /**
     * 1销售属性,2关键属性
     */
	@TableField("property_type")
	private Integer propertyType;
    /**
     * 是否为多值属性
     */
	@TableField("multi_value")
	private Integer multiValue;
    /**
     * 是否被搜索用于在前台显示,0不显示,1显示
     */
	private Integer search;
    /**
     * 排序字段，数字越小排的越前
     */
	private Integer priority;
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

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public Integer getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(Integer propertyType) {
		this.propertyType = propertyType;
	}

	public Integer getMultiValue() {
		return multiValue;
	}

	public void setMultiValue(Integer multiValue) {
		this.multiValue = multiValue;
	}

	public Integer getSearch() {
		return search;
	}

	public void setSearch(Integer search) {
		this.search = search;
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
		return "CategoryProperty{" +
			"id=" + id +
			", categoryId=" + categoryId +
			", merchantId=" + merchantId +
			", propertyId=" + propertyId +
			", propertyType=" + propertyType +
			", multiValue=" + multiValue +
			", search=" + search +
			", priority=" + priority +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleted=" + deleted +
			"}";
	}
}
