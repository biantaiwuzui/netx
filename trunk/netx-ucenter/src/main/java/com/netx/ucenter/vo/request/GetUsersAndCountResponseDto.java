package com.netx.ucenter.vo.request;

import com.netx.ucenter.model.user.User;

import java.util.List;
import java.util.Map;

public class GetUsersAndCountResponseDto {

    private List<Map> users;

    private Integer count;

    public List<Map> getUsers() {
        return users;
    }

    public void setUsers(List<Map> users) {
        this.users = users;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
