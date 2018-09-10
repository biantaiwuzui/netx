package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Create by wongloong on 17-9-1
 */
@ApiModel
public class TagsQueryRequestDto {
    @NotBlank
    @ApiModelProperty("'0'为公共标签'-1'为不限,其他为创建人id")
    private String createUser;

    @NotNull(message = "标签类型不能为空")
    @ApiModelProperty( "0:内置标签,1-技能标签,2-兴趣标签,3-商品标签")
    private Integer type;

    @ApiModelProperty("typeCate(如果为空则查询type下所有的标签)为内置/技能标签专用,填写具体汉字,如:学校,院系等")
    private String typeCate;

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
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
}
