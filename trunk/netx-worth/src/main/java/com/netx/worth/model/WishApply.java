package com.netx.worth.model;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotations.TableField;
import java.util.Date;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.annotations.TableLogic;


/**
 * <p>
 * 心愿使用表
 * </p>
 *
 * @author 黎子安
 * @since 2018-07-26
 */
@TableName("wish_apply")
public class WishApply extends Model<WishApply> {

	private static final long serialVersionUID = 1L;

	private String id;
	@TableField("user_id")
	private String userId;
	/**
	 * 主表ID
	 */
	@TableField("wish_id")
	private String wishId;
	/**
	 * 银行信息表id
	 */
	@TableField("bank_id")
	private String bankId;
	/**
	 * 申请金额
	 */
	private Long amount;
	/**
	 * 心愿余额
	 */
	private Long balance;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 失败原因
	 */
	private String reason;
	/**
	 * 凭据
	 */
	private String pic;
	/**
	 * 使用类型：
	 0：提现。
	 1：给平台网友。
	 2：给第三方。
	 */
	@TableField("apply_type")
	private Integer applyType;
	/**
	 * 使用类型：
	 0：待交易。
	 1：交易中。
	 2：交易成功。
	 3：交易失败。
	 */
	@TableField("trading_type")
	private Integer tradingType;
	/**
	 * 使用信息，如本平台的就填网号，如其他，以json形式填
	 */
	@TableField("apply_info")
	private String applyInfo;
	/**
	 * 是否通过状态：
	 0：未通过
	 1：已通过
	 2：待通过
	 */
	@TableField("is_pass")
	private Integer isPass;
	/**
	 * 监管者数量
	 */
	@TableField("manager_count")
	private Integer managerCount;
	/**
	 * 已处理过的管理员数量，不管拒绝还是同意，都算是已处理
	 */
	@TableField("opreate_manager_count")
	private Integer opreateManagerCount;
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;
	@TableField("create_user_id")
	private String createUserId;
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;
	@TableField("update_user_id")
	private String updateUserId;
	@TableField(fill = FieldFill.INSERT)
	@TableLogic
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

	public String getWishId() {
		return wishId;
	}

	public void setWishId(String wishId) {
		this.wishId = wishId;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Long getBalance() {
		return balance;
	}

	public void setBalance(Long balance) {
		this.balance = balance;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public Integer getApplyType() {
		return applyType;
	}

	public void setApplyType(Integer applyType) {
		this.applyType = applyType;
	}

	public Integer getTradingType() {
		return tradingType;
	}

	public void setTradingType(Integer tradingType) {
		this.tradingType = tradingType;
	}

	public String getApplyInfo() {
		return applyInfo;
	}

	public void setApplyInfo(String applyInfo) {
		this.applyInfo = applyInfo;
	}

	public Integer getIsPass() {
		return isPass;
	}

	public void setIsPass(Integer isPass) {
		this.isPass = isPass;
	}

	public Integer getManagerCount() {
		return managerCount;
	}

	public void setManagerCount(Integer managerCount) {
		this.managerCount = managerCount;
	}

	public Integer getOpreateManagerCount() {
		return opreateManagerCount;
	}

	public void setOpreateManagerCount(Integer opreateManagerCount) {
		this.opreateManagerCount = opreateManagerCount;
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "WishApply{" +
				"id=" + id +
				", userId=" + userId +
				", wishId=" + wishId +
				", bankId=" + bankId +
				", amount=" + amount +
				", balance=" + balance +
				", description=" + description +
				", reason=" + reason +
				", pic=" + pic +
				", applyType=" + applyType +
				", tradingType=" + tradingType +
				", applyInfo=" + applyInfo +
				", isPass=" + isPass +
				", managerCount=" + managerCount +
				", opreateManagerCount=" + opreateManagerCount +
				", createTime=" + createTime +
				", createUserId=" + createUserId +
				", updateTime=" + updateTime +
				", updateUserId=" + updateUserId +
				", deleted=" + deleted +
				"}";
	}
}

