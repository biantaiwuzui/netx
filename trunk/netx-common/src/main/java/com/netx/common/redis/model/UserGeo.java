package com.netx.common.redis.model;

public class UserGeo {
    private String userId;
    private Double lon;

    @Override
    public String toString() {
        return "UserGeo{" +
                "userId='" + userId + '\'' +
                ", lon=" + lon +
                ", lat=" + lat +
                '}';
    }

    private Double lat;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }
}
