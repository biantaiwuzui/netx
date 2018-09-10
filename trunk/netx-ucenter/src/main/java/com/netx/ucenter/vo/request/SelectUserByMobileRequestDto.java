package com.netx.ucenter.vo.request;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class SelectUserByMobileRequestDto {
    @ApiModelProperty(value = "网号或手机号")
    private String value;

    @ApiModelProperty(value = "当前页")
    @Min(value = 1,message = "当前页最少是1")
    private Integer current;

    @ApiModelProperty(value = "条数")
    @Min(value = 1,message = "查询条数最少是1")
    private Integer size;
/*
    *//**
     * 经度
     *//*
    @ApiModelProperty(value = "经度")
    @NotNull(message = "经度不能为空")
    @Range(min = -180,max = 180,message = "经度输入不在范围内")
    //@Digits(integer=10, fraction=6,message = "经度输入格式不合法")
    private BigDecimal lon;
    *//**
     * 纬度
     *//*
    @ApiModelProperty(value = "纬度")
    @NotNull(message = "纬度不能为空")
    @Range(min = -90,max = 90,message = "纬度输入不在范围内")
    //@Digits(integer=10, fraction=6,message = "纬度输入格式不合法")
    private BigDecimal lat;*/

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "SelectUserByMobileRequestDto{" +
                "value='" + value + '\'' +
                ", current=" + current +
                ", size=" + size +
                '}';
    }
}
