package com.netx.shopping.model.merchantcenter;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 网商-商家收藏表
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-09
 */
@TableName("merchant_favorite")
public class MerchantFavorite extends Model<MerchantFavorite> {

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
     * 商家id
     */
	@TableField("merchant_id")
	private String merchantId;
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

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
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
		return "MerchantFavorite{" +
			"id=" + id +
			", userId=" + userId +
			", merchantId=" + merchantId +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleted=" + deleted +
			"}";
	}
}
