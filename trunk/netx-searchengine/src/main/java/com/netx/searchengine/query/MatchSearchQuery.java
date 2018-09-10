package com.netx.searchengine.query;

import com.netx.searchengine.common.LastAscQuery;

import java.util.ArrayList;
import java.util.List;

/** 比赛搜索
 * Created by Yawn on 2018/9/5 0005.
 */
public class MatchSearchQuery extends BaseSearchQuery{

    /**
     * 用户id
     */
    private String userId;
    /**
     * 比赛标题
     */
    private String title;


    /**比赛形式：
     * 1：个人，即1对1
     * 2：团队，即多对多
     * 3：不限
     */
    private Integer matchKind;

    /**"活动状态：
     (0, "已保存"),
     (1, "待审核"),
     (2, "审核拒绝"),
     (3, "审核通过（启动中，购票和报名都还未开始）"),
     (4, "启动了，购票和报名都在进行中"),
     (5, "报名结束"),
     (6, "购票结束"),
     (7, "购票和报名都结束(全面进入进行中)"),
     (8, "赛事结束");
     **/
    private Integer status;

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

    public Integer getMatchKind() {
        return matchKind;
    }

    public void setMatchKind(Integer matchKind) {
        this.matchKind = matchKind;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCreditSum() {
        return creditSum;
    }

    public void setCreditSum(Integer creditSum) {
        this.creditSum = creditSum;
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
    public void addLastAscQuery(LastAscQuery lastAscQuery){
        this.lastAscQueries.add(lastAscQuery);
    }

    public void addFristAscQuery(LastAscQuery fristAscQueries){
        this.fristAscQueries.add(fristAscQueries);
    }
    public void setLastAscQueries(List<LastAscQuery> lastAscQueries) {
        this.lastAscQueries = lastAscQueries;
    }
}
