package com.netx.common.vo.business;

import com.netx.common.vo.common.PageRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created ChenQian
 * Description: 商品列表请求参数对象（引擎）
 * Date: 2018-4-12
 */
@ApiModel
public class GetGoodsListRequestDto extends PageRequestDto{

    @ApiModelProperty("商品名称，模糊查询")
    private String name;

    @ApiModelProperty("商品类别id，可多个")
    private List<String> categoryId;

    @ApiModelProperty("根据省份查询，可多个")
    private List<String> provinceCode;

    @ApiModelProperty("根据市查询，可多个")
    private List<String> cityCode;

    @ApiModelProperty("根据区/县查询，可多个")
    private List<String> areaCode;

    @ApiModelProperty("商家id")
    private String merchantId;

    @ApiModelProperty("是否配送")
    private Boolean isDelivery;

    @ApiModelProperty("基础价格区间查询")
    private Long minPrice;
    private Long maxPrice;

    @ApiModelProperty("订单数量区间查询")
    private Long minOrderNum;
    private Long maxOrderNum;

    @ApiModelProperty("商品状态:上架:true/下架:false")
    private Boolean status = true;

    @ApiModelProperty("距离范围")
    private double length;
    //1.综合 2.销量 3.价格 4.支持网信
    @ApiModelProperty("排序：<br>" +
            "1.综合 <br>" +
            "2.销量 <br>" +
            "3.价格 <br>" +
            "4.支持网信")
    private Integer sort = 0;

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(List<String> categoryId) {
        this.categoryId = categoryId;
    }

    public List<String> getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(List<String> provinceCode) {
        this.provinceCode = provinceCode;
    }

    public List<String> getCityCode() {
        return cityCode;
    }

    public void setCityCode(List<String> cityCode) {
        this.cityCode = cityCode;
    }

    public List<String> getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(List<String> areaCode) {
        this.areaCode = areaCode;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public Boolean getDelivery() {
        return isDelivery;
    }

    public void setDelivery(Boolean delivery) {
        isDelivery = delivery;
    }

    public Long getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Long minPrice) {
        this.minPrice = minPrice;
    }

    public Long getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Long maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Long getMinOrderNum() {
        return minOrderNum;
    }

    public void setMinOrderNum(Long minOrderNum) {
        this.minOrderNum = minOrderNum;
    }

    public Long getMaxOrderNum() {
        return maxOrderNum;
    }

    public void setMaxOrderNum(Long maxOrderNum) {
        this.maxOrderNum = maxOrderNum;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }
}
