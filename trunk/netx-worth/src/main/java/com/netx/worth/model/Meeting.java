package com.netx.worth.model;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotations.TableField;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.Version;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * 活动聚会表
 * </p>
 *
 * @author lcx
 * @since 2018-03-08
 */
public class Meeting extends Model<Meeting> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 主发起人ID
     */
	@TableField("user_id")
	private String userId;
    /**
     * 主题
     */
	private String title;
    /**
     * 活动形式：
            1：活动，即1对1
            2：聚合，即多对多
            3：纯线上活动
            4：不发生消费的线下活动
     */
	@TableField("meeting_type")
	private Integer meetingType;
    /**
     * 活动标签，逗号分隔
     */
	@TableField("meeting_label")
	private String meetingLabel;
    /**
     * 活动开始时间
     */
	@TableField("started_at")
	private Date startedAt;
    /**
     * 活动结束时间
     */
	@TableField("end_at")
	private Date endAt;
    /**
     * 活动截至报名时间
     */
	@TableField("reg_stop_at")
	private Date regStopAt;
    /**
     * 报名对象：
            1：不限制。
            2：仅限女性报名
            3：仅限男性报名
            4：仅允许我的好友报名
            5：仅限指定人员报名
     */
	private Integer obj;
    /**
     * 指定报名对象列表，逗号分隔
     */
	@TableField("obj_list")
	private String objList;
    /**
     * 报名费：0为免费
     */
	private Long amount;
    /**
     * 活动地址
     */
	private String address;
    /**
     * 订单列表
     */
	@TableField("order_ids")
	private String orderIds;
    /**
     * 活动消费
     */
	@TableField("order_price")
	private Long orderPrice;
    /**
     * 经度
     */
	private BigDecimal lon;
    /**
     * 纬度
     */
	private BigDecimal lat;
    /**
     * 描述
     */
	private String description;
    /**
     * 海报图片
     */
	@TableField("poster_images_url")
	private String posterImagesUrl;
    /**
     * 活动图片
     */
	@TableField("meeting_images_url")
	private String meetingImagesUrl;
    /**
     * 入选人数上限
     */
	private Integer ceil;
    /**
     * 入选人数下限
     */
	private Integer floor;
    /**
     * 已报名人数
     */
	@TableField("reg_count")
	private Integer regCount;
    /**
     * 已入选人数
     */
	@TableField("reg_success_count")
	private Integer regSuccessCount;
    /**
     * 活动状态：
            0：已发起，报名中
            1：报名截止，已确定入选人
            2：活动取消
            3：活动失败
            4：活动成功
            5：同意开始，分发验证码
     */
	private Integer status;
    /**
     * 费用不足时：
            1：由我补足差额，活动正常进行
            2：活动自动取消，报名费用全部返回
     */
	@TableField("fee_not_enough")
	private Integer feeNotEnough;
    /**
     * 是否确认细节（地址、消费）
     */
	@TableField("is_confirm")
	private Boolean isConfirm;
    /**
     * 版本
     */
	@TableField("lock_version")
	private Integer lockVersion;
    /**
     * 总报名费
     */
	@TableField("all_register_amount")
	private Long allRegisterAmount;
    /**
     * 消费差额，需补足的部分：总订单金额-总报名费
            
     */
	private Long balance;
    /**
     * 是否补足
     */
	@TableField("is_balance_pay")
	private Boolean isBalancePay;
    /**
     * 补足人
     */
	@TableField("pay_from")
	private String payFrom;
    /**
     * 活动费用
     */
	@TableField("meeting_fee_pay_amount")
	private BigDecimal meetingFeePayAmount;
	
	@ApiModelProperty(value = "群聊id")
	@TableField("group_id")
    private Long groupId;
	
	@TableField("status_description")
	private String statusDescription;
    /**
     * 活动费用支付人
     */
	@TableField("meeting_fee_Pay_from")
	private String meetingFeePayFrom;
    /**
     * 活动费用支付方式
     */
	@TableField("meeting_fee_pay_type")
	private String meetingFeePayType;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getMeetingType() {
		return meetingType;
	}

	public void setMeetingType(Integer meetingType) {
		this.meetingType = meetingType;
	}

	public String getMeetingLabel() {
		return meetingLabel;
	}

	public void setMeetingLabel(String meetingLabel) {
		this.meetingLabel = meetingLabel;
	}

	public Date getStartedAt() {
		return startedAt;
	}

	public void setStartedAt(Date startedAt) {
		this.startedAt = startedAt;
	}

	public Date getEndAt() {
		return endAt;
	}

	public void setEndAt(Date endAt) {
		this.endAt = endAt;
	}

	public Date getRegStopAt() {
		return regStopAt;
	}

	public void setRegStopAt(Date regStopAt) {
		this.regStopAt = regStopAt;
	}

	public Integer getObj() {
		return obj;
	}

	public void setObj(Integer obj) {
		this.obj = obj;
	}

	public String getObjList() {
		return objList;
	}

	public void setObjList(String objList) {
		this.objList = objList;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getOrderIds() {
		return orderIds;
	}

	public void setOrderIds(String orderIds) {
		this.orderIds = orderIds;
	}

	public Long getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(Long orderPrice) {
		this.orderPrice = orderPrice;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPosterImagesUrl() {
		return posterImagesUrl;
	}

	public void setPosterImagesUrl(String posterImagesUrl) {
		this.posterImagesUrl = posterImagesUrl;
	}

	public String getMeetingImagesUrl() {
		return meetingImagesUrl;
	}

	public void setMeetingImagesUrl(String meetingImagesUrl) {
		this.meetingImagesUrl = meetingImagesUrl;
	}

	public Integer getCeil() {
		return ceil;
	}

	public void setCeil(Integer ceil) {
		this.ceil = ceil;
	}

	public Integer getFloor() {
		return floor;
	}

	public void setFloor(Integer floor) {
		this.floor = floor;
	}

	public Integer getRegCount() {
		return regCount;
	}

	public void setRegCount(Integer regCount) {
		this.regCount = regCount;
	}

	public Integer getRegSuccessCount() {
		return regSuccessCount;
	}

	public void setRegSuccessCount(Integer regSuccessCount) {
		this.regSuccessCount = regSuccessCount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getFeeNotEnough() {
		return feeNotEnough;
	}

	public void setFeeNotEnough(Integer feeNotEnough) {
		this.feeNotEnough = feeNotEnough;
	}

	public Boolean getConfirm() {
		return isConfirm;
	}

	public void setConfirm(Boolean isConfirm) {
		this.isConfirm = isConfirm;
	}

	public Integer getLockVersion() {
		return lockVersion;
	}

	public void setLockVersion(Integer lockVersion) {
		this.lockVersion = lockVersion;
	}

	public Long getAllRegisterAmount() {
		return allRegisterAmount;
	}

	public void setAllRegisterAmount(Long allRegisterAmount) {
		this.allRegisterAmount = allRegisterAmount;
	}

	public Long getBalance() {
		return balance;
	}

	public void setBalance(Long balance) {
		this.balance = balance;
	}

	public Boolean getBalancePay() {
		return isBalancePay;
	}

	public void setBalancePay(Boolean isBalancePay) {
		this.isBalancePay = isBalancePay;
	}

	public String getPayFrom() {
		return payFrom;
	}

	public void setPayFrom(String payFrom) {
		this.payFrom = payFrom;
	}

	public BigDecimal getMeetingFeePayAmount() {
		return meetingFeePayAmount;
	}

	public void setMeetingFeePayAmount(BigDecimal meetingFeePayAmount) {
		this.meetingFeePayAmount = meetingFeePayAmount;
	}

	public String getMeetingFeePayFrom() {
		return meetingFeePayFrom;
	}

	public void setMeetingFeePayFrom(String meetingFeePayFrom) {
		this.meetingFeePayFrom = meetingFeePayFrom;
	}

	public String getMeetingFeePayType() {
		return meetingFeePayType;
	}

	public void setMeetingFeePayType(String meetingFeePayType) {
		this.meetingFeePayType = meetingFeePayType;
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

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    @Override
	protected Serializable pkVal() {
		return this.id;
	}


    @Override
    public String toString() {
        return "Meeting{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", title='" + title + '\'' +
                ", meetingType=" + meetingType +
                ", meetingLabel='" + meetingLabel + '\'' +
                ", startedAt=" + startedAt +
                ", endAt=" + endAt +
                ", regStopAt=" + regStopAt +
                ", obj=" + obj +
                ", objList='" + objList + '\'' +
                ", amount=" + amount +
                ", address='" + address + '\'' +
                ", orderIds='" + orderIds + '\'' +
                ", orderPrice=" + orderPrice +
                ", lon=" + lon +
                ", lat=" + lat +
                ", description='" + description + '\'' +
                ", posterImagesUrl='" + posterImagesUrl + '\'' +
                ", meetingImagesUrl='" + meetingImagesUrl + '\'' +
                ", ceil=" + ceil +
                ", floor=" + floor +
                ", regCount=" + regCount +
                ", regSuccessCount=" + regSuccessCount +
                ", status=" + status +
                ", feeNotEnough=" + feeNotEnough +
                ", isConfirm=" + isConfirm +
                ", lockVersion=" + lockVersion +
                ", allRegisterAmount=" + allRegisterAmount +
                ", balance=" + balance +
                ", isBalancePay=" + isBalancePay +
                ", payFrom='" + payFrom + '\'' +
                ", meetingFeePayAmount=" + meetingFeePayAmount +
                ", groupId=" + groupId +
                ", statusDescription='" + statusDescription + '\'' +
                ", meetingFeePayFrom='" + meetingFeePayFrom + '\'' +
                ", meetingFeePayType='" + meetingFeePayType + '\'' +
                ", createTime=" + createTime +
                ", createUserId='" + createUserId + '\'' +
                ", updateTime=" + updateTime +
                ", updateUserId='" + updateUserId + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
