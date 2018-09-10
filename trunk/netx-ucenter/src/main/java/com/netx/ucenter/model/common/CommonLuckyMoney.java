package com.netx.ucenter.model.common;

import java.io.Serializable;

import java.math.BigDecimal;
import java.sql.Time;
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
@TableName("common_lucky_money")
public class CommonLuckyMoney extends Model<CommonLuckyMoney> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	private String id;
    /**
     * 发放时间-根据时间判断哪个是第一个
     */
	@TableField("send_time")
	private Time sendTime;
    /**
     * 审核人用户id
     */
	@TableField("examine_user_id")
	private String examineUserId;
    /**
     * 状态 1.已审核 2.等待审核,3.明天生效
     */
	private Integer status;
    /**
     * 发放比例
     */
	@TableField("send_people")
	private BigDecimal sendPeople;
    /**
     * 发放人数
     */
	@TableField("send_count")
	private BigDecimal sendCount;
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


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Time getSendTime() {
		return sendTime;
	}

	public void setSendTime(Time sendTime) {
		this.sendTime = sendTime;
	}

	public String getExamineUserId() {
		return examineUserId;
	}

	public void setExamineUserId(String examineUserId) {
		this.examineUserId = examineUserId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public BigDecimal getSendPeople() {
		return sendPeople;
	}

	public void setSendPeople(BigDecimal sendPeople) {
		this.sendPeople = sendPeople;
	}

	public BigDecimal getSendCount() {
		return sendCount;
	}

	public void setSendCount(BigDecimal sendCount) {
		this.sendCount = sendCount;
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "CommonLuckyMoney{" +
			"id=" + id +
			", sendTime=" + sendTime +
			", examineUserId=" + examineUserId +
			", status=" + status +
			", sendPeople=" + sendPeople +
			", sendCount=" + sendCount +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", createUserId=" + createUserId +
			", updateUserId=" + updateUserId +
			", deleted=" + deleted +
			"}";
	}
}
