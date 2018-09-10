package com.netx.common.vo.common;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.List;

public class LuckyMoneySaveOrUpdateDto {

    @Valid
    private  List<WzCommonLuckyMoneyAddDto> list;

    @ApiModelProperty("1.超级管理员 2.普通管理员")
    private int type;

    public List<WzCommonLuckyMoneyAddDto> getList() {
        return list;
    }

    public void setList(List<WzCommonLuckyMoneyAddDto> list) {
        this.list = list;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
