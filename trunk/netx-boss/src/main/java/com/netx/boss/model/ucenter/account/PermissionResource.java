package com.netx.boss.model.ucenter.account;


import java.util.Date;

public class PermissionResource  {
    private static final long serialVersionUID = -7623363496989869819L;

    private String url;
    private String name;
    private String resource;
    private String category;

    private Integer id;
    private Integer status;
    private Date createTime;
    private Date updateTime;

    /** 域值 **/
    private Integer menuId;
    private Integer menuResourceId;
    private boolean assigned;
    private boolean checkAssignToMenu;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

    public boolean isCheckAssignToMenu() {
        return checkAssignToMenu;
    }

    public void setCheckAssignToMenu(boolean checkAssignToMenu) {
        this.checkAssignToMenu = checkAssignToMenu;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public Integer getMenuResourceId() {
        return menuResourceId;
    }

    public void setMenuResourceId(Integer menuResourceId) {
        this.menuResourceId = menuResourceId;
    }
}
