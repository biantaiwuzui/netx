package com.netx.shopping.model.business;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;

import java.io.Serializable;

/**
 * <p>
 * 网商-红包设置表
 * </p>
 *
 * @author 李威
 * @since 2018-03-08
 */
@TableName("seller_packet_set")
public class SellerPacketSet extends Model<SellerPacketSet> {

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
	private Boolean ChangeRate;
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
	private Boolean startPacket;
    /**
     * 红包金额
     */
	@TableField("packet_money")
	private Long packetMoney;
    /**
     * 红包发放时间
     */
	@TableField("send_time")
	private Date sendTime;
	/**
	 * 商家id
	 */
	@TableField("seller_id")
    private String sellerId;
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

	public Boolean getChangeRate() {
		return ChangeRate;
	}

	public void setChangeRate(Boolean changeRate) {
		ChangeRate = changeRate;
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
	public BigDecimal getFixedRate(){
		return fixedRate;
	}

	public void setFixedRate(BigDecimal fixedRate) {
		this.fixedRate = fixedRate;
	}

	public void setIsFixedRate(Boolean fixedRate) {
		isFixedRate = fixedRate;
	}

	public Boolean getStartPacket() {
		return startPacket;
	}

	public void setStartPacket(Boolean startPacket) {
		this.startPacket = startPacket;
	}

	public Long getPacketMoney() {
		return packetMoney;
	}

	public void setPacketMoney(Long packetMoney) {
		this.packetMoney = packetMoney;
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

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "SellerPacketSet{" +
				"id='" + id + '\'' +
				", userId='" + userId + '\'' +
				", ChangeRate=" + ChangeRate +
				", firstRate=" + firstRate +
				", gradualRate=" + gradualRate +
				", limitRate=" + limitRate +
				", isFixedRate=" + isFixedRate +
				", fixedRate=" + fixedRate +
				", startPacket=" + startPacket +
				", packetMoney=" + packetMoney +
				", sendTime=" + sendTime +
				", sellerId='" + sellerId + '\'' +
				", createTime=" + createTime +
				", createUserId='" + createUserId + '\'' +
				", updateTime=" + updateTime +
				", updateUserId='" + updateUserId + '\'' +
				", deleted=" + deleted +
				'}';
	}
}
