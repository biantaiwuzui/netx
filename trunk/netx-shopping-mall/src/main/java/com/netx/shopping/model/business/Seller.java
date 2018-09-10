package com.netx.shopping.model.business;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.enums.FieldFill;

import java.io.Serializable;

/**
 * <p>
 * 网商-商家表
 * </p>
 *
 * @author 李威
 * @since 2018-03-08
 */
public class Seller extends Model<Seller> {

    private static final long serialVersionUID = 1L;

    /**
     * 标识ID
     */
	private String id;
    /**
     * 客服代码
     */
	@TableField("customer_code")
	private String customerCode;
    /**
     * 用户ID
     */
	@TableField("user_id")
	private String userId;
    /**
     * 商家名称
     */
	private String name;
    /**
     * 商家职务：
            0：客服代理
            1：商户代理
            2：市场总监
            3：营运总裁商家职务
     */
	private Integer job;
    /**
     * 引荐客服代码
     */
	@TableField("referral_code")
	private String referralCode;
    /**
     * 地址-省
     */
	@TableField("province_code")
	private String provinceCode;
    /**
     * 地址-市
     */
	@TableField("city_code")
	private String cityCode;
    /**
     * 地址-区
     */
	@TableField("area_code")
	private String areaCode;
    /**
     * 地址-乡镇
     */
	@TableField("addr_country")
	private String addrCountry;
    /**
     * 地址-详细
     */
	@TableField("addr_detail")
	private String addrDetail;
    /**
     * 地址-详细-门牌
     */
	@TableField("addr_door_number")
	private String addrDoorNumber;
    /**
     * 地址-单位名称
     */
	@TableField("addr_unit_name")
	private String addrUnitName;
    /**
     * 地址-联系电话
     */
	@TableField("addr_tel")
	private String addrTel;
    /**
     * 地址-联系人
     */
	@TableField("addr_contact")
	private String addrContact;
    /**
     * 地址-经度
     */
	private BigDecimal lon;
    /**
     * 地址-纬度
     */
	private BigDecimal lat;
    /**
     * 商家红包设置ID
     */
	@TableField("pac_set_id")
	private String pacSetId;
    /**
     * 商家描述
     */
	@TableField("seller_desc")
	private String sellerDesc;
    /**
     * 标识图片，多个逗号隔开
     */
	@TableField("logo_images_url")
	private String logoImagesUrl;
    /**
     * 商家照片，多个逗号隔开数量不限
     */
	@TableField("seller_images_url")
	private String sellerImagesUrl;
    /**
     * 证件，多个逗号隔开
     */
	@TableField("certi_images_url")
	private String certiImagesUrl;
    /**
     * 验证法人代表
     */
	@TableField("verify_corporate")
	private String verifyCorporate;
    /**
     * 验证手机号码
     */
	@TableField("verify_phone")
	private String verifyPhone;
    /**
     * 验证身份证
     */
	@TableField("verify_id_card")
	private String verifyIdCard;
    /**
     * 验证注册网号
     */
	@TableField("verify_network_num")
	private String verifyNetworkNum;
    /**
     * 业务主管ID
     */
	@TableField("manage_id")
	private String manageId;
	/**
	 * 收银人员ID
	 */
	@TableField("seller_cashier_id")
    private String sellerCashierId;
    /**
     * 商家状态
            1：正常
            2：拉黑
     */
	private Integer status;
    /**
     * 拉黑原因
     */
	@TableField("back_reason")
	private String backReason;
    /**
     * 解除原因
     */
	@TableField("over_reason")
	private String overReason;
    /**
     * 商家上下架操作者
     */
	@TableField("handlers_id")
	private String handlersId;
    /**
     * 访问量
     */
	@TableField("visit_num")
	private Integer visitNum;
    /**
     * 今日红包金额
     */
	@TableField("today_packet_pool_amount")
	private Long todayPacketPoolAmount;
    /**
     * 昨日红包金额
     */
	@TableField("packet_pool_amount")
	private Long packetPoolAmount;
    /**
     * 商家收款二维码，base64字符串
     */
	@TableField("seller_qrcode")
	private String sellerQrcode;
    /**
     * 注册管理费缴费状态
0：已缴费
1：待缴费
2：待续费
     */
	@TableField("pay_status")
	private Integer payStatus;
    /**
     * 缴费有效期
0：终生
1：一年
2：三年
     */
	@TableField("effective_time")
	private Integer effectiveTime;
    /**
     * 缴费时间
     */
	@TableField("fee_time")
	private Date feeTime;
    /**
     * 过期时间：-1为终身有效
     */
	@TableField("end_time")
	private Integer endTime;
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
	/**
	 * 是否支持网信
	 */
	@TableField("is_hold_credit")
	private Boolean holdCredit;
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

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getJob() {
		return job;
	}

	public void setJob(Integer job) {
		this.job = job;
	}

	public String getReferralCode() {
		return referralCode;
	}

