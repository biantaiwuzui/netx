package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Create by wongloong on 17-8-29
 */
@ApiModel
public class AreaAddRequestDto {
    private String id;
    @NotBlank
    @ApiModelProperty(required = true, example = "0", notes = "如果为顶级为0")
    private String pid;
    @NotBlank
    private String value;
    @NotNull
    @ApiModelProperty(required = true, example = "0", notes = "层级flag,顶级为0,依次下推")
    private Integer flag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "AddAreaRequest{" +
                "id='" + id + '\'' +
                ", pid='" + pid + '\'' +
                ", value='" + value + '\'' +
                ", flag=" + flag +
                '}';
    }
}
