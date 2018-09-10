package com.netx.searchengine.query;

import com.netx.searchengine.common.LastAscQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CloudZou on 2/9/18.
 */
public class SkillSearchQuery extends BaseSearchQuery{

    /**
     * 技能标签
     */
    private List<String> skillLabels = new ArrayList<>();

    /**
     * 水平标签
     */
    private List<String> levels = new ArrayList<>();

    /**
     * 状态
     */
    private Integer status;

    /**
     * 用户id
     */
    private String userId;

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
     * 发布的网信数量
     */
    private Integer creditSum;

    /**
     * 技能描述
     */
    private String description;

    /**
     * 距离之前的排序
     */
    private List<LastAscQuery> fristAscQueries = new ArrayList<>();

    /**
     * 距离之后的排序
     */
    private List<LastAscQuery> lastAscQueries = new ArrayList<>();

    public List<String> getSkillLabels() {
        return skillLabels;
    }

    public void setSkillLabels(List<String> skillLabels) {
        this.skillLabels = skillLabels;
    }

    public List<String> getLevels() {
        return levels;
    }

    public void setLevels(List<String> levels) {
        this.levels = levels;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getLogin() {
        return isLogin;
    }

    public void setLogin(Boolean login) {
        isLogin = login;
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

    public Integer getCreditSum() {
        return creditSum;
    }

    public void setCreditSum(Integer creditSum) {
        this.creditSum = creditSum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
