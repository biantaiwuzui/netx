package com.netx.shopping.vo;

import java.util.List;

public class PictureRequestDto {

    private List<String> id;

    private List<String> pictureUrl;

    public List<String> getId() {
        return id;
    }

    public void setId(List<String> id) {
        this.id = id;
    }

    public List<String> getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(List<String> pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
