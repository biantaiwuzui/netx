package com.netx.credit.vo;

import io.swagger.annotations.ApiModelProperty;



/**
 * @author 梓
 * @date 2018-08-03 14:25
 */

public class CreditAcountDto {
    @ApiModelProperty("用户信用值")
    private Integer userCredit;

    @ApiModelProperty("全网排行")
    private Integer creditRank;

    @ApiModelProperty("网信加减分")
    private Integer credit;

    @ApiModelProperty("网信加减分描述")
    private String description;

    @ApiModelProperty("网信加减分日期")
    private Long data;

    public Integer getUserCredit() {
        return userCredit;
    }

    public void setUserCredit(Integer userCredit) {
        this.userCredit = userCredit;
    }

    public Integer getCreditRank() {
        return creditRank;
    }

    public void setCreditRank(Integer creditRank) {
        this.creditRank = creditRank;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getData() {
        return data;
    }

    public void setData(Long data) {
        this.data = data;
    }
}
