package com.netx.boss.model.ucenter.account;


import java.util.LinkedList;
import java.util.List;

public class AccountMenuUrl {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer parentId;
    private String name;
    private String menuUrl;
    private String content;
    private Integer status;

    /** 域字段 **/
    private String parentName;
    private List<AccountMenuUrl> child;
    private Integer roleId;
    private boolean checkAssignToRole;
    private boolean assigned;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<AccountMenuUrl> getChild() {
        return child;
    }

    public void setChild(List<AccountMenuUrl> child) {
        this.child = child;
    }

    public List<AccountMenuUrl> richChild() {
        if (child == null) {
            child = new LinkedList<>();
        }
        return child;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public boolean isCheckAssignToRole() {
        return checkAssignToRole;
    }

    public void setCheckAssignToRole(boolean checkAssignToRole) {
        this.checkAssignToRole = checkAssignToRole;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }
}
