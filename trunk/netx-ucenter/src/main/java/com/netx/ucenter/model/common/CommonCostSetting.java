package com.netx.ucenter.model.common;

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
 * 
 * </p>
 *
 * @author 黎子安
 * @since 2018-03-09
 */
@TableName("common_cost_setting")
public class CommonCostSetting extends Model<CommonCostSetting> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 分成
     */
	@TableField("shared_fee")
	private BigDecimal sharedFee;
    /**
     * 提现手续费
     */
	@TableField("withdraw_fee")
	private BigDecimal withdrawFee;
    /**
     * 注册商家管理费
     */
	@TableField("shop_manager_fee")
	private BigDecimal shopManagerFee;
    /**
     * 注册商家管理费有效期/0:终身有效，1一年，3三年
     */
	@TableField("shop_manager_fee_limit_date")
	private Integer shopManagerFeeLimitDate;
	/**
	 * 商品一级类目收费  元/个
	 */
	@TableField("shop_category_fee")
	private BigDecimal shopCategoryFee;
	/**
	 * 商家二级类目收费  元/个
	 */
	@TableField("shop_tags_fee")
	private BigDecimal shopTagsFee;
    /**
     * 网币发行费
     */
	@TableField("credit_issue_fee")
	private BigDecimal creditIssueFee;
    /**
     * 网币竞购系数
     */
	@TableField("credit_funds_interest")
	private BigDecimal creditFundsInterest;
    /**
     * 网币报名认购费用
     */
	@TableField("credit_subscribe_fee")
	private BigDecimal creditSubscribeFee;
    /**
     * 网币资金利息
     */
	@TableField("credit_inst")
	private BigDecimal creditInst;
    /**
     * 图文、音视的发布押金
     */
	@TableField("pic_and_voice_publish_deposit")
	private BigDecimal picAndVoicePublishDeposit;
    /**
     * 点击费用
     */
	@TableField("click_fee")
	private BigDecimal clickFee;
    /**
     *  违规图文、音视的点击费用
     */
	@TableField("violation_click_fee")
	private BigDecimal violationClickFee;
    /**
     * 心愿资金管理费
     */
	@TableField("wish_capital_manage_fee")
	private BigDecimal wishCapitalManageFee;
    /**
     * 销售收入分成
     */
	@TableField("saler_shared_fee")
	private BigDecimal salerSharedFee;
    /**
     * 审核状态,0false,1true
     */
	private Integer state;
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
    /**
     * 审核人
     */
	@TableField("dispose_user")
	private String disposeUser;
    /**
     * 审核时间
     */
	@TableField("dispose_time")
	private Date disposeTime;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getSharedFee() {
		return sharedFee;
	}

	public void setSharedFee(BigDecimal sharedFee) {
		this.sharedFee = sharedFee;
	}

	public BigDecimal getWithdrawFee() {
		return withdrawFee;
	}

	public void setWithdrawFee(BigDecimal withdrawFee) {
		this.withdrawFee = withdrawFee;
	}

	public BigDecimal getShopManagerFee() {
		return shopManagerFee;
	}

	public void setShopManagerFee(BigDecimal shopManagerFee) {
		this.shopManagerFee = shopManagerFee;
	}

	public Integer getShopManagerFeeLimitDate() {
		return shopManagerFeeLimitDate;
	}

	public void setShopManagerFeeLimitDate(Integer shopManagerFeeLimitDate) {
		this.shopManagerFeeLimitDate = shopManagerFeeLimitDate;
	}

	public BigDecimal getShopCategoryFee() {
		return shopCategoryFee;
	}

	public void setShopCategoryFee(BigDecimal shopCategoryFee) {
		this.shopCategoryFee = shopCategoryFee;
	}

	public BigDecimal getShopTagsFee() {
		return shopTagsFee;
	}

	public void setShopTagsFee(BigDecimal shopTagsFee) {
		this.shopTagsFee = shopTagsFee;
	}

	public BigDecimal getCreditIssueFee() {
		return creditIssueFee;
	}

	public void setCreditIssueFee(BigDecimal creditIssueFee) {
		this.creditIssueFee = creditIssueFee;
	}

	public BigDecimal getCreditFundsInterest() {
		return creditFundsInterest;
	}

	public void setCreditFundsInterest(BigDecimal creditFundsInterest) {
		this.creditFundsInterest = creditFundsInterest;
	}

	public BigDecimal getCreditSubscribeFee() {
		return creditSubscribeFee;
	}

	public void setCreditSubscribeFee(BigDecimal creditSubscribeFee) {
		this.creditSubscribeFee = creditSubscribeFee;
	}

	public BigDecimal getCreditInst() {
		return creditInst;
	}

	public void setCreditInst(BigDecimal creditInst) {
		this.creditInst = creditInst;
	}

	public BigDecimal getPicAndVoicePublishDeposit() {
		return picAndVoicePublishDeposit;
	}

	public void setPicAndVoicePublishDeposit(BigDecimal picAndVoicePublishDeposit) {
		this.picAndVoicePublishDeposit = picAndVoicePublishDeposit;
	}

	public BigDecimal getClickFee() {
		return clickFee;
	}

	public void setClickFee(BigDecimal clickFee) {
		this.clickFee = clickFee;
	}

	public BigDecimal getViolationClickFee() {
		return violationClickFee;
	}

	public void setViolationClickFee(BigDecimal violationClickFee) {
		this.violationClickFee = violationClickFee;
	}

	public BigDecimal getWishCapitalManageFee() {
		return wishCapitalManageFee;
	}

	public void setWishCapitalManageFee(BigDecimal wishCapitalManageFee) {
		this.wishCapitalManageFee = wishCapitalManageFee;
	}

	public BigDecimal getSalerSharedFee() {
		return salerSharedFee;
	}

	public void setSalerSharedFee(BigDecimal salerSharedFee) {
		this.salerSharedFee = salerSharedFee;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
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

	public String getDisposeUser() {
		return disposeUser;
	}

	public void setDisposeUser(String disposeUser) {
		this.disposeUser = disposeUser;
	}

	public Date getDisposeTime() {
		return disposeTime;
	}

	public void setDisposeTime(Date disposeTime) {
		this.disposeTime = disposeTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "CommonCostSetting{" +
				"id='" + id + '\'' +
				", sharedFee=" + sharedFee +
				", withdrawFee=" + withdrawFee +
				", shopManagerFee=" + shopManagerFee +
				", shopManagerFeeLimitDate=" + shopManagerFeeLimitDate +
				", shopCategoryFee=" + shopCategoryFee +
				", shopTagsFee=" + shopTagsFee +
				", creditIssueFee=" + creditIssueFee +
				", creditFundsInterest=" + creditFundsInterest +
				", creditSubscribeFee=" + creditSubscribeFee +
				", creditInst=" + creditInst +
				", picAndVoicePublishDeposit=" + picAndVoicePublishDeposit +
				", clickFee=" + clickFee +
				", violationClickFee=" + violationClickFee +
				", wishCapitalManageFee=" + wishCapitalManageFee +
				", salerSharedFee=" + salerSharedFee +
				", state=" + state +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				", createUserId='" + createUserId + '\'' +
				", updateUserId='" + updateUserId + '\'' +
				", deleted=" + deleted +
				", disposeUser='" + disposeUser + '\'' +
				", disposeTime=" + disposeTime +
				'}';
	}
}
