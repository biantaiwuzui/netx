package com.netx.searchengine.query;

import com.netx.searchengine.common.LastAscQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CloudZou on 2/9/18.
 */
public class MeetingSearchQuery extends BaseSearchQuery{

    /**
     * 活动标签
     */
    private List<String> meetingLabels = new ArrayList<>();

    /**
     * 用户id
     */
    private String userId;
    /**
     * 活动标题
     */
    private String title;

    /**活动形式：
     * 1：活动，即1对1
     * 2：聚合，即多对多
     * 3：纯线上活动
     * 4：不发生消费的线下活动
     */
    private Integer meetingType;

    /**"活动状态：
     * 0：已发起，报名中
     * 1：报名截止，已确定入选人
     * 2：活动取消
     * 3：活动失败
     * 4：活动成功
     * 5：同意开始，分发验证码
     * 6：无人验证通过，活动失败
     **/
    private Integer status;

    /**
     * 是否在线
     */
    private Boolean isLogin;

    /**
     * 发布网信数量
     */
    private Integer creditSum;

    /**
     * 距离之前的排序
     */
    private List<LastAscQuery> fristAscQueries = new ArrayList<>();

    /**
     * 距离之后的排序
     */
    private List<LastAscQuery> lastAscQueries = new ArrayList<>();


    public List<String> getMeetingLabels() {
        return meetingLabels;
    }

    public void setMeetingLabels(List<String> meetingLabels) {
        this.meetingLabels = meetingLabels;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getMeetingType() {
        return meetingType;
    }

    public void setMeetingType(Integer meetingType) {
        this.meetingType = meetingType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getLogin() {
        return isLogin;
    }

    public void setLogin(Boolean login) {
        isLogin = login;
    }

    public List<LastAscQuery> getFristAscQueries() {
        return fristAscQueries;
    }

    public void setFristAscQueries(List<LastAscQuery> fristAscQueries) {
        this.fristAscQueries = fristAscQueries;
    }

    public List<LastAscQuery> getLastAscQueries() {
        return lastAscQueries;
    }

    public void setLastAscQueries(List<LastAscQuery> lastAscQueries) {
        this.lastAscQueries = lastAscQueries;
    }

    public void addLastAscQuery(LastAscQuery lastAscQuery){
        this.lastAscQueries.add(lastAscQuery);
    }

    public void addFristAscQueries(LastAscQuery fristAscQueries){
        this.fristAscQueries.add(fristAscQueries);
    }

    public Integer getCreditSum() {
        return creditSum;
    }

    public void setCreditSum(Integer creditSum) {
        this.creditSum = creditSum;
    }
}
