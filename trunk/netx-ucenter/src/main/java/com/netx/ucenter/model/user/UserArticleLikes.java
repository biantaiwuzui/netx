package com.netx.ucenter.model.user;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 资讯点赞表
 * </p>
 *
 * @author 黎子安
 * @since 2018-03-09
 */
@TableName("user_article_likes")
public class UserArticleLikes extends Model<UserArticleLikes> {

    private static final long serialVersionUID = 1L;

	private String id;
	@TableField("user_id")
	private String userId;
	@TableField("article_id")
	private String articleId;
    /**
     * 是否是点赞（取消赞时更改此字段）
     */
	@TableField("is_like")
	private Integer isLike;
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

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public Integer getIsLike() {
		return isLike;
	}

	public void setIsLike(Integer isLike) {
		this.isLike = isLike;
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
		return "UserArticleLikes{" +
			"id=" + id +
			", userId=" + userId +
			", articleId=" + articleId +
			", isLike=" + isLike +
			", createTime=" + createTime +
			", createUserId=" + createUserId +
			", updateTime=" + updateTime +
			", updateUserId=" + updateUserId +
			", deleted=" + deleted +
			"}";
	}
}
