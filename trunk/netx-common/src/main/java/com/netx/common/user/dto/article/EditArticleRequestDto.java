package com.netx.common.user.dto.article;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;

public class EditArticleRequestDto {

    @ApiModelProperty("图文id")
    @NotBlank(message = "图文id不能为空")
    private String id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("图片")
    private String pic;

    @ApiModelProperty("图文内容，若不是图文，这里为空")
    private String content;

    @ApiModelProperty("图文内容长度")
    private int length=0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        if(StringUtils.isNotBlank(pic)){
            this.pic = pic;
        }
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLength() {
        return length;
    }

    public void setLength(Integer length) {
        if(length!=null){
            this.length = length;
        }
    }
}
