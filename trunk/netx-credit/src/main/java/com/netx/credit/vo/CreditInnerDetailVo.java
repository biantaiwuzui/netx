package com.netx.credit.vo;

import io.swagger.annotations.ApiModelProperty;


import java.util.List;

/**
 * 商家人员内部认购信息
 */
public class CreditInnerDetailVo {



    @ApiModelProperty("商家名称")
    private String merchantName;

    @ApiModelProperty("网信标签")
    private List<String> tags;

    @ApiModelProperty("商家分类")
    private List<String> categorys;

    @ApiModelProperty("商家logo")
    private  List<String> logoImagesUrl;

    @ApiModelProperty("人员类型")
    private List<CreditMerchantUserTypeDto> merchantUserTypeDtos;

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getCategorys() {
        return categorys;
    }

    public void setCategorys(List<String> categorys) {
        this.categorys = categorys;
    }

    public List<String> getLogoImagesUrl() {
        return logoImagesUrl;
    }

    public void setLogoImagesUrl(List<String> logoImagesUrl) {
        this.logoImagesUrl = logoImagesUrl;
    }

    public List<CreditMerchantUserTypeDto> getMerchantUserTypeDtos() {
        return merchantUserTypeDtos;
    }

    public void setMerchantUserTypeDtos(List<CreditMerchantUserTypeDto> merchantUserTypeDtos) {
        this.merchantUserTypeDtos = merchantUserTypeDtos;
    }
}
