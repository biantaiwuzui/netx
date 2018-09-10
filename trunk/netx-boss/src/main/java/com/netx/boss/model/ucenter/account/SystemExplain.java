package com.netx.boss.model.ucenter.account;


import java.util.Date;

/**
 * Created by tec_f on 2016/8/11.
 */
public class SystemExplain  {
    private static final long serialVersionUID = 264803488160277177L;
    private Integer id;
    private String keyName;
    private String description;
    private String content;
    private String category;
    private SystemExplainType explainType;
    private Integer status;
    private Date createTime;
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public SystemExplainType getExplainType() {
        return explainType;
    }

    public void setExplainType(SystemExplainType explainType) {
        this.explainType = explainType;
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

    public enum SystemExplainType{
        TEXT,MEDIA,HTML,IMAGE
    }
}

