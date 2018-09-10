package com.netx.credit.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;

import java.io.Serializable;
import java.util.Date;

/**
 * 网信-用户等级表
 * @author 梓
 */
@TableName("credit_stage")
public class CreditStage extends Model<CreditStage> {

    private static final long serialVersionUID = 1L;

    private String id;
    /**
     * 网信ID
     */
    @TableField("credit_id")
    private String creditId;
    /**
     * 用户等级名称:
     * 1.最高级
     * 2.好友级
     * 3.普通级
     */
    @TableField("stage_name")
    private String stageName;
    /**
     * 认购开始日期
     */
    @TableField("start_date")
    private Date startDate;
    /**
     * 认购结束日期
     */
    @TableField("end_date")
    private Date endDate;
    /**
     * 网信认购比率
     * 1.最高级 n %
     * 2.好友级 n %
     * 3.普通级 n %
     */
    @TableField("subscription_ratio")
    private double subscriptionRatio;
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getSubscriptionRatio() {
        return subscriptionRatio;
    }

    public void setSubscriptionRatio(double subscriptionRatio) {
        this.subscriptionRatio = subscriptionRatio;
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
        return id;
    }

    @Override
    public String toString() {
        return "CreditStage{" +
                "id='" + id + '\'' +
                ", creditId='" + creditId + '\'' +
                ", stageName='" + stageName + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", subscriptionRatio=" + subscriptionRatio +
                ", deleted=" + deleted +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
