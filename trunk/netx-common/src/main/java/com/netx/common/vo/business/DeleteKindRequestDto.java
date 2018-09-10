package com.netx.common.vo.business;
/*
*
 */
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel
public class DeleteKindRequestDto {
    @ApiModelProperty("商品包装明细id")
    @NotBlank(message="商品包装明细id不能为空")
    private String id;

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }
}
