package com.netx.shopping.biz.ordercenter;

import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.common.utils.OrderUtil;
import com.netx.common.express.AliyunExpressService;
import com.netx.common.redis.RedisInfoHolder;
import com.netx.common.redis.model.UserGeo;
import com.netx.common.user.enums.StatScoreEnum;
import com.netx.common.user.util.AddImgUrlPreUtil;
import com.netx.common.user.util.DateTimestampUtil;
import com.netx.common.user.util.VoPoConverter;
import com.netx.shopping.biz.cartcenter.CartItemAction;
import com.netx.shopping.biz.merchantcenter.MerchantCategoryAction;
import com.netx.shopping.biz.merchantcenter.MerchantManagerAction;
import com.netx.shopping.biz.productcenter.ProductDeliveryAction;
import com.netx.shopping.model.cartcenter.CartItem;
import com.netx.shopping.model.cartcenter.constants.CartItemStatusEnum;
import com.netx.shopping.model.merchantcenter.MerchantExpress;
import com.netx.shopping.model.merchantcenter.MerchantManager;
import com.netx.shopping.model.merchantcenter.MerchantPacketSet;
import com.netx.shopping.model.merchantcenter.constants.MerchantManagerEnum;
import com.netx.shopping.model.merchantcenter.constants.MerchantPictureEnum;
import com.netx.shopping.model.ordercenter.MerchantOrderItem;
import com.netx.shopping.model.ordercenter.constants.*;
import com.netx.shopping.model.productcenter.Sku;
import com.netx.shopping.service.merchantcenter.MerchantExpressService;
import com.netx.shopping.service.merchantcenter.MerchantManagerService;
import com.netx.shopping.service.merchantcenter.MerchantPacketSetService;
import com.netx.shopping.service.merchantcenter.MerchantPictureService;
import com.netx.shopping.service.productcenter.CategoryService;
import com.netx.shopping.vo.*;
import com.netx.shopping.biz.merchantcenter.MerchantAction;
import com.netx.shopping.biz.productcenter.ProductAction;
import com.netx.shopping.biz.productcenter.SkuAction;
import com.netx.shopping.model.merchantcenter.Merchant;
import com.netx.shopping.model.ordercenter.MerchantOrderInfo;
import com.netx.shopping.model.productcenter.Product;
import com.netx.shopping.service.ordercenter.MerchantOrderInfoService;
import com.netx.utils.DistrictUtil;
import com.netx.utils.cache.RedisCache;
import com.netx.utils.cache.RedisKeyName;
import com.netx.utils.cache.RedisTypeEnum;
import com.netx.utils.money.Money;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  ACTION
 * </p>
 *
 * @author CHENQIAN
 * @since 2018-05-04
 */
@Service
public class MerchantOrderInfoAction {

    private Logger logger = LoggerFactory.getLogger(MerchantOrderInfoAction.class);

    @Autowired
    private MerchantOrderInfoService merchantOrderInfoService;

    @Autowired
    private MerchantManagerService merchantManagerService;

    @Autowired
    private ProductAction productAction;

    @Autowired
    private MerchantAction merchantAction;

    @Autowired
    private MerchantOrderItemAction merchantOrderItemAction;

    @Autowired
    private SkuAction skuAction;

    @Autowired
    private HashCheckoutAction hashCheckoutAction;

    @Autowired
    private CartItemAction cartItemAction;

    @Autowired
    private AddImgUrlPreUtil addImgUrlPreUtil;

    @Autowired
    private MerchantManagerAction merchantManagerAction;

    @Autowired
    private AliyunExpressService aliyunExpressService;

    @Autowired
    private MerchantCategoryAction merchantCategoryAction;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private MerchantPacketSetService merchantPacketSetService;

    @Autowired
    private MerchantPictureService merchantPictureService;

    @Autowired
    private RedisInfoHolder redisInfoHolder;

    @Autowired
    private ProductDeliveryAction productDeliveryAction;

    @Autowired
    private MerchantExpressService merchantExpressService;

    private RedisCache redisCache;

    private RedisCache clientRedisCache(){
        redisCache = RedisCache.getRedisCache(redisInfoHolder.getHost(),redisInfoHolder.getPassword(),redisInfoHolder.getPort());
        return redisCache;
    }

    public MerchantOrderInfoService getMerchantOrderInfoService() {
        return merchantOrderInfoService;
    }

    public BigDecimal getDirectRedPacketAmount(MerchantOrderInfo order){
        //按商家设定的红包提成标准，按当前订单金额计提红包金额至红包池
        Merchant merchant = merchantAction.getMerchantService().selectById(order.getMerchantId());
        if (merchant==null){
            throw new RuntimeException("供应商家不存在");
        }
        MerchantPacketSet packetSet = merchantPacketSetService.selectById(merchant.getPacSetId());
        if (packetSet == null) {
            throw new RuntimeException("红包设置不存在");
        }
        long total = order.getOrderTotalFee()+order.getShippingFee();
        Money totalMoney = Money.CentToYuan(total);
        Date date = new Date();
        if (packetSet.getChangeRate() == false) {//固定计提比例
            //比例*金额（分）
            return packetSet.getFixedRate().multiply(totalMoney.getAmount()).divide(new BigDecimal(100));

        } else {//每天根据订单数量，由低到高，逐单加大红包计提比例
            //获取今天开始和结束的时间戳
            Date startTime = DateTimestampUtil.getStartOrEndOfDate(date, 1);
            Date endTime = DateTimestampUtil.getStartOrEndOfDate(date, 2);
            //查找今天订单数
            int orderNum = merchantOrderInfoService.getProductOrderCount(startTime,endTime);
            if (orderNum == 1) {//首单计提比例
                return packetSet.getFirstRate().multiply(new BigDecimal(Money.getMoneyString(total)).divide(new BigDecimal(100)));
            } else if (packetSet.getLimitRate().compareTo(new BigDecimal(orderNum).multiply(packetSet.getGradualRate()).add(packetSet.getFirstRate())) != 1) {
                //封顶计提比例
                return packetSet.getLimitRate().multiply(new BigDecimal(Money.getMoneyString(total)).divide(new BigDecimal(100)));
            } else {//逐单递增比例
                return new BigDecimal(orderNum).multiply(packetSet.getGradualRate()).add(packetSet.getFirstRate()).multiply(totalMoney.getAmount()).divide(new BigDecimal(100));
            }
        }
    }

