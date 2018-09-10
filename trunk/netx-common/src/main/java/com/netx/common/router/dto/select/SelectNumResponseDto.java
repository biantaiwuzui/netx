package com.netx.common.router.dto.select;

import io.swagger.annotations.ApiModelProperty;

public class SelectNumResponseDto {
    @ApiModelProperty("成功数")
    private Integer num;

    @ApiModelProperty("总数")
    private Integer total;

    @ApiModelProperty("已报名人数")
    private Integer applyNum;

    @ApiModelProperty("距离")
    private double distance;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Integer getApplyNum() {
        return applyNum;
    }

    public void setApplyNum(Integer applyNum) {
        this.applyNum = applyNum;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public SelectNumResponseDto() {
        this.num = 0;
        this.total = 0;
    }

    public SelectNumResponseDto(Integer num, Integer total) {
        this.num = num;
        this.total = total;
    }
}
