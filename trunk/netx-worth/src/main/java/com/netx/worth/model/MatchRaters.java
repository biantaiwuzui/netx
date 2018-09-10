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
 * 比赛评分
 * </p>
 *
 * @author Yawn
 * @since 2018-08-20
 */
@TableName("match_raters")
public class MatchRaters extends Model<MatchRaters> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 评分者ID
     */
	@TableField("raters_id")
	private String ratersId;
    /**
     * 评分者称呼
     */
	@TableField("raters_name")
	private String ratersName;
    /**
     * 赛程
     */
	@TableField("progress_id")
	private String progressId;
    /**
     * 参赛者ID
     */
	@TableField("participant_id")
	private String participantId;
    /**
     * 分数
     */
	private BigDecimal score;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRatersId() {
		return ratersId;
	}

	public void setRatersId(String ratersId) {
		this.ratersId = ratersId;
	}

	public String getRatersName() {
		return ratersName;
	}

	public void setRatersName(String ratersName) {
		this.ratersName = ratersName;
	}

	public String getProgressId() {
		return progressId;
	}

	public void setProgressId(String progressId) {
		this.progressId = progressId;
	}

	public String getParticipantId() {
		return participantId;
	}

	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}

	public BigDecimal getScore() {
		return score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MatchRaters{" +
			"id=" + id +
			", ratersId=" + ratersId +
			", ratersName=" + ratersName +
			", progressId=" + progressId +
			", participantId=" + participantId +
			", score=" + score +
			"}";
	}
}