    /**
     * 发货
     * @param merchantOrderInfo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean send(MerchantOrderInfo merchantOrderInfo){
        return merchantOrderInfoService.updateById(merchantOrderInfo)&&merchantOrderItemAction.send(merchantOrderInfo.getOrderNo());
    }

    /**
     * 修改订单状态
     * @param request
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean changeGoodsOrderStatus(ChangeOrderStatusRequestDto request){
        MerchantOrderInfo merchantOrderInfo = merchantOrderInfoService.selectById(request.getId());
        if(merchantOrderInfo==null){
            throw new RuntimeException("此订单已过期");
        }
        merchantOrderInfo.setOrderType(request.getOrderTypeEnum().name());
        if(!request.getOrderTypeEnum().equals(OrderTypeEnum.NORMAL_ORDER) && StringUtils.isNotBlank(request.getTypeId())){
            merchantOrderInfo.setOrderTypeBusinessId(request.getTypeId());
        }
        OrderStatusEnum orderStatusEnum = request.getStatusEnum();
        ShippingStatus shippingStatus = null;
        PayStatus payStatus = null;
        TradeStatus tradeStatus = null;
        switch (orderStatusEnum){
            case OS_WATTING://待生成
                shippingStatus = ShippingStatus.SS_UNSHIPPED;
                payStatus = PayStatus.PS_UNPAID;
                tradeStatus = TradeStatus.TS_WATTING;
                break;
            case OS_ORDER://待付款
                shippingStatus = ShippingStatus.SS_UNSHIPPED;
                payStatus = PayStatus.PS_UNPAID;
                tradeStatus = TradeStatus.TS_WATTING;
                break;
            case OS_RETURN://退货
                shippingStatus = ShippingStatus.SS_RETURN;
                break;
            case OS_CANCELED://取消
                tradeStatus = TradeStatus.TS_CANCELED;
                break;
            case OS_CONFIRMED://待发货
                shippingStatus = ShippingStatus.SS_UNSHIPPED;
                tradeStatus = TradeStatus.TS_TRADED;
                break;
            case OS_FINSH_PAY://已付款
                payStatus = PayStatus.PS_PAYED;
                tradeStatus = TradeStatus.TS_TRADED;
                break;
            case OS_SHIPPING://物流中
                shippingStatus = ShippingStatus.SS_SHIPPING;
                break;
            case OS_FINISH://完成
                shippingStatus = ShippingStatus.SS_RECEIVED;
                merchantOrderInfo.setIsComment(1);
                merchantOrderInfo.setFinishTime(new Date());
                payStatus = PayStatus.PS_PAYED;
                tradeStatus = TradeStatus.TS_SUCCESS;
                if(merchantOrderInfo.getShippingFee()+merchantOrderInfo.getOrderTotalFee()>=100){
                    addScore(merchantOrderInfo);
                }
                break;
        }
        merchantOrderInfo.setOrderStatus(orderStatusEnum.name());
        return updateStatus(merchantOrderInfo,payStatus,shippingStatus) && merchantOrderItemAction.updateStatus(merchantOrderInfo.getOrderNo(),orderStatusEnum,payStatus,tradeStatus,shippingStatus);
    }

    private void addScore(MerchantOrderInfo merchantOrderInfo){
        String merchantUserId = merchantAction.getUserIdById(merchantOrderInfo.getMerchantId());
        RedisKeyName redisKeyName = new RedisKeyName("userScore", RedisTypeEnum.ZSET_TYPE,null);
        clientRedisCache().zincrby(redisKeyName.getUserKey(),merchantUserId, StatScoreEnum.SS_MERCHANT_ORDER.score());
        redisCache.zincrby(redisKeyName.getUserKey(),merchantOrderInfo.getUserId(),StatScoreEnum.SS_USER_ORDER.score());
    }

    /**
     * 修改业务id
     * @param orderIds
     * @param typeId
     * @param orderTypeEnum
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTypeId(String[] orderIds,String typeId,OrderTypeEnum orderTypeEnum){
        if(StringUtils.isNotBlank(typeId)){
            List<String> orderNo = merchantOrderInfoService.queryOrderNo(typeId,orderTypeEnum);
            if(orderNo.size()>0){
                merchantOrderItemAction.getMerchantOrderItemService().deleteByOrderNo(orderNo);
                merchantOrderInfoService.deleteByOrderNo(orderNo);
            }
            MerchantOrderInfo merchantOrderInfo = new MerchantOrderInfo();
            merchantOrderInfo.setOrderTypeBusinessId(typeId);
            for(String id:orderIds){
                if(StringUtils.isNotBlank(id)) {
                    merchantOrderInfoService.updateOrder(id, merchantOrderInfo);
                }
            }
        }
        return true;
    }
    
    /* 修改网能订单 **/
    @Transactional(rollbackFor = Exception.class)
    public boolean deleted(String typeId,OrderTypeEnum orderTypeEnum){
        if(StringUtils.isNotBlank(typeId)){
            List<String> orderNo = merchantOrderInfoService.queryOrderNo(typeId,orderTypeEnum);
            if(orderNo.size()>0){
                merchantOrderItemAction.getMerchantOrderItemService().deleteByOrderNo(orderNo);
                merchantOrderInfoService.deleteByOrderNo(orderNo);
            }
        }
        return true;
    }

