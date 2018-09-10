package com.netx.worth.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 网能发布记录表
 * </p>
 *
 * @author 逍遥游
 * @since 2017-12-04
 */
@TableName("active_log")
public class ActiveLog extends Model<ActiveLog> {

    private static final long serialVersionUID = 1L;

	private String id;
	@TableField("user_id")
	private String userId;
    /**
     * 关联类型，值为Model名
     */
    @TableField("relatable_type")
	private String relatableType;
    /**
     * 关联ID
     */
	@TableField("relatable_id")
	private String relatableId;
    /**
     * 描述
     */
	private String description;
    /**
     * 经纬度hash
     */
	private String geohash;
    /**
     * 经度
     */
	private BigDecimal lon;
    /**
     * 纬度
     */
	private BigDecimal lat;
	@TableField(value = "create_time",fill = FieldFill.INSERT)
	private Date createTime;
	@TableField("create_user_id")
	private String createUserId;
	@TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
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

	public String getRelatableType() {
		return relatableType;
	}

	public void setRelatableType(String relatableType) {
		this.relatableType = relatableType;
	}

	public String getRelatableId() {
		return relatableId;
	}

	public void setRelatableId(String relatableId) {
		this.relatableId = relatableId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGeohash() {
		return geohash;
	}

	public void setGeohash(String geohash) {
		this.geohash = geohash;
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

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getCreateTime() {

		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public static long getSerialVersionUID() {

		return serialVersionUID;
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
		return "ActiveLog{" +
				"id='" + id + '\'' +
				", userId='" + userId + '\'' +
				", relatableType='" + relatableType + '\'' +
				", relatableId='" + relatableId + '\'' +
				", description='" + description + '\'' +
				", geohash='" + geohash + '\'' +
				", lon=" + lon +
				", lat=" + lat +
				", createTime=" + createTime +
				", createUserId='" + createUserId + '\'' +
				", updateTime=" + updateTime +
				", updateUserId='" + updateUserId + '\'' +
				", deleted=" + deleted +
				'}';
	}
}
