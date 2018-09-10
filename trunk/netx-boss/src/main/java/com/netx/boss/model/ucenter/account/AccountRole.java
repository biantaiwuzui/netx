package com.netx.boss.model.ucenter.account;

public class AccountRole {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String roleName;
    private Integer status;

    /** 域值 **/
    private boolean assigned;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }
}
