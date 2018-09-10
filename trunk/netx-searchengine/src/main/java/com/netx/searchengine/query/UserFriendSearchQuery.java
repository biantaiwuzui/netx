package com.netx.searchengine.query;

import com.netx.searchengine.common.LastAscQuery;
import com.netx.searchengine.common.Query;
import com.netx.searchengine.enums.FriendTypeEnum;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CloudZou on 2/9/18.
 */
public class UserFriendSearchQuery extends BaseSearchQuery {

    Map<FriendTypeEnum,Query> queryMap = null;

    /**
     * 默认不分附近
     */
    private Boolean isNearly;

    /**
     * 不需要这个用户id
     */
    private String excludeUserId = null;

    /**
     * 模糊查询
     * 用户id
     */
    private String userId;

    /**
     * 网号模糊查询
     */
    private String userNumber;

    /**
     * 年龄段搜索，minAge=0且maxAge=0，表示年龄段不限
     */
    private Integer minAge = 0 ;
    private Integer maxAge = 0 ;

    /**
     * 性别空为不限
     */
    private String sex;

    /**
     * 昵称模糊查询
     */
    private String nickname;

    /**
     * 在线时间(min)
     */
    private Long onlineTime;

    /**
     * 情感
     */
    private String emotion;

    /**
     * 是否在线
     */
    private Boolean isLogin;

    /**
     * 学历
     */
    private String degree;

    /**
     * 身高段搜索，minHeight=0且maxHeight=0，表示身高段不限
     */
    private Integer maxHeight = 0;
    private Integer minHeight = 0;

    /**
     * 体重段搜索，minWeight=0且maxWeight=0，表示体重段不限
     */
    private Integer maxWeight = 0;
    private Integer minWeight = 0;

    /**
     * 收入段搜索，minIncome=0且maxIncome=0，表示收入段不限
     */
    private Integer maxIncome = 0;
    private Integer minIncome = 0;

    /**
     * 民族
     */
    private String nation;

    /**
     * 属相
     */
    private String animalSigns;

    /**
     * 星座
     */
    private String starSign;

    /**
     * 学校
     */
    private List<String> school = new ArrayList<>();

    /**
     * 公司
     */
    private List<String> company = new ArrayList<>();

    /**
     * 兴趣爱好
     */
    private List<String> interest = new ArrayList<>();

    private List<LastAscQuery> lastAscQueries = new ArrayList<>();

    public Map<FriendTypeEnum, Query> getQueryMap() {
        return queryMap;
    }

    public void setQueryMap(Map<FriendTypeEnum, Query> queryMap) {
        if(queryMap!=null){
            this.queryMap = queryMap;
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        if(minAge!=null){
            this.minAge = minAge;
        }
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
        if(maxAge!=null){
            this.maxAge = maxAge;
        }
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Long getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(Long onlineTime) {
        this.onlineTime = onlineTime;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public Integer getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(Integer maxHeight) {
        this.maxHeight = maxHeight;
    }

    public Integer getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(Integer minHeight) {
        if(minHeight!=null){
            this.minHeight = minHeight;
        }
    }

    public Integer getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(Integer maxWeight) {
        if(maxWeight!=null) {
            this.maxWeight = maxWeight;
        }
    }

    public Integer getMinWeight() {
        return minWeight;
    }

    public void setMinWeight(Integer minWeight) {
        if(minWeight!=null){
            this.minWeight = minWeight;
        }
    }

    public Integer getMaxIncome() {
        return maxIncome;
    }

    public void setMaxIncome(Integer maxIncome) {
        if(maxIncome!=null){
            this.maxIncome = maxIncome;
        }
    }

    public Integer getMinIncome() {
        return minIncome;
    }

    public void setMinIncome(Integer minIncome) {
        if(minIncome!=null){
            this.minIncome = minIncome;
        }
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getAnimalSigns() {
        return animalSigns;
    }

    public void setAnimalSigns(String animalSigns) {
        this.animalSigns = animalSigns;
    }

    public String getStarSign() {
        return starSign;
    }

    public void setStarSign(String starSign) {
        this.starSign = starSign;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public Boolean getNearly() {
        return isNearly;
    }

    public void setNearly(Boolean nearly) {
        isNearly = nearly;
    }

    public String getExcludeUserId() {
        return excludeUserId;
    }

    public void setExcludeUserId(String excludeUserId) {
        if(excludeUserId!=null){
            this.excludeUserId = excludeUserId;
        }
    }

    public Boolean getLogin() {
        return isLogin;
    }

    public void setLogin(Boolean login) {
        isLogin = login;
    }

    public List<String> getSchool() {
        return school;
    }

    public void setSchool(List<String> school) {
        if(school!=null && school.size()>0){
            this.school = school;
        }
    }

    public List<String> getCompany() {
        return company;
    }

    public void setCompany(List<String> company) {
        if(company!=null && company.size()>0){
            this.company = company;
        }
    }

    public List<String> getInterest() {
        return interest;
    }

    public void setInterest(List<String> interest) {
        if(interest!=null && interest.size()>0){
            this.interest = interest;
        }
    }

    public List<LastAscQuery> getLastAscQueries() {
        return lastAscQueries;
    }

    public void setLastAscQueries(List<LastAscQuery> lastAscQueries) {
        if(lastAscQueries!=null && lastAscQueries.size()>0){
            this.lastAscQueries = lastAscQueries;
        }
    }

    public void addLastAscQuery(LastAscQuery lastAscQuery){
        this.lastAscQueries.add(lastAscQuery);
    }

    public void createQueryMap(FriendTypeEnum friendTypeEnum,Query query){
        this.queryMap = new HashMap<>();
        queryMap.put(friendTypeEnum,query);
    }

    @Override
    public String toString() {
        return "UserFriendSearchQuery{" 
                + "queryMap=" + queryMap 
                + ", isNearly=" + isNearly 
                + ", excludeUserId='" + excludeUserId 
                + ", currentPage=" + getCurrentPage()
                + ", from=" + getFrom()
                + ", pageSize=" + getPageSize()
                + ", userId='" + userId 
                + ", userNumber='" + userNumber 
                + ", centerGeoPoint=" + getCenterGeoPoint()
                + ", distanceUnit=" + getDistanceUnit()
                + ", minDistance=" + getMinDistance()
                + ", maxDistance=" + getMaxDistance()
                + ", minAge=" + minAge 
                + ", maxAge=" + maxAge 
                + ", sex='" + sex 
                + ", nickname='" + nickname
                + ", onlineTime=" + onlineTime
                + ", emotion='" + emotion
                + ", isLogin=" + isLogin
                + ", degree='" + degree
                + ", maxHeight=" + maxHeight
                + ", minHeight=" + minHeight
                + ", maxWeight=" + maxWeight
                + ", minWeight=" + minWeight
                + ", maxIncome=" + maxIncome
                + ", minIncome=" + minIncome
                + ", nation='" + nation
                + ", animalSigns='" + animalSigns
                + ", starSign='" + starSign
                + ", school=" + school
                + ", company=" + company
                + ", interest=" + interest
                + ", lastAscQueries=" + lastAscQueries
                + '}';
    }
}
