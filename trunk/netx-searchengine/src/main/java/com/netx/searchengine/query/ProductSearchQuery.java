package com.netx.searchengine.query;

import com.netx.searchengine.common.LastAscQuery;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ChenQian
 * @since 2018-4-10
 */
public class ProductSearchQuery extends BaseSearchQuery{

    /**
     * 商品名称
     * 模糊查询
     */
    private String name;

    /**
     * 是否在线
     */
    private Boolean isLogin = null;

    /**
     * 类别查询
     */
    private List<String> categoryId = new ArrayList<>();

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
     * 商家id
     */
    private String merchantId;

    /**
     * 是否配送
     *
     */
    private Boolean isDelivery;

    /**
     * 基础价格区间查询
     */
    private Long minPrice;
    private Long maxPrice;

    /**
     * 成交量查询
     */
    private Long volume;

    /**
     * 商品状态
     * 1.上架
     * 2.下架
     */
    private Boolean onlineStatus = true;

    /**
     * 是否支持网信
     */
    private Boolean isHoldCredit = null;

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

    public Boolean getDelivery() {
        return isDelivery;
    }

    public void setDelivery(Boolean delivery) {
        isDelivery = delivery;
    }

    public void setMinPrice(Long minPrice) {
        this.minPrice = minPrice;
    }

    public void setMaxPrice(Long maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public Long getMinPrice() {
        return minPrice;
    }

    public Long getMaxPrice() {
        return maxPrice;
    }

    public Boolean getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Boolean onlineStatus) {
        this.onlineStatus = onlineStatus;
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

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
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
}
