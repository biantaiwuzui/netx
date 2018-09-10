package com.netx.fuse.biz.shoppingmall.order;

import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.common.enums.*;
import com.netx.common.express.AliyunExpressService;
import com.netx.common.redis.model.UserGeo;
import com.netx.common.redis.service.GeoService;
import com.netx.common.router.dto.bean.UserInfoResponseDto;
import com.netx.common.router.dto.request.UserInfoRequestDto;
import com.netx.common.router.enums.SelectConditionEnum;
import com.netx.common.router.enums.SelectFieldEnum;
import com.netx.common.user.util.AddImgUrlPreUtil;
import com.netx.common.user.util.DateTimestampUtil;
import com.netx.common.user.util.VoPoConverter;
import com.netx.common.vo.business.*;
import com.netx.common.vo.common.*;
import com.netx.fuse.biz.shoppingmall.business.SellerFuseAction;
import com.netx.fuse.biz.shoppingmall.product.ProductFuseAction;
import com.netx.fuse.client.ucenter.WalletBillClientAction;
import com.netx.fuse.client.ucenter.WalletForzenClientAction;
import com.netx.fuse.proxy.ArbitrationProxy;
import com.netx.fuse.proxy.MessagePushProxy;
import com.netx.fuse.proxy.UserClientProxy;
import com.netx.shopping.biz.business.CashierAction;
import com.netx.shopping.biz.business.RedpacketPoolAction;
import com.netx.shopping.biz.business.SellerAction;
import com.netx.shopping.biz.business.SellerCategoryAction;
import com.netx.shopping.biz.order.ProductOrderAction;
import com.netx.shopping.biz.order.ProductOrderItemAction;
import com.netx.shopping.biz.order.ProductReturnAction;
import com.netx.shopping.enums.*;
import com.netx.shopping.model.business.Seller;
import com.netx.shopping.model.business.SellerCashier;
import com.netx.shopping.model.business.SellerManage;
import com.netx.shopping.model.order.*;
import com.netx.shopping.model.ordercenter.MerchantOrderInfo;
import com.netx.shopping.model.product.Product;
import com.netx.shopping.model.product.ProductSpe;
import com.netx.shopping.service.business.*;
import com.netx.shopping.service.order.ExpressService;
import com.netx.shopping.service.order.ProductOrderPutOffService;
import com.netx.shopping.service.order.ProductOrderService;
import com.netx.shopping.service.order.ProductReturnService;
import com.netx.shopping.service.product.ProductService;
import com.netx.shopping.service.product.ProductSpeService;
import com.netx.shopping.vo.GetActivityOrderResponseVo;
import com.netx.shopping.vo.GetProductOrderResponseVo;
import com.netx.shopping.vo.MerchantOrderDetailResponseDto;
import com.netx.shopping.vo.ProductOrderItemVo;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.ucenter.model.user.User;
import com.netx.utils.DistrictUtil;
import com.netx.utils.money.Money;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ProductOrderFuseAction {

    private Logger logger= LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Autowired
    ProductOrderService productOrderService;

    @Autowired
    ProductOrderAction productOrderAction;

    @Autowired
    ProductService productService;

    @Autowired
    MessagePushProxy messagePushProxy;

    @Autowired
    SellerService sellerService;

    @Autowired
    SellerFavoriteService sellerFavoriteService;

    @Autowired
    UserClientProxy userClientProxy;

    @Autowired
    SellerAction sellerAction;

    @Autowired
    ManageService manageService;

    @Autowired
    ExpressService expressService;

    @Autowired
    WalletForzenClientAction walletForzenClientAction;

    @Autowired
    WalletBillClientAction walletBillClientAction;

    @Autowired
    RedpacketPoolAction redpacketPoolAction;

    @Autowired
    SellerFuseAction sellerFuseAction;

    @Autowired
    ProductReturnService productReturnService;

    @Autowired
    ArbitrationProxy arbitrationProxy;

    @Autowired
    ProductReturnAction productReturnAction;

    @Autowired
    ProductOrderItemAction productOrderItemAction;

    @Autowired
    ProductOrderPutOffService productOrderPutOffService;

    @Autowired
    ProductReturnFuseAction productReturnFuseAction;

    @Autowired
    ProductFuseAction productFuseAction;

    @Autowired
    GeoService geoService;

    @Autowired
    CashierService cashierService;

    @Autowired
    CashierAction cashierAction;

    @Autowired
    UserAction userAction;

    @Autowired
    AliyunExpressService aliyunExpressService;

    @Autowired
    PacketSetService packetSetService;

    @Autowired
    SellerCategoryAction sellerCategoryAction;

    @Autowired
    ProductSpeService productSpeService;

    @Autowired
    AddImgUrlPreUtil addImgUrlPreUtil;

    public boolean cancel(CancelOrderRequestDto request) throws Exception{

        //修改订单状态
        ProductOrder productOrder = new ProductOrder();
        productOrder.setId(request.getId());
        productOrder.setStatus(OrderStatusEnum.CANCELED.getCode());
        //退款
        if(productOrderService.selectById(request.getId()).getStatus()!=1)
        {
            this.repealFrozen(request.getId(), productOrderService.selectById(request.getId()).getUserId());
            //如果是顾客取消，退款时扣取订单的红包提成金额
            if(productOrderService.selectById(request.getId()).getUserId().equals(request.getUserId())){
                BigDecimal packAmount=productOrderAction.getDirectRedPacketAmount(productOrder);
                this.directRedPackMoney(request.getId(),request.getUserId(),packAmount);
            }
        }

        //判断是商家取消还是用户取消还是系统取消  userId为空时系统取消，userId等于订单userId为用户取消，等于商家suerId为商家取消
        if(request.getUserId()==null){
            //系统自动取消时

            //给认购者发送信息
            MessageFormat mf=new MessageFormat("你订购的{0}商品，因商家原因未能成交，你的订购款已返回你的账户，谢谢参与");
            //获取商品名称
            String goodsId=  productOrderService.getProductId(request.getId());
            String goodsName= productService.selectById(goodsId).getName();
            String alertMsg=mf.format(new String[] {goodsName});
            //获取认购者userId
            String userId=productOrderService.selectById(request.getId()).getUserId();
            messagePushProxy.messagePushJump(MessageTypeEnum.PRODUCT_TYPE,alertMsg,"取消订单通知",userId, PushMessageDocTypeEnum.GoodsOrderDetail,request.getId());
            //扣减商家注册者、法人代表信用值

            //获取商家注册者userId
            String sellerId=productOrderService.selectById(request.getId()).getSellerId();
            Seller seller= sellerService.selectById(sellerId);
            String sellerUserId=seller.getUserId();

            //获取商家注册者乐观锁
            UserInfoRequestDto userInfoDto=new UserInfoRequestDto();
            userInfoDto.setSelectConditionEnum(SelectConditionEnum.USER_ID);
            userInfoDto.setSelectData(sellerUserId);
            List<SelectFieldEnum> list=new ArrayList<>();
            list.add(SelectFieldEnum.LOCK_VERSION);
            userInfoDto.setSelectFieldEnumList(list);
            UserInfoResponseDto responseDto=userClientProxy.selectUserInfo(userInfoDto);
            if (responseDto==null)
            {
                throw new Exception("获取乐观锁失败！");
            }

            //获取商家法人代表网号
            String verifyNetworkNum=seller.getVerifyNetworkNum();
            //获取商家法人代表userId和乐观锁
            userInfoDto.setSelectConditionEnum(SelectConditionEnum.USER_NUMBER);
            userInfoDto.setSelectData(verifyNetworkNum);
            List<SelectFieldEnum> list1=new ArrayList<>();
            list.add(SelectFieldEnum.USER_ID);
            list.add(SelectFieldEnum.LOCK_VERSION);
            userInfoDto.setSelectFieldEnumList(list1);
            UserInfoResponseDto responseDto1=userClientProxy.selectUserInfo(userInfoDto);

            //判断商家注册者和法人是否同一人

            List<String> list2=new ArrayList<>();
            list2.add(sellerUserId);
            if (StringUtils.isNotEmpty(responseDto1.getUserId())){
                list2.add(responseDto1.getUserId());
            }
            List<String> list3= sellerAction.getUntqueuUserId(list2);

            if (list3.size()<2){
//                Result result;
                //扣减商家注册者及法人信用值
                Map<String,Object> map=new HashMap<>();
                map.put("userId",sellerUserId);
                map.put("lookVersion",responseDto.getLockVersion());
                map.put("relatableType",ProductOrder.class.getSimpleName());
                map.put("relatableId","0");
                map.put("credit",-5);
                map.put("description","订单因未按时发货，已被自动取消，认购款已全额返回认购者，你的信用值已被扣减5分");
//                result=wangMingClient.addCreditRecord(map);
//                if(result.getCode()!=0){
//                    logger.error("扣除5积分失败!");
//                }
                //TODO
            }else{
//                Result result;
                //扣减商家注册者信用值
                Map<String,Object> map=new HashMap<>();
                map.put("userId",sellerUserId);
                map.put("lookVersion",responseDto.getLockVersion());
                map.put("relatableType",ProductOrder.class.getSimpleName());
                map.put("relatableId","0");
                map.put("credit",-5);
                map.put("description","订单因未按时发货，已被自动取消，认购款已全额返回认购者，你的信用值已被扣减5分");
//                result=wangMingClient.addCreditRecord(map);
//                if(result.getCode()!=0){
//                    logger.error("扣除5积分失败!");
//                }
                //TODO

//                Result result2;
                //扣减商家法人代表信用值
                Map<String,Object> map1=new HashMap<>();
                map1.put("userId",responseDto1.getUserId());
                map1.put("lookVersion",responseDto1.getLockVersion());
                map1.put("relatableType",ProductOrder.class.getSimpleName());
                map1.put("relatableId","0");
                map1.put("credit",-5);
                map1.put("description","订单因未按时发货，已被自动取消，认购款已全额返回认购者，你的信用值已被扣减5分");
//                result2=wangMingClient.addCreditRecord(map1);
//                if(result2.getCode()!=0){
//                    logger.error("扣除5积分失败!");
//                }
                //TODO

            }

            //给商家的注册者、法人代表发送信息
            MessageFormat mf1=new MessageFormat("{0}订单因未按时发货，已被自动取消，认购款已全额返回认购者，你的信用值已被扣减5分");
            String alertMsg1=mf1.format(new String[] {request.getId()});

            //去重后发推送信息
            List<String> list4=new ArrayList<>();
            list4.add(sellerUserId);
            list4.add(responseDto1.getUserId());
            List<String> list5= sellerAction.getUntqueuUserId(list4);
            for (String string:list5){
                messagePushProxy.messagePushJump(MessageTypeEnum.PRODUCT_TYPE,alertMsg,"取消订单通知",string,PushMessageDocTypeEnum.GoodsOrderDetail,request.getId());
            }

            //扣减订单红包提成金额注入到红包池
            this.directRedPackMoney(request.getId());

        }else
        {
            //用户取消时
            if(productOrderService.selectById(request.getId()).getUserId().equals(request.getUserId())){
                //给商家主管发消息

                MessageFormat mf2=new MessageFormat("用户已于{0}取消此订单。用户的订单金额扣除红包提成后的余额已退回其账户。交易结束");
                //获取当前时间
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
                String nowDate=simpleDateFormat.format(new Date().getTime());
                String alertMsg2=mf2.format(new String[] {nowDate});
                //获取商家主管userId
                String sellerId=productOrderService.selectById(request.getId()).getSellerId();
                String manageId= sellerService.selectById(sellerId).getManageId();
                String manageNetworkNum= manageService.selectById(manageId).getManageNetworkNum();

                String manageUserId = this.getUserIdByNetworkNum(manageNetworkNum);
                if (StringUtils.isNotEmpty(manageUserId)){
                    messagePushProxy.messagePushJump(MessageTypeEnum.PRODUCT_TYPE,alertMsg2,"取消订单通知",manageUserId,PushMessageDocTypeEnum.GoodsOrderDetail,request.getId());
                }

                //给认购者发消息
                MessageFormat mf3=new MessageFormat("你{0}的订单取消要求已生效，交易已结束 扣除红包计提部分后的订单款已退回你账户，请注意查收");
                String alertMsg3=mf3.format(new String[] {nowDate});

                messagePushProxy.messagePushJump(MessageTypeEnum.PRODUCT_TYPE,alertMsg3,"取消订单通知",request.getUserId(),PushMessageDocTypeEnum.GoodsOrderDetail,request.getId());

                //扣减订单红包提成金额注入到红包池
            }
            //商家取消时
            else
            {
                //给商家主管发消息
                MessageFormat mf4=new MessageFormat("你已于{0}取消此订单。用户的订单金额扣除红包提成后的余额已退回其账户。交易结束");
                //获取当前时间
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
                String nowDate=simpleDateFormat.format(new Date().getTime());
                String alertMsg4=mf4.format(new String[] {nowDate});
                //获取商家主管userId
                String sellerId=productOrderService.selectById(request.getId()).getSellerId();
                String manageId= sellerService.selectById(sellerId).getManageId();
                String manageNetworkNum= manageService.selectById(manageId).getManageNetworkNum();

                String manageUserId= this.getUserIdByNetworkNum(manageNetworkNum);
                if (StringUtils.isNotEmpty(manageUserId)){
                    messagePushProxy.messagePushJump(MessageTypeEnum.PRODUCT_TYPE,alertMsg4,"取消订单通知",manageUserId,PushMessageDocTypeEnum.GoodsOrderDetail,request.getId());
                }


                //给认购者发消息
                MessageFormat mf5=new MessageFormat("你{0}的订单已被商家取消，交易已结束 扣除红包计提部分后的订单款已退回你账户，请注意查收");
                String alertMsg5=mf5.format(new String[] {nowDate});
                //获取认购中userId
                String userId=productOrderService.selectById(request.getId()).getUserId();

                messagePushProxy.messagePushJump(MessageTypeEnum.PRODUCT_TYPE,alertMsg5,"取消订单通知",userId,PushMessageDocTypeEnum.GoodsOrderDetail,request.getId());

                //扣减订单红包提成金额注入到红包池
                this.directRedPackMoney(request.getId());
            }
        }
        return productOrderService.updateById(productOrder);
    }

    public boolean send(SendOrderRequestDto request) throws Exception {
        ProductOrder productOrder = productOrderService.selectById(request.getId());
        if (productOrder ==null){
            throw new Exception("订单不存在！");
        }
        //第三方配送
        if (request.getLogisticsCode()!=null&&request.getLogisticsCode()!=""&&request.getLogisticsNum()!=null&&request.getLogisticsNum()!=null){
            productOrder.setStatus(OrderStatusEnum.ONWAY.getCode());
            productOrder.setLogisticsCode(request.getLogisticsCode());
            productOrder.setLogisticsNum(request.getLogisticsNum());
            productOrder.setLogisticsStatus(LogisticsStatusEnum.ON.getCode());
            //推送消息
            //获取物流公司名称
            SellerExpress express= expressService.getExpress(request.getLogisticsCode());
            String logisticsName="";
            if (express!=null){
                logisticsName=express.getName();
                express.setPopularity(express.getPopularity()+1);
                expressService.updateById(express);
            }
            MessageFormat mf=new MessageFormat("你的{0}号订单商家已发货，物流公司名称为{1}，物流单号为{2}，请待查收");
            String alertMsg=mf.format(new String[] {request.getId(),logisticsName,request.getLogisticsNum()});
            String userId=productOrderService.selectById(request.getId()).getUserId();
            messagePushProxy.messagePushJump(MessageTypeEnum.PRODUCT_TYPE,alertMsg,"提醒发货通知",userId,PushMessageDocTypeEnum.GoodsOrderDetail,request.getId());

            //外卖配送
        }else {
            String logistcsDetails=request.getDeliveryman()+","+request.getDeliveryNum();
            productOrder.setStatus(OrderStatusEnum.ONWAY.getCode());
            productOrder.setLogistcsDetails(logistcsDetails);
            //推送消息
            MessageFormat mf=new MessageFormat("你的{0}号订单商家已发货，配送人员姓名{1}，配送人员联系方式：{2}，请待查收");
            String alertMsg=mf.format(new String[] {request.getId(),request.getDeliveryman(),request.getDeliveryNum()});
            String userId=productOrderService.selectById(request.getId()).getUserId();

            messagePushProxy.messagePushJump(MessageTypeEnum.PRODUCT_TYPE,alertMsg,"提醒发货通知",userId,PushMessageDocTypeEnum.GoodsOrderDetail,request.getId());
        }
        productOrder.setSendTime(new Date());

        return productOrderService.updateById(productOrder);
    }

    public boolean receive(OperateOrderRequestDto request) throws Exception{
        boolean result = false;
        ProductOrder productOrder = productOrderService.selectById(request.getOrderId());
        if( null == productOrder){
            throw new Exception("订单不存在");
        }

        ProductOrder order=new ProductOrder();
        order.setId(productOrder.getId());
        order.setStatus(OrderStatusEnum.TOEVALUAT.getCode());
        productOrderService.updateById(order);

        MessageFormat messageFormat=new MessageFormat("订单号{0}，金额{1}元，用户已确认收货并完成成交，请注意查收并评论交易");
        String alertMsg=messageFormat.format(new String[]{productOrder.getOrderNum(), String.valueOf(productOrder.getTotalPrice())});
        String manageId= sellerService.selectById(productOrder.getSellerId()).getManageId();
        String manageNetworkNum= manageService.selectById(manageId).getManageNetworkNum();

        String manageUserId= this.getUserIdByNetworkNum(manageNetworkNum);
        if (StringUtils.isNotEmpty(manageUserId)){
            messagePushProxy.messagePushJump(MessageTypeEnum.PRODUCT_TYPE,alertMsg,"用户确认收货通知",manageUserId,PushMessageDocTypeEnum.GoodsOrderDetail,request.getOrderId());

        }
        FrozenOperationRequestDto requestDto = new FrozenOperationRequestDto();
        requestDto.setTypeId(productOrder.getId());
        requestDto.setUserId(productOrder.getUserId());
        //Result result1 = walletForzenClient.pay(requestDto);
        //扣取商家的红包提成
        this.directRedPackMoney(request.getOrderId());
        requestDto.setType(FrozenTypeEnum.FTZ_PRODUCT);
        if(!walletForzenClientAction.pay(requestDto)){
            throw new Exception("使用冻结金额异常");
        }
        result = true;
        return result;
    }

    public boolean remind(OperateOrderRequestDto request) throws Exception{
        ProductOrder productOrder = productOrderService.selectById(request.getOrderId());
        if( null == productOrder){
            throw new Exception("订单不存在");
        }
        //获取当前时间前1天的时间戳 一天只能催一次 （第三方配送）
        long oneDayAgo=new Date().getTime()-24*60*60*1000;
        //判断是否可以催单
        if (productOrder.getRemindTime() !=null &&oneDayAgo< productOrder.getRemindTime().getTime()*1000l){
            return false;
        }else{
            //提醒商家发货
            MessageFormat mf=new MessageFormat("请尽快安排{0}订单号{1}发货，如订单达成后三日内未发货，订单将自动取消");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String orderDate=simpleDateFormat.format(new Date(productOrder.getOrderTime().getTime() *1000l));
            String alertMsg=mf.format(new String[] {orderDate, productOrder.getOrderNum()});
            //获取商家主管用户id
            Seller seller= sellerService.selectById(productOrder.getSellerId());
            if(seller==null){
                return false;
            }
            String manageId = seller.getManageId();
            SellerManage manage = manageService.selectById(manageId);
            String manageNetworkNum=manage.getManageNetworkNum();
            String manageUserId= this.getUserIdByNetworkNum(manageNetworkNum);
            if (StringUtils.isNotEmpty(manageUserId)){
                messagePushProxy.messagePushJump(MessageTypeEnum.PRODUCT_TYPE,alertMsg,"提醒发货通知",manageUserId, PushMessageDocTypeEnum.GoodsOrderDetail,request.getOrderId());
            }


            //催单成功 修改提醒时间
            productOrder.setRemindTime(new Date());
            productOrderService.updateById(productOrder);
        }
        return true;
    }

    public boolean remind1(OperateOrderRequestDto request) throws Exception{
        ProductOrder productOrder = productOrderService.selectById(request.getOrderId());
        if( null == productOrder){
            throw new Exception("订单不存在");
        }
        //获取当前时间前10分钟的时间戳 每10分钟可以催一次 （外卖配送）
        long tenMinAgo=new Date().getTime()-10*60*1000;
        //判断是否可以催单
        if (productOrder.getRemindTime()!=null && tenMinAgo< productOrder.getRemindTime().getTime()*1000l){
            return false;
        }else{MessageFormat mf=new MessageFormat("请尽快安排{0}的{1}号订单，对方已下单{2}分钟，如继续延误，对方有权取消订单");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String orderDate=simpleDateFormat.format(new Date(productOrder.getOrderTime().getTime()*1000l));
            //计算下单多少分钟
            long mins=(new Date().getTime()- productOrder.getCreateTime().getTime()*1000l)/1000/60;
            String alertMsg=mf.format(new String[] {orderDate, productOrder.getOrderNum(), String.valueOf(mins)});
            //获取商家主管用户id
            Seller seller= sellerService.selectById(productOrder.getSellerId());
            if(seller==null){
                return false;
            }
            String manageId = seller.getManageId();
            SellerManage manage = manageService.selectById(manageId);
            String manageNetworkNum=manage.getManageNetworkNum();
            String manageUserId= this.getUserIdByNetworkNum(manageNetworkNum);
            if (StringUtils.isNotEmpty(manageUserId)){
                //推送消息
                messagePushProxy.messagePushJump(MessageTypeEnum.PRODUCT_TYPE,alertMsg,"提醒发货通知",manageUserId, PushMessageDocTypeEnum.GoodsOrderDetail,request.getOrderId());

            }
            //催单成功 修改提醒时间
            //催单成功 修改提醒时间
            productOrder.setRemindTime(new Date());
            productOrderService.updateById(productOrder);
        }
        return true;
    }

    private boolean updateOrderAndSendMessage(List<ProductOrder> productOrders,PayOrderRequestDto request) throws Exception{
        if (productOrders != null){
            for (int i=0;i<productOrders.size();i++){
                ProductOrder productOrder = new ProductOrder();
                BeanUtils.copyProperties(productOrders.get(i),productOrder);
                productOrder.setPayTime(new Date());
                productOrder.setPayWay(request.getPayType());
//                productOrder.setAddress(request.getAddress());
//                productOrder.setDeliveryWay(request.getDeliveryWay().get(i));
//                if (request.getRemark()!=null){
//                    productOrder.setRemark(request.getRemark().get(productOrder.getSellerId()));
//                }
                productOrder.setPayPrice(new Money(request.getPayPrices().get(i)).getCent());
                //判断是立即付款还是或到付款
                if (productOrderService.selectById(request.getOrderId().get(i)).getStatus()==1){
                    productOrder.setStatus(OrderStatusEnum.UNSEND.getCode());
                }
                //判断是否是活动里的支付 是：把支付后订单状态直接改为已完成
                if (null !=request.getIsPayOfActivity())
                {
                    productOrder.setStatus(OrderStatusEnum.COMPLETED.getCode());
                }
                productOrderService.updateById(productOrder);

                //发送消息给商家业务主管
                MessageFormat mf=new MessageFormat("你有新订单生产，{0}于{1}下单，请及时处理");
                //获取认购者用户昵称
                ProductOrder productOrder1 =productOrderService.selectById(productOrder.getId());
                String nickName = userAction.getUserService().selectById(productOrder1.getUserId())==null?null:userAction.getUserService().selectById(productOrder1.getUserId()).getNickname();
                //获取当前时间
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd-HH-mm");
                String nowDate=simpleDateFormat.format(new Date().getTime());
                String alertMsg=mf.format(new String[]{nickName,nowDate});
                //获取商家业务主管userId
                Seller seller= sellerService.selectById(productOrder1.getSellerId());
                String manageNetworkNum = manageService.selectById(seller.getManageId()).getManageNetworkNum();
                String manageUserId = this.getUserIdByNetworkNum(manageNetworkNum);
                if (StringUtils.isNotEmpty(manageUserId)){
                    messagePushProxy.messagePushJump(MessageTypeEnum.PRODUCT_TYPE,alertMsg,"生产订单通知",manageUserId,PushMessageDocTypeEnum.GoodsOrderDetail, productOrder.getId());
                }
            }
            return true;
        }
        return false;
    }

    public boolean newpayOrder(PayOrderRequestDto request) throws Exception{
        if (request.getOrderId() != null) {
            String[] ids = new String[request.getOrderId().size()];
            request.getOrderId().toArray(ids);
            List<ProductOrder> list = productOrderService.getProductOrderListByIds(ids);
            if(newpay(list,request)){
                return updateOrderAndSendMessage(list,request) && subtractSpeStock(ids);
            }
        }
        return false;
    }

    private boolean subtractSpeStock(String[] orderIds){
        if (orderIds != null){
            for (String orderId : orderIds){
                List<ProductOrderItem> list = productOrderItemAction.getListByOrderId(orderId);
                if (list != null && list.size()>0){
                    for (ProductOrderItem productOrderItem : list){
                        ProductSpe productSpe = productSpeService.selectById(productOrderItem.getSpeId());
                        if (productSpe != null){
                            productSpe.setStock(productSpe.getStock()-productOrderItem.getQuantity());
                            productSpeService.updateById(productSpe);
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    public boolean payOrder(PayOrderRequestDto request) throws Exception{
        boolean result = false;
        this.pay(request);//支付

        ProductOrder productOrder = new ProductOrder();
        productOrder.setId(request.getOrderId().get(0));
        productOrder.setPayTime(new Date());
        productOrder.setPayWay(request.getPayType());
        productOrder.setPayPrice(new Money(request.getPayPrices().get(0)).getCent());
        productOrder.setCreditId(request.getCurrencyId());
        productOrder.setNetCredit(new Money(request.getNetCurrency()).getCent());
        //判断是立即付款还是或到付款
        if (productOrderService.selectById(request.getOrderId().get(0)).getStatus()==1){
            productOrder.setStatus(OrderStatusEnum.UNSEND.getCode());
        }
        //判断是否是活动里的支付 是：把支付后订单状态直接改为已完成
        if (null !=request.getIsPayOfActivity())
        {
            productOrder.setStatus(OrderStatusEnum.COMPLETED.getCode());
        }
        productOrderService.updateById(productOrder);

        result = true;

        //发送消息给商家业务主管
        MessageFormat mf=new MessageFormat("你有新订单生产，{0}于{1}下单，请及时处理");
        //获取认购者用户昵称
        ProductOrder productOrder1 =productOrderService.selectById(productOrder.getId());
        String nickName = userAction.getUserService().selectById(productOrder1.getUserId())==null?null:userAction.getUserService().selectById(productOrder1.getUserId()).getNickname();
        //获取当前时间
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        String nowDate=simpleDateFormat.format(new Date().getTime());
        String alertMsg=mf.format(new String[]{nickName,nowDate});
        //获取商家业务主管userId
        Seller seller= sellerService.selectById(productOrder1.getSellerId());
        String manageNetworkNum = manageService.selectById(seller.getManageId()).getManageNetworkNum();
        String manageUserId = this.getUserIdByNetworkNum(manageNetworkNum);
        if (StringUtils.isNotEmpty(manageUserId)){
            messagePushProxy.messagePushJump(MessageTypeEnum.PRODUCT_TYPE,alertMsg,"生产订单通知",manageUserId,PushMessageDocTypeEnum.GoodsOrderDetail, productOrder.getId());
        }

//        if (null !=request.getIsPayOfActivity())
//        {
//            //使用冻结金额
//            FrozenOperationRequestDto requestDto = new FrozenOperationRequestDto();
//            requestDto.setTypeId(request.getOrderId());
//            requestDto.setUserId(request.getUserId());
//            //Result result1 = walletForzenClient.pay(requestDto);
//            //if(result1.getCode() != 0){
//            if(!walletForzenClientAction.pay(requestDto)){
//                throw new Exception("使用冻结金额异常");
//            }
//        }
        return result;
    }

    private boolean newpay(List<ProductOrder> productOrders,PayOrderRequestDto request) throws Exception{
        if (request.getOrderId() != null){
            if (productOrderAction.judgeOrderId(productOrders,request.getOrderId())){
                for (ProductOrder productOrder : productOrders){
                    BigDecimal amount = new BigDecimal(0);
                    //获取商家收银人员userId
                    SellerCashier cashier = cashierAction.selectSellerCashierBySellerId(productOrder.getSellerId());
                    if (cashier == null ){
                        throw new Exception("获取收银人员信息失败！");
                    }
                    String moneyUserId= this.getUserIdByNetworkNum(cashier.getMoneyNetworkNum());

                    FrozenAddRequestDto requestDto = new FrozenAddRequestDto();
                    Integer tradeType = 0;
                    if(request.getPayType()==1){//网币
                        tradeType = 1;
                        amount = request.getNetCurrency();
                        requestDto.setCurrencyId(request.getCurrencyId());
                    }else if(request.getPayType()==2){//零钱
                        tradeType = 0;
                        amount = request.getPayPrices().get(0);
                    }else if(request.getPayType()==4){//零钱+网币
                        tradeType = 2;
                        requestDto.setCurrencyId(request.getCurrencyId());
                        amount = request.getPayPrices().get(0).add(request.getNetCurrency());
                    }
                    requestDto.setUserId(request.getUserId());
                    requestDto.setTradeType(tradeType);
                    requestDto.setToUserId(moneyUserId);
                    requestDto.setDescription("支付订单号"+productOrder.getOrderNum());
                    requestDto.setFrozenType("Product");
                    requestDto.setAmount(amount);
                    requestDto.setTypeId(request.getOrderId().get(0));

                    if(!walletForzenClientAction.add(requestDto)){
                        throw new Exception("使用冻结金额异常");
                    }
                }
            }
        }
        return true;
    }


    private void pay(PayOrderRequestDto request) throws Exception{
        ProductOrder productOrder = productOrderService.selectById(request.getOrderId().get(0));
        BigDecimal amount = new BigDecimal(0);
        if(null == productOrder){
            throw new Exception("支付订单不存在");
        }
        if (productOrder.getStatus()==OrderStatusEnum.TOBEGENERATE.getCode()){
            throw new Exception("订单还不可以支付");
        }
        if(productOrder.getStatus() != OrderStatusEnum.UNPAID.getCode()&& productOrder.getStatus()!=OrderStatusEnum.SERVED.getCode()){
            throw new Exception("订单已支付");
        }
        Seller seller = sellerService.selectById(productOrder.getSellerId());
        if(null == seller){
            throw new Exception("支付失败，商品已下架");
        }

        //获取商家收银人员userId
        SellerCashier cashier = cashierAction.selectSellerCashierBySellerId(productOrder.getSellerId());
        String moneyUserId= this.getUserIdByNetworkNum(cashier.getMoneyNetworkNum());


        FrozenAddRequestDto requestDto = new FrozenAddRequestDto();
        Integer tradeType = 0;
        if(request.getPayType()==1){//网币
            tradeType = 1;
            amount = request.getNetCurrency();
            requestDto.setCurrencyId(request.getCurrencyId());
        }else if(request.getPayType()==2){//零钱
            tradeType = 0;
            amount = request.getPayPrices().get(0);
        }else if(request.getPayType()==4){//零钱+网币
            tradeType = 2;
            requestDto.setCurrencyId(request.getCurrencyId());
            amount = request.getPayPrices().get(0).add(request.getNetCurrency());
        }
        requestDto.setUserId(request.getUserId());
        requestDto.setTradeType(tradeType);
        requestDto.setToUserId(moneyUserId);
        requestDto.setDescription("购买"+seller.getName()+"商家商品");
        requestDto.setFrozenType("Product");
        requestDto.setAmount(amount);
        requestDto.setTypeId(request.getOrderId().get(0));
        /*Result result = walletForzenClient.add(requestDto);
        if(result.getCode() != 0){
            throw new ClientException(result.getMessage());
        }*/
        if(!walletForzenClientAction.add(requestDto)){
            throw new Exception("使用冻结金额异常");
        }
    }

    public boolean directRedPackMoney(String orderId) throws Exception{
        ProductOrder order=productOrderService.selectById(orderId);
        //按商家设定的红包提成标准，按当前订单金额计提红包金额至红包池
        Seller seller = sellerService.selectById(order.getSellerId());

        BigDecimal packetAmount=productOrderAction.getDirectRedPacketAmount(order);
        seller.setPacketPoolAmount(new Money(new BigDecimal(Money.getMoneyString(seller.getPacketPoolAmount())).add(packetAmount)).getCent());
        if (seller != null) {
            sellerService.updateById(seller);
        }
        // 从商家钱包扣钱
        //获取商家收银人员userId
        SellerCashier cashier = cashierAction.selectSellerCashierBySellerId(seller.getId());
        if (cashier == null){
            throw new Exception("获取收银人员信息失败");
        }
        String moneyUserId=getUserIdByNetworkNum(cashier.getMoneyNetworkNum());
        if (moneyUserId==null) {
            throw new Exception("获取收银人员信息失败");
        }
        BillAddRequestDto billAddRequestDto = new BillAddRequestDto();
        billAddRequestDto.setToUserId("999");
        billAddRequestDto.setAmount(packetAmount);
        billAddRequestDto.setDescription("从商家钱包中计提金额加入红包池");
        billAddRequestDto.setPayChannel(4);
        Boolean res = walletBillClientAction.addBill(moneyUserId,billAddRequestDto);
        //if (res.getCode()!=0&&res.getMessage().equals("账户余额不足")){
        if(!res){
            //获取业务主管userId
            String manageNetworkNum = manageService.selectById(seller.getManageId()).getManageNetworkNum();
            String manageUserId = this.getUserIdByNetworkNum(manageNetworkNum);
            //去重后发推送信息
            List<String> list1=new ArrayList<>();
            list1.add(seller.getUserId());
            list1.add(manageUserId);
            List<String> list3= sellerAction.getUntqueuUserId(list1);
            for (String string:list3){
                //商家钱包余额不足计提红包，则发送信息给注册者、业务主管：”账户余额不足，无法计提红包，在未完成充值前，系统将暂停你的接单资格“。该商家所有商品及其它下单按钮，均不可再点击
                messagePushProxy.messagePushJump(MessageTypeEnum.PRODUCT_TYPE,"账户余额不足，无法计提红包，在未完成充值前，系统将暂停你的接单资格","商家钱包余额不足",string, PushMessageDocTypeEnum.GoodsOrderDetail,orderId);
                //TODO 该商家所有商品及其它下单按钮，均不可再点击
            }
        }
        //将金额加入红包池
        UpadteRedpacketPoolRequestDto upadteRedpacketPoolRequestDto=new UpadteRedpacketPoolRequestDto();
        upadteRedpacketPoolRequestDto.setAmount(packetAmount);
        upadteRedpacketPoolRequestDto.setSource(RedpacketPoolRecordSourceEnum.TRADE_AMOUNT_COMMISSION.getCode());
        upadteRedpacketPoolRequestDto.setSourceId(seller.getId());
        upadteRedpacketPoolRequestDto.setWay(RedpacketPoolRecordWayEnum.INCOME.getCode());
        redpacketPoolAction.upadteRedpacketPool(upadteRedpacketPoolRequestDto);
        MessageFormat mf=new MessageFormat("订单已成交，平台已自动扣除订单的红包提成{0}元");
        String alertMsg=mf.format(new String[]{packetAmount.toString()});
        //获取商家相关人员userId，去重后发送信息
        List<String> userIds= sellerFuseAction.getSellerAllUserId(seller.getId());
        List<String> listone = sellerAction.getUntqueuUserId(userIds);
        for (String string:listone){
            messagePushProxy.messagePushJump(MessageTypeEnum.PRODUCT_TYPE,alertMsg,"扣取订单的红包提成",string, PushMessageDocTypeEnum.GoodsOrderDetail,orderId);
        }

        return true;
    }


    /**
     * 扣取顾客订单的红包提成，当顾客取消订单时
     * @param
     * @return
     * @throws
     */
    public boolean directRedPackMoney(String orderId,String userId,BigDecimal packetAmount){
        BillAddRequestDto billAddRequestDto = new BillAddRequestDto();
        billAddRequestDto.setToUserId("999");
        billAddRequestDto.setAmount(packetAmount);
        billAddRequestDto.setDescription("从顾客钱包中计提金额加入红包池");
        billAddRequestDto.setPayChannel(4);
        Boolean res=walletBillClientAction.addBill(userId,billAddRequestDto);
        //if (res.getCode()!=0&&res.getMessage().equals("账户余额不足")) {
        if(!res){
            messagePushProxy.messagePushJump(MessageTypeEnum.PRODUCT_TYPE,"账户余额不足，无法计提由于你取消订单需扣取的红包提成金额，请充值钱包", "顾客钱包余额不足", userId, PushMessageDocTypeEnum.LuckyMoneyDetail, orderId);
        }
        //将金额加入红包池
        UpadteRedpacketPoolRequestDto upadteRedpacketPoolRequestDto=new UpadteRedpacketPoolRequestDto();
        upadteRedpacketPoolRequestDto.setAmount(packetAmount);
        upadteRedpacketPoolRequestDto.setSource(RedpacketPoolRecordSourceEnum.TRADE_AMOUNT_COMMISSION.getCode());
        upadteRedpacketPoolRequestDto.setSourceId(orderId);
        upadteRedpacketPoolRequestDto.setWay(RedpacketPoolRecordWayEnum.INCOME.getCode());
        redpacketPoolAction.upadteRedpacketPool(upadteRedpacketPoolRequestDto);
        messagePushProxy.messagePushJump(MessageTypeEnum.PRODUCT_TYPE,"由于你取消订单，平台已自动从你钱包中扣取订单的红包提成金额","扣取订单红包提成金额", userId, PushMessageDocTypeEnum.LuckyMoneyDetail, orderId);
        return true;
    }


    /**
     * 根据网号获取用户id
     * @return
     */
    public String getUserIdByNetworkNum(String networkNum) throws Exception{
        return userAction.selectUserByUserNumber(networkNum)==null?null:userAction.selectUserByUserNumber(networkNum).getUserId();
    }



    public void repealFrozen(String orderId, String userId) throws Exception{
        FrozenOperationRequestDto requestDto = new FrozenOperationRequestDto();
        requestDto.setTypeId(orderId);
        requestDto.setUserId(userId);
        /*Result result = walletForzenClient.repealFrozen(requestDto);
        if(result.getCode() != 0){
            throw new ClientException(result.getMessage());
        }*/
        requestDto.setType(FrozenTypeEnum.FTZ_PRODUCT);
        if(walletForzenClientAction.repealFrozen(requestDto)){
            throw new Exception("使用冻结金额异常");
        }
    }

    public void refund(ProductOrder productOrder, String userId, String toUserId) throws Exception{
        Integer payChannel = 0;
        BigDecimal amount = new BigDecimal(0);
        String description = "";
        if(productOrder.getPayWay() == 4){
            BillAddRequestDto billAddRequestDto = new BillAddRequestDto();
            billAddRequestDto.setToUserId(toUserId);
            billAddRequestDto.setAmount(new BigDecimal(Money.getMoneyString(productOrder.getNetCredit()==null?0:productOrder.getNetCredit())));
            billAddRequestDto.setDescription("订单网币退款");
            billAddRequestDto.setPayChannel(payChannel);
            if(!walletBillClientAction.addBill(userId,billAddRequestDto)){
                throw new Exception("订单网币退款失败！");
            }
            BillAddRequestDto billAddRequestDto1 = new BillAddRequestDto();
            billAddRequestDto1.setToUserId(toUserId);
            billAddRequestDto1.setAmount(new BigDecimal(Money.getMoneyString(productOrder.getPayPrice()==null?0:productOrder.getPayPrice())));
            billAddRequestDto1.setDescription("订单零钱退款");
            billAddRequestDto1.setPayChannel(payChannel);
            //Result result1 = walletBillClient.addBill(billAddRequestDto1);//这里很容易远程失败
            //if(result1.getCode()!=0){
            if(!walletBillClientAction.addBill(userId,billAddRequestDto1)){
                throw new Exception("订单零钱退款失败！");
            }
        }else{
            if(productOrder.getPayWay() == 1){
                payChannel = 2;
                description = "订单零钱退款";
                amount = new BigDecimal(Money.getMoneyString(productOrder.getNetCredit()==null?0:productOrder.getNetCredit()));
            }else if(productOrder.getPayWay() == 2){
                amount = new BigDecimal(Money.getMoneyString(productOrder.getPayPrice()==null?0:productOrder.getPayPrice()));
                payChannel = 3;
                description = "订单网币退款";
            }
            BillAddRequestDto billAddRequestDto = new BillAddRequestDto();
            billAddRequestDto.setToUserId(toUserId);
            billAddRequestDto.setAmount(amount);
            billAddRequestDto.setDescription(description);
            billAddRequestDto.setPayChannel(payChannel);
            /*Result result = walletBillClient.addBill(billAddRequestDto);//这里很容易远程失败
            if(result.getCode()!=0){
                throw new ClientException(result.getMessage());
            }*/
            if(!walletBillClientAction.addBill(userId,billAddRequestDto)){
                throw new Exception("退款失败！");
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean sellerRefuse(ArbitrationAddComplaintRequestDto request) {
        ProductOrder productOrder =productOrderService.selectById(request.getTypeId());
        if(productOrder ==null){
            return false;
        }
        productOrder.setStatus(OrderStatusEnum.COMPLAINTS.getCode());
        productOrder.setUpdateUserId(request.getFromUserId());
        boolean result=productOrderService.updateById(productOrder);
        //以下注释的都是用户申诉的操作
//        //更改订单状态为投诉中,并且把退货的状态变成商家拒收退货
        ProductReturn productReturn = productReturnService.getProductReturnByOrderId(productOrder.getId());
        productReturn.setStatus(ProductReturnStatusEnum.REFUSE.getCode());
        productReturn.setSettled(false);
        if(result && productReturnService.updateById(productReturn)){
            List<String> list=productOrderAction.getGoodsNameAndSellerName(productOrder.getId());
            MessageFormat messageFormat=new MessageFormat(ProductMessagePushEnum.SELLER_REFUSE_SEND_1.getMessage());
            String alartMsg="";
            if(list!=null) {
                if (list.size() == 2) {
                    alartMsg = messageFormat.format(new String[]{list.get(1), list.get(0)});
                }
            }else{
                alartMsg=new MessageFormat(ProductMessagePushEnum.SELLER_REFUSE_SEND_1.getMessage()).format(new String[]{productOrder.getOrderNum()});
            }

            messagePushProxy.messagePushJump(MessageTypeEnum.PRODUCT_TYPE,alartMsg, ProductMessagePushEnum.SELLER_REFUSE_SEND_1.getName(), productOrder.getUserId(),PushMessageDocTypeEnum.GoodsOrderDetail, productOrder.getId());
            //启动定时任务
//            boolean quartzResult=businessQuartzService.checkIsComplainted(request.getFromUserId(),request.getTypeId(),DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(new Date().getTime(),3,2));
//            if(!quartzResult){
//                logger.error("checkIsComplainted定时任务失败!");
//            }
            //TODO 定时任务
            return true;
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    public String autoRumSettleOrderArbitrationResult(CheckOrderArbitrationRequestDto request) throws Exception{
        ArbitrationResultResponseVo responseVo=new ArbitrationResultResponseVo();
        try {
            ProductOrder productOrder = productOrderService.selectById(request.getTypeId());
            if (productOrder != null && productOrder.getStatus()!=OrderStatusEnum.COMPLETED.getCode()) {
                responseVo=arbitrationProxy.getArbitrationResult(request);
                ProductReturn productReturn = productReturnService.getProductReturnByOrderId(productOrder.getId());
                if (productReturn != null && !productReturn.getSettled()) {
                    //仲裁拒绝受理
                    if (responseVo.getStatusCode() == ArbitrationEnum.ARBITRATION_REFUSE_SETTLE.getCode()) {
                        productOrder.setStatus(OrderStatusEnum.COMPLETED.getCode());
                        productReturn.setStatus(ProductReturnStatusEnum.SUCCESS.getCode());
                        productReturn.setArbitrationDescription("管理员拒绝受理，裁决理由是：\""+responseVo.getDescriptions()+"\"，订单依然有效，视作交易成功");
                        productReturn.setSettled(true);
                        if (!(productOrderService.updateById(productOrder) && productReturnService.updateById(productReturn))) {
                            logger.error("订单退货退款状态更新数据库出错!");
                            throw new Exception("订单退货退款状态更新数据库出错");
                        }
                    }//已裁决
                    else if (responseVo.getStatusCode() == ArbitrationEnum.ARBITRATION_SETTLED.getCode()) {
                        do {
                            if (responseVo.getReturnRadioButton() == true) {
                                SuccessReturnRequestDto returnRequestDto1 = new SuccessReturnRequestDto();
                                returnRequestDto1.setId(productReturn.getId());
                                returnRequestDto1.setSellerUserId(productReturn.getSellerUserId());
                                productReturn.setArbitrationDescription("卖家输了，退货");
                                productReturn.setSettled(true);
                                if (!(productReturnFuseAction.successReturn(returnRequestDto1) && productReturnService.updateById(productReturn))) {
                                    throw new Exception("商家退货退款失败");
                                }
                                break;
                            } else {
                                productOrder.setStatus(OrderStatusEnum.COMPLETED.getCode());
                                productReturn.setStatus(ProductReturnStatusEnum.SUCCESS.getCode());
                                productReturn.setArbitrationDescription("买家输了，订单有效，视作交易成功");
                                productReturn.setSettled(true);
                                if (!(productOrderService.updateById(productOrder) && productReturnService.updateById(productReturn))) {
                                    throw new Exception("订单退货退款状态更新数据库出错");
                                }
                                break;
                            }
                        }
                        while (false);
                    }
                    //对方已申诉,等待处理
                    else if (responseVo.getStatusCode() == ArbitrationEnum.ARBITRATION_OTHER_COMPLAINT.getCode()) {
                        productReturn.setArbitrationDescription("买家已申诉，等待仲裁中");
                        if(!productReturnService.updateById(productReturn)){
                            throw new Exception("用户申诉修改仲裁描述失败");
                        }
                    }
                }
                return productReturn.getArbitrationDescription();
            }
        } catch (Exception e) {
            logger.info(e.getMessage(),e);
            e.printStackTrace();
        }
        return "已有仲裁结果，请查看详情信息";
    }

    public String getRelatedSellersByUserId(String userId) {
        String sellerIds="";
        //获取用户网号 用于是否是商家收银人员的网号搜索
        String userNumber = this.getUserNumber(userId);
        if (StringUtils.isNotEmpty(userNumber)){
            //根据用户网号获取主管id，用于是否是商家的主管搜索
            String[] manageIds = null;
            String[] cashierIds = null;
            String manageIs = manageService.getManageIs(userNumber);
            if (manageIs != null){
                manageIds = manageIs.split(",");
            }
            String cashierIdList = cashierService.getCashierIds(userNumber);
            if (cashierIdList != null){
                cashierIds = cashierIdList.split(",");
            }
            sellerIds=sellerService.getSellerIds(userId,cashierIds,manageIds);
        }
        return sellerIds;

    }

    public boolean checkUpHaveCompleteGoodsOrder(String userId) {
        //作为顾客身份，检查是否有未完成的订单
        String status="1,2,3,4,5,6,10,11";
        int i=productOrderService.getProductOrderCountByUserIdAndStatus(userId,status);
        if (i>0) {
            return true;
        }
        //作为商家注册者，主管，或收银人，检查是否有管理的订单未完成
        String sellerIds=this.getRelatedSellersByUserId(userId);
        if (StringUtils.isNotBlank(sellerIds)) {
            int i1=productOrderService.getProductOrderCountBySellerIdAndStatus(sellerIds,status);
            if (i1>0) {
                return true;
            }
        }
        return false;
    }

    public String getRelatedSellersNotMoneyUserByUserId(String userId){
        String sellerIds="";
        //获取用户网号 用于是否是商家收银人员的网号搜索
        String userNumber = this.getUserNumber(userId);
        if (StringUtils.isNotEmpty(userNumber)){
            //根据用户网号获取主管id，用于是否是商家的主管搜索
            String[] manageIds = null;
            String[] cashierIds = null;
            String manageIs = manageService.getManageIs(userNumber);
            manageIds = manageIs.split(",");

            sellerIds=sellerService.getSellerIds(userId,cashierIds,manageIds);
        }
        return sellerIds;
    }

    public List<Seller> getMyManageSeller(GetListByUserIdDto getListByUserIdDto){
        //获取用户网号 用于是否是商家收银人员的网号搜索
        List<Seller> reslist = new ArrayList<>();
        /*List<String> manageIds = manageService.getManageIdByUserId(getListByUserIdDto.getUserId());
        if(manageIds != null && manageIds.size() > 0){
            reslist=sellerService.getMyManageSeller(getListByUserIdDto, manageIds);
        }else{

        }*/
        String userNumber = this.getUserNumber(getListByUserIdDto.getUserId());
        if (StringUtils.isNotEmpty(userNumber)){
            //根据用户网号获取主管id，用于是否是商家的主管搜索
            String[] manageIds = null;
            String manageIs = manageService.getManageIs(userNumber);
            if(StringUtils.isNotBlank(manageIs)){
                manageIds = manageIs.split(",");
                reslist=sellerService.getMyManageSeller(getListByUserIdDto, manageIds);
            }
        }
        return reslist;
    }

    public List<Seller> getMyFavoriteSeller(GetListByUserIdDto getListByUserIdDto){
        List<String> sellerIds = sellerFavoriteService.getSellerIdByUserId(getListByUserIdDto.getUserId());
        List<Seller> sellerList = new ArrayList<>();
        if(sellerIds != null && sellerIds.size()>0){
            sellerList = sellerService.getMyFavoriteSeller(sellerIds, getListByUserIdDto.getCurrent(), getListByUserIdDto.getSize());
        }
        return sellerList;
    }

    /*
    * 获取用户网号
    * */
    public String getUserNumber(String userId){
        User user = userAction.getUserService().selectById(userId);
        return user==null?null:user.getUserNumber();
    }

    public List<GetProductOrderResponseVo> getAllGoodsOderByUserId(GetGoodsOrderListRequestDto request) {
        List<GetProductOrderResponseVo> resultList = new ArrayList<>();
        String sellerIds=this.getRelatedSellersByUserId(request.getUserId());
        List<ProductOrder> productOrderList = new ArrayList<>();
        if(StringUtils.isNotBlank(sellerIds)){
            if (request.getStatus() != null){
                String[] sellerId=sellerIds.split(",");
                productOrderList = productOrderService.getProductOrderList(sellerId,request.getStatus());
            }else {
                //订单列表显示订单状态优先级顺序
                String[] status={"2","4","5","12","6","1","3","11","10","7","8"};
                String[] sellerId=sellerIds.split(",");
                Integer current = (request.getCurrent()-1) * request.getSize();
                productOrderList = productOrderService.getAllProductOderByUserId(sellerId,status,current,request.getSize());
            }

            for (ProductOrder productOrder : productOrderList) {
                GetProductOrderResponseVo getGoodsOrderResponseVo = new GetProductOrderResponseVo();
                BeanUtils.copyProperties(productOrder, getGoodsOrderResponseVo);
                Seller seller= sellerService.selectById(productOrder.getSellerId());
                if(seller!=null){
                    getGoodsOrderResponseVo.setSellerName(seller.getName());
                    getGoodsOrderResponseVo.setSellerUserId(seller.getUserId());
                    getGoodsOrderResponseVo.setAddrDetail(seller.getAddrDetail());
                }
                getGoodsOrderResponseVo.setItemList(productOrderItemAction.getListByOrderId(productOrder.getId())==null?null: productOrderItemAction.getListByOrderId(productOrder.getId()));
                getGoodsOrderResponseVo.setGoodsList(productFuseAction.getGoodsListByOrderId(productOrder.getId())==null?null: productFuseAction.getGoodsListByOrderId(productOrder.getId()));
                //获取商家地址详情
                resultList.add(getGoodsOrderResponseVo);

            }
        }
        return resultList;
    }

    public String getManageUserIdByOrderId(String orderId) throws Exception{
        ProductOrder productOrder =productOrderService.selectById(orderId);
        if (productOrder !=null){
            String manageId=sellerService.selectById(productOrder.getSellerId()).getManageId();
            String manageNetworkNum=manageService.getManageNetworkNum(manageId);
            return this.getUserIdByNetworkNum(manageNetworkNum);
        }
        return null;
    }

    public void changeLogisticsStatusAndPay() throws Exception{

        //获取商家已发货用户待收货订单，如果超过十五天（第三方配送型）或三天（外卖配送型）时间，则自动确认用户收货，并打款给商家
        List<ProductOrder> productOrderList = productOrderService.getProductOrderList(OrderStatusEnum.ONWAY.getCode());

        //获取当前时间前15天时间戳
        long fifteenDayAgo = DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(new Date().getTime(),15,2);
        //获取当前时间前3天时间戳
        long threeDayAgo= DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(new Date().getTime(),3,2);

        if (productOrderList != null){
            for(ProductOrder productOrder : productOrderList){
                //获取发货时间
                long sendTime = productOrder.getSendTime().getTime();
                //第三方配送
                if (productOrder.getPayWay()==1){
                    if (sendTime<fifteenDayAgo){
                        //改变订单状态与物流状态
                        productOrder.setStatus(OrderStatusEnum.TOEVALUAT.getCode());
                        productOrder.setLogisticsStatus(LogisticsStatusEnum.COMPLETED.getCode());
                        productOrderService.updateById(productOrder);
                        //使用冻结，钱转给商家
                        FrozenOperationRequestDto requestDto = new FrozenOperationRequestDto();
                        requestDto.setTypeId(productOrder.getId());
                        requestDto.setUserId(productOrder.getUserId());
                        /*Result result = walletForzenClient.pay(requestDto);
                        if(result.getCode() != 0){
                            throw new ClientException(result.getMessage());
                        }*/
                        requestDto.setType(FrozenTypeEnum.FTZ_PRODUCT);
                        if(!walletForzenClientAction.pay(requestDto)){
                            throw new Exception("使用冻结金额");
                        }
                        //扣取订单的红包提成
                        this.directRedPackMoney(productOrder.getId());
                    }
                }
                //外卖配送
                if(productOrder.getPayWay()==3){
                    if (sendTime<threeDayAgo){
                        //改变订单状态
                        productOrder.setStatus(OrderStatusEnum.TOEVALUAT.getCode());
                        productOrderService.updateById(productOrder);
                        //使用冻结，钱转给商家
                        FrozenOperationRequestDto requestDto = new FrozenOperationRequestDto();
                        requestDto.setTypeId(productOrder.getId());
                        requestDto.setUserId(productOrder.getUserId());
                        /*Result result = walletForzenClient.pay(requestDto);
                        if(result.getCode() != 0){
                            throw new ClientException(result.getMessage());
                        }*/
                        requestDto.setType(FrozenTypeEnum.FTZ_PRODUCT);
                        if(!walletForzenClientAction.pay(requestDto)){
                            throw new Exception("使用冻结金额异常");
                        }
                        //扣取订单的红包提成
                        this.directRedPackMoney(productOrder.getId());
                    }
                }
            }
        }
    }


    public boolean changeOrderStatusAndPay(String orderId) throws Exception{
        ProductOrder productOrder =productOrderService.selectById(orderId);
        if (productOrder ==null){
            throw new Exception("订单不存在");
        }
        //判断订单状态是否在物流中,是否外卖型配送，不是则不执行操作
        if (productOrder.getStatus()!=OrderStatusEnum.ONWAY.getCode()&& productOrder.getDeliveryWay()!=3){
            return false;
        }
        //改变订单状态
        productOrder.setStatus(OrderStatusEnum.TOEVALUAT.getCode());
        productOrderService.updateById(productOrder);
        //使用冻结，钱转给商家
        FrozenOperationRequestDto requestDto = new FrozenOperationRequestDto();
        requestDto.setTypeId(productOrder.getId());
        requestDto.setUserId(productOrder.getUserId());
        /*Result result = walletForzenClient.pay(requestDto);
        if(result.getCode() != 0){
            throw new ClientException(result.getMessage());
        }*/
        requestDto.setType(FrozenTypeEnum.FTZ_PRODUCT);
        if(!walletForzenClientAction.pay(requestDto)){
            throw new Exception("使用冻结金额异常");
        }
        //扣取订单的红包提成
        this.directRedPackMoney(orderId);
        return true;
    }


    public void checkIsSend() throws Exception{
        //获取待发货订单
        List<ProductOrder> productOrderList = productOrderService.getProductOrderList(OrderStatusEnum.UNSEND.getCode());

        //获取当前时间前三天同一时间戳
        long threeDayAgo= DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(new Date().getTime(),3,2);

        CancelOrderRequestDto cancelOrderRequestDto=new CancelOrderRequestDto();

        if (productOrderList != null){
            for (ProductOrder productOrder : productOrderList){
                long payTime = productOrder.getPayTime().getTime();
                if (payTime<threeDayAgo){
                    cancelOrderRequestDto.setId(productOrder.getId());
                    try {
                        this.cancel(cancelOrderRequestDto);
                    }catch (Exception e){
                        logger.error(e.getMessage(),e);
                    }
                }
            }
        }
    }

    public void changePutOffStatusAndPay() throws Exception{
        //获取申请延迟的订
        List<String> orderIdList = productOrderService.getOrderIds(OrderStatusEnum.PUTOFF.getCode());

        //获取当前时间前15天时间戳
        long fifteenDayAgo = DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(new Date().getTime(),15,2);

        if (orderIdList != null ){
            //获取商家已经同意延迟的订单
            List<ProductOrderPutOff> productOrderPutOffList = productOrderPutOffService.getProductOrderPutOffList(ProductPutOffStatusEnum.SELLER_AGREE.getCode(),orderIdList);
            if (productOrderPutOffList != null){
                for (ProductOrderPutOff productOrderPutOff : productOrderPutOffList){
                    //获取订单延期到期时间戳
                    long expirationTime = productOrderPutOff.getExpirationTime().getTime();
                    //获取当前时间戳
                    long now = new Date().getTime();
                    if (expirationTime<now){
                        //改变延期状态为双方延期成功
                        productOrderPutOff.setStatus(ProductPutOffStatusEnum.SUCCESS.getCode());
                        productOrderPutOffService.updateById(productOrderPutOff);
                        //超过延期时间，自动确认用户收货，并打款给商家
                        ProductOrder productOrder = productOrderService.selectById(productOrderPutOff.getOrderId());
                        //改变订单状态
                        productOrder.setStatus(OrderStatusEnum.TOEVALUAT.getCode());
                        productOrderService.updateById(productOrder);
                        //使用冻结，钱转给商家
                        FrozenOperationRequestDto requestDto = new FrozenOperationRequestDto();
                        requestDto.setTypeId(productOrder.getId());
                        requestDto.setUserId(productOrder.getUserId());
                        /*Result result = walletForzenClient.pay(requestDto);
                        if(result.getCode() != 0){
                            throw new ClientException(result.getMessage());
                        }*/
                        requestDto.setType(FrozenTypeEnum.FTZ_PRODUCT);
                        if(!walletForzenClientAction.pay(requestDto)){
                            throw new Exception("使用冻结金额异常");
                        }
                        //扣取订单的红包提成
                        this.directRedPackMoney(productOrderPutOff.getOrderId());
                    }
                }
            }

            // 获取商家拒接延迟的订单
            List<ProductOrderPutOff> productOrderPutOffListone = productOrderPutOffService.getProductOrderPutOffList(ProductPutOffStatusEnum.REFUSE.getCode());
            if (productOrderPutOffListone != null){
                for (ProductOrderPutOff productOrderPutOff : productOrderPutOffListone){
                    ProductOrder productOrder = productOrderService.selectById(productOrderPutOff.getOrderId());
                    if (productOrder != null){
                        long sendTime = productOrder.getSendTime().getTime();
                        if (sendTime<fifteenDayAgo){
                            //改变延期状态为双方延期成功
                            productOrderPutOff.setStatus(ProductPutOffStatusEnum.SUCCESS.getCode());
                            productOrderPutOffService.updateById(productOrderPutOff);

                            //改变订单状态
                            productOrder.setStatus(OrderStatusEnum.TOEVALUAT.getCode());
                            productOrderService.updateById(productOrder);
                        }
                    }
                }
            }
        }
    }


    public void changeResturnStatusAndPay() throws Exception{
        //获取用户申请退款订单
        List<String> orderIdList = productOrderService.getOrderIds(OrderStatusEnum.INRETURN.getCode());
        if (orderIdList != null){
            //获取当前时间前三天时间戳
            long threeDayAgo= DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(new Date().getTime(),3,2);

            //获取商家未确认退款的订单
            List<ProductReturn> productReturnList = productReturnService.getProductReturnList(ProductReturnStatusEnum.USER_APPLY.getCode(),orderIdList);
            if (productReturnList != null){
                for (ProductReturn productReturn : productReturnList){
                    //获取申请退款时间戳
                    long applyTime = productReturn.getApplyTime().getTime();
                    if (applyTime<threeDayAgo){
                        //改变退货状态、订单状态为完成
                        productReturn.setStatus(ProductReturnStatusEnum.SUCCESS.getCode());
                        productReturnService.updateById(productReturn);

                        ProductOrder productOrder = new ProductOrder();
                        productOrder.setId(productReturn.getOrderId());
                        productOrder.setStatus(OrderStatusEnum.COMPLETED.getCode());
                        productOrderService.updateById(productOrder);

                        //退款给用户
                        this.repealFrozen(productReturn.getOrderId(), productReturn.getUserId());
                    }
                }

            }
        }
    }


    public boolean notAgreeGoodsOrderReturn(String orderId) {
        ProductReturn productReturn = productReturnService.getProductReturnByOrderId(orderId);
        if (productReturn != null){
            productReturn.setStatus(ProductReturnStatusEnum.REFUSE.getCode());
            productReturnService.updateById(productReturn);

            //发送信息给用户
            messagePushProxy.messagePushJump(MessageTypeEnum.PRODUCT_TYPE,"商家拒接了你的退款申请","退款通知", productReturn.getUserId(), PushMessageDocTypeEnum.GoodsOrderDetail,orderId);

            return true;
        }
        return false;
    }

    public void getLogistcsDetailList(GetProductOrderResponseVo getProductOrderResponseVo){
        if (getProductOrderResponseVo.getLogistcsDetails() != null){
            List<Map<String,String>> mapList = aliyunExpressService.jsonToExpressDateLists(getProductOrderResponseVo.getLogistcsDetails());
            getProductOrderResponseVo.setLogistcsDetailList(mapList);
        }

    }

    public GetProductOrderResponseVo get(GetGoodsOrderRequestDto request) {
        GetProductOrderResponseVo getGoodsOrderResponseVo = new GetProductOrderResponseVo();
        ProductOrder productOrder = productOrderService.getproductOrder(request);

        if(productOrder !=null){
            getGoodsOrderResponseVo = VoPoConverter.copyProperties(productOrder,GetProductOrderResponseVo.class);
            Seller seller = sellerService.selectById(productOrder.getSellerId());
            if (seller != null){
                getGoodsOrderResponseVo.setSellerName(seller.getName());
                getGoodsOrderResponseVo.setSellerUserId(seller.getUserId());
                getGoodsOrderResponseVo.setCityCode(seller.getCityCode());
                getGoodsOrderResponseVo.setAreaCode(seller.getAreaCode());
                getGoodsOrderResponseVo.setAddrDetail(seller.getAddrDetail());
                getGoodsOrderResponseVo.setSellerLat(seller.getLat());
                getGoodsOrderResponseVo.setSellerLon(seller.getLon());
            }
            getLogistcsDetailList(getGoodsOrderResponseVo);
            getGoodsOrderResponseVo.setItemList(productOrderItemAction.getListByOrderId(productOrder.getId()));
            getGoodsOrderResponseVo.setGoodsList(productFuseAction.getGoodsListByOrderId(productOrder.getId()));
            getGoodsOrderResponseVo.setProductReturn(productReturnAction.getGoodsReturnByOrderId(productOrder.getId()));
            getGoodsOrderResponseVo.setOrderReturnStatus(productOrderAction.getOrderReturnAndPutOffStatus(productOrder.getId()).getOrderReturnStatus()==null?null:productOrderAction.getOrderReturnAndPutOffStatus(productOrder.getId()).getOrderReturnStatus());
            getGoodsOrderResponseVo.setOrderPutOffStatus(productOrderAction.getOrderReturnAndPutOffStatus(productOrder.getId()).getOrderPutOffStatus()==null?null:productOrderAction.getOrderReturnAndPutOffStatus(productOrder.getId()).getOrderPutOffStatus());

            //获取物流公司名称
            if (productOrder.getLogisticsCode()!=null){
                SellerExpress express= expressService.getExpress(productOrder.getLogisticsCode());
                String logisticsName="";
                if (express!=null){
                    logisticsName=express.getName();
                }
                getGoodsOrderResponseVo.setLogisticsName(logisticsName);
            }
        }
        return getGoodsOrderResponseVo;
    }

    public GetActivityOrderResponseVo getActivityOrder(String orderId,double lat,double lon){
        GetActivityOrderResponseVo getActivityOrderResponseVo = new GetActivityOrderResponseVo();
        ProductOrder productOrder = productOrderService.selectById(orderId);
        if (productOrder == null){
            return getActivityOrderResponseVo;
        }
        getActivityOrderResponseVo.setTotalPrice(new BigDecimal(Money.getMoneyString(productOrder.getTotalPrice())));

        Seller seller = sellerService.selectById(productOrder.getSellerId());
        if (seller != null){
            getSellerMessage(seller,getActivityOrderResponseVo,lat,lon);
        }
        getActivityOrderResponseVo.setList(getProductOrderItemVo(orderId));
        return getActivityOrderResponseVo;
    }

    private void getSellerMessage(Seller seller,GetActivityOrderResponseVo getActivityOrderResponseVo,double lat,double lon){
        BeanUtils.copyProperties(seller,getActivityOrderResponseVo);
        getActivityOrderResponseVo.setSellerImagesUrl(addImgUrlPreUtil.addImgUrlPres(seller.getSellerImagesUrl(), AliyunBucketType.ProductBucket));
        //商家名称
        getActivityOrderResponseVo.setSellerName(seller.getName());
        //信用值
        getActivityOrderResponseVo.setCredit(userAction.getUserService().selectById(seller.getUserId()).getCredit());
        //首单红包比例
        BigDecimal firstRate = packetSetService.getFirstRate(seller.getId());
        if (firstRate != null){
            getActivityOrderResponseVo.setFirstRate(firstRate);
        }
        //类目名称
//        String name = sellerCategoryAction.getCategoryName(seller.getId());
//        if (name != null){
//            getActivityOrderResponseVo.setCategoryName(name);
//        }
        //距离
        if (seller.getLat() != null && seller.getLon() != null) {
            double distance = DistrictUtil.calcDistance(new BigDecimal(lat), new BigDecimal(lon), seller.getLat(), seller.getLon());
            //保留小数点后两位小数
            String dstr = String.valueOf(distance);
            BigDecimal bd = new BigDecimal(dstr);
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            //判断值大于999.99默认为999.99
            if (bd.compareTo(new BigDecimal(999.99)) > 0) {
                bd = new BigDecimal(999.99);
            }
            getActivityOrderResponseVo.setDistance(bd.doubleValue());
        }
    }

    private List<ProductOrderItemVo> getProductOrderItemVo(String orderId){
        List<ProductOrderItemVo> list = new ArrayList<>();
        List<ProductOrderItem> productOrderItemList = productOrderItemAction.getListByOrderId(orderId);
        if (productOrderItemList != null && productOrderItemList.size()>0){
            for (ProductOrderItem productOrderItem : productOrderItemList){
                ProductOrderItemVo productOrderItemVo = new ProductOrderItemVo();
                productOrderItemVo.setPrice(new BigDecimal(Money.getMoneyString(productOrderItem.getPrice())));
                productOrderItemVo.setQuanty(productOrderItem.getQuantity());
                //规格名称
                productOrderItemVo.setSpeName(productSpeService.selectById(productOrderItem.getSpeId()).getName()==null?null:productSpeService.selectById(productOrderItem.getSpeId()).getName());
                //商品名称、图片
                Product product = productService.selectById(productOrderItem.getProductId());
                if (product != null){
                    productOrderItemVo.setProductName(product.getName());
                    productOrderItemVo.setProductImagesUrl(addImgUrlPreUtil.addImgUrlPres(product.getProductImagesUrl(), AliyunBucketType.ProductBucket));
                }
                list.add(productOrderItemVo);
            }
        }
        return list;
    }


    public List<GetProductOrderResponseVo> list(GetGoodsOrderListRequestDto request) throws Exception {
        List<GetProductOrderResponseVo> resultList = new ArrayList<>();
        List<ProductOrder> productOrderList =new ArrayList<>();
        if(StringUtils.isNotEmpty(request.getUserId())){
            if (request.getStatus() != null){
                productOrderList = productOrderService.getProductOrderListByUserIdAndStatus(request.getUserId(),request.getStatus());
            }else {
                String[] status={"1","4","5","12","6","9","2","3","10","11","7","8"};
                Integer current = (request.getCurrent()-1)*request.getSize();
                productOrderList = productOrderService.getProductOrderList(request.getUserId(),status,current,request.getSize());}
        }
        if(StringUtils.isNotEmpty(request.getSelleId())){
            Page<ProductOrder> resultPage = productOrderService.getPage(request);
            productOrderList = resultPage.getRecords();
        }

        for (ProductOrder productOrder : productOrderList) {
            GetProductOrderResponseVo getGoodsOrderResponseVo = new GetProductOrderResponseVo();
            BeanUtils.copyProperties(productOrder, getGoodsOrderResponseVo);
            getGoodsOrderResponseVo.setSellerName(sellerService.selectById(productOrder.getSellerId())==null?null: sellerService.selectById(productOrder.getSellerId()).getName());
            getGoodsOrderResponseVo.setSellerUserId(sellerService.selectById(productOrder.getSellerId())==null?null: sellerService.selectById(productOrder.getSellerId()).getUserId());
            getGoodsOrderResponseVo.setItemList(productOrderItemAction.getListByOrderId(productOrder.getId()));
            getGoodsOrderResponseVo.setGoodsList(productFuseAction.getGoodsListByOrderId(productOrder.getId()));
            //获取商家地址详情
            Seller seller= sellerService.selectById(productOrder.getSellerId());
            getGoodsOrderResponseVo.setAddrDetail(seller.getAddrDetail());

            //获取用户的经纬度
            UserGeo userGeo=geoService.getUserGeo(productOrder.getUserId());
            if (userGeo.getLon()!=null && userGeo.getLat()!=null)
            {
                getGoodsOrderResponseVo.setUserLon(new BigDecimal(userGeo.getLon()));
                getGoodsOrderResponseVo.setUserLat(new BigDecimal(userGeo.getLat()));
            }
            resultList.add(getGoodsOrderResponseVo);
        }

        return resultList;
    }

    public boolean changeActivityOrderStatusAndSendMessage(ChangeActivityOrderStatusAndSendMessageRequestDto request) throws Exception{
        ProductOrder productOrder = productOrderService.selectById(request.getOrderId());
        if (productOrder != null){
            //改变订单状态为已付款
            productOrder.setStatus(OrderStatusEnum.PAID.getCode());
            productOrderService.updateById(productOrder);

            //发送消息给商家业务主管
            MessageFormat mf=new MessageFormat("你有新订单生产，{0}于{1}下单，预约到现场消费时间为{2}，请及时处理");
            //获取认购者用户昵称
            String nickName = userAction.getUserService().selectById(productOrder.getUserId()).getNickname()==null?null:userAction.getUserService().selectById(productOrder.getUserId()).getNickname();
            //获取当前时间
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy:MM:dd:HH:mm");
            String nowDate=simpleDateFormat.format(new Date().getTime());
            String startedAt=simpleDateFormat.format(request.getStartedAt());
            String alertMsg=mf.format(new String[]{nickName,nowDate,startedAt});
            //获取商家业务主管userId
            Seller seller= sellerService.selectById(productOrder.getSellerId());
            String manageNetworkNum = manageService.selectById(seller.getManageId()).getManageNetworkNum();
            String manageUserId = this.getUserIdByNetworkNum(manageNetworkNum);
            if (StringUtils.isNotEmpty(manageUserId)){
                messagePushProxy.messagePushJump(MessageTypeEnum.PRODUCT_TYPE,alertMsg,"生产订单通知",manageUserId, PushMessageDocTypeEnum.GoodsOrderDetail, productOrder.getId());
            }
            return true;
        }
        return false;
    }
}
