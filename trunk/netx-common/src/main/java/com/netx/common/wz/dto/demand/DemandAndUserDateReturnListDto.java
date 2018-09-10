package com.netx.common.wz.dto.demand;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class DemandAndUserDateReturnListDto {
    private String id;
    private String userId;
    private String title;
    private Integer demandType;
    private String demandLabel;
    private Boolean isOpenEnded;
    private Long startAt;
    private Long endAt;
    private String unit;
    private String about;
    private Integer amount;
    private Integer obj;
    private String objList;
    private String address;
    private BigDecimal lon;
    private BigDecimal lat;
    private String description;
    private String pic;
    private String pic2;
    private String orderIds;
    private BigDecimal orderPrice;
    private Boolean isPickUp;
    private BigDecimal wage;
    private Boolean isEachWage;
    private BigDecimal bail;


    @ApiModelProperty(value = "是否支付（托管）")
    private Boolean isPay;
}
