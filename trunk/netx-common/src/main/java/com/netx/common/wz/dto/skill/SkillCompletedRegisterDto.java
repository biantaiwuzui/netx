package com.netx.common.wz.dto.skill;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

public class SkillCompletedRegisterDto {


    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "网号")
    private String userNumber;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "头像")
    private String headImgUrl;
    
    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "信用")
    private Integer credit;

    @ApiModelProperty(value = "等级")
    private Integer lv;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "预约者ID")
    private  String userId;

    @ApiModelProperty(value = "技能ID")
    private String SkillId;

    @ApiModelProperty(value = "预约费用")
    private BigDecimal amount;
    
    
    
}
