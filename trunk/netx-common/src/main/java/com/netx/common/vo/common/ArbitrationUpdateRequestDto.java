package com.netx.common.vo.common;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * 投诉/申诉修改dto
 * @Author haojun
 * @Date create by 2017/9/30
 */
@ApiModel
public class ArbitrationUpdateRequestDto {
    /**
     * 对应manager_arbitration的记录的主键id
     */
    @NotBlank(message = "仲裁主键id不能为空")
    @ApiModelProperty("arbitrationId主键")
    private String arbitrationId;

    /**
     * 修改仲裁信息的话,用户自己修改
     * 根据用户id判断是投诉方还是被投诉方
     * 然后修改字段,更新表数据
     * hidden隐藏域
     */
    @ApiModelProperty("用户Id")
    @NotBlank(message = "用户Id不能为空")
    private String userId;
    /**
     * 非1,3，不能进行修改操作
     */
    @NotNull(message = "状态码不能为空")
    @ApiModelProperty("状态码")
    private Integer statusCode;

    @ApiModelProperty("申诉人证据图片")
    private String appealSrcUrl;

    @ApiModelProperty("申诉人描述")
    private String descriptions;

    @ApiModelProperty("投诉的主题")
    private String theme;

    @ApiModelProperty("投诉的理由")
    private String reason;

    public String getArbitrationId() {
        return arbitrationId;
    }

    public void setArbitrationId(String arbitrationId) {
        this.arbitrationId = arbitrationId;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getAppealSrcUrl() {
        return appealSrcUrl.trim();
    }

    public void setAppealSrcUrl(String appealSrcUrl) {
        this.appealSrcUrl = appealSrcUrl;
    }

    public String getDescriptions() {
        return descriptions.trim();
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public String getTheme() {
        return theme.trim();
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getReason() {
        return reason.trim();
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
