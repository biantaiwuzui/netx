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
 * 网商-红包记录表
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-09
 */
@TableName("redpacket_record")
public class RedpacketRecord extends Model<RedpacketRecord> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 红包发放id
     */
	@TableField("redpacket_send_id")
	private String redpacketSendId;
    /**
     * 用户id
     */
	@TableField("user_id")
	private String userId;
    /**
     * 红包金额
     */
	private Long amount;
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

	public String getRedpacketSendId() {
		return redpacketSendId;
	}

	public void setRedpacketSendId(String redpacketSendId) {
		this.redpacketSendId = redpacketSendId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
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
		return "RedpacketRecord{" +
			"id=" + id +
			", redpacketSendId=" + redpacketSendId +
			", userId=" + userId +
			", amount=" + amount +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleted=" + deleted +
			"}";
	}
}
