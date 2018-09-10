package com.netx.worth.model;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotations.TableField;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableLogic;

/**
 * <p>
 * 需求表
 * </p>
 *
 * @author lcx
 * @since 2018-03-08
 */
public class Demand extends Model<Demand> {

    private static final long serialVersionUID = 1L;

	private String id;
	@TableField("user_id")
	private String userId;
    /**
     * 主题
     */
	private String title;
    /**
     * 需求分类：
            1：技能
            2：才艺
            3：知识
            4：资源
     */
	@TableField("demand_type")
	private Integer demandType;
    /**
     * 标签，逗号分隔
     */
	@TableField("demand_label")
	private String demandLabel;
    /**
     * 是否长期有效
     */
	@TableField("is_open_ended")
	private Boolean isOpenEnded;
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
    /**
     * 时间要求：只有大概的要求，如：50天内、仅限周末等
     */
	private String about;
    /**
     * 需求人数
     */
	private Integer amount;
	private String unit;
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
     * 描述
     */
	private String description;
    /**
     * 活动图片
     */
	@TableField("images_url")
	private String imagesUrl;
    /**
     * 详情图片
     */
	@TableField("details_images_url")
	private String detailsImagesUrl;
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
     * 如发生消费，是否由我全额承担
     */
	@TableField("is_pick_up")
	private Boolean isPickUp;
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
            1：已发布
            2：已取消
            3：已关闭
     */
	private Integer status;
    /**
     * 是否支付（托管）
     */
	@TableField("is_pay")
	private Boolean isPay;
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

	public Integer getDemandType() {
		return demandType;
	}

	public void setDemandType(Integer demandType) {
		this.demandType = demandType;
	}

	public String getDemandLabel() {
		return demandLabel;
	}

	public void setDemandLabel(String demandLabel) {
		this.demandLabel = demandLabel;
	}

	public Boolean getOpenEnded() {
		return isOpenEnded;
	}

	public void setOpenEnded(Boolean isOpenEnded) {
		this.isOpenEnded = isOpenEnded;
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

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImagesUrl() {
		return imagesUrl;
	}

	public void setImagesUrl(String imagesUrl) {
		this.imagesUrl = imagesUrl;
	}

	public String getDetailsImagesUrl() {
		return detailsImagesUrl;
	}

	public void setDetailsImagesUrl(String detailsImagesUrl) {
		this.detailsImagesUrl = detailsImagesUrl;
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

	public Boolean getPickUp() {
		return isPickUp;
	}

	public void setPickUp(Boolean isPickUp) {
		this.isPickUp = isPickUp;
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

	public Boolean getPay() {
		return isPay;
	}

	public void setPay(Boolean isPay) {
		this.isPay = isPay;
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
		return "Demand{" +
			"id=" + id +
			", userId=" + userId +
			", title=" + title +
			", demandType=" + demandType +
			", demandLabel=" + demandLabel +
			", isOpenEnded=" + isOpenEnded +
			", startAt=" + startAt +
			", endAt=" + endAt +
			", about=" + about +
			", amount=" + amount +
			", unit=" + unit +
			", obj=" + obj +
			", objList=" + objList +
			", address=" + address +
			", lon=" + lon +
			", lat=" + lat +
			", description=" + description +
			", imagesUrl=" + imagesUrl +
			", detailsImagesUrl=" + detailsImagesUrl +
			", orderIds=" + orderIds +
			", orderPrice=" + orderPrice +
			", isPickUp=" + isPickUp +
			", wage=" + wage +
			", isEachWage=" + isEachWage +
			", bail=" + bail +
			", status=" + status +
			", isPay=" + isPay +
			", createTime=" + createTime +
			", createUserId=" + createUserId +
			", updateTime=" + updateTime +
			", updateUserId=" + updateUserId +
			", deleted=" + deleted +
			"}";
	}
}
