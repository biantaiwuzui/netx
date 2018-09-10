package com.netx.worth.model;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotations.TableField;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.Version;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 需求单表，每次入选就生成一张，以应对长期的需求，长期需求只能有1人入选。
 * </p>
 *
 * @author lcx
 * @since 2018-03-08
 */
@TableName("demand_order")
public class DemandOrder extends Model<DemandOrder> {

    private static final long serialVersionUID = 1L;

	private String id;
	@TableField("user_id")

	private String userId;

	@TableField("demand_id")
	private String demandId;
    /**
     * 开始时间
     */
	@TableField("start_at")
	private Date startAt;
    /**
     * 结束时间
     */
	@TableField("end_at")
	private Date endAt;
	private String unit;
    /**
     * 地址：
            这里商家地址，订单之类的都是发布需求时的，在生成订单时还会生成实际的。
     */
	private String address;
    /**
     * 经度
     */
	private BigDecimal lon;
    /**
     * 纬度
     */
	private BigDecimal lat;
    /**
     * 订单列表
     */
	@TableField("order_ids")
	private String orderIds;
    /**
     * 订单消费
     */
	@TableField("order_price")
	private Long orderPrice;
    /**
     * 报酬，根据isEachWage判断是总报酬还是单位报酬
     */
	private Long wage;
    /**
     * 是否为单位报酬，即：以上报酬是否为每个入选者的单位报酬
     */
	@TableField("is_each_wage")
	private Boolean isEachWage;
    /**
     * 已经托管的保证金
     */
	private Long bail;
    /**
     * 状态：
            1：已接受，即已确定入选人
            2：已确定细节
            3：需求启动，只要入选者有人启动成功，就设置为该值
            4：超时未确认细节
            5,：需求成功，即：距离、验证码都通过
            6：退款状态
            7：超时未启动需求
     */
	@TableField("status")
	private Integer status;
    /**
     * 验证状态
            （邀请开始后30分钟，未通过验证且与设定地址距离100m以上）：
            0：未验证
            1：已验证
            2：验证失败
            
     */
	@TableField("validation_status")
	private Boolean validationStatus;
    /**
     * 是否支付（托管）
     */
	@TableField("is_pay")
	private Boolean isPay;
    /**
     * 入选人是否确认付款
     */
	@TableField("is_confirm_pay")
	private Boolean isConfirmPay;
    /**
     * 确认付款人ID，任意一个入选人确认即可
     */
	@TableField("confirm_pay_user_id")
	private String confirmPayUserId;

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

	public String getDemandId() {
		return demandId;
	}

	public void setDemandId(String demandId) {
		this.demandId = demandId;
	}

	public Date getStartAt() {
		return startAt;
	}

	public void setStartAt(Date startAt) {
		this.startAt = startAt;
	}

	public Date getEndAt() {
		return endAt;
	}

	public void setEndAt(Date endAt) {
		this.endAt = endAt;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public Long getWage() {
		return wage;
	}

	public void setWage(Long wage) {
		this.wage = wage;
	}

	public Boolean getEachWage() {
		return isEachWage;
	}

	public void setEachWage(Boolean isEachWage) {
		this.isEachWage = isEachWage;
	}

	public Long getBail() {
		return bail;
	}

	public void setBail(Long bail) {
		this.bail = bail;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Boolean getValidationStatus() {
		return validationStatus;
	}

	public void setValidationStatus(Boolean validationStatus) {
		this.validationStatus = validationStatus;
	}

	public Boolean getPay() {
		return isPay;
	}

	public void setPay(Boolean isPay) {
		this.isPay = isPay;
	}

	public Boolean getConfirmPay() {
		return isConfirmPay;
	}

	public void setConfirmPay(Boolean isConfirmPay) {
		this.isConfirmPay = isConfirmPay;
	}

	public String getConfirmPayUserId() {
		return confirmPayUserId;
	}

	public void setConfirmPayUserId(String confirmPayUserId) {
		this.confirmPayUserId = confirmPayUserId;
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

}
