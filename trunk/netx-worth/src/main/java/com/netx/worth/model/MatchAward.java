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
 * 奖项表
 * </p>
 *
 * @author Yawn
 * @since 2018-08-15
 */
@TableName("match_award")
public class MatchAward extends Model<MatchAward> {

    private static final long serialVersionUID = 1L;

	private String id;
	@TableField("match_id")
	private String matchId;
    /**
     * 奖项名字
     */
	@TableField("award_name")
	private String awardName;
    /**
     * 奖项名额
     */
	@TableField("award_number")
	private Integer awardNumber;
    /**
     * 奖金
     */
	private BigDecimal bonus;
    /**
     * 奖品
     */
	private String prize;
    /**
     * 证书
     */
	private String certificate;
    /**
     * 排序
     */
	private Integer sort;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMatchId() {
		return matchId;
	}

	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}

	public String getAwardName() {
		return awardName;
	}

	public void setAwardName(String awardName) {
		this.awardName = awardName;
	}

	public Integer getAwardNumber() {
		return awardNumber;
	}

	public void setAwardNumber(Integer awardNumber) {
		this.awardNumber = awardNumber;
	}

	public BigDecimal getBonus() {
		return bonus;
	}

	public void setBonus(BigDecimal bonus) {
		this.bonus = bonus;
	}

	public String getPrize() {
		return prize;
	}

	public void setPrize(String prize) {
		this.prize = prize;
	}

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MatchAward{" +
			"id=" + id +
			", matchId=" + matchId +
			", awardName=" + awardName +
			", awardNumber=" + awardNumber +
			", bonus=" + bonus +
			", prize=" + prize +
			", certificate=" + certificate +
			", sort=" + sort +
			"}";
	}
}
