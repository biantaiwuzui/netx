package com.netx.shopping.model.marketingcenter;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 网商-提成记录表
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-22
 */
@TableName("merchant_recording_history")
public class MerchantRecordingHistory extends Model<MerchantRecordingHistory> {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
	private String id;
    /**
     * 商家id
     */
	@TableField("merchant_id")
	private String merchantId;
    /**
     * 提成金额
     */
	@TableField("to_merchant_id")
	private String toMerchantId;
    /**
     * 提成类型：
            0：直接提成
            1：间接提成
     */
	private Integer type;
    /**
     * 金额
     */
	private Long money;
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

	public String getToMerchantId() {
		return toMerchantId;
	}

	public void setToMerchantId(String toMerchantId) {
		this.toMerchantId = toMerchantId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getMoney() {
		return money;
	}

	public void setMoney(Long money) {
		this.money = money;
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
		return "MerchantRecordingHistory{" +
			"id=" + id +
			", merchantId=" + merchantId +
			", toMerchantId=" + toMerchantId +
			", type=" + type +
			", money=" + money +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleted=" + deleted +
			"}";
	}
}
