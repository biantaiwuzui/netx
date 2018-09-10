package com.netx.ucenter.vo.request;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.netx.common.vo.common.PageRequestDto;

public class QueryArticleListRequestDto extends PageRequestDto{

    private String title;

    private Integer statusCode;

    private Integer advertorialType;

    private String userNumber;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getAdvertorialType() {
        return advertorialType;
    }

    public void setAdvertorialType(Integer advertorialType) {
        this.advertorialType = advertorialType;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    @Override
    public String toString() {
        return "QueryArticleListRequestDto{" +
                "title='" + title + '\'' +
                ", statusCode=" + statusCode +
                ", advertorialType=" + advertorialType +
                '}';
    }
}
