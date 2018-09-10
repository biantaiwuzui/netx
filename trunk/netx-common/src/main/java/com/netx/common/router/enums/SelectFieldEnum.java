package com.netx.common.router.enums;

/**
 * 查询字段枚举
 * 需要哪些字段数据，就用哪个枚举值
 */
public enum SelectFieldEnum{

    //========== 1、用户表 ==========
    //------ 基本信息 ------
    USER_NUMBER("网号", "user_number", "user"),
    NICKNAME("昵称", "nickname", "user"),
    SEX("性别", "sex", "user"),
    LV("等级lv", "lv", "user"),
    USER_PROFILE_SCORE("用户资料分值", "user_profile_pcore", "user"),

    USER_ID("用户id", "id", "own"),//特殊

    //------ 认证信息 ------
    MOBILE("手机号", "mobile", "user"),
    /*ID_NUMBER("身份证号", "idNumber", "user"),
    VIDEO("视频信息", "video", "user"),
    CAR("车辆信息", "car", "user"),
    HOUSE("房产信息", "house", "user"),
    DEGREE("学历信息", "degree", "user"),*/

    //------ 综合信息 ------
    /*EDUCATION_LABEL("文化教育概况", "educationLabel", "user"),
    PROFESSION_LABEL("工作经历概况", "professionLabel", "user"),
    INTEREST_LABEL("兴趣爱好概况", "interestLabel", "user"),*/

    //------ 网名信息 ------
    SCORE("总积分", "score", "user"),
    CREDIT("总信用", "credit", "user"),
    VALUE("总身价", "value", "user"),
    INCOME("总收益", "income", "user"),
    CONTRIBUTION("总贡献", "contribution", "user"),

    //------ 其他信息 ------
    LOCK_VERSION("乐观锁", "lockVersion", "user"),
    LAST_LOGIN_AT("最后登录时间", "lastLoginAt", "user"),
    //LOGIN_DAYS("连续登录天数", "loginDays", "user"),
    GIFT_SETTING("礼物设置", "giftSetting", "user"),
    INVITATION_SETTING("邀请设置", "invitationSetting", "user"),
    ROLE("角色", "role", "user"),

    //========== 2、用户详情表 ==========
    OFTEN_IN("常驻", "oftenIn", "UserProfile"),
    DISPOSITION("性格", "disposition", "UserProfile"),

    //========== 3、用户照片表 ==========
    HEAD_IMG_URL("用户头像", "url", "UserPhoto");


    SelectFieldEnum(String name, String value, String model){
        this.name = name;
        this.value = value;
        this.model = model;
    }
    private String name;
    private String value;
    private String model;

    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

}
