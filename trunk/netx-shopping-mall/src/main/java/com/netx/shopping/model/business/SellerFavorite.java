package com.netx.shopping.model.business;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;

/**
 * <p>
 * 网商-商家收藏表
 * </p>
 *
 * @author 李威
 * @since 2018-03-08
 */
@TableName("seller_favorite")
public class SellerFavorite extends Model<SellerFavorite> {

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
	@TableField("seller_id")
	private String sellerId;
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;
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
		return "SellerFavorite{" +
			"id=" + id +
			", userId=" + userId +
			", sellerId=" + sellerId +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleted=" + deleted +
			"}";
	}
}
