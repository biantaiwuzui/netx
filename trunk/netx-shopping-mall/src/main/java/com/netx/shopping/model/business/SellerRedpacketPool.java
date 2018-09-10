package com.netx.shopping.model.business;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;

import java.io.Serializable;

/**
 * <p>
 * 网商-红包池
 * </p>
 *
 * @author 李威
 * @since 2018-03-08
 */
@TableName("redpacket_pool")
public class SellerRedpacketPool extends Model<SellerRedpacketPool> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 今日红包金额
     */
	@TableField("redpacket_amount")
	private Long redpacketAmount;
    /**
     * 总额
     */
	@TableField("total_amount")
	private Long totalAmount;
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
	private Integer delTag;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getRedpacketAmount() {
		return redpacketAmount;
	}

	public void setRedpacketAmount(Long redpacketAmount) {
		this.redpacketAmount = redpacketAmount;
	}

	public Long getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Long totalAmount) {
		this.totalAmount = totalAmount;
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

	public Integer getDelTag() {
		return delTag;
	}

	public void setDelTag(Integer delTag) {
		this.delTag = delTag;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "SellerRedpacketPool{" +
			"id=" + id +
			", redpacketAmount=" + redpacketAmount +
			", totalAmount=" + totalAmount +
			", createTime=" + createTime +
			", createUserId=" + createUserId +
			", updateTime=" + updateTime +
			", updateUserId=" + updateUserId +
			", delTag=" + delTag +
			"}";
	}
}
