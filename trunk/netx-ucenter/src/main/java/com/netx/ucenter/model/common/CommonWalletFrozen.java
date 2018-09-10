package com.netx.ucenter.model.common;

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
 * @author 黎子安
 * @since 2018-03-09
 */
@TableName("common_wallet_frozen")
public class CommonWalletFrozen extends Model<CommonWalletFrozen> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	private String id;
    /**
     * 消费渠道活动,需求,心愿,技能,商品,网币,用类名表示
     */
	@TableField("frozen_type")
	private String frozenType;
    /**
     * 冻结金额(以分为单位)
     */
	private Long amount;
    /**
     * 用户id
     */
	@TableField("user_id")
	private String userId;
    /**
     * 描述
     */
	private String description;
    /**
     * 事件id,即参加活动，需求等条件的id
     */
	@TableField("type_id")
	private String typeId;
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;
	@TableField("create_user_id")
	private String createUserId;
	@TableField("update_user_id")
	private String updateUserId;
    /**
     * 是否生效
     */
	@TableField(fill = FieldFill.INSERT)
	private Integer deleted;
    /**
     * 是否已抵扣,0未抵扣，1已抵扣
     */
	@TableField("has_consume")
	private Integer hasConsume;
	private String bak1;
	private String bak2;
	private String bak3;
	private String bak4;
	private String bak5;
    /**
     * 乐观锁
     */
	private Integer vsn;
    /**
     * 交易对象id，即钱包id
     */
	@TableField("to_user_id")
	private String toUserId;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFrozenType() {
		return frozenType;
	}

	public void setFrozenType(String frozenType) {
		this.frozenType = frozenType;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
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

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
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

	public Integer getHasConsume() {
		return hasConsume;
	}

	public void setHasConsume(Integer hasConsume) {
		this.hasConsume = hasConsume;
	}

	public String getBak1() {
		return bak1;
	}

	public void setBak1(String bak1) {
		this.bak1 = bak1;
	}

	public String getBak2() {
		return bak2;
	}

	public void setBak2(String bak2) {
		this.bak2 = bak2;
	}

	public String getBak3() {
		return bak3;
	}

	public void setBak3(String bak3) {
		this.bak3 = bak3;
	}

	public String getBak4() {
		return bak4;
	}

	public void setBak4(String bak4) {
		this.bak4 = bak4;
	}

	public String getBak5() {
		return bak5;
	}

	public void setBak5(String bak5) {
		this.bak5 = bak5;
	}

	public Integer getVsn() {
		return vsn;
	}

	public void setVsn(Integer vsn) {
		this.vsn = vsn;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "CommonWalletFrozen{" +
			"id=" + id +
			", frozenType=" + frozenType +
			", amount=" + amount +
			", userId=" + userId +
			", description=" + description +
			", typeId=" + typeId +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", createUserId=" + createUserId +
			", updateUserId=" + updateUserId +
			", deleted=" + deleted +
			", hasConsume=" + hasConsume +
			", bak1=" + bak1 +
			", bak2=" + bak2 +
			", bak3=" + bak3 +
			", bak4=" + bak4 +
			", bak5=" + bak5 +
			", vsn=" + vsn +
			", toUserId=" + toUserId +
			"}";
	}
}
