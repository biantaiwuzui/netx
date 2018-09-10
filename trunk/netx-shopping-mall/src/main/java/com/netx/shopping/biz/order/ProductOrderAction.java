package com.netx.shopping.biz.order;

import com.netx.common.common.utils.OrderUtil;
import com.netx.common.express.AliyunExpressService;
import com.netx.common.user.util.DateTimestampUtil;
import com.netx.common.vo.business.*;
import com.netx.shopping.biz.product.ProductAction;
import com.netx.shopping.enums.*;
import com.netx.shopping.model.business.Seller;
import com.netx.shopping.model.business.SellerPacketSet;
import com.netx.shopping.model.order.*;
import com.netx.shopping.model.product.Product;
import com.netx.shopping.service.business.PacketSetService;
import com.netx.shopping.service.business.SellerService;
import com.netx.shopping.service.order.*;
import com.netx.shopping.service.product.ProductService;
import com.netx.shopping.service.product.ProductSpeService;
import com.netx.shopping.vo.GetProductOrderResponseVo;
import com.netx.utils.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 * 网商-商品订单表 服务实现类
 * </p>
 *
 * @author wj.liu
 * @since 2017-08-29
 */
@Service("productOrderAction")
@Transactional(rollbackFor = Exception.class)
public class ProductOrderAction{

    private Logger logger= LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Autowired
    AliyunExpressService aliyunExpressService;

    @Autowired
    HashCheckoutAction hashCheckoutAction;

    @Autowired
    ProductService productService;

    @Autowired
    ProductOrderService productOrderService;

    @Autowired
    SellerService sellerService;

    @Autowired
    ProductReturnService productReturnService;

    @Autowired
    ProductOrderItemService productOrderItemService;

    @Autowired
    ProductOrderPutOffService productOrderPutOffService;

    @Autowired
    PacketSetService packetSetService;

    @Autowired
    ProductSpeService productSpe;

    @Autowired
    ProductAction productAction;

    public ProductOrder commit(CommitOrderRequestDto request) throws Exception{

//        //判断是否重复提交
//        boolean res= hashCheckoutAction.hashCheckout(request.getHash());
//        if (!res){
//            throw new Exception("机器无效提交订单");
//        }

        String sellerId = productService.selectById((request.getGoodsList()).get(0).getProductId())==null?
                null: productService.selectById((request.getGoodsList()).get(0).getProductId()).getSellerId();

//        //商家缴费状态不是已缴费不能下订单
//        if (sellerId !=null){
//
//            if (sellerService.selectById(sellerId).getPayStatus() != null && sellerService.selectById(sellerId).getPayStatus() != 0)
//            {
//                throw new Exception("商家管理费未续费，不能下订单！");
//            }
//        }

        ProductOrderItem p;
        if (request.getIsPayOfActivity() != null && request.getIsPayOfActivity() == 1){
            p = null;
        }else {
            //判断购物车里是否已有相同规格的商品，是：在原来的订单上加数量
            p = this.judgeCommitProduct(request);
        }
        if (p != null){
            return this.updateOrder(request,p);
        }
        else {
            return insetOrder(request,sellerId);
        }
    }

