package com.netx.common.vo.business;

import com.netx.common.vo.common.PageRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created By wj.liu
 * Description: 获取商家列表请求参数对象
 * Date: 2017-09-13
 */
@ApiModel
public class GetSellerListRequestDto extends PageRequestDto{

    @ApiModelProperty("商家名称")
    private String name;

    @ApiModelProperty("类别id，可多个")
    private List<String> categoryId;

    @ApiModelProperty("根据省份查询，可多个")
    private List<String> provinceCode;

    @ApiModelProperty("根据市查询，可多个")
    private List<String> cityCode;

    @ApiModelProperty("根据区/县查询，可多个")
    private List<String> areaCode;

    @ApiModelProperty("根据访问量查询")
    private Integer minVisitNum;
    private Integer maxVisitNum;

    @ApiModelProperty("搜索距离，不限传null")
    private Double length;

    @ApiModelProperty("是否首页请求：前端不用传值，供后端判断调用" )
    private Integer isHomerPageRepuest;

    @ApiModelProperty("排序：<br>" +
            "1.综合 <br>" +
            "2.距离 <br>" +
            "3.信用 <br>" +
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

    public Integer getMinVisitNum() {
        return minVisitNum;
    }

    public void setMinVisitNum(Integer minVisitNum) {
        this.minVisitNum = minVisitNum;
    }

    public Integer getMaxVisitNum() {
        return maxVisitNum;
    }

    public void setMaxVisitNum(Integer maxVisitNum) {
        this.maxVisitNum = maxVisitNum;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Integer getIsHomerPageRepuest() {
        return isHomerPageRepuest;
    }

    public void setIsHomerPageRepuest(Integer isHomerPageRepuest) {
        this.isHomerPageRepuest = isHomerPageRepuest;
    }
}
