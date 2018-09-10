package com.netx.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * 管理员:后台查询待审核仲裁列表VO
 * @Author hjMao
 */
@ApiModel
public class ArbitrationSelectByTypeVo extends PageRequestDto{

    @ApiModelProperty("仲裁类型:null.全部投诉\t1.订单仲裁\t2.其他（网号投诉）\t3.需求投诉\t4.心愿投诉\t5.技能投诉\n")
    private Integer type;

    @ApiModelProperty("状态： \n" +
            "1.未处理 \n" +
            "2.已处理 \n" +
            "3.已申诉 \n" +
            "4.已裁决 \n" +
            "5.拒绝受理 \n")
    private Integer statusCode;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
}