	public void setReferralCode(String referralCode) {
		this.referralCode = referralCode;
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

	public String getAddrUnitName() {
		return addrUnitName;
	}

	public void setAddrUnitName(String addrUnitName) {
		this.addrUnitName = addrUnitName;
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

	public String getPacSetId() {
		return pacSetId;
	}

	public void setPacSetId(String pacSetId) {
		this.pacSetId = pacSetId;
	}

	public String getSellerDesc() {
		return sellerDesc;
	}

	public void setSellerDesc(String sellerDesc) {
		this.sellerDesc = sellerDesc;
	}

	public String getLogoImagesUrl() {
		return logoImagesUrl;
	}

	public void setLogoImagesUrl(String logoImagesUrl) {
		this.logoImagesUrl = logoImagesUrl;
	}

	public String getSellerImagesUrl() {
		return sellerImagesUrl;
	}

	public void setSellerImagesUrl(String sellerImagesUrl) {
		this.sellerImagesUrl = sellerImagesUrl;
	}

	public String getCertiImagesUrl() {
		return certiImagesUrl;
	}

	public void setCertiImagesUrl(String certiImagesUrl) {
		this.certiImagesUrl = certiImagesUrl;
	}

	public String getVerifyCorporate() {
		return verifyCorporate;
	}

	public void setVerifyCorporate(String verifyCorporate) {
		this.verifyCorporate = verifyCorporate;
	}

	public String getVerifyPhone() {
		return verifyPhone;
	}

	public void setVerifyPhone(String verifyPhone) {
		this.verifyPhone = verifyPhone;
	}

	public String getVerifyIdCard() {
		return verifyIdCard;
	}

	public void setVerifyIdCard(String verifyIdCard) {
		this.verifyIdCard = verifyIdCard;
	}

	public String getVerifyNetworkNum() {
		return verifyNetworkNum;
	}

	public void setVerifyNetworkNum(String verifyNetworkNum) {
		this.verifyNetworkNum = verifyNetworkNum;
	}

	public String getManageId() {
		return manageId;
	}

	public void setManageId(String manageId) {
		this.manageId = manageId;
	}

	public String getSellerCashierId() {
		return sellerCashierId;
	}

	public void setSellerCashierId(String sellerCashierId) {
		this.sellerCashierId = sellerCashierId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getBackReason() {
		return backReason;
	}

	public void setBackReason(String backReason) {
		this.backReason = backReason;
	}

	public String getOverReason() {
		return overReason;
	}

	public void setOverReason(String overReason) {
		this.overReason = overReason;
	}

	public String getHandlersId() {
		return handlersId;
	}

	public void setHandlersId(String handlersId) {
		this.handlersId = handlersId;
	}

	public Integer getVisitNum() {
		return visitNum;
	}

	public void setVisitNum(Integer visitNum) {
		this.visitNum = visitNum;
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

	public String getSellerQrcode() {
		return sellerQrcode;
	}

	public void setSellerQrcode(String sellerQrcode) {
		this.sellerQrcode = sellerQrcode;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public Integer getEffectiveTime() {
		return effectiveTime;
	}

	public void setEffectiveTime(Integer effectiveTime) {
		this.effectiveTime = effectiveTime;
	}

	public Date getFeeTime() {
		return feeTime;
	}

	public void setFeeTime(Date feeTime) {
		this.feeTime = feeTime;
	}

	public Integer getEndTime() {
		return endTime;
	}

	public void setEndTime(Integer endTime) {
		this.endTime = endTime;
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

	public Boolean getHoldCredit() {
		return holdCredit;
	}

	public void setHoldCredit(Boolean holdCredit) {
		this.holdCredit = holdCredit;
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
		return "Seller{" +
				"id='" + id + '\'' +
				", customerCode='" + customerCode + '\'' +
				", userId='" + userId + '\'' +
				", name='" + name + '\'' +
				", job=" + job +
				", referralCode='" + referralCode + '\'' +
				", provinceCode='" + provinceCode + '\'' +
				", cityCode='" + cityCode + '\'' +
				", areaCode='" + areaCode + '\'' +
				", addrCountry='" + addrCountry + '\'' +
				", addrDetail='" + addrDetail + '\'' +
				", addrDoorNumber='" + addrDoorNumber + '\'' +
				", addrUnitName='" + addrUnitName + '\'' +
				", addrTel='" + addrTel + '\'' +
				", addrContact='" + addrContact + '\'' +
				", lon=" + lon +
				", lat=" + lat +
				", pacSetId='" + pacSetId + '\'' +
				", sellerDesc='" + sellerDesc + '\'' +
				", logoImagesUrl='" + logoImagesUrl + '\'' +
				", sellerImagesUrl='" + sellerImagesUrl + '\'' +
				", certiImagesUrl='" + certiImagesUrl + '\'' +
				", verifyCorporate='" + verifyCorporate + '\'' +
				", verifyPhone='" + verifyPhone + '\'' +
				", verifyIdCard='" + verifyIdCard + '\'' +
				", verifyNetworkNum='" + verifyNetworkNum + '\'' +
				", manageId='" + manageId + '\'' +
				", sellerCashierId='" + sellerCashierId + '\'' +
				", status=" + status +
				", backReason='" + backReason + '\'' +
				", overReason='" + overReason + '\'' +
				", handlersId='" + handlersId + '\'' +
				", visitNum=" + visitNum +
				", todayPacketPoolAmount=" + todayPacketPoolAmount +
				", packetPoolAmount=" + packetPoolAmount +
				", sellerQrcode='" + sellerQrcode + '\'' +
				", payStatus=" + payStatus +
				", effectiveTime=" + effectiveTime +
				", feeTime=" + feeTime +
				", endTime=" + endTime +
				", secondNum=" + secondNum +
				", thirdNum=" + thirdNum +
				", dayNum=" + dayNum +
				", monthNum=" + monthNum +
				", monthSecondNum=" + monthSecondNum +
				", monthThirdNum=" + monthThirdNum +
				", groupNo=" + groupNo +
				", achievementMonth=" + achievementMonth +
				", achievementTotal=" + achievementTotal +
				", holdCredit=" + holdCredit +
				", createTime=" + createTime +
				", createUserId='" + createUserId + '\'' +
				", updateTime=" + updateTime +
				", updateUserId='" + updateUserId + '\'' +
				", deleted=" + deleted +
				'}';
	}
}
