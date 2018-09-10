package com.netx.common.vo.common;

import com.netx.common.vo.common.UserInfomationResponseVo;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * <p>主要展示与资讯的详情和发布咨询者信息</p>
 * @Author hj.Mao
 * @Date 2018-1-11
 */
public class ArticleCommonResponseDto extends UserInfomationResponseVo {

    @ApiModelProperty("资讯id")
    private String id;
    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("文章标题")
    private String title;

    @ApiModelProperty("封面图直接存图片url")
    private String pic;

    @ApiModelProperty("附件（音视频直接对应文件url）")
    private String atta;

    @ApiModelProperty("文章内容")
    private String content;

    @ApiModelProperty("谁可以看此图文动态：\n" +
            "     0：所有人\n" +
            "     1：全部好友\n" +
            "     2：我关注的人\n" +
            "     3：关注我的人\n" +
            "     4：指定用户（可多个）\n" +
            "     */")
    private Integer who;

    @ApiModelProperty("当who字段为指定好友时，该字段有效。将指定好友的id以逗号分隔保存")
    private String receiver;

    @ApiModelProperty("是否匿名（是不是隐身发布）：\n" +
            "     0：不匿名\n" +
            "     1：匿名")
    private Boolean isAnonymity;

    @ApiModelProperty("是否显示位置")
    private Boolean isShowLocation;

    @ApiModelProperty("是否申明为软文")
    private Boolean isAdvertorial;

    @ApiModelProperty("软文类型：\n" +
            "     0：普通咨讯（不是软文）\n" +
            "     1：正常软文\n" +
            "     2：违规软文")
    private Integer advertorialType;

    @ApiModelProperty("资讯类型：\n" +
            "     1：图文\n" +
            "     2：音视")
    private Integer articleType;

    @ApiModelProperty("活动经纬度hash")
    private String geohash;

    @ApiModelProperty("经度")
    private BigDecimal lon;
    /**
     * 纬度
     */
    @ApiModelProperty("纬度")
    private BigDecimal lat;

    @ApiModelProperty("置顶：\n" +
            "     1：列表置顶。\n" +
            "     2：分类置顶。")
    private Integer top;

    @ApiModelProperty("发布时的位置信息")
    private String location;

    @ApiModelProperty("置顶过期时间")
    private Long topExpiredAt;

    @ApiModelProperty("押金（点击费用都是从这里扣掉）")
    private BigDecimal amount;

    @ApiModelProperty("押金类型：\n" +
            "     1：零钱\n" +
            "     2：网币")
    private Integer amountType;

    @ApiModelProperty("状态码：\n" +
            "     0：正常\n" +
            "     1：异常\n" +
            "     2：待审核\n" +
            "     3：待交押金\n" +
            "     4：押金余额不足")
    private Integer statusCode;

    @ApiModelProperty("原因")
    private String reason;

    @ApiModelProperty("是否封禁")
    private Boolean isLock;

    @ApiModelProperty("点击数")
    private Long hits;

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

    public Boolean getAdvertorial() {
        return isAdvertorial;
    }

    public void setAdvertorial(Boolean advertorial) {
        isAdvertorial = advertorial;
    }

    public Integer getAdvertorialType() {
        return advertorialType;
    }

    public void setAdvertorialType(Integer advertorialType) {
        this.advertorialType = advertorialType;
    }

    public Integer getArticleType() {
        return articleType;
    }

    public void setArticleType(Integer articleType) {
        this.articleType = articleType;
    }

    public String getGeohash() {
        return geohash;
    }

    public void setGeohash(String geohash) {
        this.geohash = geohash;
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

    public Long getTopExpiredAt() {
        return topExpiredAt;
    }

    public void setTopExpiredAt(Long topExpiredAt) {
        this.topExpiredAt = topExpiredAt;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getAmountType() {
        return amountType;
    }

    public void setAmountType(Integer amountType) {
        this.amountType = amountType;
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

    public Boolean getLock() {
        return isLock;
    }

    public void setLock(Boolean lock) {
        isLock = lock;
    }

    public Long getHits() {
        return hits;
    }

    public void setHits(Long hits) {
        this.hits = hits;
    }

}
