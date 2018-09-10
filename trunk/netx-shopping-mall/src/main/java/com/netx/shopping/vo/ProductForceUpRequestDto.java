package com.netx.shopping.vo;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * boss
 *强制商品上架
 * @author bxt
 * @since 2018-05-25
 */
public class ProductForceUpRequestDto {

    @ApiModelProperty("商品id")
    @NotBlank(message = "商品id不能为空")
    private String productId;

    @ApiModelProperty("操作者id")
    @NotBlank(message = "操作者不能为空")
    private String userId;

    @ApiModelProperty("商品状态,1.上架 2.下架 3.强制下架")
    @NotNull(message = "状态值不能为空")
    @Max(message = "状态值最大为3",value = 3)
    @Min(message = "状态值最小为1",value = 1)
    private Integer onlineStatus;

    @ApiModelProperty("上架/下架原因")
    @NotBlank(message = "原因不能为空")
    private String reason;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "ProductManagement{" +
                ", productId=" + productId +
                ", userId=" + userId +
                ", onlineStatus=" + onlineStatus +
                ", reason=" + reason +
                "}";
    }
}
