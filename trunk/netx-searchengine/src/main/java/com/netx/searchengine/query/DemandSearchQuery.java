package com.netx.searchengine.query;

import com.netx.searchengine.common.LastAscQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CloudZou on 2/9/18.
 */
public class DemandSearchQuery extends BaseSearchQuery{

    /**
     * 需求发起者id
     */
    private String userId;

    /**
     * 需求主题
     */
    private String title;

    /**
     * 需求标签
     */
    private List<String> demandLabels = new ArrayList<>();

    /**
     * 需求分类:
     * 1.技能
     * 2.才艺
     * 3.知识
     * 4.资源
     */
    private Integer demandType;

    /**
     * 需求状态:
     * 1.已发布
     * 2.已取消
     * 3.已关闭
     */
    private Integer status;

    /**
     * 是否在线
     */
    private Boolean isLogin;

    /**
     * 经度
     */
    private double lon;

    /**
     * 纬度
     */
    private double lat;

    /**
     * 发布网信数量
     */
    private Integer creditSum;

    /**
     * 距离之前的排序
     */
    private List<LastAscQuery> fristAscQueries = new ArrayList<>();

    /**
     * 距离之后的排序
     */
    private List<LastAscQuery> lastAscQueries = new ArrayList<>();

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getDemandLabels() {
        return demandLabels;
    }

    public void setDemandLabels(List<String> demandLabels) {
        this.demandLabels = demandLabels;
    }

    public Integer getDemandType() {
        return demandType;
    }

    public void setDemandType(Integer demandType) {
        this.demandType = demandType;
    }

    public Boolean getLogin() {
        return isLogin;
    }

    public void setLogin(Boolean login) {
        isLogin = login;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public List<LastAscQuery> getFristAscQueries() {
        return fristAscQueries;
    }

    public void setFristAscQueries(List<LastAscQuery> fristAscQueries) {
        this.fristAscQueries = fristAscQueries;
    }

    public List<LastAscQuery> getLastAscQueries() {
        return lastAscQueries;
    }

    public void setLastAscQueries(List<LastAscQuery> lastAscQueries) {
        this.lastAscQueries = lastAscQueries;
    }

    public void addLastAscQuery(LastAscQuery lastAscQuery){
        this.lastAscQueries.add(lastAscQuery);
    }

    public void addFristAscQueries(LastAscQuery fristAscQueries){
        this.fristAscQueries.add(fristAscQueries);
    }

    public Integer getCreditSum() {
        return creditSum;
    }

    public void setCreditSum(Integer creditSum) {
        this.creditSum = creditSum;
    }
}
