package com.netx.common.vo.common;

import com.netx.common.user.dto.common.CommonListDto;
import io.swagger.annotations.ApiModelProperty;

public class GetInnerTagsRequestDto extends CommonListDto{
    @ApiModelProperty("标签名")
    private String value;

    @ApiModelProperty("标签项")
    private String typeCate;

    @ApiModelProperty("类型")
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public String getTypeCate() {
        return typeCate;
    }

    public void setTypeCate(String typeCate) {
        this.typeCate = typeCate;
    }

    public void setValue(String value) {
        this.value = value;

    }
}
