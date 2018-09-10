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
 * 
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-22
 */
@TableName("merchant")
public class Merchant extends Model<Merchant> {

    private static final long serialVersionUID = 1L;

	private String id;
	@TableField("user_id")
	private String userId;
    /**
     * 客服代码
     */
	@TableField("customer_service_code")
	private String customerServiceCode;
	private String name;
    /**
     * 商家职务：
            0：客服代理
            1：商户代理
            2：市场总监
            3：营运总裁商家职务
     */
	private Integer position;
    /**
     * 父商家ID
     */
	@TableField("parent_merchant_id")
	private String parentMerchantId;
    /**
     * 引荐客服代码
     */
	@TableField("referral_service_code")
	private String referralServiceCode;
	@TableField("province_code")
	private String provinceCode;
	@TableField("city_code")
	private String cityCode;
	@TableField("area_code")
	private String areaCode;
	@TableField("addr_country")
	private String addrCountry;
	@TableField("addr_detail")
	private String addrDetail;
	@TableField("addr_door_number")
	private String addrDoorNumber;
	@TableField("addr_tel")
	private String addrTel;
    /**
     * 地址-联系人
     */
	@TableField("addr_contact")
	private String addrContact;
	private BigDecimal lon;
	private BigDecimal lat;
	private String desc;
    /**
     * 商家状态
            1：正常
            2：拉黑
     */
	private Integer status;
    /**
     * 禁用原因
     */
	@TableField("disable_reason")
	private String disableReason;
    /**
     * 启用原因
     */
	@TableField("enable_reason")
	private String enableReason;
    /**
     * 访问量
     */
	@TableField("visit_count")
	private Integer visitCount;
	private String qrcode;
    /**
     * 注册管理费缴费状态
0：已缴费
1：待缴费
2：待续费
     */
	@TableField("pay_status")
	private Integer payStatus;
    /**
     * 缴费时间
     */
	@TableField("fee_time")
	private Date feeTime;
    /**
     * 过期时间
     */
	@TableField("expire_time")
	private Date expireTime;
    /**
     * 是否支持网信
     */
	@TableField("is_support_credit")
	private Boolean isSupportCredit;
    /**
     * 红包设置
     */
	@TableField("pac_set_id")
	private String pacSetId;
    /**
     * 今日红包池金额
     */
	@TableField("today_packet_pool_amount")
	private Long todayPacketPoolAmount;
    /**
     * 昨日红包池金额
     */
	@TableField("packet_pool_amount")
	private Long packetPoolAmount;
    /**
     * 二级数量
     */
	@TableField("second_num")
	private Integer secondNum;
    /**
     * 三级数量
     */
	@TableField("third_num")
	private Integer thirdNum;
    /**
     * 日增加下级数
     */
	@TableField("day_num")
	private Integer dayNum;
    /**
     * 一个月内发展的商家数
     */
	@TableField("month_num")
	private Integer monthNum;
    /**
     * 每月累积第二级商家数
     */
	@TableField("month_second_num")
	private Integer monthSecondNum;
    /**
     * 每月累积第三级商家数
     */
	@TableField("month_third_num")
	private Integer monthThirdNum;
    /**
     * 组内第几名
     */
	@TableField("group_no")
	private Integer groupNo;
    /**
     * 月业绩
     */
	@TableField("achievement_month")
	private Integer achievementMonth;
    /**
     * 累计业绩
     */
	@TableField("achievement_total")
	private Long achievementTotal;
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;
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

	public String getCustomerServiceCode() {
		return customerServiceCode;
	}

