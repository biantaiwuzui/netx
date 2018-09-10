package com.netx.worth.model;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotations.TableField;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.annotations.Version;
import java.io.Serializable;

/**
 * <p>
 * 赛区表
 * </p>
 *
 * @author Yawn
 * @since 2018-09-06
 */
@TableName("match_zone")
public class MatchZone extends Model<MatchZone> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 分区名称
     */
	@TableField("zone_name")
	private String zoneName;
    /**
     * 分区地址
     */
	@TableField("zone_adress")
	private String zoneAdress;
    /**
     * 分区地区
     */
	@TableField("zone_site")
	private String zoneSite;
    /**
     * 排序
     */
	private Integer sort;
	@TableField("match_id")
	private String matchId;
    /**
     * 经度
     */
	private BigDecimal lon;
    /**
     * 纬度
     */
	private BigDecimal lat;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public String getZoneAdress() {
		return zoneAdress;
	}

	public void setZoneAdress(String zoneAdress) {
		this.zoneAdress = zoneAdress;
	}

	public String getZoneSite() {
		return zoneSite;
	}

	public void setZoneSite(String zoneSite) {
		this.zoneSite = zoneSite;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getMatchId() {
		return matchId;
	}

	public void setMatchId(String matchId) {
		this.matchId = matchId;
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MatchZone{" +
			"id=" + id +
			", zoneName=" + zoneName +
			", zoneAdress=" + zoneAdress +
			", zoneSite=" + zoneSite +
			", sort=" + sort +
			", matchId=" + matchId +
			", lon=" + lon +
			", lat=" + lat +
			"}";
	}
}
