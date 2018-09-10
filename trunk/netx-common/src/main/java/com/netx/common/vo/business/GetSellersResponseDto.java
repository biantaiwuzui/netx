package com.netx.common.vo.business;
/**
 * Created by CloudZou on 2/6/18.
 * 给定时任务定时
 */
public class GetSellersResponseDto {
    private String id;

    private String categoryId;

    private String geoHash;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getGeoHash() {
        return geoHash;
    }

    public void setGeoHash(String geoHash) {
        this.geoHash = geoHash;
    }
}
