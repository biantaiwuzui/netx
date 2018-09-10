package com.netx.ucenter.vo;

import java.util.List;

/**
 * Created by 85169 on 2017/11/20.
 */
public class AreaVO {

    private String id;

    private String name;

    private Integer flag;

    private List<AreaVO> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AreaVO> getChildren() {
        return children;
    }

    public void setChildren(List<AreaVO> children) {
        this.children = children;
    }
}
