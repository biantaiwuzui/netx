package com.netx.shopping.vo;

import com.netx.shopping.model.business.Category;
import com.netx.shopping.model.product.Product;
import com.netx.shopping.model.product.ProductPackage;
import com.netx.shopping.model.product.ProductSpe;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created By wj.liu
 * Description: 获取商品详情返回结果对象
 * Date: 2017-09-14
 */
public class GetProductResponseVo extends Product {
 
    /**
     * 商品第一类目
     */
    private List<Category> category;
    /**
     * 商品第二类目，标签
     */
    private List<Category> Tages;
    /**
     * 商品发行的总数
     */
    private Integer sumQuantity;
    /**
     * 商品现有的数量
     */
    private Integer nowQuantity;
    /**
     * 订单总数
     */
    private Long oderSumQuantity;
    /**
     * 现有订单数量
     */
    private Integer oderNowQuantity;
    /**
     * 商品成交额
     */
    private BigDecimal goodsDealAmount;
//    /**
//     * 商品包装明细列表
//     */
//    private List<ProductPackage> packageList;
//    /**
//     * 商品规格列表
//     */
//    private List<ProductSpecResponseVo> specList;

    private List<ProductSpe> speList;
    /**
     * 供应商家用户id
     */
    private String sellerUserId;
    /**
     * 商家名称
     */
    private String sellerName;

    /**
     * 是否有未完成订单
     */
    private Boolean isHaveNoCompleteOrder;

    /**
     * 注册者昵称
     */
    private String nickname;

    /**
     * 注册者性别
     */
    private String sex;

    /**
     * 注册者等级lv
     */
    private Integer lv;
    /**
     * 注册者信用值
     */
    private Integer credit;

    /**
     * 注册者年龄
     */
    private Integer age;
    /**
     * 距离
     */
    private Double distance;

    /**
     *订单金额百分百1
     */
    private BigDecimal orderPercent1;

    /**
     *订单金额百分百2
     */
    private BigDecimal orderPercent2;

    /**
     *红包发放时间
     */
    private Long sendTime;

    /**
     * 红包池
     */
    private BigDecimal packetPoolAmount;

    /**
     * 商家操作者昵称
     */
    private String handlersNikname;

    /**
     * 地址-经度
     */
    private BigDecimal lon;
    /**
     * 地址-纬度
     */
    private BigDecimal lat;

    /**
     *用户是否收藏商家状态，有为1，没有则为2
     */
    private Integer isHaveCollect;

    /**
     *供应商家主管id
     */
    private String sellerManageUserId;

    /**
     *供应商家收银人员id
     */
    private String sellerMoneyUserId;

    /**
     *供应商家图片
     */
    private String sellerImagesUrl;

    public Long getOderSumQuantity() {
        return oderSumQuantity;
    }

    public void setOderSumQuantity(Long oderSumQuantity) {
        this.oderSumQuantity = oderSumQuantity;
    }

    public Integer getOderNowQuantity() {
        return oderNowQuantity;
    }

    public void setOderNowQuantity(Integer oderNowQuantity) {
        this.oderNowQuantity = oderNowQuantity;
    }

    public Integer getSumQuantity() {
        return sumQuantity;
    }

    public void setSumQuantity(Integer sumQuantity) {
        this.sumQuantity = sumQuantity;
    }

    public Integer getNowQuantity() {
        return nowQuantity;
    }

    public void setNowQuantity(Integer nowQuantity) {
        this.nowQuantity = nowQuantity;
    }

    public BigDecimal getGoodsDealAmount() {
        return goodsDealAmount;
    }

    public void setGoodsDealAmount(BigDecimal goodsDealAmount) {
        this.goodsDealAmount = goodsDealAmount;
    }

//    public List<ProductPackage> getPackageList() {
//        return packageList;
//    }
//
//    public void setPackageList(List<ProductPackage> packageList) {
//        this.packageList = packageList;
//    }
//
//    public List<ProductSpecResponseVo> getSpecList() {
//        return specList;
//    }
//
//    public void setSpecList(List<ProductSpecResponseVo> specList) {
//        this.specList = specList;
//    }


    public List<ProductSpe> getSpeList() {
        return speList;
    }

    public void setSpeList(List<ProductSpe> speList) {
        this.speList = speList;
    }

    public String getSellerUserId() {
        return sellerUserId;
    }

    public void setSellerUserId(String sellerUserId) {
        this.sellerUserId = sellerUserId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public Boolean getIsHaveNoCompleteOrder(){
        return isHaveNoCompleteOrder;
    }

    public Boolean getHaveNoCompleteOrder() {
        return isHaveNoCompleteOrder;
    }

    public void setHaveNoCompleteOrder(Boolean haveNoCompleteOrder) {
        isHaveNoCompleteOrder = haveNoCompleteOrder;
    }

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

    public Integer getLv() {
        return lv;
    }

    public void setLv(Integer lv) {
        this.lv = lv;
    }

    public void setIsHaveNoCompleteOrder(Boolean isHaveNoCompleteOrder){
        this.isHaveNoCompleteOrder = isHaveNoCompleteOrder;

    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public BigDecimal getOrderPercent1() {
        return orderPercent1;
    }

    public void setOrderPercent1(BigDecimal orderPercent1) {
        this.orderPercent1 = orderPercent1;
    }

    public BigDecimal getOrderPercent2() {
        return orderPercent2;
    }

    public void setOrderPercent2(BigDecimal orderPercent2) {
        this.orderPercent2 = orderPercent2;
    }

    public Long getSendTime() {
        return sendTime;
    }

    public void setSendTime(Long sendTime) {
        this.sendTime = sendTime;
    }

    public BigDecimal getPacketPoolAmount() {
        return packetPoolAmount;
    }

    public void setPacketPoolAmount(BigDecimal packetPoolAmount) {
        this.packetPoolAmount = packetPoolAmount;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getHandlersNikname() {
        return handlersNikname;
    }

    public void setHandlersNikname(String handlersNikname) {
        this.handlersNikname = handlersNikname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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

    public Integer getIsHaveCollect() {
        return isHaveCollect;
    }

    public void setIsHaveCollect(Integer isHaveCollect) {
        this.isHaveCollect = isHaveCollect;
    }

    public String getSellerManageUserId() {
        return sellerManageUserId;
    }

    public void setSellerManageUserId(String sellerManageUserId) {
        this.sellerManageUserId = sellerManageUserId;
    }

    public String getSellerMoneyUserId() {
        return sellerMoneyUserId;
    }

    public void setSellerMoneyUserId(String sellerMoneyUserId) {
        this.sellerMoneyUserId = sellerMoneyUserId;
    }

    public List<Category> getTages() {
        return Tages;
    }

    public void setTages(List<Category> tages) {
        Tages = tages;
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

    public String getSellerImagesUrl() {
        return sellerImagesUrl;
    }

    public void setSellerImagesUrl(String sellerImagesUrl) {
        this.sellerImagesUrl = sellerImagesUrl;
    }
}