	public void setCustomerServiceCode(String customerServiceCode) {
		this.customerServiceCode = customerServiceCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public String getParentMerchantId() {
		return parentMerchantId;
	}

	public void setParentMerchantId(String parentMerchantId) {
		this.parentMerchantId = parentMerchantId;
	}

	public String getReferralServiceCode() {
		return referralServiceCode;
	}

	public void setReferralServiceCode(String referralServiceCode) {
		this.referralServiceCode = referralServiceCode;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getAddrCountry() {
		return addrCountry;
	}

	public void setAddrCountry(String addrCountry) {
		this.addrCountry = addrCountry;
	}

	public String getAddrDetail() {
		return addrDetail;
	}

	public void setAddrDetail(String addrDetail) {
		this.addrDetail = addrDetail;
	}

	public String getAddrDoorNumber() {
		return addrDoorNumber;
	}

	public void setAddrDoorNumber(String addrDoorNumber) {
		this.addrDoorNumber = addrDoorNumber;
	}

	public String getAddrTel() {
		return addrTel;
	}

	public void setAddrTel(String addrTel) {
		this.addrTel = addrTel;
	}

	public String getAddrContact() {
		return addrContact;
	}

	public void setAddrContact(String addrContact) {
		this.addrContact = addrContact;
	}

	public BigDecimal getLon() {
		return lon;
	}

	public void setLon(BigDecimal lon) {
		this.lon = lon;
	}

	public BigDecimal getLat() {
		return lat;
	}

	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getDisableReason() {
		return disableReason;
	}

	public void setDisableReason(String disableReason) {
		this.disableReason = disableReason;
	}

	public String getEnableReason() {
		return enableReason;
	}

	public void setEnableReason(String enableReason) {
		this.enableReason = enableReason;
	}

	public Integer getVisitCount() {
		return visitCount;
	}

	public void setVisitCount(Integer visitCount) {
		this.visitCount = visitCount;
	}

	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public Date getFeeTime() {
		return feeTime;
	}

	public void setFeeTime(Date feeTime) {
		this.feeTime = feeTime;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public Boolean getSupportCredit() {
		return isSupportCredit;
	}

	public void setSupportCredit(Boolean isSupportCredit) {
		this.isSupportCredit = isSupportCredit;
	}

	public String getPacSetId() {
		return pacSetId;
	}

	public void setPacSetId(String pacSetId) {
		this.pacSetId = pacSetId;
	}

	public Long getTodayPacketPoolAmount() {
		return todayPacketPoolAmount;
	}

	public void setTodayPacketPoolAmount(Long todayPacketPoolAmount) {
		this.todayPacketPoolAmount = todayPacketPoolAmount;
	}

	public Long getPacketPoolAmount() {
		return packetPoolAmount;
	}

	public void setPacketPoolAmount(Long packetPoolAmount) {
		this.packetPoolAmount = packetPoolAmount;
	}

	public Integer getSecondNum() {
		return secondNum;
	}

	public void setSecondNum(Integer secondNum) {
		this.secondNum = secondNum;
	}

	public Integer getThirdNum() {
		return thirdNum;
	}

	public void setThirdNum(Integer thirdNum) {
		this.thirdNum = thirdNum;
	}

	public Integer getDayNum() {
		return dayNum;
	}

	public void setDayNum(Integer dayNum) {
		this.dayNum = dayNum;
	}

	public Integer getMonthNum() {
		return monthNum;
	}

	public void setMonthNum(Integer monthNum) {
		this.monthNum = monthNum;
	}

	public Integer getMonthSecondNum() {
		return monthSecondNum;
	}

	public void setMonthSecondNum(Integer monthSecondNum) {
		this.monthSecondNum = monthSecondNum;
	}

	public Integer getMonthThirdNum() {
		return monthThirdNum;
	}

	public void setMonthThirdNum(Integer monthThirdNum) {
		this.monthThirdNum = monthThirdNum;
	}

	public Integer getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(Integer groupNo) {
		this.groupNo = groupNo;
	}

	public Integer getAchievementMonth() {
		return achievementMonth;
	}

	public void setAchievementMonth(Integer achievementMonth) {
		this.achievementMonth = achievementMonth;
	}

	public Long getAchievementTotal() {
		return achievementTotal;
	}

	public void setAchievementTotal(Long achievementTotal) {
		this.achievementTotal = achievementTotal;
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
		return "Merchant{" +
			"id=" + id +
			", userId=" + userId +
			", customerServiceCode=" + customerServiceCode +
			", name=" + name +
			", position=" + position +
			", parentMerchantId=" + parentMerchantId +
			", referralServiceCode=" + referralServiceCode +
			", provinceCode=" + provinceCode +
			", cityCode=" + cityCode +
			", areaCode=" + areaCode +
			", addrCountry=" + addrCountry +
			", addrDetail=" + addrDetail +
			", addrDoorNumber=" + addrDoorNumber +
			", addrTel=" + addrTel +
			", addrContact=" + addrContact +
			", lon=" + lon +
			", lat=" + lat +
			", desc=" + desc +
			", status=" + status +
			", disableReason=" + disableReason +
			", enableReason=" + enableReason +
			", visitCount=" + visitCount +
			", qrcode=" + qrcode +
			", payStatus=" + payStatus +
			", feeTime=" + feeTime +
			", expireTime=" + expireTime +
			", isSupportCredit=" + isSupportCredit +
			", pacSetId=" + pacSetId +
			", todayPacketPoolAmount=" + todayPacketPoolAmount +
			", packetPoolAmount=" + packetPoolAmount +
			", secondNum=" + secondNum +
			", thirdNum=" + thirdNum +
			", dayNum=" + dayNum +
			", monthNum=" + monthNum +
			", monthSecondNum=" + monthSecondNum +
			", monthThirdNum=" + monthThirdNum +
			", groupNo=" + groupNo +
			", achievementMonth=" + achievementMonth +
			", achievementTotal=" + achievementTotal +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleted=" + deleted +
			"}";
	}
}
