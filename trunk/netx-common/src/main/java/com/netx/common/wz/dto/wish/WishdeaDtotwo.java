package com.netx.common.wz.dto.wish;

public class WishdeaDtotwo {

    private Integer staus;
    private String userid;

    public Integer getStaus() {
        return staus;
    }

    public void setStaus(Integer staus) {
        this.staus = staus;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "WishdeaDto2{" +
                "staus=" + staus +
                ", userid='" + userid + '\'' +
                '}';
    }
}
