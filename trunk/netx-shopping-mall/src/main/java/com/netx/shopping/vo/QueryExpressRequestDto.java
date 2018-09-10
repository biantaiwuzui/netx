package com.netx.shopping.vo;

import com.netx.common.vo.common.PageRequestDto;
import io.swagger.annotations.ApiModelProperty;

public class QueryExpressRequestDto extends PageRequestDto {

    @ApiModelProperty("快递公司名称")
    private String name;

    @ApiModelProperty("开头字母")
    private String letter;

    @ApiModelProperty("代号")
    private String number;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
