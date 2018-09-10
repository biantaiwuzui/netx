package com.netx.searchengine.query;

import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;

/**
 * 公共搜索类
 * @Author 黎子安
 */
public class BaseSearchQuery {
    /**
     * 第几页，默认从1开始
     */
    private Integer currentPage = 1;

    /**
     * 从多少条开始，默认从0开始
     */
    private Integer from = 0;

    /**
     * 每次获取的数量
     */
    private Integer pageSize = 20;

    /**
     * 以该中心点为点搜索周围，由近到远
     */
    private GeoPoint centerGeoPoint;

    /**
     * 周围多少范围的单位
     */
    private DistanceUnit distanceUnit = DistanceUnit.KILOMETERS;

    /**
     * 范围搜索，比如 1km到5km, 如果是 500km以内，则min=0, max = 500
     */
    private double minDistance = 0;
    private double maxDistance = 20000;

    public GeoPoint getCenterGeoPoint() {
        return centerGeoPoint;
    }

    public void setCenterGeoPoint(GeoPoint centerGeoPoint) {
        this.centerGeoPoint = centerGeoPoint;
    }

    public DistanceUnit getDistanceUnit() {
        return distanceUnit;
    }

    public void setDistanceUnit(DistanceUnit distanceUnit) {
        this.distanceUnit = distanceUnit;
    }

    public double getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(Double minDistance) {
        if(minDistance!=null){
            this.minDistance = minDistance;
        }
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(Double maxDistance) {
        if(maxDistance!=null){
            this.maxDistance = maxDistance;
        }
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * 设置分页
     * @param currentPage
     * @param pageSize
     */
    public void setPage(Integer currentPage,Integer pageSize){
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.from = (currentPage-1)*pageSize;
    }
}
