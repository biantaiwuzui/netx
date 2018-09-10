package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Create on 17-10-20
 *
 * @author wongloong
 */
@ApiModel
public class QueryFriendListRequestDto {
    private String userId;

    @ApiModelProperty(notes = "过滤好友请求的类别,如果无过滤需求不要传这个字段,1:心愿过滤,其他有需求的时候再加")
    private String fitlerName;

    @ApiModelProperty(value = "每页记录数", required = true, example = "10")
    @NotNull(message = "分页属性不能为空")
    private Integer size = 10;
    @ApiModelProperty(value = "当前页码", required = true, example = "1")
    @NotNull(message = "分页属性不能为空")
    private Integer current = 1;

    public String getFitlerName() {
        return fitlerName;
    }

    public void setFitlerName(String fitlerName) {
        this.fitlerName = fitlerName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }
}
