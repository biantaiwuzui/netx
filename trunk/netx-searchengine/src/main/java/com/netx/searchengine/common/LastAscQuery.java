package com.netx.searchengine.common;

public class LastAscQuery {
    private String key;
    private Boolean isAsc;

    public LastAscQuery(String key, Boolean isAsc) {
        this.key = key;
        this.isAsc = isAsc;
    }

    public String getKey() {
        return key;
    }

    public Boolean getAsc() {
        return isAsc;
    }

}
