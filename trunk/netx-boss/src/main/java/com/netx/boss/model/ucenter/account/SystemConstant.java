package com.netx.boss.model.ucenter.account;


import com.netx.utils.annotation.EnableCache;

import java.util.Date;

/**
 * Created by spark on 2015/12/12.
 */
@EnableCache
public class SystemConstant  {
    private static final long serialVersionUID = -8182774313330733486L;

    private String constKey;
    private String constValue;
    private String constComment;
    private String constCategory;

    private Integer id;
    private Integer status;
    private Date createTime;
    private Date updateTime;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getConstKey() {
        return constKey;
    }

    public void setConstKey(String constKey) {
        this.constKey = constKey;
    }

    public String getConstValue() {
        return constValue;
    }

    public void setConstValue(String constValue) {
        this.constValue = constValue;
    }

    public String getConstComment() {
        return constComment;
    }

    public void setConstComment(String constComment) {
        this.constComment = constComment;
    }

    public String getConstCategory() {
        return constCategory;
    }

    public void setConstCategory(String constCategory) {
        this.constCategory = constCategory;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
}
