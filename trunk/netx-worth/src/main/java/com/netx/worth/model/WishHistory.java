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
 *
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-22
 */
@TableName("wish_history")
public class WishHistory extends Model<WishHistory> {

    private static final long serialVersionUID = 1L;

    private String id;
    @TableField("wish_apply_id")
    private String wishApplyId;
    @TableField("user_id")
    private String userId;
    /**
     * 使用类型：
     1：待转账。
     2：转账成功。
     3：转账失败。
     */
    private Integer status;
    /**
     * 失败原因
     */
    private String reason;
    /**
     * 处理人
     */
    @TableField(value = "admin_user_id", fill = FieldFill.INSERT)
    private String adminUserId;
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    private Integer deleted;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWishApplyId() {
        return wishApplyId;
    }

    public void setWishApplyId(String wishApplyId) {
        this.wishApplyId = wishApplyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(String adminUserId) {
        this.adminUserId = adminUserId;
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
        return "WishHistory{" +
                "id='" + id + '\'' +
                ", wishApplyId='" + wishApplyId + '\'' +
                ", userId='" + userId + '\'' +
                ", status=" + status +
                ", reason='" + reason + '\'' +
                ", adminUserId='" + adminUserId + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", deleted=" + deleted +
                '}';
    }
}
