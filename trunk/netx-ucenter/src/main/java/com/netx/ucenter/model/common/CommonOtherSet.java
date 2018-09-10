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
@TableName("common_other_set")
public class CommonOtherSet extends Model<CommonOtherSet> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	private String id;
    /**
     * 是否审核 状态 1.已审核 0.等待审核
     */
	@TableField("can_use")
	private Integer canUse;
    /**
     * 审核用户id
     */
	@TableField("dispose_user_id")
	private String disposeUserId;
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
     * 需求活动技能限制类别，0人员名单，1限制条件
     */
	@TableField("skill_limit_type")
	private Integer skillLimitType;
    /**
     * 需求活动技能限制分数
     */
	@TableField("skill_limit_point")
	private Integer skillLimitPoint;
    /**
     * 需求活动技能限制允许用户id,String，逗号分隔
     */
	@TableField("skill_limit_user_Ids")
	private String skillLimitUserIds;
    /**
     * 需求活动技能限制条件,0,信用值不低与skillLimitPoint,1:通过手机验证,2:通过身份验证,3通过视频验证,String类型,用逗号分隔
     */
	@TableField("skill_limit_condition")
	private String skillLimitCondition;
    /**
     * 图文音视限制类别，0人员名单，1限制条件
     */
	@TableField("pic_limit_type")
	private Integer picLimitType;
    /**
     * 图文音视限制分数
     */
	@TableField("pic_limit_point")
	private Integer picLimitPoint;
    /**
     * 图文音视限制允许用户id,String，逗号分隔
     */
	@TableField("pic_limit_user_Ids")
	private String picLimitUserIds;
    /**
     * 图文音视限制条件,0,信用值不低与LimitPoint,1:通过手机验证,2:通过身份验证,3通过视频验证,String类型,用逗号分隔
     */
	@TableField("pic_limit_condition")
	private String picLimitCondition;
    /**
     * 心愿限制类别，0人员名单，1限制条件
     */
	@TableField("wish_limit_type")
	private Integer wishLimitType;
    /**
     * 心愿限制分数
     */
	@TableField("wish_limit_point")
	private Integer wishLimitPoint;
    /**
     * 心愿限制允许用户id,String，逗号分隔
     */
	@TableField("wish_limit_user_Ids")
	private String wishLimitUserIds;
    /**
     * 心愿限制条件,0,信用值不低与LimitPoint,1:通过手机验证,2:通过身份验证,3通过视频验证,4:推荐者与发布者同等资格，String类型,用逗号分隔
     */
	@TableField("wish_limit_condition")
	private String wishLimitCondition;
    /**
     * 注册商家限制类别，0人员名单，1限制条件
     */
	@TableField("reg_merchant_limit_type")
	private Integer regMerchantLimitType;
    /**
     * 注册商家限制分数
     */
	@TableField("reg_merchant_limit_point")
	private Integer regMerchantLimitPoint;
    /**
     * 注册商家制允许用户id,String，逗号分隔
     */
	@TableField("reg_merchant_limit_user_Ids")
	private String regMerchantLimitUserIds;
    /**
     * 注册商家条件,0,信用值不低与LimitPoint,1:通过手机验证,2:通过身份验证,3通过视频验证,String类型,用逗号分隔
     */
	@TableField("reg_merchant_limit_condition")
	private String regMerchantLimitCondition;
    /**
     * 发行网币限制类别，0人员名单，1限制条件
     */
	@TableField("credit_limit_type")
	private Integer creditLimitType;
    /**
     * 发行网币限制分数
     */
	@TableField("credit_limit_Point")
	private Integer creditLimitPoint;
    /**
     * 发行网币允许用户id,String，逗号分隔
     */
	@TableField("credit_limit_user_ids")
	private String creditLimitUserIds;
    /**
     * 发行网币条件,0,信用值不低与LimitPoint,1:通过手机验证,2:通过身份验证,3通过视频验证4:上月业绩不低于currencylimitMoney,5:上月发行网币余额小于currencyLimitTotal,6,达到了上期网币发型时的业绩增长承诺,String类型,用逗号分隔
     */
	@TableField("credit_limit_condition")
	private String creditLimitCondition;
    /**
     * 发行网币上月业绩不低于
     */
	@TableField("credit_limit_moreThen")
	private Integer creditLimitMoreThen;
    /**
     * 发行网币余额小于
     */
	@TableField("credit_limit_balance")
	private Integer creditLimitBalance;
    /**
     * 允许接受礼物,邀请及被分享的限制类别，0人员名单，1限制条件
     */
	@TableField("share_limit_type")
	private Integer shareLimitType;
    /**
     * 允许接受礼物,邀请及被分享的限制分数
     */
	@TableField("share_limit_point")
	private Integer shareLimitPoint;
    /**
     * 允许接受礼物,邀请及被分享的限制条件,0,信用值不低与LimitPoint,1:通过手机验证,2:通过身份验证,3通过视频验证,String类型,用逗号分隔
     */
	@TableField("share_limit_condition")
	private String shareLimitCondition;
    /**
     * 备用1
     */
	private String bak1;
    /**
     * 备用2
     */
	private String bak2;
    /**
     * 备用3
     */
	private String bak3;
    /**
     * 备用4
     */
	private String bak4;
    /**
     * 备用5
     */
	private String bak5;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getCanUse() {
		return canUse;
	}

	public void setCanUse(Integer canUse) {
		this.canUse = canUse;
	}

	public String getDisposeUserId() {
		return disposeUserId;
	}

	public void setDisposeUserId(String disposeUserId) {
		this.disposeUserId = disposeUserId;
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

	public Integer getSkillLimitType() {
		return skillLimitType;
	}

	public void setSkillLimitType(Integer skillLimitType) {
		this.skillLimitType = skillLimitType;
	}

	public Integer getSkillLimitPoint() {
		return skillLimitPoint;
	}

	public void setSkillLimitPoint(Integer skillLimitPoint) {
		this.skillLimitPoint = skillLimitPoint;
	}

	public String getSkillLimitUserIds() {
		return skillLimitUserIds;
	}

	public void setSkillLimitUserIds(String skillLimitUserIds) {
		this.skillLimitUserIds = skillLimitUserIds;
	}

	public String getSkillLimitCondition() {
		return skillLimitCondition;
	}

	public void setSkillLimitCondition(String skillLimitCondition) {
		this.skillLimitCondition = skillLimitCondition;
	}

	public Integer getPicLimitType() {
		return picLimitType;
	}

	public void setPicLimitType(Integer picLimitType) {
		this.picLimitType = picLimitType;
	}

	public Integer getPicLimitPoint() {
		return picLimitPoint;
	}

	public void setPicLimitPoint(Integer picLimitPoint) {
		this.picLimitPoint = picLimitPoint;
	}

	public String getPicLimitUserIds() {
		return picLimitUserIds;
	}

	public void setPicLimitUserIds(String picLimitUserIds) {
		this.picLimitUserIds = picLimitUserIds;
	}

	public String getPicLimitCondition() {
		return picLimitCondition;
	}

	public void setPicLimitCondition(String picLimitCondition) {
		this.picLimitCondition = picLimitCondition;
	}

	public Integer getWishLimitType() {
		return wishLimitType;
	}

	public void setWishLimitType(Integer wishLimitType) {
		this.wishLimitType = wishLimitType;
	}

	public Integer getWishLimitPoint() {
		return wishLimitPoint;
	}

	public void setWishLimitPoint(Integer wishLimitPoint) {
		this.wishLimitPoint = wishLimitPoint;
	}

	public String getWishLimitUserIds() {
		return wishLimitUserIds;
	}

	public void setWishLimitUserIds(String wishLimitUserIds) {
		this.wishLimitUserIds = wishLimitUserIds;
	}

	public String getWishLimitCondition() {
		return wishLimitCondition;
	}

	public void setWishLimitCondition(String wishLimitCondition) {
		this.wishLimitCondition = wishLimitCondition;
	}

	public Integer getRegMerchantLimitType() {
		return regMerchantLimitType;
	}

	public void setRegMerchantLimitType(Integer regMerchantLimitType) {
		this.regMerchantLimitType = regMerchantLimitType;
	}

	public Integer getRegMerchantLimitPoint() {
		return regMerchantLimitPoint;
	}

	public void setRegMerchantLimitPoint(Integer regMerchantLimitPoint) {
		this.regMerchantLimitPoint = regMerchantLimitPoint;
	}

	public String getRegMerchantLimitUserIds() {
		return regMerchantLimitUserIds;
	}

	public void setRegMerchantLimitUserIds(String regMerchantLimitUserIds) {
		this.regMerchantLimitUserIds = regMerchantLimitUserIds;
	}

	public String getRegMerchantLimitCondition() {
		return regMerchantLimitCondition;
	}

	public void setRegMerchantLimitCondition(String regMerchantLimitCondition) {
		this.regMerchantLimitCondition = regMerchantLimitCondition;
	}

	public Integer getCreditLimitType() {
		return creditLimitType;
	}

	public void setCreditLimitType(Integer creditLimitType) {
		this.creditLimitType = creditLimitType;
	}

	public Integer getCreditLimitPoint() {
		return creditLimitPoint;
	}

	public void setCreditLimitPoint(Integer creditLimitPoint) {
		this.creditLimitPoint = creditLimitPoint;
	}

	public String getCreditLimitUserIds() {
		return creditLimitUserIds;
	}

	public void setCreditLimitUserIds(String creditLimitUserIds) {
		this.creditLimitUserIds = creditLimitUserIds;
	}

	public String getCreditLimitCondition() {
		return creditLimitCondition;
	}

	public void setCreditLimitCondition(String creditLimitCondition) {
		this.creditLimitCondition = creditLimitCondition;
	}

	public Integer getCreditLimitMoreThen() {
		return creditLimitMoreThen;
	}

	public void setCreditLimitMoreThen(Integer creditLimitMoreThen) {
		this.creditLimitMoreThen = creditLimitMoreThen;
	}

	public Integer getCreditLimitBalance() {
		return creditLimitBalance;
	}

	public void setCreditLimitBalance(Integer creditLimitBalance) {
		this.creditLimitBalance = creditLimitBalance;
	}

	public Integer getShareLimitType() {
		return shareLimitType;
	}

	public void setShareLimitType(Integer shareLimitType) {
		this.shareLimitType = shareLimitType;
	}

	public Integer getShareLimitPoint() {
		return shareLimitPoint;
	}

	public void setShareLimitPoint(Integer shareLimitPoint) {
		this.shareLimitPoint = shareLimitPoint;
	}

	public String getShareLimitCondition() {
		return shareLimitCondition;
	}

	public void setShareLimitCondition(String shareLimitCondition) {
		this.shareLimitCondition = shareLimitCondition;
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "CommonOtherSet{" +
			"id=" + id +
			", canUse=" + canUse +
			", disposeUserId=" + disposeUserId +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", createUserId=" + createUserId +
			", updateUserId=" + updateUserId +
			", deleted=" + deleted +
			", skillLimitType=" + skillLimitType +
			", skillLimitPoint=" + skillLimitPoint +
			", skillLimitUserIds=" + skillLimitUserIds +
			", skillLimitCondition=" + skillLimitCondition +
			", picLimitType=" + picLimitType +
			", picLimitPoint=" + picLimitPoint +
			", picLimitUserIds=" + picLimitUserIds +
			", picLimitCondition=" + picLimitCondition +
			", wishLimitType=" + wishLimitType +
			", wishLimitPoint=" + wishLimitPoint +
			", wishLimitUserIds=" + wishLimitUserIds +
			", wishLimitCondition=" + wishLimitCondition +
			", regMerchantLimitType=" + regMerchantLimitType +
			", regMerchantLimitPoint=" + regMerchantLimitPoint +
			", regMerchantLimitUserIds=" + regMerchantLimitUserIds +
			", regMerchantLimitCondition=" + regMerchantLimitCondition +
			", creditLimitType=" + creditLimitType +
			", creditLimitPoint=" + creditLimitPoint +
			", creditLimitUserIds=" + creditLimitUserIds +
			", creditLimitCondition=" + creditLimitCondition +
			", creditLimitMoreThen=" + creditLimitMoreThen +
			", creditLimitBalance=" + creditLimitBalance +
			", shareLimitType=" + shareLimitType +
			", shareLimitPoint=" + shareLimitPoint +
			", shareLimitCondition=" + shareLimitCondition +
			", bak1=" + bak1 +
			", bak2=" + bak2 +
			", bak3=" + bak3 +
			", bak4=" + bak4 +
			", bak5=" + bak5 +
			"}";
	}
}
