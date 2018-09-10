package com.netx.searchengine.query;

import com.netx.searchengine.common.LastAscQuery;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ChenQian
 * @since 2018-4-10
 */
public class SellerSearchQuery extends BaseSearchQuery{

    /**
     * 根据商家名称模糊查询
     */
    private String name;
    /**
     * 用户id
     */
    private String userId;

    /**
     * 类别查询
     */
    private List<String> categoryId = new ArrayList<>();

    /**
     * 是否在线
     */
    private Boolean isLogin = null;

    /**
     * 根据省份查询
     */
    private List<String> provinceCode = new ArrayList<>();

    /**
     * 根据市查询
     */
    private List<String> cityCode = new ArrayList<>();

    /**
     * 根据区/县查询
     */
    private List<String> areaCode = new ArrayList<>();

    /**
     * 商品描述
     */
    private String sellerDesc;

    /**
     * 根据访问量查询
     */
    private Integer minVisitNum;
    private Integer maxVisitNum;

    /**
     * 是否支持网信
     */
    private Boolean isHoldCredit;

    /**
     * 首单红包
     */
    private BigDecimal firstRate;

    /**
     * 距离之前的排序
     */
    private List<LastAscQuery> fristAscQueries = new ArrayList<>();

    /**
     * 距离之后的排序
     */
    private List<LastAscQuery> lastAscQueries = new ArrayList<>();

    /**
     * 是否完成缴费
     */
    private Integer payStatus;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSellerDesc() {
        return sellerDesc;
    }

    public void setSellerDesc(String sellerDesc) {
        this.sellerDesc = sellerDesc;
    }

    public List<String> getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(List<String> categoryId) {
        if(categoryId!=null && categoryId.size()>0){
            this.categoryId = categoryId;
        }
    }

    public void addCategoryId(String categoryId){
        this.categoryId.add(categoryId);
    }

    public List<String> getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(List<String> provinceCode) {
        if(provinceCode!=null && provinceCode.size()>0) {
            this.provinceCode = provinceCode;
        }
    }

    public void addProvinceCode(String provinceCode){
        this.provinceCode.add(provinceCode);
    }

    public List<String> getCityCode() {
        return cityCode;
    }

    public void setCityCode(List<String> cityCode) {
        if(cityCode!=null && cityCode.size()>0) {
            this.cityCode = cityCode;
        }
    }

    public void addCityCode(String cityCode){
        this.cityCode.add(cityCode);
    }

    public List<String> getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(List<String> areaCode) {
        if(areaCode!=null && areaCode.size()>0) {
            this.areaCode = areaCode;
        }
    }

    public void addAreaCode(String areaCode){
        this.areaCode.add(areaCode);
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<LastAscQuery> getLastAscQueries() {
        return lastAscQueries;
    }

    public void setLastAscQueries(List<LastAscQuery> lastAscQueries) {
        if(lastAscQueries!=null && lastAscQueries.size()>0){
            this.lastAscQueries = lastAscQueries;
        }
    }

    public void addLastAscQuery(LastAscQuery lastAscQuery){
        this.lastAscQueries.add(lastAscQuery);
    }

    public List<LastAscQuery> getFristAscQueries() {
        return fristAscQueries;
    }

    public void setFristAscQueries(List<LastAscQuery> fristAscQueries) {
        if(fristAscQueries!=null && fristAscQueries.size()>0){
            this.fristAscQueries = fristAscQueries;
        }
    }

    public void addFristAscQueries(LastAscQuery fristAscQueries){
        this.fristAscQueries.add(fristAscQueries);
    }


    public Boolean getLogin() {
        return isLogin;
    }

    public void setLogin(Boolean login) {
        if(login!=null){
            isLogin = login;
        }
    }

    public Boolean getHoldCredit() {
        return isHoldCredit;
    }

    public void setHoldCredit(Boolean holdCredit) {
        if(holdCredit!=null){
            isHoldCredit = holdCredit;
        }
    }

    public BigDecimal getFirstRate() {
        return firstRate;
    }

    public void setFirstRate(BigDecimal firstRate) {
        this.firstRate = firstRate;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }
}
