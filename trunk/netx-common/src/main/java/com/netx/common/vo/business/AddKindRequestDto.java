package com.netx.common.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created By liwei
 * Description: 新增或修改商品分类请求参数对象
 * Date: 2017-09-05
 */
@ApiModel
public class AddKindRequestDto {
    @ApiModelProperty("商品分类id")
    private String id;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("商品分类名称")
    @NotBlank(message = "名称不能为空")
    private String name;

    @ApiModelProperty("商品分类描述")
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
