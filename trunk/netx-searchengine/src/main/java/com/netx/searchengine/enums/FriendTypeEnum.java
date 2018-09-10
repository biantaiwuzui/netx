package com.netx.searchengine.enums;

public enum FriendTypeEnum {
    /**
     * 校友：文化教育中，有相同学校名称的人，由近及远（如搜索者没有填写自己的学校，则不可以搜索）【即搜索为空】
     * 同事：工作经历中，有相同单位名称的人，由近及远（如搜索者没有填写自己的学校，则不可以搜索）【即搜索为空】
     * 美丽：外貌中，有含”美丽“、”漂亮“文字标签的人，由近及远
     * 帅气：外貌中，有含”帅气“、”英俊“文字标签的人，由近及远
     * 高管：工作经历中，职务中包含”总“字的人，由近及远
     * 多金：收入由高到低，由近及远
     * 未婚：情感中，有包含”未婚“就文字的标签的异性，由近及远
     * 博学：学历为本科、学士、硕士、博士的人，由近及远
     * 同龄之缘：年龄相差5岁的异性，由近及远（如搜索者没有填写自己的生日，则不可以搜索）【即搜索为空】
     * 共同爱好：兴趣爱好中有相同标签的人，由近及远（如搜索者没有填写自己的兴趣爱好详情，则不可以搜索）【即搜索为空】
     * 同乡近邻：家乡在同一个地方的人，由近及远（如搜索者没有填写自己的家乡，则不可以搜索）【即搜索为空】
     */
    SCHOOL_TYPE("school","校友","文化教育中，有相同学校名称的人",true),
    COMPANY_TYPE("company","同事","工作经历中，有相同单位名称的人",true),
    BEAUTIFUL_TYPE("appearance","美丽","外貌中，有含”美丽“、”漂亮“文字标签的人",false),
    HANDSOME_TYPE("appearance","帅气","外貌中，有含”帅气“、”英俊“文字标签的人",false),
    EXECUTIVE_TYPE("topProfession","高管","工作经历中，职务中包含”总“字的人",false),
    RICH_TYPE("maxIncome","多金","收入由高到低",false),
    UNMARRIED_TYPE("emotion","未婚","情感中，有包含”未婚“就文字的标签的异性",true),
    ERUDITE_TYPE("degreeItem","博学","学历为本科、学士、硕士、博士的人",false),
    SAME_AGE_TYPE("birthday","同龄之缘","年龄相差5岁的异性",true),
    COMMON_HOBBY_TYPE("interestDetails","共同爱好","兴趣爱好中有相同标签的人",true),
    SAME_HOMETOWN_TYPE("homeTown","同乡近邻","家乡在同一个地方的人",true);

    private String key;//搜索键值
    private String name;//键名
    private String describe;//描述
    private Boolean isLogin;//是否需要登录

    FriendTypeEnum(String key, String name, String describe,Boolean isLogin) {
        this.key = key;
        this.name = name;
        this.describe = describe;
        this.isLogin = isLogin;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getDescribe() {
        return describe;
    }

    public Boolean getLogin() {
        return isLogin;
    }

    public void setLogin(Boolean login) {
        isLogin = login;
    }
}
