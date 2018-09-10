package com.netx.worth.model;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotations.TableField;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.Version;
import java.io.Serializable;

/**
 * <p>
 * 礼物表
 * </p>
 *
 * @author lcx
 * @since 2018-03-08
 */
public class Gift extends Model<Gift> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 礼物标题
     */
	private String title;
    /**
     * 赠送人
     */
	@TableField("from_user_id")
	private String fromUserId;
    /**
     * 接受人
     */
	@TableField("to_user_id")
	private String toUserId;
    /**
     * 赠送时间
     */
	@TableField("send_at")
	private Date sendAt;
    /**
     * 礼物类型：
            1：红包
            2：网币
            3：商品
     */
	@TableField("gift_type")
	private Integer giftType;
    /**
     * 关联主键，没有就是0
     */
	@TableField("relatable_id")
	private String relatableId;
    /**
     * 金额
     */
	private Long amount;
    /**
     * 留言
     */
	private String description;
    /**
     * 是否匿名
     */
	@TableField("is_anonymity")
	private Boolean isAnonymity;
    /**
     * 是否填写物流
     */
	@TableField("is_set_logistics")
	private Boolean isSetLogistics;
    /**
     * 礼物状态：
             1：已送出
             2：已接受
            3：已拒绝
     */
	private Integer status;
    /**
     * 物流信息
     */
	private String address;
    /**
     * 其它要求
     */
	private String message;
    /**
     * 要求送达时间
     */
	@TableField("delivery_at")
	private Date deliveryAt;
    /**
     * 来源资讯id，即：从哪里点过来的
     */
	@TableField("article_id")
	private String articleId;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public Date getSendAt() {
		return sendAt;
	}

	public void setSendAt(Date sendAt) {
		this.sendAt = sendAt;
	}

	public Integer getGiftType() {
		return giftType;
	}

	public void setGiftType(Integer giftType) {
		this.giftType = giftType;
	}

	public String getRelatableId() {
		return relatableId;
	}

	public void setRelatableId(String relatableId) {
		this.relatableId = relatableId;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getAnonymity() {
		return isAnonymity;
	}

	public void setAnonymity(Boolean isAnonymity) {
		this.isAnonymity = isAnonymity;
	}

	public Boolean getSetLogistics() {
		return isSetLogistics;
	}

	public void setSetLogistics(Boolean isSetLogistics) {
		this.isSetLogistics = isSetLogistics;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getDeliveryAt() {
		return deliveryAt;
	}

	public void setDeliveryAt(Date deliveryAt) {
		this.deliveryAt = deliveryAt;
	}

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
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
		return "Gift{" +
			"id=" + id +
			", title=" + title +
			", fromUserId=" + fromUserId +
			", toUserId=" + toUserId +
			", sendAt=" + sendAt +
			", giftType=" + giftType +
			", relatableId=" + relatableId +
			", amount=" + amount +
			", description=" + description +
			", isAnonymity=" + isAnonymity +
			", isSetLogistics=" + isSetLogistics +
			", status=" + status +
			", address=" + address +
			", message=" + message +
			", deliveryAt=" + deliveryAt +
			", articleId=" + articleId +
			", createTime=" + createTime +
			", createUserId=" + createUserId +
			", updateTime=" + updateTime +
			", updateUserId=" + updateUserId +
			", deleted=" + deleted +
			"}";
	}
}
