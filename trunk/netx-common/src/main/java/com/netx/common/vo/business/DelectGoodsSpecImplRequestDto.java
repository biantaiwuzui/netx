package com.netx.common.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created By liwei
 * Description: 添加商品规格请求参数
 * Date: 2017-10-23
 */
@ApiModel
public class DelectGoodsSpecImplRequestDto {
    @ApiModelProperty("商品规格分类项id")
    @NotBlank(message = "商品规格分类项id不能为空")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
