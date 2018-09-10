package com.netx.common.user.enums;

public enum FilteringKeyEnum {
    MEETING_FILTERING("/worth/worth/meeting/send",new String[]{"title","description"}),
    SKILL_FILTERING("/worth/worth/skill/publish",new String[]{"intr","description"}),
    WISH_FILTERING("/worth/worth/wish/publish",new String[]{"title","description"}),
    DEMAND_FILTERING("/worth/worth/demand/publish",new String[]{"title","description"}),
    SELLER_FILTERING("/shoppingmall/api/shoppingmall/seller/register",new String[]{"name","sellerDesc"}),
    GOODS_FILTERING("/shoppingmall/api/shoppingmall/goods/releaseGoods",new String[]{"name","description"});

    FilteringKeyEnum(String url, String[] keys) {
        this.url = url;
        this.keys = keys;
    }

    private String url;
    private String[] keys;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String[] getKeys() {
        return keys;
    }

    public void setKeys(String[] keys) {
        this.keys = keys;
    }
}
