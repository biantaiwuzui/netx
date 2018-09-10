package com.netx.boss.components;

import com.netx.boss.model.ucenter.account.Account;
import com.netx.boss.model.ucenter.account.AccountRole;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Pattern;

public class LoginAccount implements Serializable {
    private static final long serialVersionUID = 1L;

    private Account account;
    private List<AccountRole> accountRoles;
    private List<Pattern> accountRoleUrls;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<AccountRole> getAccountRoles() {
        return accountRoles;
    }

    public void setAccountRoles(List<AccountRole> accountRoles) {
        this.accountRoles = accountRoles;
    }

    public List<Pattern> getAccountRoleUrls() {
        return accountRoleUrls;
    }

    public void setAccountRoleUrls(List<Pattern> accountRoleUrls) {
        this.accountRoleUrls = accountRoleUrls;
    }
}
