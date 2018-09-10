package com.netx.common.user.enums;

/**
 * 我的资料计分
 * @author 李卓
 */
public enum UserInformationScoreEnum {
    //一共36项，名称，分数
    //必选项、照片、自我介绍、详情
    NICKNAME("昵称", 5), SEX("性别", 5), AGE("年龄", 5), BIRTHDAY("生日", 5),
    OFTEN_IN("常驻", 5), HOMETOWN("家乡", 5), DISPOSITION("性格", 5),
    PHOTO("照片", 5), INTRODUCE("自我介绍", 5), DESCRIPTION("图文详情", 5),
    //认证部分
    PHONE("手机认证", 3), IDCARD("身份认证", 2), VIDEO("视频认证", 1),
    CAR("车辆认证", 3), HOUSE("房产认证", 3), DEGREE("学历认证", 3),
    //综合部分
    EDUCATION_LABEL("文化教育-概况", 2), EDUCATION_SCHOOL("文化教育-学校", 2), EDUCATION_DEGREE("文化教育-学位", 2),
    PROFESSION_LABEL("工作经历-职业", 2), PROFESSION_COMPANY("工作经历-单位", 2), PROFESSION_TOPPROFESSION("工作经历-最高职位（职务）", 2),
    INTEREST_LABEL("兴趣爱好-概述（综评）", 2), INTEREST_TYPE("兴趣爱好-类别", 2), INTEREST_DETAIL("兴趣爱好-内容", 2),
    //地址、身高、外貌、体重
    ADDRESS("地址", 3), HEIGHT("身高", 2), WEIGHT("体重", 2), APPEARANCE("外貌", 2),
    //其余项
    ALREADY_TO("去过", 1), WANT_TO("想去", 1), INCOME("收入", 1), EMOTION("情感", 1),
    NATION("民族", 1), ANIMAL_SIGNS("属相", 1),STAR_SIGN("星座", 1), BLOOD_TYPE("血型", 1);

    UserInformationScoreEnum(String name, Integer score){
        this.name = name;
        this.score = score;
    }

    private String name;
    private Integer score;

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
