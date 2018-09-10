package com.netx.common.user.enums;

/**
 * @Author haojun
 * @Date create by 2017/10/1
 */
public enum ArticleIntroduceDateTypeEnum {
    ARTICLE_INTRODUCE_DATE_TYPE_HOUR("小时",1),
    ARTICLE_INTRODUCE_DATE_TYPE_DAY("天",2),
    ARTICLE_INTRODUCE_DATE_TYPE_WEEK("周",3),
    ARTICLE_INTRODUCE_DATE_TYPE_MONTH("月",4);

    private String descrition;
    private Integer value;

    ArticleIntroduceDateTypeEnum(String description,Integer value)
    {
        this.descrition=description;
        this.value=value;
    }

    public String getDescrition() {
        return descrition;
    }

    public void setDescrition(String descrition) {
        this.descrition = descrition;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
