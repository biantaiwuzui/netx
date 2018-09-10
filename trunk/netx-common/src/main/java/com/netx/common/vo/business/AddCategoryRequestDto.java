package com.netx.common.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 添加或修改网商类目请求参数
 */
@ApiModel
public class AddCategoryRequestDto {

    @ApiModelProperty("类别id")
    private String id;

    @ApiModelProperty("类别名称")
    @NotBlank(message = "名称不能为空")
    @Length(min = 1, max = 20, message = "名称长度最多20个汉字或字符")
    private String name;

    @ApiModelProperty("父类目")
    private String parentId;

    @ApiModelProperty("类别类型:" +
            "1.前台类目 \n" +
            "2.后台类目 \n")
    private Integer icon;

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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }
}
