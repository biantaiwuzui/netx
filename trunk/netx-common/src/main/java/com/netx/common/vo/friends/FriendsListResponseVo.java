package com.netx.common.vo.friends;

/**
 * Create by wongloong on 17-8-28
 */
public class FriendsListResponseVo {
    private String id;
    private String nickname;
    private String userNumber;
    private String sex;
    private Integer age;
    private Integer level;
    private String url;
    private String tags;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "FriendsListResponseVo{" +
                "id='" + id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", userNumber='" + userNumber + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", level=" + level +
                ", url='" + url + '\'' +
                ", tags='" + tags + '\'' +
                '}';
    }
}