    private ProductOrder insetOrder(CommitOrderRequestDto request,String sellerId){
        ProductOrder productOrder = new ProductOrder();
        BeanUtils.copyProperties(request, productOrder);
        productOrder.setSellerId(sellerId);
        BigDecimal totalPrice = BigDecimal.valueOf(0);
        //计算订单总额，所有商品基础价格+规格项价格，再乘以数量
        List<CommitOrderGoodsListDto> goodList = request.getGoodsList();
        for (CommitOrderGoodsListDto goods: goodList) {
            BigDecimal price = goods.getPrice();
            totalPrice = totalPrice.add(price.multiply(BigDecimal.valueOf(goods.getQuantity())));
        }
        productOrder.setTotalPrice(new Money(totalPrice).getCent());//订单总额

        //判断是操作方式类型
        if (request.getWay() == 1){
            productOrder.setStatus(OrderStatusEnum.GOUWUCHE.getCode());
        }else if(request.getIsPayOfActivity() != null && request.getIsPayOfActivity() == 1) {
            //判断是否是活动里的支付 是：把支付后订单状态改为待生成，待生成状态商家列表里是不显示的，需要过滤掉
            productOrder.setStatus(OrderStatusEnum.TOBEGENERATE.getCode());
            productOrder.setOrderNum(OrderUtil.getOrderNumber());//订单号
        }else {
            productOrder.setStatus(OrderStatusEnum.UNPAID.getCode());
            productOrder.setOrderNum(OrderUtil.getOrderNumber());//订单号
        }
        productOrder.setOrderTime(new Date());
        productOrder.setCreateUserId(request.getUserId());
        productOrder.setDeleted(0);
        productOrderService.insert(productOrder);
        //保存订单项
        for (CommitOrderGoodsListDto goods: goodList) {
            ProductOrderItem productOrderItem = new ProductOrderItem();
            BeanUtils.copyProperties(goods, productOrderItem);
            productOrderItem.setOrderId(productOrder.getId());
            productOrderItem.setSpeId(goods.getSkuId());
            productOrderItem.setPrice(new Money(goods.getPrice()).getCent());
            productOrderItem.setCreateUserId(request.getUserId());
            productOrderItem.setDeleted(0);
            productOrderItemService.insert(productOrderItem);
        }
        return productOrder;
    }

    private ProductOrderItem judgeCommitProduct(CommitOrderRequestDto request){
        if (request.getGoodsList().get(0).getProductId() != null && request.getGoodsList().get(0).getSkuId() != null){
            Integer status = 0;
            List<String> orderIds = productOrderService.getOrderIds(request.getUserId(),status);
            if (orderIds != null && orderIds.size()>0){
                ProductOrderItem productOrderItem = productOrderItemService.getProductOrderItem(orderIds,request.getGoodsList().get(0).getProductId(),request.getGoodsList().get(0).getSkuId());
                return productOrderItem;
            }
        }
        return null;
    }

    private ProductOrder updateOrder(CommitOrderRequestDto request,ProductOrderItem productOrderItem){
        //修改订单总价格
        ProductOrder productOrder = productOrderService.selectById(productOrderItem.getOrderId());
        productOrder.setTotalPrice(productOrder.getTotalPrice()+productOrderItem.getPrice()*request.getGoodsList().get(0).getQuantity());
        productOrderService.updateById(productOrder);
        //修改订单商品详细数量
        productOrderItem.setQuantity(productOrderItem.getQuantity()+request.getGoodsList().get(0).getQuantity());
        productOrderItemService.updateById(productOrderItem);
        return productOrder;
    }
    
    public boolean update(UpdateOrderRequestDto request){
        return productOrderService.update(request);
    }
    
    public Integer oderQuantity(String userId, Integer status){
        Integer resultQuantity=0;
        Integer quantity=0;//订单总量,包含所有状态
        Integer okQuantity=0;//订单已完成的数量
        Integer cancelQuantity=0;//订单已取消的数量
        quantity=productOrderService.getProductOrderCount(userId);
        //查询已完成
        okQuantity=productOrderService.getOkProductOrderCount(userId);
        //查询已取消
        cancelQuantity=productOrderService.getCancelProductOrderCount(userId);

        if (status==8)
        {
            resultQuantity=quantity-cancelQuantity;
            return resultQuantity;
        }
        resultQuantity=quantity-cancelQuantity-okQuantity;
        return resultQuantity;

    }

    
    public BigDecimal getSumOrderAmountBySellerId(String sellerId){
        if (sellerId==null)
        {
            return new BigDecimal(0);
        }
        return productOrderService.getSumOrderAmountBySellerId(sellerId);
    }
    
