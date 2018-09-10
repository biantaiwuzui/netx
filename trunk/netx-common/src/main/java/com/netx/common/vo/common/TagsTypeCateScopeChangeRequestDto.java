package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * Create on 17-11-10
 *
 * @author wongloong
 */
@ApiModel
public class TagsTypeCateScopeChangeRequestDto {
    @ApiModelProperty("标签项内容")
    @NotNull
    private String typeCate;
    @ApiModelProperty("0为降级为自选标签,1为升级为公用标签")
    @NotNull
    private int toPublic;

    public String getTypeCate() {
        return typeCate;
    }

    public void setTypeCate(String typeCate) {
        this.typeCate = typeCate;
    }

    public int getToPublic() {
        return toPublic;
    }

    public void setToPublic(int toPublic) {
        this.toPublic = toPublic;
    }
}
