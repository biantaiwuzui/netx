package com.netx.common.wz.dto.matchEvent;

import com.netx.common.vo.common.PageRequestDto;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * 比赛搜索DTO
 * Created by Yawn on 2018/9/6 0006.
 */
public class MatchSearchDTO  extends PageRequestDto {
    @ApiModelProperty(value = "活动发起者id")
    private String userId;

    @ApiModelProperty(value = "赛事标题")
    private String title;

    @ApiModelProperty(value = "赛事标签")
    private List<String> matchLabels = new ArrayList<>();

    @ApiModelProperty(value = "排序方式：<br>" +
            "0.最热(报名人数多) <br>" +
            "1.最新 <br>" +
            "2.最近 <br>" +
            "3.信用<br>")
    private Integer sort = 0;

    @ApiModelProperty(value = "比赛形式：<br>" +
            "    * 1：个人，即1对1\n" +
            "     * 2：团队，即多对多\n" +
            "     * 3：不限")
    private Integer matchType;

    @ApiModelProperty(value = "活动状态：<br>" +
            "    (0, \"已保存\"),\n" +
            "     (1, \"待审核\"),\n" +
            "     (2, \"审核拒绝\"),\n" +
            "     (3, \"审核通过（启动中，购票和报名都还未开始）\"),\n" +
            "     (4, \"启动了，购票和报名都在进行中\"),\n" +
            "     (5, \"报名结束\"),\n" +
            "     (6, \"购票结束\"),\n" +
            "     (7, \"购票和报名都结束(全面进入进行中)\"),\n" +
            "     (8, \"赛事结束\");")
    private Integer status;

    @ApiModelProperty(value = "经度")
    private double lon;
    @ApiModelProperty(value = "纬度")
    private double lat;

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

    public List<String> getMatchLabels() {
        return matchLabels;
    }

    public void setMatchLabels(List<String> matchLabels) {
        this.matchLabels = matchLabels;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getMatchType() {
        return matchType;
    }

    public void setMatchType(Integer matchType) {
        this.matchType = matchType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