    public boolean isExistSellerNoCompleteOrder(String sellerId){
        List<ProductOrder> productOrderList = productOrderService.getProductOrderListBySellerIdAndStatus(sellerId,OrderStatusEnum.COMPLETED.getCode());
        if(null != productOrderList && productOrderList.size() > 0){
            return true;
        }
        return false;
    }

    
    public GetlastMonthOdersAmountResponseVo getlastMonthOdersAmount(String userId){
        //根据userId获取注册的商家id
        GetlastMonthOdersAmountResponseVo responseVo=new GetlastMonthOdersAmountResponseVo();
        BigDecimal sumTotalPrice=new BigDecimal(0);
        List<Seller> sellers= sellerService.getSellerListByUserId(userId);
        List<String> sellersIds=new ArrayList<>();
        for (Seller s:sellers)
        {
            sellersIds.add(s.getId());
        }
        //获取上个月1号零时时间戳
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND, 0);
        Long start=cal.getTimeInMillis();
        //获取本月1号零时时间戳
        Calendar cal2 = Calendar.getInstance();
        cal2.set(Calendar.DAY_OF_MONTH,0);//设置为1号,当前日期既为本月第一天
        cal2.set(Calendar.HOUR_OF_DAY, 24);
        cal2.set(Calendar.MINUTE,0);
        cal2.set(Calendar.SECOND,0);
        Long end=cal2.getTimeInMillis();
        //根据商家ID查询上个月的订单交易额
        for (String ids:sellersIds)
        {
            BigDecimal totalPrice=productOrderService.getTotalPrice(ids,new Date(start),new Date(end));
            if(totalPrice==null)
            {
                totalPrice=new BigDecimal(0);
            }
            sumTotalPrice=sumTotalPrice.add(totalPrice);
        }
        responseVo.setSumTotalPrice(sumTotalPrice);
        return responseVo;
    }

    
    public GetEveryMonthOrderAmountResponseDto getEveryMonthOrderAmounts(GetEveryMonthOrderAmountRequestDto request){
        GetEveryMonthOrderAmountResponseDto responseDto=new GetEveryMonthOrderAmountResponseDto();
        List<BigDecimal> result=new ArrayList<>();
        List<Seller> sellers= sellerService.getSellerListByUserId(request.getUserId());
        List<String> sellersIds=new ArrayList<>();
        for (Seller s:sellers)
        {
            sellersIds.add(s.getId());
        }

        Long start=request.getStart().getTime();
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        Long end=0l;
        //获取每个月的时间戳范围，根据范围获取交易额
        do {
            BigDecimal sumTotalPrice=new BigDecimal(0);
            cal = Calendar.getInstance();
            cal.setTime(new Date(start*1000l));
            cal.add(Calendar.MONTH, 1);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            end = cal.getTimeInMillis() / 1000;

            //根据商家ID查询上个月的订单交易额
            for (String ids : sellersIds) {
                BigDecimal totalPrice = productOrderService.getTotalPrice(ids,new Date(start),new Date(end));
                if (totalPrice == null) {
                    totalPrice = new BigDecimal(0);
                }
                sumTotalPrice=sumTotalPrice.add(totalPrice);
            }
            result.add(sumTotalPrice);
            start=end;
        }while(((now.getTime()-end*1000l )/DateTimestampUtil.TIME_DIFFERENCE*30>0));
        responseDto.setSumTotalPrices(result);
        return responseDto;
    }

    /**
     * 获取订单需扣取的红包提成金额
     * @param
     * @return
     * @throws
     */
    public BigDecimal getDirectRedPacketAmount(ProductOrder order)throws Exception{
        //按商家设定的红包提成标准，按当前订单金额计提红包金额至红包池
        Seller seller = sellerService.selectById(order.getSellerId());
        if (seller==null){
            throw new Exception("供应商家不存在");
        }
        SellerPacketSet packetSet = packetSetService.selectById(seller.getPacSetId());
        if (packetSet == null) {
            throw new Exception("红包设置不存在");
        }
        BigDecimal packetAmount = new BigDecimal(0);
        if (packetSet.getChangeRate() == false) {//固定计提比例
            packetAmount = packetSet.getFixedRate().multiply(new BigDecimal(Money.getMoneyString(order.getTotalPrice())).divide(new BigDecimal(100)));

        } else {//每天根据订单数量，由低到高，逐单加大红包计提比例
            //获取今天开始和结束的时间戳
            long startTimeStamp = DateTimestampUtil.getStartOrEndOfTimestamp(new Date().getTime(), 1);
            long endTimeStamp = DateTimestampUtil.getStartOrEndOfTimestamp(new Date().getTime(), 2);
            //查找今天订单数
            int orderNum = productOrderService.getProductOrderCount(new Date(startTimeStamp),new Date(endTimeStamp));
            if (orderNum == 1) {//首单计提比例
                packetAmount = packetSet.getFirstRate().multiply(new BigDecimal(Money.getMoneyString(order.getTotalPrice())).divide(new BigDecimal(100)));
            } else if (packetSet.getLimitRate().compareTo(new BigDecimal(orderNum).multiply(packetSet.getGradualRate()).add(packetSet.getFirstRate())) != 1) {
                //封顶计提比例
                packetAmount = packetSet.getLimitRate().multiply(new BigDecimal(Money.getMoneyString(order.getTotalPrice())).divide(new BigDecimal(100)));
            } else {//逐单递增比例
                packetAmount = new BigDecimal(orderNum).multiply(packetSet.getGradualRate()).add(packetSet.getFirstRate()).multiply(new BigDecimal(Money.getMoneyString(order.getTotalPrice())).divide(new BigDecimal(100)));
            }
        }
        return packetAmount;
    }

    public List<String> getGoodsNameAndSellerName(String goodOrderId){
        List<String> result=new ArrayList<>();
        String  goodsId=productOrderItemService.getProductId(goodOrderId);
        if(goodsId==null){
            return null;
        }
        Product product = productService.selectById(goodsId);
        if(product !=null){
            result.add(product.getName());
        }
        String sellerName= sellerService.selectById(product.getSellerId()).getName();
        result.add(sellerName);
        return result;
    }
    
    public boolean  changeGoodsOrderStatusAndReturnStatus(CheckOrderArbitrationRequestDto request) {
        ProductOrder productOrder =productOrderService.selectById(request.getTypeId());
        ProductReturn productReturn = productReturnService.getProductReturnByOrderId(request.getTypeId());
        Date date=new Date();
        if(productOrder !=null){
            productOrder.setStatus(OrderStatusEnum.COMPLETED.getCode());
        }
        if(productReturn !=null){
            productReturn.setStatus(ProductReturnStatusEnum.USER_CANCEL.getCode());
        }
        return productOrderService.updateById(productOrder) && productReturnService.updateById(productReturn);
    }
    
    public Integer oneDayOrderQuantity(String goodsId){
        //获取当前日期开始时间戳
        long starts= DateTimestampUtil.getStartOrEndOfTimestamp(new Date().getTime(),1);
        //获取当前时间戳
        long now=new Date().getTime();
        //根据商品id查询订单详情里的订单id
        List<ProductOrderItem> list= productOrderItemService.getProductOrderItemList(goodsId,new Date(starts),new Date(now));
        List<String> oderIds=new ArrayList<>();
        //根据订单id过滤掉订单状态为取消的订单
        if(list.size()>0)
        {
            for (ProductOrderItem g:list)
            {
                oderIds.add(g.getOrderId());
            }
            List<ProductOrder> productOrders =productOrderService.getProductOrderList(oderIds);
            Integer result= productOrders.size();
            for (ProductOrder g: productOrders)
            {
                if(g.getStatus()==8)
                {
                    result--;
                }
            }
            return result;
        }
        return 0;
    }

    
    public boolean changeGoodsOrderStatus(ChangeGoodsOrderStatusRequestDto request){
        ProductOrder productOrder =new ProductOrder();
        BeanUtils.copyProperties(request, productOrder);
        return productOrderService.updateById(productOrder);
    }

    public boolean changeProductOrderStatusAndUpdate(ChangeGoodsOrderStatusAndStatusRequestDto request) throws Exception{
        if (request.getOrderId() != null && request.getOrderId().size()>0) {
            String[] ids = new String[request.getOrderId().size()];
            request.getOrderId().toArray(ids);
            List<ProductOrder> list = productOrderService.getProductOrderListByIds(ids);
            if (judgeOrderId(list,request.getOrderId())){
                for (int i=0;i<list.size();i++){
                    ProductOrder productOrder = new ProductOrder();
                    BeanUtils.copyProperties(list.get(i),productOrder);
                    productOrder.setAddress(request.getAddress());
                    productOrder.setDeliveryWay(request.getDeliveryWay().get(i));
                    productOrder.setDeliveryWay(request.getDeliveryWay().get(i));
                    productOrder.setStatus(request.getStatus());
                    if (request.getRemark()!=null){
                        productOrder.setRemark(request.getRemark().get(productOrder.getSellerId()));
                    }
                    Map<String,String> map = new HashMap<>();
                    if (productOrder.getStatus() == 0){
                        if (i == 0){
                            String orderNum = OrderUtil.getOrderNumber();
                            productOrder.setOrderNum(orderNum);//订单号
                            map.put(productOrder.getSellerId(),orderNum);
                        }else {
                            if (map.containsKey(productOrder.getSellerId())){
                                productOrder.setOrderNum(map.get(productOrder.getSellerId()));
                            }else {
                                String orderNum = OrderUtil.getOrderNumber();
                                productOrder.setOrderNum(orderNum);
                                map.put(productOrder.getSellerId(),orderNum);
                            }
                        }

                    }
                    productOrderService.updateById(productOrder);
                }
                return true;
            }
        }
        return false;
    }

    public void getOrderLogistcsDetails() throws Exception {
        //查询需要返回订单物流详情的订单

        List<ProductOrder> list=productOrderService.getProductOrderList(1,1);
        //获取订单物流详情
        if(list!=null&&list.size()>0){
            for (ProductOrder productOrder :list){
                try {
                    String logistcsDetails=aliyunExpressService.getExpressData(productOrder.getLogisticsNum(), productOrder.getLogisticsCode());
                    productOrder.setLogistcsDetails(logistcsDetails);
                    productOrderService.updateById(productOrder);
                }catch (Exception e){
                    logger.error(e.getMessage(),e);
                }

            }
        }
    }

    public boolean judgeOrderId(List<ProductOrder> productOrders,List<String> productOrderIds) throws Exception{
        if (productOrders != null && productOrders.size() == productOrderIds.size()){
            for (ProductOrder productOrder : productOrders){
                if (productOrder.getStatus()==OrderStatusEnum.TOBEGENERATE.getCode()){
                    throw new Exception("订单还不可以支付");
                }
                if(productOrder.getStatus() != OrderStatusEnum.UNPAID.getCode()&& productOrder.getStatus()!=OrderStatusEnum.SERVED.getCode() && productOrder.getStatus()!=OrderStatusEnum.GOUWUCHE.getCode()){
                    throw new Exception("订单已支付");
                }
                Seller seller = sellerService.selectById(productOrder.getSellerId());
                if(null == seller){
                    throw new Exception("订单号支付失败，商品已下架");
                }
            }
            return true;
        }else {
            throw new Exception("支付订单不存在或缺失");
        }
    }

    /**
     * 获取昨日有订单完成的商家id
     * @return
     */
    public List<String> getYesterdayOrderIds(){
        //昨日今时时间戳
        Long yesterday=DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(System.currentTimeMillis(),1,1);
        Long start=DateTimestampUtil.getStartOrEndOfTimestamp(yesterday,1);
        Long end=DateTimestampUtil.getStartOrEndOfTimestamp(yesterday,2);
        List<String> res=productOrderService.getSellerIds(OrderStatusEnum.COMPLETED.getCode(),new Date(start),new Date(end));
        return res;
    }

    
    public void changeResturnStatusAndOrderStatus(){
        //获取用户申请退款订单
        List<String> orderIdList = productOrderService.getOrderIds(OrderStatusEnum.INRETURN.getCode());
        if (orderIdList != null){
            //获取当前时间前15天时间戳
            long fifteenDayAgo = DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(new Date().getTime(),15,2);
            //获取商家确认退款的订单,用户未确认收款的订单
            List<ProductReturn> productReturnList = productReturnService.getProductReturnList(ProductReturnStatusEnum.SELLER_AGREE.getCode(),orderIdList);
            if (productReturnList != null){
                for (ProductReturn productReturn : productReturnList){
                    long agreeTime = productReturn.getAgreeTime().getTime();
                    if (agreeTime<fifteenDayAgo){
                        //改变退货状态、订单状态为完成
                        productReturn.setStatus(ProductReturnStatusEnum.SUCCESS.getCode());
                        productReturnService.updateById(productReturn);

                        ProductOrder productOrder = new ProductOrder();
                        productOrder.setId(productReturn.getOrderId());
                        productOrder.setStatus(OrderStatusEnum.COMPLETED.getCode());
                        productOrderService.updateById(productOrder);
                    }
                }
            }
        }

    }

    
    public GetProductOrderResponseVo getOrderReturnAndPutOffStatus(String orderId){
        GetProductOrderResponseVo getGoodsOrderResponseVo=new GetProductOrderResponseVo();
        ProductReturn productReturn = productReturnService.getProductReturnByOrderId(orderId);
        if (productReturn != null){
            getGoodsOrderResponseVo.setOrderReturnStatus(productReturn.getStatus());
        }

        ProductOrderPutOff productOrderPutOff = productOrderPutOffService.getProductOrderPutOff(orderId);
        if (productOrderPutOff != null ){
            getGoodsOrderResponseVo.setOrderPutOffStatus(productOrderPutOff.getStatus());
        }
        return getGoodsOrderResponseVo;
    }

    
    public void changePutOffAndOrderStatus(){
        //获取用户申请延期的订单
        List<ProductOrderPutOff> productOrderPutOffList = productOrderPutOffService.getProductOrderPutOffList(ProductPutOffStatusEnum.USER_APPLY.getCode());

        if (productOrderPutOffList != null){
            //获取当前时间前三天时间戳
            long threeDayAgo= DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(new Date().getTime(),3,2);
            for (ProductOrderPutOff productOrderPutOff : productOrderPutOffList){
                //获取用户申请延期时间
                long applyTime = productOrderPutOff.getApplyTime().getTime();
                if (applyTime<threeDayAgo){
                    //改变延迟收货状态为商家同意延期，改变延期到期时间
                    productOrderPutOff.setStatus(ProductPutOffStatusEnum.SELLER_AGREE.getCode());
                    ProductOrder productOrder = productOrderService.selectById(productOrderPutOff.getOrderId());
                    if(productOrder!=null && productOrder.getSendTime()!=null){
                        //获取订单发货时间 并计算出延迟收货到期时间
                        long sendTime= productOrder.getSendTime().getTime();
                        long expirationTime= DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(sendTime,7,2);
                        productOrderPutOff.setExpirationTime(new Date(expirationTime));
                        productOrderPutOffService.updateById(productOrderPutOff);
                    }
                }
            }

        }
    }

    
    public void changeOrderStatusToCompleted(){
        //获取所有待评论的订单
        List<ProductOrder> productOrderList = productOrderService.getProductOrderList(OrderStatusEnum.TOEVALUAT.getCode());
        if (productOrderList != null){
            //获取当前时间前三天时间戳
            long threeDayAgo = DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(new Date().getTime(),3,2);
            for (ProductOrder productOrder : productOrderList){
                long updateTime = productOrder.getUpdateTime().getTime();
                if (updateTime < threeDayAgo){
                    productOrder.setStatus(OrderStatusEnum.COMPLETED.getCode());
                    productOrderService.updateById(productOrder);
                }
            }
        }
    }

    /**
     * 获取购物车信息
     * @param userId
     * @return
     */
    public Map<String, List<ShoppingTrolleyRequestVo>> getShoppingTrolleyByUserId(String userId){
        Map<String, List<ShoppingTrolleyRequestVo>> map = new HashMap<>();
        List<String> sellerIds = productOrderService.getSellerIdByUserId(userId);
        for(String sellerId : sellerIds){
            List<ShoppingTrolleyRequestVo> list = new ArrayList<>();
            String sellerName = sellerService.getNameById(sellerId);
            List<String> orderIds = productOrderService.getProductIdByUserIdAndSellerId(userId, sellerId);
            for(String orderId : orderIds){
                ShoppingTrolleyRequestVo shoppingTrolleyRequestVo = new ShoppingTrolleyRequestVo();
                shoppingTrolleyRequestVo.setSellerId(sellerId);
                shoppingTrolleyRequestVo.setSellerName(sellerName);
                shoppingTrolleyRequestVo.setOrderId(orderId);
                ProductOrderItem productOrderItem = productOrderItemService.getProductOrderItemByOrderId(orderId);
                if(productOrderItem != null) {
                    shoppingTrolleyRequestVo.setPrice(new BigDecimal(Money.getMoneyString(productOrderItem.getPrice())));
                    shoppingTrolleyRequestVo.setProductSpeId(productOrderItem.getSpeId());
                    shoppingTrolleyRequestVo.setQuantity(productOrderItem.getQuantity());
                    String speName = null;
                    if(productOrderItem.getSpeId() != null) {
                        speName = productSpe.getNameById(productOrderItem.getSpeId());
                    }
                    if (speName != null) {
                        shoppingTrolleyRequestVo.setProductSpeName(speName);
                    }
                    shoppingTrolleyRequestVo.setProductId(productOrderItem.getProductId());
                    Map<String, Object> map2 = new HashMap<>();
                    if(productOrderItem.getProductId() != null) {
                        map2 = productService.getNameAndProductImagesUrlById(productOrderItem.getProductId());
                    }
                    if(map2 != null && map2.size() > 0) {
                        shoppingTrolleyRequestVo.setProductName((String) map2.get("productName"));
                        shoppingTrolleyRequestVo.setDeliveryWay(new Integer(map2.get("deliveryWay").toString()));
                        shoppingTrolleyRequestVo.setDelivery(Boolean.getBoolean(map2.get("isDelivery").toString()));
                        shoppingTrolleyRequestVo.setReturn(Boolean.getBoolean(map2.get("isReturn").toString()));
                        String productImagesUrls[] = ((String) map2.get("productImagesUrl")).split(",");
                        if(productImagesUrls.length > 0) {
                            shoppingTrolleyRequestVo.setProductImagesUrl(productAction.updateImnagesUrl(productImagesUrls[0]));
                        }
                    }
                }
                list.add(shoppingTrolleyRequestVo);

            }
            System.out.println(list);
            map.put(sellerName, list);
        }
        return map;
    }

    /**
     * 删除购物车里的订单
     * @param orderId
     * @return
     */
    public boolean delete(String orderId){
        return productOrderService.delete(orderId) && productOrderItemService.deleteByOrderId(orderId);
    }

    /**
     * 编辑购物车里的订单
     * @param orderId
     * @return
     */
    public boolean update(String orderId,int quanty){
        ProductOrderItem productOrderItem = productOrderItemService.getProductOrderItemByOrderId(orderId);
        productOrderItem.setQuantity(quanty);
        ProductOrder productOrder = productOrderService.selectById(orderId);
        productOrder.setTotalPrice(productOrderItem.getPrice()*quanty);
        return productOrderService.updateById(productOrder) &&  productOrderItemService.updateById(productOrderItem);
    }

}