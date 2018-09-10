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
 * 网商-添加客服代理请求表
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-22
 */
@TableName("merchant_add_customeragent")
public class MerchantAddCustomeragent extends Model<MerchantAddCustomeragent> {

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
     * 被添加的商家id
     */
	@TableField("to_merchant_id")
	private String toMerchantId;
    /**
     * 状态：
            0：等待同意
            1：同意请求
            2：不同意请求
     */
	private Integer state;
    /**
     * 添加理由
     */
	private String reason;
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

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
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
		return "MerchantAddCustomeragent{" +
			"id=" + id +
			", merchantId=" + merchantId +
			", toMerchantId=" + toMerchantId +
			", state=" + state +
			", reason=" + reason +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleted=" + deleted +
			"}";
	}
}
