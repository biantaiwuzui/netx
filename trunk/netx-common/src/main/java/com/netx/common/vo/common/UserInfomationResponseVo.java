package com.netx.common.vo.common;

/**
 * 仲裁用户查询vo
 * create by haojun
 * date 2017.9.28
 */
public class UserInfomationResponseVo {

    private String nickname;

    private String headImgUrl;

    private String sex;

    private Integer age;

    private Integer credit;

    private Integer lv;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Integer getLv() {
        return lv;
    }

    public void setLv(Integer lv) {
        this.lv = lv;
    }

    public String getHeadImgUrl() { return headImgUrl; }

    public void setHeadImgUrl(String headImgUrl) { this.headImgUrl = headImgUrl; }
}
