package com.netx.credit.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author 梓
 * @date 2018-08-04 13:32
 */

public class CreditAcountVo {

    @ApiModelProperty("用户头像")
    private String userPicture;

    @ApiModelProperty("用户信用值")
    private Integer userCredit;

    @ApiModelProperty("全网排行")
    private Double creditRank;

    @ApiModelProperty("信用流水")
    private List<CreditAcountDto> creditAcountDtoList;

    public String getUserPicture() {
        return userPicture;
    }

    public void setUserPicture(String userPicture) {
        this.userPicture = userPicture;
    }

    public Integer getUserCredit() {
        return userCredit;
    }

    public void setUserCredit(Integer userCredit) {
        this.userCredit = userCredit;
    }

    public Double getCreditRank() {
        return creditRank;
    }

    public void setCreditRank(Double creditRank) {
        this.creditRank = creditRank;
    }

    public List<CreditAcountDto> getCreditAcountDtoList() {
        return creditAcountDtoList;
    }

    public void setCreditAcountDtoList(List<CreditAcountDto> creditAcountDtoList) {
        this.creditAcountDtoList = creditAcountDtoList;
    }
}
