package com.netx.worth.model;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotations.TableField;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.annotations.Version;
import java.io.Serializable;

/**
 * <p>
 * 参赛门票详情
 * </p>
 *
 * @author Yawn
 * @since 2018-08-19
 */
@TableName("match_ticket")
public class MatchTicket extends Model<MatchTicket> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
	private String id;
    /**
     * 比赛ID
     */
	@TableField("zone_id")
	private String zoneId;
	@TableField("ticket_name")
	private String ticketName;
    /**
     * 门票场次
     */
	@TableField("venue_ids")
	private String venueIds;
    /**
     * 门票价钱
     */
	private BigDecimal free;
    /**
     * 名额
     */
	private Integer number;
    /**
     * 门票描述
     */
	private String description;
    /**
     * 开始发售时间
     */
	@TableField(value = "begin_time", fill = FieldFill.INSERT)
	private Date beginTime;
    /**
     * 结束发售时间
     */
	@TableField(value = "end_time", fill = FieldFill.INSERT_UPDATE)
	private Date endTime;
    /**
     * 乐观锁
     */
	@TableField("optimistic_locking")
	private Integer optimisticLocking;
    /**
     * 排序
     */
	private Integer sort;
    /**
     * 是否默认门票档次
     */
	@TableField("is_defalut")
	private Boolean isDefalut;
    /**
     * 使用默认的
     */
	@TableField("use_defalut")
	private Boolean useDefalut;


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

	public String getTicketName() {
		return ticketName;
	}

	public void setTicketName(String ticketName) {
		this.ticketName = ticketName;
	}

	public String getVenueIds() {
		return venueIds;
	}

	public void setVenueIds(String venueIds) {
		this.venueIds = venueIds;
	}

	public BigDecimal getFree() {
		return free;
	}

	public void setFree(BigDecimal free) {
		this.free = free;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Integer getOptimisticLocking() {
		return optimisticLocking;
	}

	public void setOptimisticLocking(Integer optimisticLocking) {
		this.optimisticLocking = optimisticLocking;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Boolean getDefalut() {
		return isDefalut;
	}

	public void setDefalut(Boolean isDefalut) {
		this.isDefalut = isDefalut;
	}

	public Boolean getUseDefalut() {
		return useDefalut;
	}

	public void setUseDefalut(Boolean useDefalut) {
		this.useDefalut = useDefalut;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MatchTicket{" +
			"id=" + id +
			", zoneId=" + zoneId +
			", ticketName=" + ticketName +
			", venueIds=" + venueIds +
			", free=" + free +
			", number=" + number +
			", description=" + description +
			", beginTime=" + beginTime +
			", endTime=" + endTime +
			", optimisticLocking=" + optimisticLocking +
			", sort=" + sort +
			", isDefalut=" + isDefalut +
			", useDefalut=" + useDefalut +
			"}";
	}
}
