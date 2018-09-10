package com.netx.searchengine.query;

import com.netx.searchengine.common.LastAscQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CloudZou on 2/9/18.
 */
public class WishSearchQuery extends BaseSearchQuery{

    /**
     * 心愿标签
     */
    private List<String> wishLabels = new ArrayList<>();

    /**
     * 用户id
     */
    private String userId;

    /**
     * 心愿主题
     */
    private String title;

    /**
     * 需求状态:
     * 1.已发布
     * 2.已取消
     * 3.已关闭，即推荐人数不足50%
     * 4.推荐成功
     * 5.已失败，即筹款目标未达成
     * 6.筹集目标达成，即心愿发起成功
     * 7.已完成，即金额使用完毕
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

    private Boolean isLock;

    /**
     * 距离之前的排序
     */
    private List<LastAscQuery> fristAscQueries = new ArrayList<>();

    /**
     * 距离之后的排序
     */
    private List<LastAscQuery> lastAscQueries = new ArrayList<>();

    public List<String> getWishLabels() {
        return wishLabels;
    }

    public void setWishLabels(List<String> wishLabels) {
        this.wishLabels = wishLabels;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Boolean getLock() {
        return isLock;
    }

    public void setLock(Boolean lock) {
        isLock = lock;
    }
}
