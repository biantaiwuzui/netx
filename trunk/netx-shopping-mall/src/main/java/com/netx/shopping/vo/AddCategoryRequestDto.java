package com.netx.shopping.vo;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class AddCategoryRequestDto {

    @ApiModelProperty("主键id")
    private String id;

    @ApiModelProperty("父集的类目id")
    private String parentId;

    @ApiModelProperty("类目名")
    @NotBlank(message = "类目名不能为空")
    @Length(min = 1, max = 20, message = "名称长度最多20个汉字或字符")
    private String name;

    @ApiModelProperty("类别类型:" +
            "1.前台类目 \n" +
            "2.后台类目 \n")
    @NotNull(message = "类别类型不能为空")
    private Integer icon;

    @ApiModelProperty("优先级")
    private Long priority;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }
}
