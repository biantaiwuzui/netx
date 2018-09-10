package com.netx.worth.model;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotations.TableField;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.annotations.Version;
import java.io.Serializable;

/**
 * <p>
 * 比赛场次
 * </p>
 *
 * @author Yawn
 * @since 2018-08-17
 */
@TableName("match_venue")
public class MatchVenue extends Model<MatchVenue> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	private String id;
    /**
     * 比赛id
     */
	@TableField("zone_id")
	private String zoneId;
    /**
     * 场次名称
     */
	private String title;
    /**
     * 赛制类型，填赛制ID
     */
	private String kind;
    /**
     * 赛组的ID，多个逗号隔开
     */
	@TableField("group_ids")
	private String groupIds;
    /**
     * 开始时间
     */
	@TableField(value = "begin_time", fill = FieldFill.INSERT)
	private Date beginTime;
    /**
     * 结束时间
     */
	@TableField(value = "end_time", fill = FieldFill.INSERT_UPDATE)
	private Date endTime;
    /**
     * 比赛地址
     */
	private String address;
    /**
     * 比赛场地
     */
	private String site;
    /**
     * 比赛场地图片
     */
	@TableField("site_image_url")
	private String siteImageUrl;
    /**
     * 排序
     */
	private Integer sort;
    /**
     * 存流程的顺序，比如流程1的就扔个1过来
     */
	@TableField("flow_path")
	private Integer flowPath;
    /**
     * 流程顺序中的排序
     */
	@TableField("flow_path_sort")
	private Integer flowPathSort;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(String groupIds) {
		this.groupIds = groupIds;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getSiteImageUrl() {
		return siteImageUrl;
	}

	public void setSiteImageUrl(String siteImageUrl) {
		this.siteImageUrl = siteImageUrl;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getFlowPath() {
		return flowPath;
	}

	public void setFlowPath(Integer flowPath) {
		this.flowPath = flowPath;
	}

	public Integer getFlowPathSort() {
		return flowPathSort;
	}

	public void setFlowPathSort(Integer flowPathSort) {
		this.flowPathSort = flowPathSort;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MatchVenue{" +
			"id=" + id +
			", zoneId=" + zoneId +
			", title=" + title +
			", kind=" + kind +
			", groupIds=" + groupIds +
			", beginTime=" + beginTime +
			", endTime=" + endTime +
			", address=" + address +
			", site=" + site +
			", siteImageUrl=" + siteImageUrl +
			", sort=" + sort +
			", flowPath=" + flowPath +
			", flowPathSort=" + flowPathSort +
			"}";
	}
}
