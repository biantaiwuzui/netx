package com.netx.shopping.model.business;

import java.io.Serializable;

import java.sql.Time;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;

import java.io.Serializable;

/**
 * <p>
 * 网商-红包发放表
 * </p>
 *
 * @author 李威
 * @since 2018-03-08
 */
@TableName("seller_redpacket_send")
public class SellerRedpacketSend extends Model<SellerRedpacketSend> {

    private static final long serialVersionUID = 1L;

    /**
     * 红包发放id
     */
	private String id;
    /**
     * 红包个数
     */
	@TableField("redpacket_num")
	private Integer redpacketNum;
    /**
     * 发放人数
     */
	private Integer num;
	/**
	 * 红包金额
	 */
	private Long amount;
    /**
     * 剩余红包金额
     */
	@TableField("surplus_amount")
	private Long surplusAmount;
    /**
     * 发放时间
     */
	@TableField("send_time")
	private Time sendTime;
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

	public Integer getRedpacketNum() {
		return redpacketNum;
	}

	public void setRedpacketNum(Integer redpacketNum) {
		this.redpacketNum = redpacketNum;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Long getSurplusAmount() {
		return surplusAmount;
	}

	public void setSurplusAmount(Long surplusAmount) {
		this.surplusAmount = surplusAmount;
	}

	public Time getSendTime() {
		return sendTime;
	}

	public void setSendTime(Time sendTime) {
		this.sendTime = sendTime;
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
		return "SellerRedpacketSend{" +
			"id=" + id +
			", redpacketNum=" + redpacketNum +
			", level=" + num +
			", amount=" + amount +
			", surplusAmount=" + surplusAmount +
			", sendTime=" + sendTime +
			", createTime=" + createTime +
			", createUserId=" + createUserId +
			", updateTime=" + updateTime +
			", updateUserId=" + updateUserId +
			", deleted=" + deleted +
			"}";
	}
}
