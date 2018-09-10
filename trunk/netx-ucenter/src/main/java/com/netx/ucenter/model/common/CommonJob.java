package com.netx.ucenter.model.common;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
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
@TableName("common_job")
public class CommonJob extends Model<CommonJob> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	private String id;
	private String name;
	@TableField("class_name")
	private String className;
	@TableField("group_name")
	private String groupName;
	@TableField("cron_time")
	private Date cronTime;
	private Integer status;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Date getCronTime() {
		return cronTime;
	}

	public void setCronTime(Date cronTime) {
		this.cronTime = cronTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "CommonJob{" +
			"id=" + id +
			", name=" + name +
			", className=" + className +
			", groupName=" + groupName +
			", cronTime=" + cronTime +
			", status=" + status +
			"}";
	}
}
