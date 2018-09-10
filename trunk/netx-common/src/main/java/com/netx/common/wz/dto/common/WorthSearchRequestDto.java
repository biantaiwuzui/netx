package com.netx.common.wz.dto.common;

import com.netx.common.common.enums.WorthTypeEnum;
import com.netx.common.vo.common.PageRequestDto;
import io.swagger.annotations.ApiModelProperty;

public class WorthSearchRequestDto extends PageRequestDto {

    @ApiModelProperty(value = "网能标题")
    private String title;

    @ApiModelProperty(value = "网能类型")
    private WorthTypeEnum worthTypeEnum;

    @ApiModelProperty(value = "排序方式：<br>" +
            "0.最热 <br>" +
            "1.最新 <br>" +
            "2.最近 <br>" +
            "3.支持网信 <br>" +
            "4.信用")
    private Integer sort = 0;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public WorthTypeEnum getWorthTypeEnum() {
        return worthTypeEnum;
    }

    public void setWorthTypeEnum(WorthTypeEnum worthTypeEnum) {
        this.worthTypeEnum = worthTypeEnum;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        if(sort!=null){
            this.sort = sort;
        }
    }
}
