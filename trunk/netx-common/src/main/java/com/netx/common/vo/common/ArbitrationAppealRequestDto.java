package com.netx.common.vo.common;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@ApiModel
public class ArbitrationAppealRequestDto {

    /**
     * 对应投诉列表在数据库中的ID
     */
    @ApiModelProperty(" 对应投诉列表在数据库中的ID")
    @NotBlank
    private String id;

    /**
     * 申诉描述
     */
    @ApiModelProperty("申诉描述")
    @NotBlank
    private String descriptions;

    /**
     * 申诉证据URL
     */
    @ApiModelProperty("申诉证据")
    @NotBlank
    private String appealSrcUrl;

    @ApiModelProperty("申诉者用户id")
    @NotBlank(message = "申诉者用户id不能为空")
    private String userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public String getAppealSrcUrl() {
        return appealSrcUrl;
    }

    public void setAppealSrcUrl(String appealSrcUrl) {
        this.appealSrcUrl = appealSrcUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
