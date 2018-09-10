package com.netx.ucenter.model.common;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author 黎子安
 * @since 2018-03-09
 */
@TableName("common_evaluate")
public class CommonEvaluate extends Model<CommonEvaluate> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 分数
     */
	private Integer score;
	@TableField("from_user_id")
	private String fromUserId;
	@TableField("to_user_id")
	private String toUserId;

    /**
     * 内容
     */
	private String content;
	@TableField("p_id")
	private String pId;
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
    /**
     * 事件id
     */
	@TableField("type_id")
	private String typeId;
    /**
     * 事件名称
     */
	@TableField("type_name")
	private String typeName;
    /**
     * 订单id
     */
	@TableField("order_id")
	private String orderId;

	/**
	 * 评论类型
	 */
	@TableField("evaluate_type")
	private String evaluateType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
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

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getEvaluateType() {
		return evaluateType;
	}

	public void setEvaluateType(String evaluateType) {
		this.evaluateType = evaluateType;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "CommonEvaluate{" +
				"id='" + id + '\'' +
				", score=" + score +
				", fromUserId='" + fromUserId + '\'' +
				", toUserId='" + toUserId + '\'' +
				", content='" + content + '\'' +
				", pId='" + pId + '\'' +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				", createUserId='" + createUserId + '\'' +
				", updateUserId='" + updateUserId + '\'' +
				", deleted=" + deleted +
				", typeId='" + typeId + '\'' +
				", typeName='" + typeName + '\'' +
				", orderId='" + orderId + '\'' +
				", evaluateType='" + evaluateType + '\'' +
				'}';
	}
}
