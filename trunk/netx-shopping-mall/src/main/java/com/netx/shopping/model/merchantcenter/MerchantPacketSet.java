package com.netx.shopping.model.merchantcenter;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 网商-红包设置表
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-09
 */
@TableName("merchant_packet_set")
public class MerchantPacketSet extends Model<MerchantPacketSet> {

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
     * 是否变动提成
     */
	@TableField("is_change_rate")
	private Boolean isChangeRate;
    /**
     * 首单提成比例
     */
	@TableField("first_rate")
	private BigDecimal firstRate;
    /**
     * 逐单提成比例
     */
	@TableField("gradual_rate")
	private BigDecimal gradualRate;
    /**
     * 封顶提成比例
     */
	@TableField("limit_rate")
	private BigDecimal limitRate;
    /**
     * 是否固定提成
     */
	@TableField("is_fixed_rate")
	private Boolean isFixedRate;
    /**
     * 固定提成比例
     */
	@TableField("fixed_rate")
	private BigDecimal fixedRate;
    /**
     * 是否启动红包金额
     */
	@TableField("is_start_packet")
	private Boolean isStartPacket;
    /**
     * 红包金额
     */
	@TableField("packet_money")
	private Long packetMoney;
    /**
     * 商家id
     */
	@TableField("merchant_id")
	private String merchantId;
    /**
     * 红包发放时间
     */
	@TableField("send_time")
	private Date sendTime;
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

	public Boolean getChangeRate() {
		return isChangeRate;
	}

	public void setChangeRate(Boolean isChangeRate) {
		this.isChangeRate = isChangeRate;
	}

	public BigDecimal getFirstRate() {
		return firstRate;
	}

	public void setFirstRate(BigDecimal firstRate) {
		this.firstRate = firstRate;
	}

	public BigDecimal getGradualRate() {
		return gradualRate;
	}

	public void setGradualRate(BigDecimal gradualRate) {
		this.gradualRate = gradualRate;
	}

	public BigDecimal getLimitRate() {
		return limitRate;
	}

	public void setLimitRate(BigDecimal limitRate) {
		this.limitRate = limitRate;
	}

	public Boolean getIsFixedRate() {
		return isFixedRate;
	}

	public void setIsFixedRate(Boolean isFixedRate) {
		this.isFixedRate = isFixedRate;
	}

	public BigDecimal getFixedRate() {
		return fixedRate;
	}

	public void setFixedRate(BigDecimal fixedRate) {
		this.fixedRate = fixedRate;
	}

	public Boolean getStartPacket() {
		return isStartPacket;
	}

	public void setStartPacket(Boolean isStartPacket) {
		this.isStartPacket = isStartPacket;
	}

	public Long getPacketMoney() {
		return packetMoney;
	}

	public void setPacketMoney(Long packetMoney) {
		this.packetMoney = packetMoney;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
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
		return "MerchantPacketSet{" +
			"id=" + id +
			", userId=" + userId +
			", isChangeRate=" + isChangeRate +
			", firstRate=" + firstRate +
			", gradualRate=" + gradualRate +
			", limitRate=" + limitRate +
			", isFixedRate=" + isFixedRate +
			", fixedRate=" + fixedRate +
			", isStartPacket=" + isStartPacket +
			", packetMoney=" + packetMoney +
			", merchantId=" + merchantId +
			", sendTime=" + sendTime +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleted=" + deleted +
			"}";
	}
}
