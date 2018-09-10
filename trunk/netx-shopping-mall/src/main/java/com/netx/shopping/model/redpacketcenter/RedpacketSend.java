package com.netx.shopping.model.redpacketcenter;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 网商-红包发放表
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-09
 */
@TableName("redpacket_send")
public class RedpacketSend extends Model<RedpacketSend> {

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
	private Date sendTime;
    /**
     * 创建时间
     */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;
    /**
     * 更新时间
     */
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;
    /**
     * 删除标识
     */
	@TableField(fill = FieldFill.INSERT)
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

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
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
		return "RedpacketSend{" +
			"id=" + id +
			", redpacketNum=" + redpacketNum +
			", num=" + num +
			", amount=" + amount +
			", surplusAmount=" + surplusAmount +
			", sendTime=" + sendTime +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleted=" + deleted +
			"}";
	}
}
