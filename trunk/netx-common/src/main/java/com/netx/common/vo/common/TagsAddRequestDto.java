package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Create by wongloong on 17-9-1
 */
@ApiModel
public class TagsAddRequestDto {

    private String id;
    @NotBlank
    @ApiModelProperty("标签值")
    private String value;
    @NotNull
    @ApiModelProperty(value = "标签类别,0:内置标签,1-技能标签,2-兴趣标签,3-商品标签", required = true)
    private Integer type;
    @ApiModelProperty(value = "typeCate为内置标签专用,填写具体汉字,如:学校,院系等")
    private String typeCate;

    @ApiModelProperty(value = "如果是公共标签,createUser为0", required = true)
    private String createUser;
//    @NotBlank
//    @ApiModelProperty(value = "如果是用户传0（必传），如果是管理员传1（必传）", required = true)
//    private String isBoss;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTypeCate() {
        return typeCate;
    }

    public void setTypeCate(String typeCate) {
        this.typeCate = typeCate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

//    public String getIsBoss() {
//        return isBoss;
//    }
//
//    public void setIsBoss(String isBoss) {
//        this.isBoss = isBoss;
//    }
}