    /* 修改网能订单 **/
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String typeId,OrderTypeEnum orderTypeEnum){
        if(StringUtils.isNotBlank(typeId)){
            List<String> orderNo = merchantOrderInfoService.queryOrderNo(typeId,orderTypeEnum);
            if(orderNo.size()>0){
                MerchantOrderItem merchantOrderItem = new MerchantOrderItem();
                merchantOrderItem.setDeleted(1);
                merchantOrderItemAction.getMerchantOrderItemService().updateOrderItem(orderNo,merchantOrderItem);
                MerchantOrderInfo merchantOrderInfo = new MerchantOrderInfo();
                merchantOrderInfo.setDeleted(1);
                merchantOrderInfoService.updateWz(orderNo,merchantOrderInfo);
            }
        }
        return true;
    }
    
    /**
     * 修改订单状态
     * @param merchantOrderInfo
     * @param payStatus
     * @param shippingStatus
     * @return
     */
    public boolean updateStatus(MerchantOrderInfo merchantOrderInfo,PayStatus payStatus,ShippingStatus shippingStatus){
        if(payStatus!=null){
            merchantOrderInfo.setPayStatus(payStatus.name());
        }
        if(shippingStatus!=null){
            merchantOrderInfo.setShippingStatus(shippingStatus.name());
        }
        return merchantOrderInfoService.updateById(merchantOrderInfo);
    }

    /**
     * 定时更新物流信息
     */
    public void updateOrderInfoShippingDetails(){
        List<MerchantOrderInfo> list = merchantOrderInfoService.getShippingList();
        if(list!=null && list.size()>0){
            list.forEach(merchantOrderInfo -> {
                if(StringUtils.isNotBlank(merchantOrderInfo.getShippingCode())){
                    String json = aliyunExpressService.getExpressData(merchantOrderInfo.getShippingLogisticsNo(),merchantOrderInfo.getShippingCode());
                    if(StringUtils.isNotBlank(json)){
                        merchantOrderInfo.setShippingLogisticsDetails(json);
                        merchantOrderInfoService.updateById(merchantOrderInfo);
                    }
                }
            });
        }
    }

    /**
     *
     * @param orderId
     * @return
     */
    public MerchantOrderDetailResponseDto queryDetail(String orderId,String userNumber,double lat,double lon){
        MerchantOrderInfo merchantOrderInfo = merchantOrderInfoService.selectById(orderId);
        if(merchantOrderInfo==null){
            throw new RuntimeException("此订单已不存在");
        }
        return createMerchantOrderDetailResponseDto(merchantOrderInfo,userNumber,lat,lon);
    }

    /**
     *
     * @param merchantOrderInfo
     * @return
     */
    private MerchantOrderDetailResponseDto createMerchantOrderDetailResponseDto(MerchantOrderInfo merchantOrderInfo,String userNumber,double lat,double lon){
        MerchantOrderDetailResponseDto merchantOrderDetailResponseDto = VoPoConverter.copyProperties(merchantOrderInfo,MerchantOrderDetailResponseDto.class);
//        if(StringUtils.isNotBlank(merchantOrderInfo.getShippingLogisticsDetails())){
//            merchantOrderDetailResponseDto.setShippingDetail(aliyunExpressService.jsonToExpressDateLists(merchantOrderInfo.getShippingLogisticsDetails()));
        if(StringUtils.isNotBlank(merchantOrderInfo.getShippingLogisticsNo()) && StringUtils.isNotBlank(merchantOrderInfo.getShippingCode())){
            merchantOrderDetailResponseDto.setShippingDetail(aliyunExpressService.jsonToExpressDateLists(aliyunExpressService.getExpressData(merchantOrderInfo.getShippingLogisticsNo(),merchantOrderInfo.getShippingCode())));
        }
//        }
        if(StringUtils.isNotBlank(merchantOrderInfo.getShippingCode())){
            merchantOrderDetailResponseDto.setShippingName( merchantExpressService.getNameByType(merchantOrderInfo.getShippingCode()));
        }
        merchantOrderDetailResponseDto.setIsAdmin(merchantManagerAction.checkAdmin(merchantOrderInfo.getMerchantId(),userNumber));
        Merchant merchant = merchantAction.getMerchantService().selectById(merchantOrderInfo.getMerchantId());
        if(merchant!=null){
            merchantOrderDetailResponseDto.setMerchantName(merchant.getName());
            merchantOrderDetailResponseDto.setMerchantUserId(merchant.getUserId());
            merchantOrderDetailResponseDto.setPhone(merchant.getAddrTel());
            merchantOrderDetailResponseDto.setDistance(DistrictUtil.calcDistance(merchant.getLat().doubleValue(),merchant.getLon().doubleValue(),lat,lon));
        }
        merchantOrderDetailResponseDto.setProductBeans(queryOrderProductBean(merchantOrderInfo.getOrderNo()));
        if(merchantOrderInfo.getShippingFee()!=null){
            merchantOrderDetailResponseDto.setShippingFeeBig(Money.CentToYuan(merchantOrderInfo.getShippingFee()).getAmount());
        }
        if(merchantOrderInfo.getOrderTotalFee()!=null){
            merchantOrderDetailResponseDto.setTotalBig(Money.CentToYuan(merchantOrderInfo.getOrderTotalFee()).getAmount());
        }
        OrderStatusEnum orderStatusEnum = OrderStatusEnum.valueOf(merchantOrderInfo.getOrderStatus());
        if(orderStatusEnum!=null){
            merchantOrderDetailResponseDto.setStatus(orderStatusEnum.order());
        }
        return merchantOrderDetailResponseDto;
    }



    /**
     * 立即下单
     * @param userId
     * @param orderRequestDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public String add(String userId,String userNumber,String userNickname,AddOrderRequestDto orderRequestDto){
        if(!hashCheckoutAction.hashCheckout(userId,orderRequestDto.getHash())){
            throw new RuntimeException("机器无效提交订单");
        }
        Merchant merchant = merchantAction.getMerchantService().selectById(orderRequestDto.getMerchantId());
        checkMerchant(merchant);
        if(merchantManagerAction.checkAdmin(merchant.getId(),userNumber)){
            throw new RuntimeException("商家职员不能下单");
        }
        return addOrderItemBeans(orderRequestDto.getList(),userId,userNickname,merchant.getId(),orderRequestDto.getOrderTypeEnum());
    }

    /**
     * 对商品进行配送方式拆单
     * @param list
     * @param userId
     * @param userNickname
     * @param merchantId
     * @param orderTypeEnum
     * @return
     */
    private String addOrderItemBeans(List<ProductOrderBean> list,String userId,String userNickname,String merchantId,OrderTypeEnum orderTypeEnum){
        String orderIds = null;
        while(list.size()>0){
            String temp = addOrderItemBean(list, userId, userNickname, merchantId, orderTypeEnum);
            if(temp!=null){
                if(orderIds==null){
                    orderIds = temp;
                }else{
                    orderIds+=","+temp;
                }
            }
        }
        return orderIds;
    }

    private String addOrderItemBean(List<ProductOrderBean> list,String userId,String userNickname,String merchantId,OrderTypeEnum orderTypeEnum){
        ProductOrderBean productOrderBean = list.remove(0);
        MerchantOrderInfo merchantOrderInfo = create(userId,userNickname,merchantId,0l,0l,orderTypeEnum,0l,productOrderBean.getDeliveryWay());
        ProductOrderBean temp;
        if(merchantOrderInfoService.insert(merchantOrderInfo)) {
            Map<String,Long> priceMap = addMerchantOrderItem(merchantOrderInfo,productOrderBean);
            long total = priceMap.get("price")*productOrderBean.getQuantity();;
            long marketPrice = priceMap.get("marketPrice")*productOrderBean.getQuantity();
            long shippingFee = priceMap.get("shippingFee");
            int len = list.size();
            for(int i=len-1;i>=0;i--){
                if(list.get(i).getDeliveryWay().equals(productOrderBean.getDeliveryWay())){
                    temp = list.remove(i);
                    priceMap = addMerchantOrderItem(merchantOrderInfo,temp);
                    total+=priceMap.get("price")*temp.getQuantity();
                    marketPrice+=priceMap.get("marketPrice")*temp.getQuantity();
                    if(priceMap.get("shippingFee")>shippingFee){
                        shippingFee=priceMap.get("shippingFee");
                    }
                }
            }
            //更新物流费用
            merchantOrderInfo.setOrderTotalFee(marketPrice);
            merchantOrderInfo.setProductTotalFee(total);
            merchantOrderInfo.setShippingFee(shippingFee);
            merchantOrderInfoService.updateById(merchantOrderInfo);
            return merchantOrderInfo.getId();
        }
        return null;
    }

    /**
     * 购物车订单生成
     * @param userId
     * @param merchantId
     * @param addCartRequestDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public String add(String userId,String nickname,String merchantId,List<CartRequestDto> addCartRequestDto,OrderTypeEnum orderTypeEnum){
        Merchant merchant = merchantAction.getMerchantService().selectById(merchantId);
        checkMerchant(merchant);
        return addOrderItems(addCartRequestDto,userId,nickname,merchant.getId(),orderTypeEnum);
    }

    private String addOrderItems(List<CartRequestDto> list,String userId,String userNickname,String merchantId,OrderTypeEnum orderTypeEnum){
        String orderIds = null;
        while(list.size()>0){
            String temp = addOrderItem(list, userId, userNickname, merchantId, orderTypeEnum);
            if(temp!=null){
                if(orderIds==null){
                    orderIds = temp;
                }else{
                    orderIds+=","+temp;
                }
            }
        }
        return orderIds;
    }

    private String addOrderItem(List<CartRequestDto> list,String userId,String userNickname,String merchantId,OrderTypeEnum orderTypeEnum){
        ProductOrderBean productOrderBean = list.remove(0);
        MerchantOrderInfo merchantOrderInfo = create(userId,userNickname,merchantId,0l,0l,orderTypeEnum,0l,productOrderBean.getDeliveryWay());
        ProductOrderBean temp;
        if(merchantOrderInfoService.insert(merchantOrderInfo)) {
            Map<String,Long> priceMap = addMerchantOrderItem(merchantOrderInfo,productOrderBean);
            long total = priceMap.get("price")*productOrderBean.getQuantity();;
            long marketPrice = priceMap.get("marketPrice")*productOrderBean.getQuantity();
            long shippingFee = priceMap.get("shippingFee");
            int len = list.size();
            for(int i=len-1;i>=0;i--){
                if(list.get(i).getDeliveryWay().equals(productOrderBean.getDeliveryWay())){
                    temp = list.remove(i);
                    priceMap = addMerchantOrderItem(merchantOrderInfo,temp);
                    total+=priceMap.get("price")*temp.getQuantity();
                    marketPrice+=priceMap.get("marketPrice")*temp.getQuantity();
                    if(priceMap.get("shippingFee")>shippingFee){
                        shippingFee=priceMap.get("shippingFee");
                    }
                }
            }
            //更新物流费用
            merchantOrderInfo.setOrderTotalFee(marketPrice);
            merchantOrderInfo.setProductTotalFee(total);
            merchantOrderInfo.setShippingFee(shippingFee);
            merchantOrderInfoService.updateById(merchantOrderInfo);
            return merchantOrderInfo.getId();
        }
        return null;
    }

    /**
     * 添加订单详情【购物车结算】
     * @param merchantOrderInfo
     * @param cartRequestDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    Map<String,Long> addMerchantItem(MerchantOrderInfo merchantOrderInfo,CartRequestDto cartRequestDto){
        if(StringUtils.isNotBlank(cartRequestDto.getId())) {
            CartItem cartItem = cartItemAction.getCartItemService().query(cartRequestDto.getId(), CartItemStatusEnum.CIS_WATTING);
            if (cartItem == null) {
                throw new RuntimeException("此商品已结算");
            }
            Map<String,Long> map = addMerchantOrderItem(merchantOrderInfo,cartRequestDto);
            cartItem.setStatus(CartItemStatusEnum.CIS_FINISH.name());
            cartItemAction.getCartItemService().updateById(cartItem);
            return map;
        }else {
            throw new RuntimeException("清单id不能为空");
        }
    }

    /**
     * 添加订单详情
     * @param merchantOrderInfo
     * @param productOrderBean
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    Map<String,Long> addMerchantOrderItem(MerchantOrderInfo merchantOrderInfo,ProductOrderBean productOrderBean){
        Map<String,Long> map = new HashMap<>();
        Sku sku = skuAction.checkSkuNum(productOrderBean.getSkuId(),productOrderBean.getQuantity());
        map.put("price",sku.getPrice());
        map.put("marketPrice",sku.getMarketPrice());
        Product product = productAction.getProductService().selectById(productOrderBean.getProductId());
        checkProduct(product, productOrderBean.getProductName());
        if (!merchantOrderItemAction.add(merchantOrderInfo, productOrderBean.getSkuId(), product, sku.getPrice(), sku.getMarketPrice(), productOrderBean.getQuantity())) {
            throw new RuntimeException(productOrderBean.getProductName() + "结算失败");
        }
//        long shippingFee = 0l;
//        if(!productOrderBean.getDeliveryWay().equals(2)){
//            shippingFee = product.getShippingFee();
//        }
//        map.put("shippingFee",shippingFee);
        map.put("shippingFee",productDeliveryAction.queryFeeByProductIdAndDeliveryWay(productOrderBean.getProductId(),productOrderBean.getDeliveryWay()));
        return map;
    }

    /**
     * 修改订单收货信息
     * @param orderAdressRequestDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateAddress(OrderAdressRequestDto orderAdressRequestDto){
        String[] ids = orderAdressRequestDto.getId().split(",");
        if(ids.length>0){
            if(StringUtils.isBlank(ids[0])){
                throw new RuntimeException("订单id错误");
            }
            MerchantOrderInfo merchantOrderInfo = new MerchantOrderInfo();
            merchantOrderInfo.setConsignee(orderAdressRequestDto.getName());
            merchantOrderInfo.setMobile(orderAdressRequestDto.getMobile());
            merchantOrderInfoService.updateOrder(ids,null,merchantOrderInfo);
            merchantOrderInfo.setFullAddress(orderAdressRequestDto.getAddress());
            merchantOrderInfo.setZipCode(orderAdressRequestDto.getZipCode());
            merchantOrderInfoService.updateOrder(ids,2,merchantOrderInfo);
        }
        return true;
    }

    /**
     * 修改备注
     * @param list
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateRemark(List<AddRemarkBeanDto> list,String userId){
        list.forEach(addRemarkBeanDto -> {
            if(StringUtils.isNotBlank(addRemarkBeanDto.getOrderId())){
                MerchantOrderInfo merchantOrderInfo = merchantOrderInfoService.query(userId,addRemarkBeanDto.getOrderId(),null);
                if(merchantOrderInfo!=null){
                    merchantOrderInfo.setRemark(addRemarkBeanDto.getRemark());
                    merchantOrderInfoService.updateById(merchantOrderInfo);
                }
            }
        });
        return true;
    }

    /**
     * 网能订单详情【无信用】
     * @param lon
     * @param lat
     * @param typeId
     * @param orderTypeEnum
     * @return
     */
    public WorthOrderDetailResponseDto queryWorthOrderDetail(BigDecimal lon,BigDecimal lat,String typeId,OrderTypeEnum orderTypeEnum) {
        List<MerchantOrderInfo> merchantOrderInfos = merchantOrderInfoService.getWorthOrder(orderTypeEnum, typeId);
        WorthOrderDetailResponseDto worthOrderDetailResponseDto = null;
        if (merchantOrderInfos != null && merchantOrderInfos.size() > 0) {
            worthOrderDetailResponseDto = new WorthOrderDetailResponseDto();
            MerchantOrderInfo merchantOrderFrist = merchantOrderInfos.get(0);
            Merchant merchant = merchantAction.getMerchantService().selectById(merchantOrderFrist.getMerchantId());
            worthOrderDetailResponseDto.setMerchantId(merchantOrderFrist.getMerchantId());
            if (merchant != null) {
                worthOrderDetailResponseDto.setUserId(merchant.getUserId());
                worthOrderDetailResponseDto.setMerchantName(merchant.getName());
                worthOrderDetailResponseDto.setHoldCredit(merchant.getSupportCredit());
                worthOrderDetailResponseDto.setAddress(getAddress(merchant));
                worthOrderDetailResponseDto.setDistrict(DistrictUtil.calcDistance(lat, lon, merchant.getLat(), merchant.getLon()));
            }
            //添加类目
            addCategory(worthOrderDetailResponseDto, merchantOrderFrist.getMerchantId());
            //添加logo
            addLogo(worthOrderDetailResponseDto, merchantOrderFrist.getMerchantId());
            //添加首单红包
            worthOrderDetailResponseDto.setRedRate(merchantPacketSetService.getFirstRateByMerchatId(merchantOrderFrist.getMerchantId()));
            long total = 0l;
            List<OrderProductBean> orderProductBeans = new ArrayList<>();
            List<OrderProductBean> tempProducts;
            for (MerchantOrderInfo merchantOrderInfo : merchantOrderInfos) {
                total += merchantOrderInfo.getOrderTotalFee() + merchantOrderInfo.getShippingFee();
                tempProducts = queryOrderProductBean(merchantOrderInfo.getOrderNo());
                if (tempProducts.size() > 0) {
                    orderProductBeans.addAll(tempProducts);
                }
            }
            worthOrderDetailResponseDto.setTotalPrice(Money.CentToYuan(total).getAmount());
            worthOrderDetailResponseDto.setProductBeans(orderProductBeans);
        }
        return worthOrderDetailResponseDto;
    }

    /**
     * 补充商家logo
     * @param worthOrderDetailResponseDto
     * @param merchantId
     */
    private void addLogo(WorthOrderDetailResponseDto worthOrderDetailResponseDto,String merchantId){
        List<String> list = merchantPictureService.getPictureUrl(merchantId, MerchantPictureEnum.LOGO.getType());
        if(list!=null && list.size()>0){
            worthOrderDetailResponseDto.setMerchantLogo(addImgUrlPreUtil.getProductImgPre(list.get(0)));
        }
    }

    /**
     * 补充商家类目
     * @param worthOrderDetailResponseDto
     * @param merchantId
     */
    private void addCategory(WorthOrderDetailResponseDto worthOrderDetailResponseDto,String merchantId){
        List<String> categoryIds = merchantCategoryAction.getCategoryIdByMerchantId(merchantId);
        if(categoryIds!=null && categoryIds.size()>0){
            List<String> tages = categoryService.getKidCategoryName(categoryIds);
            List<String> categories = categoryService.getParentCategoryName(categoryIds);
            if(tages == null){
                tages = new ArrayList<>();
            }
            if(categories == null){
                categories = new ArrayList<>();
            }
            worthOrderDetailResponseDto.setCategories(categories);
            worthOrderDetailResponseDto.setTages(tages);
        }
    }

    /**
     * 补充商家地址
     * @param merchant
     * @return
     */
    private String getAddress(Merchant merchant){
        String address = "";
//        if(StringUtils.isNotBlank(merchant.getProvinceCode())){
//            address+=merchant.getProvinceCode();
//        }
//        if(StringUtils.isNotBlank(merchant.getCityCode())){
//            address+=merchant.getCityCode();
//        }
//        if(StringUtils.isNotBlank(merchant.getAreaCode())){
//            address+=merchant.getAreaCode();
//        }
//        if(StringUtils.isNotBlank(merchant.getAddrCountry())){
//            address+=merchant.getAddrCountry();
//        }
        if(StringUtils.isNotBlank(merchant.getAddrDetail())){
            address+=merchant.getAddrDetail();
        }
        if(StringUtils.isNotBlank(merchant.getAddrDoorNumber())){
            address+=merchant.getAddrDoorNumber();
        }
        return address;
    }

    /**
     * 通用订单列表
     * @param orderQueryEnum
     * @param current
     * @param size
     * @return
     */
    public List<OrderListResponseDto> getOrderList(UserGeo userGeo, OrderQueryEnum orderQueryEnum, int current, int size) throws Exception {
        List<MerchantOrderInfo> merchantOrderInfos = merchantOrderInfoService.getList(userGeo.getUserId(),null,new Page(current,size),orderQueryEnum.getStatus());
        return queryOrderList(merchantOrderInfos,userGeo.getLat(),userGeo.getLon());
    }

    /* 获取我收到的订单列表 **/
    public List<OrderListResponseDto> getOrderListByUserNumber(UserGeo userGeo,String userNumber, OrderQueryEnum orderQueryEnum, int current, int size) throws Exception {
        /* 根据网号获取商家Id **/
        List<String> merchantIds = merchantManagerAction.getMerchantIdByUserNetworkNum(userNumber);
        if(merchantIds==null || merchantIds.size()<1){
            return new ArrayList<>();
        }
        /* 获取订单列表 **/
        List<MerchantOrderInfo> merchantOrderInfos = merchantOrderInfoService.getList(null,merchantIds,new Page(current,size),orderQueryEnum.getStatus());
        return queryOrderList(merchantOrderInfos,userGeo.getLat(),userGeo.getLon());
    }

    private List<OrderListResponseDto> queryOrderList(List<MerchantOrderInfo> merchantOrderInfos,Double lat,Double lon) throws Exception {
        List<OrderListResponseDto> list = new ArrayList<>();
        if(merchantOrderInfos!=null){
            for(MerchantOrderInfo merchantOrderInfo:merchantOrderInfos){
                list.add(createOrderListResponseDto(merchantOrderInfo,lat,lon));
            }
        }
        return list;
    }

    /**
     * 未支付的订单列表
     * @param userId
     * @return
     */
    public List<OrderItemListResponseDto> getOrderList(String userId,String[] ids){
        List<MerchantOrderInfo> merchantOrderInfos = merchantOrderInfoService.getList(userId,OrderStatusEnum.OS_ORDER,ids);
        List<OrderItemListResponseDto> list = new ArrayList<>();
        if(merchantOrderInfos!=null){
            for(MerchantOrderInfo merchantOrderInfo:merchantOrderInfos){
                list.add(createOrderItemListResponseDto(merchantOrderInfo));
            }
        }
        return list;
    }

    public Map<String,Object> getOrderMap(String userId,String[] ids){
        List<MerchantOrderInfo> merchantOrderInfos = merchantOrderInfoService.getList(userId,OrderStatusEnum.OS_ORDER,ids);
        Map<String,Object> map = new HashMap<>();
        if(merchantOrderInfos!=null && merchantOrderInfos.size()>0){
            MerchantOrderInfo merchantOrderInfo = merchantOrderInfos.remove(0);
            map.put("merchantId",merchantOrderInfo.getMerchantId());
            map.put("name",merchantAction.getMerchantService().getMerchantNameById(merchantOrderInfo.getMerchantId()));
            List<OrderItemResponseDto> list = new ArrayList<>();
            OrderItemResponseDto responseDto = createOrderItemResponseDto(merchantOrderInfo);
            list.add(responseDto);
            long total = merchantOrderInfo.getOrderTotalFee()+merchantOrderInfo.getShippingFee();
            for (MerchantOrderInfo orderInfo:merchantOrderInfos) {
                responseDto = createOrderItemResponseDto(orderInfo);
                total+=orderInfo.getOrderTotalFee()+orderInfo.getShippingFee();
                list.add(responseDto);
            }
            map.put("total",Money.CentToYuan(total).getAmount());
            map.put("list",list);
        }
        return map;
    }

    /**
     * 创建订单列表
     * @param merchantOrderInfo
     * @return
     */
    public OrderListResponseDto createOrderListResponseDto(MerchantOrderInfo merchantOrderInfo, Double lat, Double lon) {
        Merchant merchant = merchantAction.getMerchantService().selectById(merchantOrderInfo.getMerchantId());
        OrderListResponseDto orderListResponseDto = new OrderListResponseDto();
        if(StringUtils.isNotBlank(merchantOrderInfo.getShippingLogisticsNo()) && StringUtils.isNotBlank(merchantOrderInfo.getShippingCode())){
            orderListResponseDto.setShippingDetail(aliyunExpressService.jsonToExpressDateLists(aliyunExpressService.getExpressData(merchantOrderInfo.getShippingLogisticsNo(),merchantOrderInfo.getShippingCode())));
            orderListResponseDto.setShippingLogisticsNo(merchantOrderInfo.getShippingLogisticsNo());
        }
        if(StringUtils.isNotBlank(merchantOrderInfo.getShippingCode())){
            orderListResponseDto.setShippingName( merchantExpressService.getNameByType(merchantOrderInfo.getShippingCode()));
        }
        orderListResponseDto.setOrderTime(merchantOrderInfo.getOrderTime());
        orderListResponseDto.setMerchantUserId(merchant.getUserId());
        orderListResponseDto.setDistance(DistrictUtil.calcDistance(merchant.getLat().doubleValue(),merchant.getLon().doubleValue(),lat,lon));
        orderListResponseDto.setOrderNo(merchantOrderInfo.getOrderNo());
        orderListResponseDto.setRemark(merchantOrderInfo.getRemark());
        orderListResponseDto.setDeliveryWay(merchantOrderInfo.getDeliveryWay());
        orderListResponseDto.setOrderId(merchantOrderInfo.getId());
        OrderStatusEnum orderStatusEnum = OrderStatusEnum.valueOf(merchantOrderInfo.getOrderStatus());
        orderListResponseDto.setOrderStatus(orderStatusEnum.order());
        orderListResponseDto.setMerchantId(merchantOrderInfo.getMerchantId());
        orderListResponseDto.setName(merchantAction.getMerchantService().getMerchantNameById(merchantOrderInfo.getMerchantId()));
        orderListResponseDto.setTotalPrice(Money.CentToYuan(merchantOrderInfo.getOrderTotalFee()+merchantOrderInfo.getShippingFee()).getAmount());
        try{
            orderListResponseDto.setProductBeans(queryOrderProductBean(merchantOrderInfo.getOrderNo()));
        }catch (Exception e){
            logger.error(merchantOrderInfo.getOrderNo(),e.getMessage());
        }
        orderListResponseDto.setUserId(merchantOrderInfo.getUserId());
        orderListResponseDto.setUserName(merchantOrderInfo.getUserName());
        orderListResponseDto.setShippingFee(Money.CentToYuan(merchantOrderInfo.getShippingFee()).getAmount());
        return orderListResponseDto;
    }

    /**
     *
     * @param merchantOrderInfo
     * @return
     */
    private OrderItemListResponseDto createOrderItemListResponseDto(MerchantOrderInfo merchantOrderInfo){
        OrderItemListResponseDto orderItemListResponseDto = new OrderItemListResponseDto();
        orderItemListResponseDto.setId(merchantOrderInfo.getId());
        orderItemListResponseDto.setMerchantId(merchantOrderInfo.getMerchantId());
        orderItemListResponseDto.setName(merchantAction.getMerchantService().getMerchantNameById(merchantOrderInfo.getMerchantId()));
        orderItemListResponseDto.setOrderPrice(Money.CentToYuan(merchantOrderInfo.getOrderTotalFee()).getAmount());
        orderItemListResponseDto.setProductPrice(Money.CentToYuan(merchantOrderInfo.getProductTotalFee()).getAmount());
        orderItemListResponseDto.setRemark(merchantOrderInfo.getRemark());
        orderItemListResponseDto.setShippingFee(Money.CentToYuan(merchantOrderInfo.getShippingFee()).getAmount());
        orderItemListResponseDto.setProducts(queryOrderProductBean(merchantOrderInfo.getOrderNo()));
        orderItemListResponseDto.setDeliveryWay(merchantOrderInfo.getDeliveryWay());
        return orderItemListResponseDto;
    }

    private OrderItemResponseDto createOrderItemResponseDto(MerchantOrderInfo merchantOrderInfo){
        OrderItemResponseDto orderItemListResponseDto = new OrderItemResponseDto();
        orderItemListResponseDto.setId(merchantOrderInfo.getId());
        orderItemListResponseDto.setOrderPrice(Money.CentToYuan(merchantOrderInfo.getOrderTotalFee()).getAmount());
        orderItemListResponseDto.setProductPrice(Money.CentToYuan(merchantOrderInfo.getProductTotalFee()).getAmount());
        orderItemListResponseDto.setRemark(merchantOrderInfo.getRemark());
        orderItemListResponseDto.setShippingFee(Money.CentToYuan(merchantOrderInfo.getShippingFee()).getAmount());
        orderItemListResponseDto.setProducts(queryOrderProductBean(merchantOrderInfo.getOrderNo()));
        orderItemListResponseDto.setDeliveryWay(merchantOrderInfo.getDeliveryWay());
        return orderItemListResponseDto;
    }

    /**
     * 根据订单号获取商品
     * @param orderNo
     * @return
     */
    public List<OrderProductBean>  queryOrderProductBean(String orderNo){
        List<OrderProductBean> list = new ArrayList<>();
        List<Map<String,Object>> merchantOrderItems = merchantOrderItemAction.getMerchantOrderItemService().queryByOrderNo(orderNo);
        if(merchantOrderItems.size()>0){
            merchantOrderItems.forEach(map -> {
                list.add(createOrderProductBean(map));
            });
        }
        return list;
    }

    /**
     * 获取订单详情类
     * @param map
     * @return
     */
    private OrderProductBean createOrderProductBean(Map<String,Object> map){
        OrderProductBean orderProductBean = VoPoConverter.copyProperties(map,OrderProductBean.class);
        orderProductBean.setMarketPrice(centToYuan(map.get("finalUnitPrice")));
        orderProductBean.setPrice(centToYuan(map.get("unitPrice")));
        orderProductBean.setProductPic(addImgUrlPreUtil.getProductImgPre(orderProductBean.getProductPic()));
        orderProductBean.setDeliveryWay(productAction.queryDeliveryWay(orderProductBean.getProductId()));
        Sku sku = skuAction.getSkuMinMarketPrice(orderProductBean.getProductId());
        if (sku == null || sku.getUnit() == null) {
            logger.error("productId:"+ orderProductBean.getProductId()+",productName:"+orderProductBean.getName()+",skuId:"+sku.getId());
        }
        if(sku.getUnit()!=null){
            orderProductBean.setUnit(sku.getUnit());
        }
        Product product = productAction.getProductService().selectById(orderProductBean.getProductId());
        orderProductBean.setReturn(product.getReturn());
        if(product.getCharacteristic() != null){
            orderProductBean.setCharacteristic(product.getCharacteristic());
        }
        return orderProductBean;
    }

    private BigDecimal centToYuan(Object o){
        if(o!=null){
            return Money.CentToYuan((Long)o).getAmount();
        }
        return BigDecimal.ZERO;
    }

    /**
     * 创建订单
     * @param userId
     * @param merchantId
     * @param totalPrice
     * @param marketPrice
     * @param orderTypeEnum
     * @return
     */
    private MerchantOrderInfo create(String userId,String userName,String merchantId, Long totalPrice,Long marketPrice,OrderTypeEnum orderTypeEnum,Long shippingFee,Integer deliveryWay){
        MerchantOrderInfo productOrder = new MerchantOrderInfo();
        productOrder.setUserName(userName);
        productOrder.setMerchantId(merchantId);
        productOrder.setOrderTotalFee(marketPrice);//订单总额
        productOrder.setProductTotalFee(totalPrice);//商品总额
        productOrder.setOrderNo(OrderUtil.getOrderNumber());//订单号
        productOrder.setDeliveryWay(deliveryWay);
        //判断是否是活动里的支付 是：把支付后订单状态改为待生成，待生成状态商家列表里是不显示的，需要过滤掉
        productOrder.setOrderType(orderTypeEnum.name());
        if(orderTypeEnum.equals(OrderTypeEnum.NORMAL_ORDER)){
            productOrder.setOrderStatus(OrderStatusEnum.OS_ORDER.name());
        }else{
            productOrder.setOrderStatus(OrderStatusEnum.OS_WATTING.name());
        }
        productOrder.setPayStatus(PayStatus.PS_UNPAID.name());
        productOrder.setShippingFee(shippingFee);
        productOrder.setOrderTime(new Date());
        productOrder.setUserId(userId);
        return productOrder;
    }

    /**
     * 验证商家
     * @param merchant
     */
    private void checkMerchant(Merchant merchant){
        if(merchant==null){
            throw new RuntimeException("商家已注销");
        }
/*        if(merchant.getPayStatus()!=null && merchant.getPayStatus()!=0 ){
            throw new RuntimeException("此商家（"+merchant.getName()+"）的管理费未续费，不能添加这些商品！");
        }*/
    }

    /**
     * 验证商品
     * @param product
     * @param productName
     */
    private void checkProduct(Product product,String productName){
        if(product==null){
            throw new RuntimeException(productName+"已下架");
        }
    }

    public void changeOrderStatusToCompleted(){
        //获取所有待评论的订单
        List<MerchantOrderInfo> merchantOrderInfos = merchantOrderInfoService.getList(null, OrderStatusEnum.OS_COMMENT, null);
        if (merchantOrderInfos != null && merchantOrderInfos.size() > 0){
            //获取当前时间前三天时间戳
//            long threeDayAgo = DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(new Date().getTime(),3,2);
            long threeDayAgo = new Date().getTime();
            for (MerchantOrderInfo merchantOrderInfo : merchantOrderInfos){
                if(merchantOrderInfo.getUpdateTime() != null) {
                    long updateTime = merchantOrderInfo.getUpdateTime().getTime();
                    if (threeDayAgo-updateTime >= 259200000) {
                        merchantOrderInfo.setOrderStatus(OrderStatusEnum.OS_FINISH.name());
                        merchantOrderInfoService.updateById(merchantOrderInfo);
                    }
                }
            }
        }
    }
}

