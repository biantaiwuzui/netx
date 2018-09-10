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
 * 网商-红包池
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-09
 */
@TableName("redpacket_pool")
public class RedpacketPool extends Model<RedpacketPool> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 今日红包金额
     */
	@TableField("redpacket_amount")
	private Long redpacketAmount;
    /**
     * 总额
     */
	@TableField("total_amount")
	private Long totalAmount;
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
	@TableField(fill = FieldFill.INSERT)
	private Integer deleted;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getRedpacketAmount() {
		return redpacketAmount;
	}

	public void setRedpacketAmount(Long redpacketAmount) {
		this.redpacketAmount = redpacketAmount;
	}

	public Long getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Long totalAmount) {
		this.totalAmount = totalAmount;
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
		return "RedpacketPool{" +
			"id=" + id +
			", redpacketAmount=" + redpacketAmount +
			", totalAmount=" + totalAmount +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleted=" + deleted +
			"}";
	}
}
