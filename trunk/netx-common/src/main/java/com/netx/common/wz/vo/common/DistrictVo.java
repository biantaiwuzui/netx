package com.netx.common.wz.vo.common;

public class DistrictVo<T> {
    private T t;
    private Double distance;


    public DistrictVo(T t) {
        this.t = t;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

}