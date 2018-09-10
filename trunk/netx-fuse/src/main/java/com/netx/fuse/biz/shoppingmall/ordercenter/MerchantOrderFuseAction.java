package com.netx.fuse.biz.shoppingmall.ordercenter;

import com.netx.common.common.enums.AliyunBucketType;
import com.netx.common.common.enums.FrozenTypeEnum;
import com.netx.common.common.enums.MessageTypeEnum;
import com.netx.common.common.enums.PushMessageDocTypeEnum;
import com.netx.common.redis.RedisInfoHolder;
import com.netx.common.user.dto.user.UserInfoAndHeadImg;
import com.netx.common.user.util.AddImgUrlPreUtil;
import com.netx.common.user.util.DateTimestampUtil;
import com.netx.common.vo.business.CancelOrderRequestDto;
import com.netx.common.vo.business.UpadteRedpacketPoolRequestDto;
import com.netx.common.vo.common.BillAddRequestDto;
import com.netx.common.vo.common.FrozenAddRequestDto;
import com.netx.common.vo.common.FrozenOperationRequestDto;
import com.netx.fuse.biz.FuseBaseAction;
import com.netx.fuse.biz.shoppingmall.merchantcenter.MerchantFuseAction;
import com.netx.fuse.biz.shoppingmall.merchantcenter.MerchantManagerFuseAction;
import com.netx.fuse.client.ucenter.WalletBillClientAction;
import com.netx.fuse.client.ucenter.WalletForzenClientAction;
import com.netx.fuse.proxy.MessagePushProxy;
import com.netx.shopping.biz.merchantcenter.MerchantAction;
import com.netx.shopping.biz.merchantcenter.MerchantExpressAction;
import com.netx.shopping.biz.merchantcenter.MerchantManagerAction;
import com.netx.shopping.biz.merchantcenter.MerchantPacketSetAction;
import com.netx.shopping.biz.ordercenter.MerchantOrderInfoAction;
import com.netx.shopping.biz.ordercenter.MerchantOrderItemAction;
import com.netx.shopping.biz.productcenter.SkuAction;
import com.netx.shopping.biz.redpacketcenter.RedpacketPoolAction;
import com.netx.shopping.enums.RedpacketPoolRecordSourceEnum;
import com.netx.shopping.enums.RedpacketPoolRecordWayEnum;
import com.netx.shopping.model.merchantcenter.Merchant;
import com.netx.shopping.model.merchantcenter.MerchantExpress;
import com.netx.shopping.model.merchantcenter.MerchantManager;
import com.netx.shopping.model.merchantcenter.constants.MerchantManagerEnum;
import com.netx.shopping.model.ordercenter.MerchantOrderInfo;
import com.netx.shopping.model.ordercenter.MerchantOrderItem;
import com.netx.shopping.model.ordercenter.constants.*;
import com.netx.shopping.model.productcenter.Sku;
import com.netx.shopping.vo.*;
import com.netx.ucenter.biz.common.WalletFrozenAction;
import com.netx.ucenter.biz.common.WzCommonImHistoryAction;
import com.netx.ucenter.biz.router.StatAction;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.util.ListToString;
import com.netx.ucenter.util.TupleToList;
import com.netx.ucenter.vo.UserIdAndNumber;
import com.netx.ucenter.vo.response.UserStatData;
import com.netx.utils.DistrictUtil;
import com.netx.utils.cache.RedisCache;
import com.netx.utils.money.Money;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Tuple;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class MerchantOrderFuseAction extends FuseBaseAction {

    private Logger logger = LoggerFactory.getLogger(MerchantOrderFuseAction.class);

    @Autowired
    private MerchantOrderInfoAction merchantOrderInfoAction;

    @Autowired
    private MerchantOrderItemAction merchantOrderItemAction;

    @Autowired
    private MerchantAction merchantAction;

    @Autowired
    private WzCommonImHistoryAction wzCommonImHistoryAction;

    @Autowired
    private SkuAction skuAction;

    @Autowired
    private WalletForzenClientAction walletForzenClientAction;

    @Autowired
    private MerchantManagerFuseAction merchantManagerFuseAction;

    @Autowired
    private MerchantManagerAction merchantManagerAction;

    @Autowired
    private MerchantExpressAction merchantExpressAction;

    @Autowired
    private UserAction userAction;

    @Autowired
    private AddImgUrlPreUtil addImgUrlPreUtil;

    @Autowired
    private MerchantFuseAction merchantFuseAction;

    @Autowired
    private MerchantPacketSetAction merchantPacketSetAction;

    @Autowired
    private WalletBillClientAction walletBillClientAction;

    @Autowired
    private RedpacketPoolAction redpacketPoolAction;

    @Autowired
    private WalletFrozenAction walletFrozenAction;

    @Autowired
    private StatAction statAction;

    @Autowired
    private RedisInfoHolder redisInfoHolder;

    private RedisCache redisCache;

    private RedisCache clientRedis() {
        redisCache = RedisCache.getRedisCache(redisInfoHolder.getHost(), redisInfoHolder.getPassword(), redisInfoHolder.getPort());
        return redisCache;
    }

    /**
     * 快递催单
     *
     * @param orderId
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean remind(String orderId, String userId) throws Exception {
        long oneDayAgo = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
        String message = "请尽快安排{0}在{1}订单号{2}发货，如订单达成后{3}内未发货，订单将自动取消";
        return remind(orderId, userId, message, oneDayAgo, "三日");
    }

    /**
     * 买手排行榜
     *
     * @param fromUserId
     * @param start
     * @param end
     * @return
     */
    /*public Map<String,Object> queryShoppingStat(String fromUserId,int start,int end){
        List<Map<String,Object>> list = merchantOrderInfoAction.getMerchantOrderInfoService().queryShoppingStat();
        Map<String,Object> myStat = null;
        Map<String,Object> shoppingStat = new HashMap<>();
        boolean flag = StringUtils.isNotBlank(fromUserId);
        List<Map<String,Object>> result = new ArrayList<>();
        if(list!=null && list.size()>0){
            myStat = statAction.queryStat(start,end,fromUserId,flag,list,result);
            for(Map<String,Object> map:result){
                changeTotal(map);
            }
        }
        if(myStat!=null){
            changeTotal(myStat);
        }else{
            myStat = new HashMap<>();
            myStat.put("total",0);
            statAction.queryUserSuggest(myStat,fromUserId,null);
        }
        shoppingStat.put("my",myStat);
        shoppingStat.put("list",result);
        return shoppingStat;
    }*/
    public Map<String, Object> queryShoppingStat(String fromUserId, int start, int end) {
        Set<Tuple> set = clientRedis().zrevrangeWithScores("ShoppingStat", 0, -1);
        Map<String, Object> result = new HashMap<>();
        //如果redis取到了数据
        if (set.size() > 0) {
            result.put("list", TupleToList.tupleToList(set, fromUserId, result, start, end));
        } else {
            long startTime = System.nanoTime();
            //List<User> userList = userAction.getUserService().getAllUserList();
            List<UserStatData> userStatDataList=userAction.getUserStatData();
            if (userStatDataList != null && userStatDataList.size() > 0) {
                userStatDataList.forEach(userStatData -> {
                    //UserStatData userStatData = userAction.queryUserStatData(user);
                    userStatData.setHeadImg(addImgUrlPreUtil.getUserImgPre(userStatData.getHeadImg()));
                    userStatData.setNum(Money.CentToYuan(merchantOrderInfoAction.getMerchantOrderInfoService().queryShoppingStat(userStatData.getId())).getAmount());
                    clientRedis().zaddOne("ShoppingStat", userStatData.getNum().doubleValue(), ListToString.objectToJson(userStatData), 600);
                });
            }
            Set<Tuple> set1 = clientRedis().zrevrangeWithScores("ShoppingStat", 0, -1);
            if (set1.size() > 0) {
                result.put("list", TupleToList.tupleToList(set1, fromUserId, result, start, end));
            }
            //获取结束时间
            long endTime = System.nanoTime();
            System.out.println("ShoppingStat排行榜遍历sql程序运行时间： " + (endTime - startTime) + "ns"+(endTime - startTime)/1000000000+"s");
        }
        return result;
    }

    private void changeTotal(Map<String, Object> map) {
        BigDecimal total = (BigDecimal) map.get("total");
        if (total == null) {
            map.put("total", 0);
        } else {
            map.put("total", Money.CentToYuan(total.longValue()).getAmount());
        }
    }

    /*public Map<String,Object> queryProduceStat(String fromUserId,int start,int end){
        List<Map<String,Object>> list = merchantAction.getMerchantService().queryMerchantIds();
        Map<String,Object> produceStat = new HashMap<>();
        Map<String,Object> myStat = null;
        List<Map<String,Object>> result = new ArrayList<>();
        boolean flag = StringUtils.isNotBlank(fromUserId);
        if(list!=null && list.size()>0){
            change(list);
            if(list.size()>0){
                list.forEach(map -> {
                    BigDecimal total = (BigDecimal) map.get("total");
                    if(total!=null){
                        map.put("total",Money.CentToYuan(total.longValue()).getAmount());
                    }
                });
                sortList(list,"total");
                myStat=statAction.queryStat(start,end,fromUserId,flag,list,result);
            }
        }
        if(flag && myStat==null){
            myStat = new HashMap<>();
            myStat.put("total",0);
            statAction.queryUserSuggest(myStat,fromUserId,null);
        }
        produceStat.put("list",result);
        produceStat.put("my",myStat);
        return produceStat;
    }*/

    public Map<String, Object> queryProduceStat(String fromUserId, int start, int end) {
        Set<Tuple> set = clientRedis().zrevrangeWithScores("ProduceStat", 0, -1);
        Map<String, Object> result = new HashMap<>();
        if (set.size() > 0) {
            result.put("list", TupleToList.tupleToList(set, fromUserId, result, start, end));
        }
        else {
            long startTime = System.nanoTime();
            //List<User> userList = userAction.getUserService().getAllUserList();
            List<UserStatData> userStatDataList=userAction.getUserStatData();
            if (userStatDataList != null && userStatDataList.size() > 0) {
                userStatDataList.forEach(userStatData -> {
                    List<String> merchantId = merchantAction.getMerchantIdByUserId(userStatData.getId());
                    userStatData.setHeadImg(addImgUrlPreUtil.getUserImgPre(userStatData.getHeadImg()));
                    if (merchantId != null && merchantId.size() > 0) {
                        long total = merchantOrderInfoAction.getMerchantOrderInfoService().queryShoppingStat(merchantId);
                        userStatData.setNum(Money.CentToYuan(total).getAmount());
                    } else {
                        userStatData.setNum(BigDecimal.ZERO);
                    }
                    clientRedis().zaddOne("ProduceStat", userStatData.getNum().doubleValue(), ListToString.objectToJson(userStatData), 600);
                });
                Set<Tuple> set1 = clientRedis().zrevrangeWithScores("ProduceStat", 0, -1);
                if (set1.size() > 0) {
                    result.put("list", TupleToList.tupleToList(set1, fromUserId, result, start, end));
                }
            }
            //获取结束时间
            long endTime = System.nanoTime();
            System.out.println("ProduceStat排行榜遍历sql程序运行时间： " + (endTime - startTime) + "ns"+(endTime - startTime)/1000000000+"s");
        }
        return result;
    }

    private void change(List<Map<String, Object>> list) {
        Map<String, Object> map;
        for (int i = list.size() - 1; i >= 0; i--) {
            map = list.get(i);
            try {
                queryProduceStat(map);
            } catch (Exception e) {
                logger.warn(e.getMessage());
                list.remove(map);
            }
        }
    }

    public void queryProduceStat(Map<String, Object> map) {
        Object o = map.remove("ids");
        long total = 0l;
        if (o != null) {
            String merchantIds = o.toString();
            if (StringUtils.isNotBlank(merchantIds)) {
                String[] ids = merchantIds.split(",");
                total = merchantOrderInfoAction.getMerchantOrderInfoService().queryShoppingStat(ids);
            }
        }
        map.put("total", Money.CentToYuan(total).getAmount());
        map.put("userData", userAction.queryUserStatData(map.get("userId").toString()));
    }

    /**
     * 催单业务
     *
     * @param orderId
     * @param userId
     * @param message
     * @param time
     * @param other
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    boolean remind(String orderId, String userId, String message, long time, String other) throws Exception {
        MerchantOrderInfo merchantOrderInfo = merchantOrderInfoAction.getMerchantOrderInfoService().query(userId, orderId, null);
        if (merchantOrderInfo == null) {
            throw new RuntimeException("此订单已不存在");
        }

        //获取当前时间前1天的时间戳 一天只能催一次 （第三方配送）
        //判断是否可以催单
        if (merchantOrderInfo.getRemindTime() != null && time < merchantOrderInfo.getRemindTime().getTime()) {
            return false;
        } else {
            //提醒商家发货
            MessageFormat mf = new MessageFormat(message);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
            String orderDate = simpleDateFormat.format(merchantOrderInfo.getOrderTime());
            if (other == null) {
                other = (System.currentTimeMillis() - merchantOrderInfo.getOrderTime().getTime()) / (60 * 1000) + "";
            }
            User user = userAction.getUserService().selectById(userId);
            String nickname = null;
            if (user != null) {
                nickname = user.getNickname();
            }
            String alertMsg = mf.format(new String[]{nickname, orderDate, merchantOrderInfo.getOrderNo(), other});
            //获取商家主管用户id
            String manageUserId = getMangerUserId(merchantOrderInfo.getMerchantId());
            if (StringUtils.isNotEmpty(manageUserId)) {
                wzCommonImHistoryAction.add(userId, manageUserId, alertMsg, merchantOrderInfo.getId(), MessageTypeEnum.PRODUCT_TYPE, PushMessageDocTypeEnum.GoodsOrderDetail, null);
            }
            //催单成功 修改提醒时间
            merchantOrderInfo.setRemindTime(new Date());
            merchantOrderInfoAction.getMerchantOrderInfoService().updateById(merchantOrderInfo);
        }
        return true;
    }

    /**
     * 外卖催单
     *
     * @param orderId
     * @param userId
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean reminder(String orderId, String userId) throws Exception {
        //获取当前时间前10分钟的时间戳 每10分钟可以催一次 （外卖配送）
        long tenMinAgo = System.currentTimeMillis() - 10 * 60 * 1000;
        String message = "请尽快安排{0}在{1}的{2}号订单，对方已下单{3}分钟，如继续延误，对方有权取消订单";
        return remind(orderId, userId, message, tenMinAgo, null);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean send(String userId, MerchantOrderSendRequestDto request) throws Exception {
        MerchantExpress merchantExpress = merchantExpressAction.getExpressService().getExpressByType(request.getLogisticsCode());
        if (merchantExpress == null) {
            throw new RuntimeException("物流公司不存在");
        }
        MerchantOrderInfo merchantOrderInfo = merchantOrderInfoAction.getMerchantOrderInfoService().selectById(request.getId());
        if (merchantOrderInfo == null) {
            throw new RuntimeException("订单不存在！");
        }
        merchantOrderInfo.setShippingTime(new Date());
        merchantOrderInfo.setShippingStatus(ShippingStatus.SS_SHIPPING.name());
        merchantOrderInfo.setShippingLogisticsNo(request.getLogisticsNo());
        merchantOrderInfo.setShippingCode(request.getLogisticsCode());
        merchantOrderInfo.setOrderStatus(OrderStatusEnum.OS_SHIPPING.name());
        MessageFormat mf = new MessageFormat("你的{0}号订单商家已发货，物流公司名称为{1}，物流单号为{2}，请待查收");
        String alertMsg = mf.format(new String[]{merchantOrderInfo.getOrderNo(), merchantExpress.getName(), request.getLogisticsNo()});
        wzCommonImHistoryAction.add(userId, merchantOrderInfo.getUserId(), alertMsg, merchantOrderInfo.getId(), MessageTypeEnum.PRODUCT_TYPE, PushMessageDocTypeEnum.GoodsOrderDetail, null);
        return merchantOrderInfoAction.send(merchantOrderInfo);
    }

    /**
     * 确认收货
     *
     * @param orderId
     * @param userId
     */
    @Transactional(rollbackFor = Exception.class)
    public void receive(String orderId, String userId) throws Exception {
        MerchantOrderInfo merchantOrderInfo = merchantOrderInfoAction.getMerchantOrderInfoService().selectById(orderId);
        if (merchantOrderInfo == null) {
            throw new RuntimeException("订单不存在");
        }
        merchantOrderInfo.setOrderStatus(OrderStatusEnum.OS_COMMENT.name());
        ShippingStatus shippingStatus = null;
        if (!merchantOrderInfo.getDeliveryWay().equals(2)) {
            shippingStatus = ShippingStatus.SS_RECEIVED;
            merchantOrderInfo.setShippingStatus(shippingStatus.name());
        }
        merchantOrderInfoAction.getMerchantOrderInfoService().updateById(merchantOrderInfo);
        merchantOrderItemAction.updateStatus(merchantOrderInfo.getOrderNo(), OrderStatusEnum.OS_COMMENT, null, null, shippingStatus);
        Money money = Money.CentToYuan(merchantOrderInfo.getOrderTotalFee() + merchantOrderInfo.getShippingFee());
        MessageFormat messageFormat = new MessageFormat("订单号{0}，金额{1}元，用户已确认收货并完成成交，请注意查收并评论交易");
        String alertMsg = messageFormat.format(new String[]{merchantOrderInfo.getOrderNo(), money.getAmount().toString()});
        //查询主管id
        String manageUserId = getMangerUserId(merchantOrderInfo.getMerchantId());
        if (StringUtils.isNotBlank(manageUserId)) {
            wzCommonImHistoryAction.add(userId, manageUserId, alertMsg, merchantOrderInfo.getId(), MessageTypeEnum.PRODUCT_TYPE, PushMessageDocTypeEnum.GoodsOrderDetail, null);
        }
        FrozenOperationRequestDto requestDto = new FrozenOperationRequestDto();
        requestDto.setTypeId(orderId);
        requestDto.setUserId(userId);
        requestDto.setType(FrozenTypeEnum.FTZ_PRODUCT);
        if (!walletFrozenAction.pay(requestDto)) {
            throw new RuntimeException("使用冻结金额异常");
        }
    }

    private String getMangerUserId(String merchantId) {
        String userNumber = merchantManagerAction.getMerchantManagerService().getUserNetworkNumByMerchantId(merchantId, MerchantManagerEnum.MANAGER);
        if (StringUtils.isNotBlank(userNumber)) {
            return userAction.getUserService().getUserIdByUserNumber(userNumber);
        }
        return null;
    }

    /**
     * 网能详情
     *
     * @param lon
     * @param lat
     * @param typeId
     * @param orderTypeEnum
     * @return
     */
    public WorthOrderDetailResponseDto queryWorthOrderDetail(BigDecimal lon, BigDecimal lat, String typeId, OrderTypeEnum orderTypeEnum) {
        //这里是根据前端传过来的详情id，查找“显示用户”。（不是当前登录用户！）
        WorthOrderDetailResponseDto worthOrderDetailResponseDto = merchantOrderInfoAction.queryWorthOrderDetail(lon, lat, typeId, orderTypeEnum);
        if (worthOrderDetailResponseDto != null && StringUtils.isNotBlank(worthOrderDetailResponseDto.getUserId())) {
            //  System.out.println(worthOrderDetailResponseDto.getUserId());
            //这里就是去dto里面的“显示用户”id。取用户信息
            User user = userAction.queryUser(worthOrderDetailResponseDto.getUserId());
            //System.out.println(user.getCredit());
            //这是返回dto，没有其他干扰
            worthOrderDetailResponseDto.setCredit(user.getCredit());
            worthOrderDetailResponseDto.setCashierId(getManageId(worthOrderDetailResponseDto.getMerchantId(), MerchantManagerEnum.CASHIER));
        }
        return worthOrderDetailResponseDto;
    }

    public MerchantOrderDetailResponseDto queryDetail(String orderId, String userNumber, double lat, double lon) {
        MerchantOrderDetailResponseDto merchantOrderDetailResponseDto = merchantOrderInfoAction.queryDetail(orderId, userNumber, lat, lon);
        if (merchantOrderDetailResponseDto != null) {
            if (merchantOrderDetailResponseDto.getMerchantUserId() != null) {
                UserInfoAndHeadImg userInfoAndHeadImg = userAction.getUserInfoAndHeadImg(merchantOrderDetailResponseDto.getUserId());
                if (userInfoAndHeadImg != null) {
                    merchantOrderDetailResponseDto.setHeadImgUrl(userInfoAndHeadImg.getHeadImgUrl());
                }
            }
        }
        merchantOrderDetailResponseDto.setCashierId(getManageId(merchantOrderDetailResponseDto.getMerchantId(), MerchantManagerEnum.CASHIER));
        merchantOrderDetailResponseDto.setCustomeUserId(getManageId(merchantOrderDetailResponseDto.getMerchantId(), MerchantManagerEnum.CUSTOMERSERVICE));
        return merchantOrderDetailResponseDto;
    }


    private String getManageId(String merchantId, MerchantManagerEnum merchantManagerEnum) {
        MerchantManager merchantManager = merchantManagerAction.getMerchantManagerService().getMerchantManagerByMerchantIdAndMerchantUserType(merchantId, merchantManagerEnum.getName());
        if (merchantManager == null) {
            logger.warn(merchantId + "收银员不存在");
            return null;
        }
        return userAction.getUserService().getUserIdByUserNumber(merchantManager.getUserNetworkNum());
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean newPayOrder(String userId, MerchantPayOrderRequestDto dto) throws Exception {
        Merchant merchant = merchantAction.getMerchantService().selectById(dto.getMerchantId());
        if (merchant == null) {
            throw new RuntimeException("此商家不存在");
        }
        User user = userAction.getUserService().selectById(userId);
        if (user == null) {
            throw new Exception("获取用户信息异常");
        }
        List<MerchantOrderInfo> merchantOrderInfos = merchantOrderInfoAction.getMerchantOrderInfoService().selectBatchIds(dto.getOrderId());
        long total = 0l;
        //计算支付总额
        for (MerchantOrderInfo merchantOrderInfo : merchantOrderInfos) {
            total += merchantOrderInfo.getOrderTotalFee() + merchantOrderInfo.getShippingFee();
        }
        long price = new Money(dto.getPayPrices()).getCent() + getCreditPrice(dto.getPayType(), dto.getCreditPay());
        if (price != total) {
            throw new RuntimeException("支付的金额不等于所需要支付的金额");
        }
        Map<String, UserIdAndNumber> userIdAndNumberMap = merchantManagerFuseAction.queryMangerByMerchantId(dto.getMerchantId());
        //对商家的商品库存进行修改
        Date date = new Date();
        List<String> orderNo = new ArrayList<>();
        int max = skuAction.max;
        int temp;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
//        String alert = "你有新订单产生，"+user.getNickname()+"于"+simpleDateFormat.format(date)+"下单，请及时处理";
        String alert = "你有新订单产生，于" + simpleDateFormat.format(date) + "下单，请及时处理";
        UserIdAndNumber manager = getUserIdAndNumber(userIdAndNumberMap, MerchantManagerEnum.MANAGER);
        UserIdAndNumber cashier = getUserIdAndNumber(userIdAndNumberMap, MerchantManagerEnum.CASHIER);
        for (MerchantOrderInfo merchantOrderInfo : merchantOrderInfos) {
            List<OrderProductBean> orderProductBeans = merchantOrderInfoAction.queryOrderProductBean(merchantOrderInfo.getOrderNo());
            orderNo.add(merchantOrderInfo.getOrderNo());
            for (OrderProductBean orderProductBean : orderProductBeans) {
                Sku sku;
                try {
                    sku = skuAction.checkSkuNum(orderProductBean.getSkuId(), orderProductBean.getQuantity());
                } catch (RuntimeException e) {
                    String ti = e.getMessage();
                    if (e.getMessage().equals("此规格库存不足")) {
                        sendMessage(userId, orderProductBean.getValue(), merchant, orderProductBean.getProductId(), orderProductBean.getName());
                        ti = orderProductBean.getName() + "的" + orderProductBean.getValue() + "规格库存不足，请重新购买";
                    }
                    throw new RuntimeException(ti);
                }
                //商品库存减少
                sku.setStorageNums(sku.getStorageNums() - orderProductBean.getQuantity());
                sku.setSellNums(sku.getSellNums() + orderProductBean.getQuantity());
                if (skuAction.getSkuService().updateById(sku)) {
                    //库存少于一次最高购买数量时，发送提醒给商家
                    temp = sku.getTradeMaxNums() == 0 ? max : sku.getTradeMaxNums();
                    if (sku.getStorageNums() <= temp) {
                        sendMessage(userId, orderProductBean.getValue(), merchant, orderProductBean.getProductId(), orderProductBean.getName());
                    }
                }
            }
            //修改订单状态
            merchantOrderInfo.setPaySubmitTime(date);
            merchantOrderInfo.setOrderStatus(OrderStatusEnum.OS_CONFIRMED.name());
            merchantOrderInfo.setPayStatus(PayStatus.PS_PAYED.name());
            merchantOrderInfoAction.getMerchantOrderInfoService().updateById(merchantOrderInfo);
            //转账给收银员
            pay(userId, cashier.getUserId(), dto.getPayType(), null, Money.CentToYuan(merchantOrderInfo.getOrderTotalFee() + merchantOrderInfo.getShippingFee()).getAmount(), merchantOrderInfo.getId(), merchantOrderInfo.getOrderNo());
            //提醒业务主管发货
            wzCommonImHistoryAction.add(userId, manager.getUserId(), alert, merchantOrderInfo.getId(),
                    MessageTypeEnum.PRODUCT_TYPE, PushMessageDocTypeEnum.GoodsOrderDetail, null);
        }
        //更新订单详情
        return updateOrderItem(orderNo);
    }

    private UserIdAndNumber getUserIdAndNumber(Map<String, UserIdAndNumber> userIdAndNumberMap, MerchantManagerEnum merchantManagerEnum) {
        UserIdAndNumber userIdAndNumber = userIdAndNumberMap.get(merchantManagerEnum.getName());
        if (userIdAndNumber == null) {
            throw new RuntimeException(merchantManagerEnum.getName() + "不存在");
        }
        return userIdAndNumber;
    }

    /**
     * 转账
     *
     * @param userId
     * @param toUserId
     * @param payTypeEnum
     * @param currencyId
     * @param amount
     * @param orderId
     * @param orderNo
     */
    @Transactional(rollbackFor = Exception.class)
    void pay(String userId, String toUserId, PayTypeEnum payTypeEnum, String currencyId, BigDecimal amount, String orderId, String orderNo) {
        FrozenAddRequestDto requestDto = new FrozenAddRequestDto();
        requestDto.setCurrencyId(currencyId);
        requestDto.setUserId(userId);
        requestDto.setTradeType(payTypeEnum.getTradeType());
        requestDto.setToUserId(toUserId);
        requestDto.setDescription("支付订单号：" + orderNo);
        requestDto.setFrozenType(FrozenTypeEnum.FTZ_PRODUCT.getName());
        requestDto.setAmount(amount);
        requestDto.setTypeId(orderId);
        if (!walletForzenClientAction.add(requestDto)) {
            throw new RuntimeException("添加冻结金额异常");
        }
    }

    /**
     * 更新订单详情的信息
     *
     * @param orderNos
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    boolean updateOrderItem(List<String> orderNos) {
        MerchantOrderItem merchantOrderItem = new MerchantOrderItem();
        merchantOrderItem.setTradeStatus(TradeStatus.TS_TRADED.name());
        merchantOrderItem.setUpdateTime(new Date());
        merchantOrderItem.setOrderStatus(OrderStatusEnum.OS_FINSH_PAY.name());
        merchantOrderItem.setPayStatus(PayStatus.PS_PAYED.name());
        return merchantOrderItemAction.getMerchantOrderItemService().updateOrderItem(orderNos, merchantOrderItem);
    }

    /**
     * 获取网信转换成的余额
     *
     * @param payTypeEnum
     * @param creditPayDtos
     * @return
     */
    private long getCreditPrice(PayTypeEnum payTypeEnum, List<CreditPayDto> creditPayDtos) {
        long creditPrice = 0l;
        switch (payTypeEnum) {
            case PT_ALI:
                return creditPrice;
            case PT_NONE:
                return creditPrice;
            case PT_WECHAT:
                return creditPrice;
        }
        for (CreditPayDto creditPayDto : creditPayDtos) {
            creditPrice += new Money(creditPayDto.getPrice()).getCent();
            //缺网信兑付
        }
        return creditPrice;
    }

    /**
     * 提醒商家补仓
     *
     * @param fromUserId
     * @param skuValue
     * @param merchant
     * @param productId
     * @param productName
     */
    @Transactional(rollbackFor = Exception.class)
    public void sendMessage(String fromUserId, String skuValue, Merchant merchant, String productId, String productName) throws Exception {
        wzCommonImHistoryAction.add(fromUserId, merchant.getUserId(),
                "你的\"" + merchant.getName() + "\"店内的\"" + productName + "\"的\"" + skuValue
                        + "\"规格库存不足，为了能正常运作，请快点补仓", productId,
                MessageTypeEnum.PRODUCT_TYPE, PushMessageDocTypeEnum.PRODUCT_INVENTORY_DETAIL, null);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean cancel(CancelOrderRequestDto request) throws Exception {
        //修改订单状态
        MerchantOrderInfo merchantOrderInfo = merchantOrderInfoAction.getMerchantOrderInfoService().selectById(request.getId());
        merchantOrderInfo.setId(request.getId());
        merchantOrderInfo.setCancelReason(request.getReason());
        merchantOrderInfo.setCancelTime(new Date());
        //获取商家Id
        String merchantId = merchantOrderInfo.getMerchantId();
        PayStatus payStatus = PayStatus.valueOf(merchantOrderInfo.getPayStatus());
        boolean updateRet = false;
        switch (payStatus) {
            case PS_UNPAID:
                merchantOrderInfo.setOrderStatus(OrderStatusEnum.OS_CANCELED.name());
                updateRet = merchantOrderInfoAction.getMerchantOrderInfoService().updateById(merchantOrderInfo);
                break;
            case PS_PAYED:
                if (merchantOrderInfo.getOrderStatus().equals(OrderStatusEnum.OS_CONFIRMED.name())) {
                    merchantOrderInfo.setOrderStatus(OrderStatusEnum.OS_CANCELED.name());
                    updateRet = merchantOrderInfoAction.getMerchantOrderInfoService().updateById(merchantOrderInfo);
                } else {
                    OrderStatusEnum orderStatusEnum = OrderStatusEnum.valueOf(merchantOrderInfo.getOrderStatus());
                    throw new RuntimeException("该订单处于" + orderStatusEnum.getUserDesc() + "不能取消订单，请走退款退货流程");
                }
                break;
            default:
                throw new RuntimeException("订单状态不合法:" + merchantOrderInfo.getOrderNo());
        }


        //如果订单已支付则退款
        if (merchantOrderInfo.getPayStatus().equals(PayStatus.PS_PAYED.name())) {
            this.repealFrozen(request.getId(), merchantOrderInfo.getUserId());
            /* 如果是顾客取消，退款时扣取订单的红包提成金额  大赛期间，不扣取红包提成 **/
//            if(merchantOrderInfo.getUserId().equals(request.getUserId())){
//                BigDecimal packAmount=merchantOrderInfoAction.getDirectRedPacketAmount(merchantOrderInfo);
//                this.directRedPackMoney(request.getId(),request.getUserId(),packAmount);
//            }
        }

        //判断是商家取消还是用户取消还是系统取消  userId为空时系统取消，userId等于订单userId为用户取消，等于商家suerId为商家取消
        if (request.getUserId() == null) {
            //系统自动取消时 给认购者发送信息
//            MessageFormat mf=new MessageFormat("您订购的订单号为：{0} 的商品，因商家原因未能成交，你的订购款已返回你的账户，谢谢参与");
//            String alertMsg=mf.format(new String[] {merchantOrderInfo.getOrderNo()});
            //获取认购者userId
            String userId = merchantOrderInfo.getUserId();
            //系统推送
//            wzCommonImHistoryAction.add(request.getUserId(),merchantOrderInfo.getUserId(), alertMsg,merchantOrderInfo.getId(),
//                    MessageTypeEnum.PRODUCT_TYPE, PushMessageDocTypeEnum.GoodsOrderDetail,null);
            //扣减商家注册者、法人代表信用值

            MerchantManager merchantManager = getManageOne(merchantId, 2);
            //获取商家注册者userId
            String merchantUserId = merchantManager.getUserId();
            //获取商家法人代表网号
            String verifyNetworkNum = merchantManager.getUserNetworkNum();
            List<String> userIds = new ArrayList<>();
            userIds.add(merchantUserId);
            //获取商家注册者乐观锁
            User user = userAction.getUserService().selectById(userId);
            if (user != null) {
                merchantFuseAction.addCredit(merchantId, user, -5, "订单（" + merchantOrderInfo.getOrderNo() + "）因未按时发货，已被自动取消，认购款已全额返回认购者，你的信用值已被扣减5分");
                if (!user.getUserNumber().equals(verifyNetworkNum)) {
                    User verify = userAction.getUserByUserNumber(verifyNetworkNum);
                    if (verify != null) {
                        userIds.add(verify.getId());
                        merchantFuseAction.addCredit(merchantId, verify, -5, "订单（" + merchantOrderInfo.getOrderNo() + "）因未按时发货，已被自动取消，认购款已全额返回认购者，你的信用值已被扣减5分");
                    }
                }
            }
        } else {
            //获取当前时间
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String nowDate = simpleDateFormat.format(new Date().getTime());
            //获取商家主管userId
            MerchantManager merchantManager = getManageOne(merchantId, 1);
            String manageUserId = userAction.getUserService().getUserIdByUserNumber(merchantManager.getUserNetworkNum());
            if (manageUserId == null) {
                throw new RuntimeException("获取商家主管userId失败");
            }
            //用户取消时
            if (merchantOrderInfo.getUserId().equals(request.getUserId())) {
                //给商家主管发消息
                MessageFormat mf2 = new MessageFormat("用户已于{0}取消此订单。用户的订单金额扣除红包提成后的余额已退回其账户。交易结束");
                //获取当前时间
                String alertMsg2 = mf2.format(new String[]{nowDate});
                if (StringUtils.isNotEmpty(manageUserId)) {
                    wzCommonImHistoryAction.add(request.getUserId(), manageUserId, alertMsg2, merchantOrderInfo.getId(),
                            MessageTypeEnum.PRODUCT_TYPE, PushMessageDocTypeEnum.GoodsOrderDetail, null);
                }
            } else {
                //商家取消时
                //给商家主管发消息
                MessageFormat mf4 = new MessageFormat("你已于{0}取消此订单。用户的订单金额扣除红包提成后的余额已退回其账户。交易结束");
                String alertMsg4 = mf4.format(new String[]{nowDate});
                if (StringUtils.isNotEmpty(manageUserId)) {
                    wzCommonImHistoryAction.add(request.getUserId(), manageUserId, alertMsg4, merchantOrderInfo.getId(),
                            MessageTypeEnum.PRODUCT_TYPE, PushMessageDocTypeEnum.GoodsOrderDetail, null);
                }
                //给认购者发消息
                MessageFormat mf5 = new MessageFormat("你{0}的订单已被商家取消，交易已结束 扣除红包计提部分后的订单款已退回你账户，请注意查收");
                String alertMsg5 = mf5.format(new String[]{nowDate});
                wzCommonImHistoryAction.add(request.getUserId(), merchantOrderInfo.getUserId(), alertMsg5, merchantOrderInfo.getId(),
                        MessageTypeEnum.PRODUCT_TYPE, PushMessageDocTypeEnum.GoodsOrderDetail, null);

            }
        }
        return updateRet;
    }

    @Transactional(rollbackFor = Exception.class)
    public void repealFrozen(String orderId, String userId) {
        FrozenOperationRequestDto requestDto = new FrozenOperationRequestDto();
        requestDto.setTypeId(orderId);
        requestDto.setUserId(userId);
        requestDto.setType(FrozenTypeEnum.FTZ_PRODUCT);
        if (!walletFrozenAction.repeal(requestDto)) {
            throw new RuntimeException("使用冻结金额异常");
        }
    }

    /**
     * 扣取顾客订单的红包提成，当顾客取消订单时
     *
     * @param
     * @return
     * @throws
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean directRedPackMoney(String orderId, String userId, BigDecimal packetAmount) {
        BillAddRequestDto billAddRequestDto = new BillAddRequestDto();
        billAddRequestDto.setToUserId("999");
        billAddRequestDto.setAmount(packetAmount);
        billAddRequestDto.setDescription("从顾客钱包中计提金额加入红包池");
        billAddRequestDto.setPayChannel(4);
        Boolean res = walletBillClientAction.addBill(userId, billAddRequestDto);
 /*       if(!res){
            messagePushProxy.messagePushJump(MessageTypeEnum.PRODUCT_TYPE,"账户余额不足，无法计提由于你取消订单需扣取的红包提成金额，请充值钱包", "顾客钱包余额不足", userId, PushMessageDocTypeEnum.LuckyMoneyDetail, orderId);
        }*/
        //将金额加入红包池
        UpadteRedpacketPoolRequestDto upadteRedpacketPoolRequestDto = new UpadteRedpacketPoolRequestDto();
        upadteRedpacketPoolRequestDto.setAmount(packetAmount);
        upadteRedpacketPoolRequestDto.setSource(RedpacketPoolRecordSourceEnum.TRADE_AMOUNT_COMMISSION.getCode());
        upadteRedpacketPoolRequestDto.setSourceId(orderId);
        upadteRedpacketPoolRequestDto.setWay(RedpacketPoolRecordWayEnum.INCOME.getCode());
        redpacketPoolAction.upadteRedpacketPool(upadteRedpacketPoolRequestDto);
        //messagePushProxy.messagePushJump(MessageTypeEnum.PRODUCT_TYPE,"由于你取消订单，平台已自动从你钱包中扣取订单的红包提成金额","扣取订单红包提成金额", userId, PushMessageDocTypeEnum.LuckyMoneyDetail, orderId);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean directRedPackMoney(String orderId) throws Exception {
        MerchantOrderInfo merchantOrderInfo = merchantOrderInfoAction.getMerchantOrderInfoService().selectById(orderId);
        //按商家设定的红包提成标准，按当前订单金额计提红包金额至红包池
        Merchant merchant = merchantAction.getMerchantService().selectById(merchantOrderInfo.getMerchantId());
        if (merchant != null) {
            BigDecimal packetAmount = merchantPacketSetAction.getDirectRedPacketAmount(merchantOrderInfo);
            if (packetAmount != null) {
                merchant.setPacketPoolAmount(new Money(merchant.getPacketPoolAmount()).getCent() + new Money(packetAmount).getCent());
                merchantAction.getMerchantService().updateById(merchant);
                // 从商家钱包扣钱
                //获取商家收银人员userId
                MerchantManager merchantManager = getManageOne(merchant.getId(), 0);
                String moneyUserId = userAction.getUserService().getUserIdByUserNumber(merchantManager.getUserNetworkNum());
                if (moneyUserId == null) {
                    throw new RuntimeException("获取收银人员信息失败");
                }
                BillAddRequestDto billAddRequestDto = new BillAddRequestDto();
                billAddRequestDto.setToUserId("999");
                billAddRequestDto.setAmount(packetAmount);
                billAddRequestDto.setDescription("从商家钱包中计提金额加入红包池");
                billAddRequestDto.setPayChannel(4);
                Boolean res = walletBillClientAction.addBill(moneyUserId, billAddRequestDto);
                //if (res.getCode()!=0&&res.getMessage().equals("账户余额不足")){
                if (!res) {
                    //获取业务主管userId
                    MerchantManager manager = getManageOne(merchant.getId(), 1);
                    String manageUserId = userAction.getUserService().getUserIdByUserNumber(manager.getUserNetworkNum());
                    if (manageUserId == null) {
                        throw new RuntimeException("获取主管信息失败");
                    }
                    //系统推送
                    //去重后发推送信息
//                    List<String> userIds = new ArrayList<>();
//                    userIds.add(merchant.getUserId());
//                    if(!manageUserId.equals(merchant.getUserId())){
//                        userIds.add(manageUserId);
//                    }
//                    for (String string : userIds){
//                        //商家钱包余额不足计提红包，则发送信息给注册者、业务主管：”账户余额不足，无法计提红包，在未完成充值前，系统将暂停你的接单资格“。该商家所有商品及其它下单按钮，均不可再点击
//                        wzCommonImHistoryAction.add(null, merchantOrderInfo.getUserId(), "账户余额不足，无法计提红包，在未完成充值前，系统将暂停你的接单资格",merchantOrderInfo.getId(),
//                                MessageTypeEnum.PRODUCT_TYPE, PushMessageDocTypeEnum.GoodsOrderDetail,null);
//                    }
                }
                //将金额加入红包池
                UpadteRedpacketPoolRequestDto upadteRedpacketPoolRequestDto = new UpadteRedpacketPoolRequestDto();
                upadteRedpacketPoolRequestDto.setAmount(packetAmount);
                upadteRedpacketPoolRequestDto.setSource(RedpacketPoolRecordSourceEnum.TRADE_AMOUNT_COMMISSION.getCode());
                upadteRedpacketPoolRequestDto.setSourceId(merchant.getId());
                upadteRedpacketPoolRequestDto.setWay(RedpacketPoolRecordWayEnum.INCOME.getCode());
                redpacketPoolAction.upadteRedpacketPool(upadteRedpacketPoolRequestDto);
                MessageFormat mf = new MessageFormat("订单已成交，平台已自动扣除订单的红包提成{0}元");
                String alertMsg = mf.format(new String[]{packetAmount.toString()});
                //获取商家相关人员userId，去重后发送信息
                List<String> userIds = getMerchantUserIds(merchant.getId());
                for (String string : userIds) {
                    wzCommonImHistoryAction.add("999", string, alertMsg, orderId, MessageTypeEnum.PRODUCT_TYPE, PushMessageDocTypeEnum.GoodsOrderDetail, null);
                }
            }
        }
        return true;
    }

    private MerchantManager getManageOne(String merchantId, Integer type) {
        List<MerchantManager> merchantManagers = merchantManagerAction.getMerchantManagerListByMerchantId(merchantId, type);
        if (merchantManagers == null || merchantManagers.size() < 1) {
            throw new RuntimeException("获取相关人员信息失败");
        }
        return merchantManagers.get(0);
    }

    private List<String> getMerchantUserIds(String merchantId) {
        List<MerchantManager> merchantManagers = merchantManagerAction.getMerchantManagerByMerchantId(merchantId);
        List<String> list = new ArrayList<>();
        for (MerchantManager merchantManager : merchantManagers) {
            list.add(merchantManager.getUserNetworkNum());
        }
        List<String> userNumbers = new ArrayList<>(new HashSet<>(list));
        List<String> userIds = userAction.getUserService().getUserIdsByUserNumbers(userNumbers);
        if (userIds == null || userIds.size() < 0) {
            throw new RuntimeException("获取相关人员UserId失败");
        }
        return userIds;
    }

    /**
     * 定时任务--
     * 改变物流状态为已完成并付款给商家
     *
     * @throws Exception
     */
    public void changeLogisticsStatusAndPay() throws Exception {

        //获取商家已发货用户待收货订单，如果超过十五天（第三方配送型）或三天（外卖配送型）时间，则自动确认用户收货，并打款给商家
        List<MerchantOrderInfo> merchantOrderInfos = merchantOrderInfoAction.getMerchantOrderInfoService().getList(null, OrderStatusEnum.OS_SHIPPING, null);

        //获取当前时间前15天时间戳
        long fifteenDayAgo = DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(new Date().getTime(), 15, 2);
        //获取当前时间前3天时间戳
        long threeDayAgo = DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(new Date().getTime(), 3, 2);

        if (merchantOrderInfos != null) {
            for (MerchantOrderInfo merchantOrderInfo : merchantOrderInfos) {
                //获取发货时间
                long sendTime = 0;
                if (merchantOrderInfo.getShippingTime() != null) {
                    sendTime = merchantOrderInfo.getShippingTime().getTime();
                }
                //第三方配送
                if (merchantOrderInfo.getDeliveryWay() == 1) {
                    if (sendTime < fifteenDayAgo) {
                        //改变订单状态与物流状态
                        merchantOrderInfo.setOrderStatus(OrderStatusEnum.OS_COMMENT.name());
                        merchantOrderInfo.setShippingStatus(ShippingStatus.SS_RECEIVED.name());
                        merchantOrderInfoAction.getMerchantOrderInfoService().updateById(merchantOrderInfo);
                        //使用冻结，钱转给商家
                        pay(merchantOrderInfo.getId(), merchantOrderInfo.getUserId());
                    }
                }
                //外卖配送
                if (merchantOrderInfo.getDeliveryWay() == 3) {
                    if (sendTime < threeDayAgo) {
                        //改变订单状态
                        merchantOrderInfo.setOrderStatus(OrderStatusEnum.OS_COMMENT.name());
                        merchantOrderInfo.setShippingStatus(ShippingStatus.SS_RECEIVED.name());
                        merchantOrderInfoAction.getMerchantOrderInfoService().updateById(merchantOrderInfo);
                        //使用冻结，钱转给商家
                        pay(merchantOrderInfo.getId(), merchantOrderInfo.getUserId());
                    }
                }
            }
        }
    }

    /**
     * 付款
     *
     * @param orderId
     * @param userId
     * @throws Exception
     */
    private void pay(String orderId, String userId) throws Exception {
        FrozenOperationRequestDto requestDto = new FrozenOperationRequestDto();
        requestDto.setTypeId(orderId);
        requestDto.setUserId(userId);
        requestDto.setType(FrozenTypeEnum.FTZ_PRODUCT);
        if (!walletFrozenAction.pay(requestDto)) {
            throw new Exception("使用冻结金额异常");
        }
        //扣取订单的红包提成
        this.directRedPackMoney(orderId);
    }

    /**
     * 定时任务--
     * 三天不发货退款给用户
     *
     * @throws Exception
     */
    public void checkIsSend() throws Exception {
        //获取待发货订单
        List<MerchantOrderInfo> merchantOrderInfos = merchantOrderInfoAction.getMerchantOrderInfoService().getListByDeliveryWay(null, OrderStatusEnum.OS_CONFIRMED, 3);

        //获取当前时间前三天同一时间戳
        long threeDayAgo = DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(new Date().getTime(), 3, 2);

        CancelOrderRequestDto cancelOrderRequestDto = new CancelOrderRequestDto();

        for (MerchantOrderInfo merchantOrderInfo : merchantOrderInfos) {
            if (merchantOrderInfo.getPaySubmitTime() != null) {
                long payTime = merchantOrderInfo.getPaySubmitTime().getTime();
                if (payTime < threeDayAgo) {
                    cancelOrderRequestDto.setId(merchantOrderInfo.getId());
                    try {
                        this.cancel(cancelOrderRequestDto);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        }

    }

    /* 补足订单列表人员信息 **/
    public List<OrderListResponseDto> addUserInfo(List<OrderListResponseDto> list, boolean conditions) {
        if (list.size() > 0) {
            list.forEach(orderListResponseDto -> {
                try {
                    if (conditions) {//当订单状态类型为购买时 距离为商店和收货地址间的距离
                        User user = userAction.queryUser(orderListResponseDto.getUserId());
                        if (user == null) {
                            throw new Exception("用户位置信息获取异常");
                        } else {
                            Merchant merchant = merchantAction.getMerchantService().selectById(orderListResponseDto.getMerchantId());
                            if (merchant == null) {
                                throw new Exception("店铺位置信息获取异常");
                            } else {
                                orderListResponseDto.setDistance(DistrictUtil.calcDistance(merchant.getLat(), merchant.getLon(), user.getLat(), user.getLon()));
                            }
                        }
                    }
                    //获取客服人员
                    MerchantManager merchantManager = merchantManagerAction.getMerchantManagerService().getMerchantManagerByMerchantIdAndMerchantUserType(orderListResponseDto.getMerchantId(), MerchantManagerEnum.CUSTOMERSERVICE.getName());
                    if (merchantManager != null) {
                        if (StringUtils.isNotBlank(merchantManager.getUserName())) {
                            orderListResponseDto.setCustomeUserId(userAction.getUserService().getUserIdByUserNumber(merchantManager.getUserNetworkNum()));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        return list;
    }

}

