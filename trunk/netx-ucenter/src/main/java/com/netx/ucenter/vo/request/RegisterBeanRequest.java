package com.netx.ucenter.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ApiModel(value = "注册基础vo类")
public class RegisterBeanRequest {

    @ApiModelProperty(value = "头像")
    @NotBlank(message = "头像不能为空")
    private String headImg;

    @ApiModelProperty(value = "昵称")
    @NotBlank(message = "昵称不能为空")
    private String nickName;

    @ApiModelProperty(value = "性别")
    @NotBlank(message = "性别不能为空")
    @Length(max = 1,message = "性别格式不对")
    private String sex;

    /**
     * 经度
     */
    @ApiModelProperty(value = "经度")
    @NotNull(message = "经度不能为空")
    @Range(min = -180,max = 180,message = "经度输入不在范围内")
    private BigDecimal lon;

    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度")
    @NotNull(message = "纬度不能为空")
    @Range(min = -90,max = 90,message = "纬度输入不在范围内")
    private BigDecimal lat;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public BigDecimal getLon() {
        return lon;
    }

    public void setLon(BigDecimal lon) {
        this.lon = lon;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
