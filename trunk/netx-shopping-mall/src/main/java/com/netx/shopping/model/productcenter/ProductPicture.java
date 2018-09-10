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
 * 
 * </p>
 *
 * @author CHENQIAN
 * @since 2018-05-04
 */
@TableName("product_picture")
public class ProductPicture extends Model<ProductPicture> {

    private static final long serialVersionUID = 1L;

	private String id;
	@TableField("product_id")
	private String productId;
	@TableField("picture_url")
	private String pictureUrl;
	/**
	 * 排序字段，数字越小排的越前
	 */
	private Integer priority;
	@TableField("product_picture_type")
	private String productPictureType;
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

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getProductPictureType() {
		return productPictureType;
	}

	public void setProductPictureType(String productPictureType) {
		this.productPictureType = productPictureType;
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
		return "ProductPicture{"
				+ "id='" + id + '\''
				+ ", productId='" + productId + '\''
				+ ", pictureUrl='" + pictureUrl + '\''
				+ ", priority=" + priority
				+ ", productPictureType='" + productPictureType + '\''
				+ ", createTime=" + createTime
				+ ", updateTime=" + updateTime
				+ ", deleted=" + deleted
				+ '}';
	}
}
