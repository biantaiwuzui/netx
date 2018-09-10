package com.netx.common.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created By wj.liu
 * Description: 添加或修改系统类别请求参数
 * Date: 2017-09-05
 */
@ApiModel
public class AddSellerCategoryRequestDto {

    @ApiModelProperty("类别id")
    private String id;

    @ApiModelProperty("商家类别名称")
    @NotBlank(message = "名称不能为空")
    @Length(min = 2, max = 20, message = "名称长度最多20个汉字或字符")
    private String name;

    @ApiModelProperty("商家类别描述")
    @NotBlank(message = "描述不能为空")
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
