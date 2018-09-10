package com.netx.shopping.vo;

import com.netx.common.user.dto.common.CommonListDto;
import io.swagger.annotations.ApiModelProperty;
import org.elasticsearch.common.recycler.Recycler;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


public class QueryBusinessProductRequestDto extends CommonListDto {

    @ApiModelProperty("商家名称")
    private String merchantName;

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("商品状态")
    private Integer onlineStatus;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }
}
