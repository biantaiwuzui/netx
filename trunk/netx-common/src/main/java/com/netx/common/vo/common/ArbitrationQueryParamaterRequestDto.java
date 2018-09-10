package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;


/**
 * 仲裁查询参数请求dto
 * @Author haojun
 * @Date create by 2017/9/28
 */

@ApiModel
public class ArbitrationQueryParamaterRequestDto {

    @ApiModelProperty("查询方式:"+
        "0----userId不能为空\n"+
            "1------nickname不能为空\n"+
            "2------网号不能为空\n"+
            "3-------输入用户ID不能为空\n"+
            "4-------操纵者ID不能为空"
    )
    @NotNull(message = "查询方式不能为空")
    @Range(min = 0, max = 4,message = "查询方式的值范围:0-3,非空")
    private Integer queryType=0;


    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("用户网号")
    private String userNetworkNum;

    @ApiModelProperty("返回值方式\n"+
            "0------全部内容\n"+
            "1------返回可修改仲裁信息\n"+
            "2-------返回只读仲裁信息")
    @NotNull(message = "返回值类型不能为空")
    @Range(min = 0,max = 2,message = "返回域值范围:0-2,非空")
    private Integer returnType;

    @ApiModelProperty("当前用户ID,次值由前段传递")
    private String userId;

    @ApiModelProperty("需要查询的用户ID")
    private String inputUserId;


    @ApiModelProperty("操纵者ID")
    private String opUserId;



    public Integer getQueryType() {
        return queryType;
    }

    public void setQueryType(Integer queryType) {
        this.queryType = queryType;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserNetworkNum() {
        return userNetworkNum;
    }

    public void setUserNetworkNum(String userNetworkNum) {
        this.userNetworkNum = userNetworkNum;
    }

    public Integer getReturnType() {
        return returnType;
    }

    public void setReturnType(Integer returnType) {
        this.returnType = returnType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getInputUserId() {
        return inputUserId;
    }

    public void setInputUserId(String inputUserId) {
        this.inputUserId = inputUserId;
    }

    public String getOpUserId() {
        return opUserId;
    }

    public void setOpUserId(String opUserId) {
        this.opUserId = opUserId;
    }
}
