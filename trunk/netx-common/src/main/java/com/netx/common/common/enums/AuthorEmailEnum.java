package com.netx.common.common.enums;

public enum AuthorEmailEnum {
    ZI_AN("子安","1163337644@qq.com"),
    LI_NAN("礼南","1294235127@qq.com"),
    ZHENG_JUE("首东","1807003524@qq.com"),
    DAI_HO("代红","545242724@qq.com"),
    WEN_YI("汶苡","1696337265@qq.com"),
    FRWIN("锐荣","904881067@qq.com"),
    DIAN_QV("典渠","1342439191@qq.com"),
    SHI_JIE("世杰","741940091@qq.com");

    private String name;

    private String email;

    AuthorEmailEnum(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
