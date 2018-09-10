package com.netx.worth.model;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotations.TableField;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.annotations.Version;
import java.io.Serializable;

/**
 * <p>
 * 賽事公告表
 * </p>
 *
 * @author Yawn
 * @since 2018-09-08
 */
@TableName("match_notice")
public class MatchNotice extends Model<MatchNotice> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 标题
     */
	private String title;
    /**
     * 公共内容
     */
	@TableField("affiche_content")
	private String afficheContent;
    /**
     * 賽事id
     */
	@TableField("match_id")
	private String matchId;
    /**
     * 發佈公告者id
     */
	@TableField("user_id")
	private String userId;
	@TableField("user_type")
	private String userType;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Date createTime;
    /**
     * 更新时间
     */
	@TableField("update_time")
	private Date updateTime;
    /**
     * 發佈公告的商家id
     */
	@TableField("merchant_id")
	private String merchantId;
    /**
     * 商家名称
     */
	@TableField("merchant_name")
	private String merchantName;
    /**
     * 商家类型
     */
	@TableField("merchant_type")
	private String merchantType;
    /**
     * 消息类型/0/1/2/3/4越小优先级越高
     */
	@TableField("message_type")
	private Integer messageType;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAfficheContent() {
		return afficheContent;
	}

	public void setAfficheContent(String afficheContent) {
		this.afficheContent = afficheContent;
	}

	public String getMatchId() {
		return matchId;
	}

	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
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

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getMerchantType() {
		return merchantType;
	}

	public void setMerchantType(String merchantType) {
		this.merchantType = merchantType;
	}

	public Integer getMessageType() {
		return messageType;
	}

	public void setMessageType(Integer messageType) {
		this.messageType = messageType;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MatchNotice{" +
			"id=" + id +
			", title=" + title +
			", afficheContent=" + afficheContent +
			", matchId=" + matchId +
			", userId=" + userId +
			", userType=" + userType +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", merchantId=" + merchantId +
			", merchantName=" + merchantName +
			", merchantType=" + merchantType +
			", messageType=" + messageType +
			"}";
	}
}
