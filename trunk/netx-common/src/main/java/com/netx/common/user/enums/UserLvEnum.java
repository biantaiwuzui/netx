package com.netx.common.user.enums;

import java.math.BigDecimal;

/**
 * 用户lv等级枚举
 * 根据积分总值而定lv等级
 * @author 李卓
 */
public enum UserLvEnum {

    LV1("200及以下", BigDecimal.valueOf(200), 1),
    LV2("201~500", BigDecimal.valueOf(500), 2),
    LV3("501~800", BigDecimal.valueOf(800), 3),
    LV4("801~1,000", BigDecimal.valueOf(1000), 4),
    LV5("1,001~1,500", BigDecimal.valueOf(1500), 5),
    LV6("1,501~2,000", BigDecimal.valueOf(2000), 6),
    LV7("2,001~3,000", BigDecimal.valueOf(3000), 7),
    LV8("3,001~5,000", BigDecimal.valueOf(5000), 8),
    LV9("5,001~10,000", BigDecimal.valueOf(10000), 9),
    LV10("10,001及以上", BigDecimal.valueOf(10001), 10);//最后一个比较特殊，score属性代表最小值

    public static Integer getLvByScore(BigDecimal score){
        int length = UserLvEnum.values().length;
        //循环，若积分标准大于或等于积分值，则返回当前lv，若小于，进入下一循环，若进入了lv10枚举，则直接返回lv10
        for(int i=0; i<length; i++){
            UserLvEnum userLvEnum = UserLvEnum.values()[i];
            if(userLvEnum.lv == LV10.lv){ return userLvEnum.lv; }
            if(userLvEnum.score.compareTo(score) != -1){
                return userLvEnum.lv;
            }
        }
        return 1;
    }

    UserLvEnum(String name, BigDecimal score, Integer lv){
        this.name = name;
        this.score = score;
        this.lv = lv;
    };

    private String name;
    //最大积分值（在lv10里比较特殊，代表最小积分值）
    private BigDecimal score;

    private Integer lv;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public Integer getLv() {
        return lv;
    }

    public void setLv(Integer lv) {
        this.lv = lv;
    }
}
