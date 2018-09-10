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
@TableName("common_bill")
public class CommonBill extends Model<CommonBill> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	private String id;
    /**
	 * 用户id
	 */
	@TableField("user_id")
	private String userId;
	/**
	 * 钱包id
	 */
	@TableField("wallet_id")
	private String wallerId;
    /**
     * 交易方式，0微信，1支付宝，2网币，3零钱 4.红包
     */
	@TableField("pay_channel")
	private Integer payChannel;
    /**
     * 流水类型：0平台，1经营
     */
	private Integer type;
    /**
     * 交易类型，0支出，1收入
     */
	@TableField("trade_type")
	private Integer tradeType;
    /**
     * 金额
     */
	private Long amount;
	/**
	 * 余额
	 */
	@TableField("total_amount")
	private Long totalAmount;
    /**
     * 描述
     */
	private String description;
    /**
     * 第三方单号
     */
	@TableField("third_bill_id")
	private String thirdBillId;
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;
	@TableField("create_user_id")
	private String createUserId;
	@TableField("update_user_id")
	private String updateUserId;
	@TableField(fill = FieldFill.INSERT)
	private Integer deleted;
	private String bak1;
	private String bak2;
	private String bak3;
	private String bak4;
	private String bak5;
    /**
     * 第三方充值提现用:0未到账 1已到账 2表示未用到此字段
     */
	@TableField("to_account")
	private Integer toAccount;


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

	public String getWallerId() {
		return wallerId;
	}

	public void setWallerId(String wallerId) {
		this.wallerId = wallerId;
	}

	public Integer getPayChannel() {
		return payChannel;
	}

	public void setPayChannel(Integer payChannel) {
		this.payChannel = payChannel;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getTradeType() {
		return tradeType;
	}

	public void setTradeType(Integer tradeType) {
		this.tradeType = tradeType;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Long getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Long totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getThirdBillId() {
		return thirdBillId;
	}

	public void setThirdBillId(String thirdBillId) {
		this.thirdBillId = thirdBillId;
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

	public Integer getToAccount() {
		return toAccount;
	}

	public void setToAccount(Integer toAccount) {
		this.toAccount = toAccount;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "CommonBill{" +
			"id=" + id +
			", userId=" + userId +
			", payChannel=" + payChannel +
			", type=" + type +
			", tradeType=" + tradeType +
			", amount=" + amount +
			", totalAmount=" + totalAmount +
			", description=" + description +
			", thirdBillId=" + thirdBillId +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", createUserId=" + createUserId +
			", updateUserId=" + updateUserId +
			", deleted=" + deleted +
			", bak1=" + bak1 +
			", bak2=" + bak2 +
			", bak3=" + bak3 +
			", bak4=" + bak4 +
			", bak5=" + bak5 +
			", toAccount=" + toAccount +
			"}";
	}
}
