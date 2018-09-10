package com.netx.common.user.model;

import com.baomidou.mybatisplus.annotations.TableField;

public class StatData {

    @TableField("user_id")
    private String id;

    @TableField("num")
    private Integer num;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
