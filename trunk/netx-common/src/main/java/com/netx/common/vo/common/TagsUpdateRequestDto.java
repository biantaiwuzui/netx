package com.netx.common.vo.common;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

public class TagsUpdateRequestDto {
    private String id;
    @NotBlank
    @ApiModelProperty("标签值")
    private String value;
    @ApiModelProperty(value = "typeCate为内置标签专用,填写具体汉字,如:学校,院系等")
    private String typeCate;

    @ApiModelProperty("更新者")
    private String updateUser;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTypeCate() {
        return typeCate;
    }

    public void setTypeCate(String typeCate) {
        this.typeCate = typeCate;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
}
