package com.netx.searchengine.model;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 资讯ES查询相应类
 * @Author hj.Mao
 */
public class ArticleSearchResponse {
    /**
     * 资讯id
     */
    private String id;
    /**
     * 发布资讯用户id
     */
    private String userId;
    /**
     * 文章标题
     */
    private String title;
    /**
     * 封面图直接存图片url
     */
    private String pic;
    /**
     * 附件（音视频直接对应文件url）
     */
    private String atta;
    /**
     * 文章内容
     */
    private String content;
    /**
     * 谁可以看此图文动态：
     0：所有人
     1：全部好友
     2：我关注的人
     3：关注我的人
     4：指定用户（可多个）
     */
    private Integer who;
    /**
     * 当who字段为指定好友时，该字段有效。将指定好友的id以逗号分隔保存
     */
    private String receiver;
    /**
     * 是否匿名（是不是隐身发布）：
     0：不匿名
     1：匿名
     */
    private Boolean isAnonymity;
    /**
     * 是否显示位置
     */
    private Boolean isShowLocation;

    /**
     * 软文类型：
     0：免费图文
     1：软文
     */
    private Integer advertorialType;

    /**
     * 是否违规
     */
    private Boolean isIllegal;
    /**
     * 经度
     */
    private BigDecimal lon;
    /**
     * 纬度
     */
    private BigDecimal lat;
    /**
     * 置顶：
     1：列表置顶。
     2：分类置顶。
     */
    private Integer top;
    /**
     * 发布时的位置信息
     */
    private String location;

    private Double distance;
    /**
     * 置顶过期时间
     */
    private Date topExpiredAt;
    /**
     * 状态码：
     0：正常
     1：异常
     2：待审核
     3：待交押金
     4：押金余额不足
     */
    private Integer statusCode;
    /**
     * 原因
     */
    private String reason;
    /**
     * 是否封禁
     */
    private Integer isLock;
    /**
     * 点击数
     */
    private Long hits;

    private Date createTime;

    private Integer deleted;

    /**
     * 评论数
     */
    private Integer commentNum;

    /**
     * 礼物数
     */
    private Integer giftNum;

    /**
     * 邀请数
     */
    private Integer invitationNum;

    /**
     * 点赞数
     */
    private Integer likeNum;

    /**
     * 标签集合
     */
    private List<String> tagNames;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getAtta() {
        return atta;
    }

    public void setAtta(String atta) {
        this.atta = atta;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getWho() {
        return who;
    }

    public void setWho(Integer who) {
        this.who = who;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Boolean getAnonymity() {
        return isAnonymity;
    }

    public void setAnonymity(Boolean anonymity) {
        isAnonymity = anonymity;
    }

    public Boolean getShowLocation() {
        return isShowLocation;
    }

    public void setShowLocation(Boolean showLocation) {
        isShowLocation = showLocation;
    }

    public Integer getAdvertorialType() {
        return advertorialType;
    }

    public void setAdvertorialType(Integer advertorialType) {
        this.advertorialType = advertorialType;
    }

    public BigDecimal getLon() {
        return lon;
    }

    public void setLon(BigDecimal lon) {
        this.lon = lon;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getTopExpiredAt() {
        return topExpiredAt;
    }

    public void setTopExpiredAt(Date topExpiredAt) {
        this.topExpiredAt = topExpiredAt;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getIsLock() {
        return isLock;
    }

    public void setIsLock(Integer isLock) {
        this.isLock = isLock;
    }

    public Long getHits() {
        return hits;
    }

    public void setHits(Long hits) {
        this.hits = hits;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public Integer getGiftNum() {
        return giftNum;
    }

    public void setGiftNum(Integer giftNum) {
        this.giftNum = giftNum;
    }

    public Integer getInvitationNum() {
        return invitationNum;
    }

    public void setInvitationNum(Integer invitationNum) {
        this.invitationNum = invitationNum;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public Boolean getIllegal() {
        return isIllegal;
    }

    public void setIllegal(Boolean illegal) {
        isIllegal = illegal;
    }

    public List<String> getTagNames() {
        return tagNames;
    }

    public void setTagNames(List<String> tagNames) {
        this.tagNames = tagNames;
    }
}
