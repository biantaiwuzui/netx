package com.netx.common.vo.business;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created By liwei
 * Description: 添加或修改商品包装明细请求参数
 * Date: 2017-09-14
 */
@ApiModel
public class AddGoodsPackageRequestDto {


    @ApiModelProperty("包装明细id")
    private String id;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("商品名称")
    @NotBlank(message = "商品名称不能为空")
    private String name;

    @ApiModelProperty("商品数量")
    @NotNull(message = "商品数量不能为空")
    @Min(value = 1, message = "数量必须大于0")
    private Integer quantity;

    @ApiModelProperty("单位名称")
    @NotBlank(message = "单位名称不能为空")
    private String unitName;

    @ApiModelProperty("商品规格")
    @NotBlank(message = "规格不能为空")
    private String spec;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }
}
