package com.netx.ucenter.model.user;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 黎子安
 * @since 2018-04-03
 */
@TableName("article")
public class Article extends Model<Article> {

    private static final long serialVersionUID = 1L;

	private String id;
	@TableField("user_id")
	private String userId;
    /**
     * 文章标题
     */
	private String title;
    /**
     * 封面图直接存图片url
     */
	private String pic;
    /**
     * 附件
     */
	private String atta;
    /**
     * 文章内容
     */
	private String content;

	/**
	 * 字数
	 */
	private Integer length;
    /**
     * 谁可以看此图文动态：
            0：所有人
            1：全部好友
            2：我关注的人
            3：关注我的人
            4：指定用户（可多个）
     */
	private Integer who;
    /**
     * 当who字段为指定好友时，该字段有效。将指定好友的id以逗号分隔保存
     */
	private String receiver;
    /**
     * 是否匿名（是不是隐身发布）：
            0：不匿名
            1：匿名
     */
	@TableField("is_anonymity")
	private Boolean isAnonymity;
    /**
     * 是否显示位置
     */
	@TableField("is_show_location")
	private Boolean isShowLocation;
    /**
     * 是否违规
     */
	@TableField("is_illegal")
	private Boolean isIllegal;
    /**
     * 软文类型：
            0：免费图文
            1：软文【广告】 
     */
	@TableField("advertorial_type")
	private Integer advertorialType;
    /**
     * 经度
     */
	private BigDecimal lon;
    /**
     * 纬度
     */
	private BigDecimal lat;
    /**
     * 置顶：
            1：列表置顶。
            2：分类置顶。
     */
	private Integer top;
    /**
     * 发布时的位置信息
     */
	private String location;
    /**
     * 置顶过期时间
     */
	@TableField("top_expired_at")
	private Date topExpiredAt;
    /**
     * 押金(以分为单位)（点击费用都是从这里扣掉）
     */
	private Long amount;
    /**
     * 押金类型：
            1：零钱
            2：网币
     */
	@TableField("amount_type")
	private Integer amountType;
    /**
     * 状态码：
            0：正常
            1：异常
            2：待审核
            3：待交押金
            4：押金余额不足
     */
	@TableField("status_code")
	private Integer statusCode;
    /**
     * 原因
     */
	private String reason;
    /**
     * 是否封禁
     */
	@TableField("is_lock")
	private Integer isLock;
    /**
     * 点击数
     */
	private Long hits;
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Date createTime;
	@TableField("create_user_id")
	private String createUserId;
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;
	@TableField("update_user_id")
	private String updateUserId;
	@TableField(fill = FieldFill.INSERT)
	private Integer deleted;
	@TableField("worth_type")
	private String worthType;
	@TableField("worth_ids")
	private String worthIds;

	public String getWorthType() {
		return worthType;
	}

	public void setWorthType(String worthType) {
		this.worthType = worthType;
	}

	public String getWorthIds() {
		return worthIds;
	}

	public void setWorthIds(String worthIds) {
		this.worthIds = worthIds;
	}

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getAtta() {
		return atta;
	}

	public void setAtta(String atta) {
		this.atta = atta;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer getWho() {
		return who;
	}

	public void setWho(Integer who) {
		this.who = who;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public Boolean getAnonymity() {
		return isAnonymity;
	}

	public void setAnonymity(Boolean isAnonymity) {
		this.isAnonymity = isAnonymity;
	}

	public Boolean getShowLocation() {
		return isShowLocation;
	}

	public void setShowLocation(Boolean isShowLocation) {
		this.isShowLocation = isShowLocation;
	}

	public Boolean getIllegal() {
		return isIllegal;
	}

	public void setIllegal(Boolean isIllegal) {
		this.isIllegal = isIllegal;
	}

	public Integer getAdvertorialType() {
		return advertorialType;
	}

	public void setAdvertorialType(Integer advertorialType) {
		this.advertorialType = advertorialType;
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

	public Integer getTop() {
		return top;
	}

	public void setTop(Integer top) {
		this.top = top;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getTopExpiredAt() {
		return topExpiredAt;
	}

	public void setTopExpiredAt(Date topExpiredAt) {
		this.topExpiredAt = topExpiredAt;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Integer getAmountType() {
		return amountType;
	}

	public void setAmountType(Integer amountType) {
		this.amountType = amountType;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Integer getIsLock() {
		return isLock;
	}

	public void setIsLock(Integer isLock) {
		this.isLock = isLock;
	}

	public Long getHits() {
		return hits;
	}

	public void setHits(Long hits) {
		this.hits = hits;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
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
		return "Article{" +
			"id=" + id +
			", userId=" + userId +
			", title=" + title +
			", pic=" + pic +
			", atta=" + atta +
			", content=" + content +
			", length=" + length +
			", who=" + who +
			", receiver=" + receiver +
			", isAnonymity=" + isAnonymity +
			", isShowLocation=" + isShowLocation +
			", isIllegal=" + isIllegal +
			", advertorialType=" + advertorialType +
			", lon=" + lon +
			", lat=" + lat +
			", top=" + top +
			", location=" + location +
			", topExpiredAt=" + topExpiredAt +
			", amount=" + amount +
			", amountType=" + amountType +
			", statusCode=" + statusCode +
			", reason=" + reason +
			", isLock=" + isLock +
			", hits=" + hits +
			", createTime=" + createTime +
			", createUserId=" + createUserId +
			", updateTime=" + updateTime +
			", updateUserId=" + updateUserId +
			", deleted=" + deleted +
			"}";
	}
}
