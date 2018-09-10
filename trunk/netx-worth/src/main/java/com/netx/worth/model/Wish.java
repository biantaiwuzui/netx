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
import java.io.Serializable;

/**
 * <p>
 * 心愿表
 * </p>
 *
 * @author lcx
 * @since 2018-03-08
 */
public class Wish extends Model<Wish> {

    private static final long serialVersionUID = 1L;

	private String id;
	@TableField("user_id")
	private String userId;
    /**
     * 主题
     */
	private String title;
    /**
     * 标签，逗号分隔
     */
	@TableField("wish_label")
	private String wishLabel;
    /**
     * 希望筹集的金额
     */
	private Long amount;
    /**
     * 当前筹集数
     */
	@TableField("current_amount")
	private Long currentAmount;
    /**
     * 当前已使用金额
     */
	@TableField("current_apply_amount")
	private Long currentApplyAmount;
    /**
     * 截至时间
     */
	@TableField("expired_at")
	private Date expiredAt;
    /**
     * 推荐人，逗号分隔
     */

	@TableField("referee_ids")

	private String refereeIds;
    /**
     * 推荐人数量
     */
	@TableField("referee_count")
	private Integer refereeCount;
    /**
     * 同意推荐数量
     */
	@TableField("referee_accept_count")
	private Integer refereeAcceptCount;
    /**
     * 拒绝推荐数量
     */
	@TableField("referee_refuse_count")
	private Integer refereeRefuseCount;
    /**
     * 描述
     */
	private String description;
    /**
     * 图片
     */
	@TableField("wish_images_url")
	private String wishImagesUrl;
    /**
     * 图片
     */
	@TableField("wish_images_two_url")
	private String wishImagesTwoUrl;
    /**
     * 状态：
            1：已发布
            2：已取消
            3：已关闭，即推荐人数不足50%
            4：推荐成功
            5：已失败，即筹款目标未达成
            6：筹集目标达成，即心愿发起成功
            7：已完成，即金额使用完毕
     */
	private Integer status;

	@TableField("is_lock")
	private Boolean isLock;

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

	public String getWishLabel() {
		return wishLabel;
	}

	public void setWishLabel(String wishLabel) {
		this.wishLabel = wishLabel;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Long getCurrentAmount() {
		return currentAmount;
	}

	public void setCurrentAmount(Long currentAmount) {
		this.currentAmount = currentAmount;
	}

	public Long getCurrentApplyAmount() {
		return currentApplyAmount;
	}

	public void setCurrentApplyAmount(Long currentApplyAmount) {
		this.currentApplyAmount = currentApplyAmount;
	}

	public Date getExpiredAt() {
		return expiredAt;
	}

	public void setExpiredAt(Date expiredAt) {
		this.expiredAt = expiredAt;
	}

	public String getRefereeIds() {
		return refereeIds;
	}

	public void setRefereeIds(String refereeIds) {
		this.refereeIds = refereeIds;
	}

	public Integer getRefereeCount() {
		return refereeCount;
	}

	public void setRefereeCount(Integer refereeCount) {
		this.refereeCount = refereeCount;
	}

	public Integer getRefereeAcceptCount() {
		return refereeAcceptCount;
	}

	public void setRefereeAcceptCount(Integer refereeAcceptCount) {
		this.refereeAcceptCount = refereeAcceptCount;
	}

	public Integer getRefereeRefuseCount() {
		return refereeRefuseCount;
	}

	public void setRefereeRefuseCount(Integer refereeRefuseCount) {
		this.refereeRefuseCount = refereeRefuseCount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWishImagesUrl() {
		return wishImagesUrl;
	}

	public void setWishImagesUrl(String wishImagesUrl) {
		this.wishImagesUrl = wishImagesUrl;
	}

	public String getWishImagesTwoUrl() {
		return wishImagesTwoUrl;
	}

	public void setWishImagesTwoUrl(String wishImagesTwoUrl) {
		this.wishImagesTwoUrl = wishImagesTwoUrl;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Boolean getLock() {
		return isLock;
	}

	public void setLock(Boolean lock) {
		isLock = lock;
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
		return "Wish{" +
				"id='" + id + '\'' +
				", userId='" + userId + '\'' +
				", title='" + title + '\'' +
				", wishLabel='" + wishLabel + '\'' +
				", amount=" + amount +
				", currentAmount=" + currentAmount +
				", currentApplyAmount=" + currentApplyAmount +
				", expiredAt=" + expiredAt +
				", refereeIds='" + refereeIds + '\'' +
				", refereeCount=" + refereeCount +
				", refereeAcceptCount=" + refereeAcceptCount +
				", refereeRefuseCount=" + refereeRefuseCount +
				", description='" + description + '\'' +
				", wishImagesUrl='" + wishImagesUrl + '\'' +
				", wishImagesTwoUrl='" + wishImagesTwoUrl + '\'' +
				", status=" + status +
				", isLock=" + isLock +
				", createTime=" + createTime +
				", createUserId='" + createUserId + '\'' +
				", updateTime=" + updateTime +
				", updateUserId='" + updateUserId + '\'' +
				", deleted=" + deleted +
				'}';
	}
}
