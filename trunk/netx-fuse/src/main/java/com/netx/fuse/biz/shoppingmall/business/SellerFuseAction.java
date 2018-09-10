package com.netx.fuse.biz.shoppingmall.business;

import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.common.enums.MessageTypeEnum;
import com.netx.common.common.enums.PushMessageDocTypeEnum;
import com.netx.common.redis.RedisInfoHolder;
import com.netx.common.router.dto.bean.UserInfoResponseDto;
import com.netx.common.router.dto.request.UserInfoRequestDto;
import com.netx.common.router.enums.SelectConditionEnum;
import com.netx.common.router.enums.SelectFieldEnum;
import com.netx.common.user.model.UserSynopsisData;
import com.netx.common.user.util.AddImgUrlPreUtil;
import com.netx.common.user.util.DateTimestampUtil;
import com.netx.common.user.util.VoPoConverter;
import com.netx.common.vo.business.*;
import com.netx.common.vo.common.BillStatisticsRequestDto;
import com.netx.common.vo.common.PageRequestDto;
import com.netx.common.vo.currency.GetCanCurrencyMessageResquesDto;
import com.netx.common.vo.currency.GetCurrencyMessageResponseVo;
import com.netx.fuse.biz.shoppingmall.order.ProductOrderFuseAction;
import com.netx.fuse.client.ucenter.WangMingClientAction;
import com.netx.fuse.proxy.MessagePushProxy;
import com.netx.fuse.proxy.UserClientProxy;
import com.netx.fuse.proxy.WalletBillProxy;
import com.netx.searchengine.SearchServiceProvider;
import com.netx.searchengine.model.ProductSearchResponse;
import com.netx.searchengine.model.SellerSearchResponse;
import com.netx.searchengine.query.SellerSearchQuery;
import com.netx.shopping.biz.business.*;
import com.netx.shopping.biz.order.ProductOrderAction;
import com.netx.shopping.biz.product.ProductAction;
import com.netx.shopping.enums.OrderStatusEnum;
import com.netx.shopping.enums.ProductStatusEnum;
import com.netx.shopping.enums.SellerStatusEnum;
import com.netx.shopping.model.business.Seller;
import com.netx.shopping.model.business.SellerCashier;
import com.netx.shopping.model.business.SellerFavorite;
import com.netx.shopping.model.business.SellerManage;
import com.netx.shopping.model.order.ProductOrder;
import com.netx.shopping.service.business.*;
import com.netx.shopping.service.order.ProductOrderService;
import com.netx.shopping.service.product.ProductPackageService;
import com.netx.shopping.service.product.ProductService;
import com.netx.shopping.service.product.ProductSpecService;
import com.netx.shopping.vo.*;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.service.user.UserService;
import com.netx.utils.DistrictUtil;
import com.netx.utils.cache.RedisCache;
import com.netx.utils.cache.RedisKeyName;
import com.netx.utils.cache.RedisTypeEnum;
import com.sun.javafx.collections.MappingChange;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.common.geo.GeoPoint;
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
public class SellerFuseAction {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Autowired
    ProductAction productAction;

    @Autowired
    SellerService sellerService;

    @Autowired
    UserClientProxy userClientProxy;

    @Autowired
    ProductOrderService productOrderService;

    @Autowired
    WalletBillProxy walletBillProxy;

    @Autowired
    WangMingClientAction wangMingClientAction;

    @Autowired
    SellerAction sellerAction;

    @Autowired
    ManageAction manageAction;

    @Autowired
    UserAction userAction;

    @Autowired
    ProductOrderAction productOrderAction;

    @Autowired
    RedpacketSendAction redpacketSendAction;

    @Autowired
    SellerCollectAction sellerCollectAction;

    @Autowired
    SellerCategoryService sellerCategoryService;

    @Autowired
    ManageService manageService;

    @Autowired
    MessagePushProxy messagePushProxy;

    @Autowired
    CashierService cashierService;

    @Autowired
    ProductService productService;

    @Autowired
    ProductPackageService productPackageService;

    @Autowired
    ProductSpecService productSpecService;

    @Autowired
    AddImgUrlPreUtil addImgUrlPreUtil;

    @Autowired
    SearchServiceProvider searchServiceProvider;

    @Autowired
    SellerCollectService sellerCollectService;

    @Autowired
    ProductOrderFuseAction productOrderFuseAction;

    @Autowired
    CashierAction cashierAction;

    @Autowired
    SellerCategoryAction sellerCategoryAction;

    RedisCache redisCache;

    @Autowired
    RedisInfoHolder redisInfoHolder;

    @Autowired
    PacketSetService packetSetService;

    private void createRedis(){
        redisCache = RedisCache.getRedisCache(redisInfoHolder.getHost(),redisInfoHolder.getPassword(),redisInfoHolder.getPort());
    }

    @Autowired
    UserService userService;

    @Autowired
    CategoryService categoryService;

    public boolean subtractCredit1(BusinessManagementRequestDto request) throws Exception{
        //查询商家表里是否有对应商家
        Seller seller = sellerService.getSeller(request.getSellerId(), SellerStatusEnum.NORMAL.getCode(),"0");
        if (seller == null) {
            throw new Exception("商家不存在");
        }

        //获取注册商家乐观锁
        UserInfoRequestDto userInfoDto = new UserInfoRequestDto();
        userInfoDto.setSelectConditionEnum(SelectConditionEnum.USER_ID);
        userInfoDto.setSelectData(seller.getUserId());
        List<SelectFieldEnum> list = new ArrayList<>();
        list.add(SelectFieldEnum.NICKNAME);
        list.add(SelectFieldEnum.LOCK_VERSION);
        userInfoDto.setSelectFieldEnumList(list);
        UserInfoResponseDto responseDto = userClientProxy.selectUserInfo(userInfoDto);
        if (responseDto == null) {
            throw new Exception("获取乐观锁失败！");
        }

        //获取当前时间戳
        long now = new Date().getTime();
        //获取90天前同一时间戳
        long old = DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(new Date().getTime(), 90, 1);

        //根据商家id查询90天内订单成交记录
        Integer i = productOrderService.getProductOrderCount(seller.getId(), OrderStatusEnum.COMPLETED.getCode(),new Date(old), new Date());

        SellerCashier cashier = cashierAction.selectSellerCashierBySellerId(seller.getId());
        if (cashier == null){
            throw new Exception("获取收银人员信息失败");
        }
        String moneyUserId= productOrderFuseAction.getUserIdByNetworkNum(cashier.getMoneyNetworkNum());
        //根据商家收银人员网号获取收银人员userId
        userInfoDto.setSelectConditionEnum(SelectConditionEnum.USER_NUMBER);
        userInfoDto.setSelectData(moneyUserId);
        List<SelectFieldEnum> list1 = new ArrayList<>();
        list.add(SelectFieldEnum.USER_ID);
        userInfoDto.setSelectFieldEnumList(list1);
        UserInfoResponseDto responseDto1 = userClientProxy.selectUserInfo(userInfoDto);
        if (responseDto1 == null) {
            throw new Exception("获取收银人员userId失败！");
        }

        //查询90天内商家收银人员支付记录
        BillStatisticsRequestDto billQueryRequestDto = new BillStatisticsRequestDto();
        billQueryRequestDto.setQueryType(1);
        billQueryRequestDto.setStartTime(old);
        billQueryRequestDto.setEndTime(now);
        billQueryRequestDto.setUserId(responseDto1.getUserId());
        BigDecimal bigDecimal = walletBillProxy.queryBillAmountList(billQueryRequestDto);

        //判断商家90天内是否无成交记录或交易记录或支付记录定时器，否扣减注册商家者信用值5分
        if (i < 1 && bigDecimal.intValue() == 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", seller.getUserId());
            map.put("lookVersion", responseDto.getLockVersion());
            map.put("relatableType", Seller.class.getSimpleName());
            map.put("relatableId", "0");
            map.put("credit", -5);
            map.put("description", "90天内无成交记录或交易记录或支付记录，每90天扣减信用值5分");
            if(!wangMingClientAction.addCreditRecord(map)){
                logger.error("扣除5积分失败!");
            }
        }

        //获取90天后同一时间戳
        long start = DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(new Date().getTime(), 90, 2);
        //开启90天后定时器
//        businessQuartzService.autoBusinessManagement1(seller.getUserId(), start);
        //TODO 定时任务
        return true;
    }

    public GetSellerResponseVo get(GetSellerRequestDto request,double lat,double lot) throws Exception{
        GetSellerResponseVo result = new GetSellerResponseVo();
        if (!StringUtils.isNotEmpty(request.getId())) {
            return result;
        }
        Seller seller = sellerService.getSeller(request.getId());//商家对象
        if (seller != null) {
            seller = sellerAction.updateImages(seller);
            VoPoConverter.copyProperties(seller, result);
        }
        SellerManage manage = manageAction.getManage(seller.getManageId());
        if (manage != null) {
            result.setManageUserId(userAction.selectUserByUserNumber(manage.getManageNetworkNum())==null?null:userAction.selectUserByUserNumber(manage.getManageNetworkNum()).getUserId());
        }
        SellerCashier cashier = cashierAction.selectById(seller.getSellerCashierId());
        if (cashier != null) {
            result.setCashierUserId(userAction.selectUserByUserNumber(cashier.getMoneyNetworkNum())==null?null:userAction.selectUserByUserNumber(cashier.getMoneyNetworkNum()).getUserId());
            result.setMoneyNikName(userAction.selectUserByUserNumber(cashier.getMoneyNetworkNum())==null?null:userAction.selectUserByUserNumber(cashier.getMoneyNetworkNum()).getNickname());
        }
        result.setOrderNum(sellerAction.getOrderNumBySellerId(request.getId()));
        result.setGoodsNum(sellerAction.getGoodsNumBySellerId(request.getId()));
        result.setSumOrderAmount(productOrderAction.getSumOrderAmountBySellerId(request.getId())==null?null:productOrderAction.getSumOrderAmountBySellerId(request.getId()));

        //返回今日红包总额
        BigDecimal dto = redpacketSendAction.getLastDayAmount();
        if (dto == null) {
            result.setPacketPoolAmount(0L);
        } else {
            result.setPacketPoolAmount(dto.longValue());
        }

        SellerFavorite sellerCollect = new SellerFavorite();
        sellerCollect.setSellerId(request.getId());
        sellerCollect.setUserId(request.getUserId());
        if (sellerCollectAction.isHaveCollect(sellerCollect) != null) {
            result.setIsHaveCollect(1);
        } else {
            result.setIsHaveCollect(2);
        }
//
//        SellerCategoryVo sellerCategoryVo = sellerCategoryAction.getSellerCategoryVo(seller.getId());
//        result.setCategoryId(sellerCategoryVo.getCategories()==null?null:sellerCategoryVo.getCategories());
//        result.setTagIds(sellerCategoryVo.getTages()==null?null:sellerCategoryVo.getTages());
        //返回距离
        result.setDistance(this.getDistance(lat,lat,seller.getLat(),seller.getLon()));
        //修改访问量
        Seller updateSeller = new Seller();
        updateSeller.setId(seller.getId());
        updateSeller.setVisitNum(seller.getVisitNum() == null ? 0 : seller.getVisitNum() + 1);
        sellerService.updateById(updateSeller);
        return result;
    }

    public double getDistance(double lat,double lon,BigDecimal slat,BigDecimal slon){
        if (slat != null && slon != null) {
            double distance = DistrictUtil.calcDistance(new BigDecimal(lat), new BigDecimal(lon), slat, slon);
            //保留小数点后两位小数
            String dstr = String.valueOf(distance);
            BigDecimal bd = new BigDecimal(dstr);
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            //判断值大于999.99默认为999.99
            if (bd.compareTo(new BigDecimal(999.99)) > 0) {
                bd = new BigDecimal(999.99);
            }
            return (bd.doubleValue());
        }
        return 0;
    }

    public boolean cancelBuyUserId(String userId) throws Exception{
        //获取用户相关信息
        UserInfoRequestDto userInfoDto = new UserInfoRequestDto();
        userInfoDto.setSelectConditionEnum(SelectConditionEnum.USER_ID);
        userInfoDto.setSelectData(userId);
        List<SelectFieldEnum> list = new ArrayList<>();
        list.add(SelectFieldEnum.USER_NUMBER);
        userInfoDto.setSelectFieldEnumList(list);
        UserInfoResponseDto responseDto = userClientProxy.selectUserInfo(userInfoDto);
        if (StringUtils.isEmpty(responseDto.getUserNumber())){
            throw new Exception("查询用户网号失败！");
        }

        //注销身份作为顾客的相关数据
        //订单注销
        ProductOrder productOrder = new ProductOrder();
        productOrder.setDeleted(1);
        productOrderService.updateProductOrder(productOrder,userId);

        //注销身份作为商家相关人员信息
        //注销商家
        Seller seller = new Seller();
        seller.setDeleted(1);
        seller.setUpdateUserId(userId);
        sellerService.updateSeller(seller,userId);

        //注销主管
        SellerManage manage = new SellerManage();
        manage.setDeleted(1);
        manage.setUpdateUserId(userId);
        manageService.updateManage(manage,userId);

        //注销用户作为别人商家的主管的主管信息
        List<SellerManage> manages = manageService.getManageList(responseDto.getUserNumber(),userId);
        if (manages.size() > 0) {
            for (SellerManage manage1 : manages) {
                //删除对应商家的manageId
                Seller seller1 = new Seller();
                seller1.setManageId("已注销");
                seller1.setUpdateUserId(userId);
                sellerService.updateSellerByManageId(seller1, manage1.getId());
                //推送信息给商家注册者，提示商家主管的用户已撤销，请重新编辑商家主管信息
                MessageFormat mf1 = new MessageFormat("您注册的商家的业务主管人员关联的网号：{0}的用户已注销，请重新填写主管信息，避免造成影响");
                String alertMsg1 = mf1.format(new String[]{responseDto.getUserNumber()});
                messagePushProxy.messagePushJump(MessageTypeEnum.PRODUCT_TYPE,alertMsg1, "注销通知", manage1.getUserId(), PushMessageDocTypeEnum.GoodsOrderDetail, userId);
            }
            manageService.updateManage(manage, responseDto.getUserNumber(),userId);
        }

        //注销收银人
        SellerCashier cashier = new SellerCashier();
        cashier.setDeleted(1);
        cashier.setUpdateUserId(userId);
        cashierService.updateCashier(cashier,userId);

        //注销作为别人商家的收银人的收银信息
        List<Seller> sellerList = sellerService.getSellerList(responseDto.getUserNumber(),userId);
        if (sellerList.size() > 0) {
            for (Seller sellers : sellerList) {
                //删除对应商家的收银人信息
                Seller seller1 = new Seller();
                seller1.setId(sellers.getId());
                seller1.setSellerCashierId("已注销");
                seller1.setUpdateUserId(userId);
                sellerService.updateById(seller1);

                //推送信息给商家注册者，提示商家收银人的用户已撤销，请重新编辑商家收银人信息
                MessageFormat mf1 = new MessageFormat("您注册的商家的收银人员关联的网号：{0}的用户已注销，请重新填写收银人员信息，避免造成影响");
                String alertMsg1 = mf1.format(new String[]{responseDto.getUserNumber()});
                messagePushProxy.messagePushJump(MessageTypeEnum.PRODUCT_TYPE,alertMsg1, "注销通知", sellers.getUserId(), PushMessageDocTypeEnum.GoodsOrderDetail, userId);
            }
            cashierService.updateCashierByMoneyNetworkNum(cashier,responseDto.getUserNumber());
        }

        //注销商品
        productService.deleteByUserId(userId);

        //注销商品包装明细
        productPackageService.deleteByUserId(userId);

        //注销商品商品规格
        productSpecService.deleteByUserId(userId);

        return true;
    }

    public List<String> getSellerAllUserId(String sellerId){
        List<String> userIds = new ArrayList<>();
        Seller seller = sellerService.selectById(sellerId);
        if (seller != null) {
            UserInfoRequestDto userInfoRequestDto = new UserInfoRequestDto();
            userInfoRequestDto.setSelectConditionEnum(SelectConditionEnum.USER_NUMBER);//设计查询条件
            List<SelectFieldEnum> selectFieldEnumList = new ArrayList<>();//设置查询返回结果
            selectFieldEnumList.add(SelectFieldEnum.USER_ID);
            userInfoRequestDto.setSelectFieldEnumList(selectFieldEnumList);

            //获取注册UserId
            userIds.add(seller.getUserId());

            //获取主管UserId
            SellerManage manage = manageService.selectById(seller.getManageId());
            if (manage != null) {
                userInfoRequestDto.setSelectData(manage.getManageNetworkNum());//输入查询数据
                UserInfoResponseDto responseDto = userClientProxy.selectUserInfo(userInfoRequestDto);
                if (responseDto == null) {
                    userIds.add("");
                }
                userIds.add(responseDto.getUserId());
            } else {
                userIds.add("");
            }

            //获取收银人员UserId
            SellerCashier cashier = cashierAction.selectById(seller.getSellerCashierId());
            if (cashier.getMoneyNetworkNum() != null) {
                userInfoRequestDto.setSelectData(cashier.getMoneyNetworkNum());//输入查询数据
                UserInfoResponseDto responseDto = userClientProxy.selectUserInfo(userInfoRequestDto);
                if (responseDto == null) {
                    userIds.add("");
                }
                userIds.add(responseDto.getUserId());
            } else {
                userIds.add("");
            }
        }

        return userIds;
    }

    public List<GetSellerListResponseVo> sellerList(BackManageSellerListRequestDto request){
        List<GetSellerListResponseVo> list = new ArrayList<GetSellerListResponseVo>();
        List<Seller> resList = new ArrayList<>();
        Page<Seller> resultPage = sellerService.getPageList(request);
        resList = resultPage.getRecords();
        for (Seller seller : resList) {
            GetSellerListResponseVo getSellerListResponseVo = new GetSellerListResponseVo();
            seller = sellerAction.updateImages(seller);
            BeanUtils.copyProperties(seller, getSellerListResponseVo);
            //String categoryName = sellerCategoryAction.getCategoryName(seller.getId())==null?null:sellerCategoryAction.getCategoryName(seller.getId());
            /*getSellerListResponseVo.setCategoryName(categoryName);

            getSellerListResponseVo.setOrderNum(sellerAction.getOrderNumBySellerId(seller.getId()));
            getSellerListResponseVo.setGoodsNum(sellerAction.getGoodsNumBySellerId(seller.getId()));
            getSellerListResponseVo.setSuccessNum(sellerAction.getSuccessNum(seller.getId()));
            getSellerListResponseVo.setSumOrderAmount(productOrderAction.getSumOrderAmountBySellerId(seller.getId()));*/

            //根据userId查询用户昵称、等级、性别
            UserInfoResponseDto userInfoResponseDto = new UserInfoResponseDto();
            UserInfoRequestDto userInfoRequestDto = new UserInfoRequestDto();
            userInfoRequestDto.setSelectData(seller.getUserId());//输入查询数据
            userInfoRequestDto.setSelectConditionEnum(SelectConditionEnum.USER_ID);//设计查询条件
            List<SelectFieldEnum> selectFieldEnumList = new ArrayList<>();//设置查询返回结果
            selectFieldEnumList.add(SelectFieldEnum.LV);
            selectFieldEnumList.add(SelectFieldEnum.NICKNAME);
            selectFieldEnumList.add(SelectFieldEnum.SEX);
            selectFieldEnumList.add(SelectFieldEnum.CREDIT);
            userInfoRequestDto.setSelectFieldEnumList(selectFieldEnumList);
            userInfoResponseDto = userClientProxy.selectUserInfo(userInfoRequestDto);//跨域查询
            /*getSellerListResponseVo.setNickname(userInfoResponseDto == null ? null : userInfoResponseDto.getNickname());
            getSellerListResponseVo.setLv(userInfoResponseDto == null ? null : userInfoResponseDto.getLv());
            getSellerListResponseVo.setSex(userInfoResponseDto == null ? null : userInfoResponseDto.getSex());
            getSellerListResponseVo.setCredit(userInfoResponseDto == null ? null : userInfoResponseDto.getCredit());
            getSellerListResponseVo.setAge(userInfoResponseDto == null ? null : userInfoResponseDto.getAge());*/

            //获取商家上下架操作者昵称
            if (seller.getHandlersId() != null) {
                UserInfoRequestDto userInfoRequestDto1 = new UserInfoRequestDto();
                userInfoRequestDto.setSelectData(seller.getHandlersId());//输入查询数据
                userInfoRequestDto.setSelectConditionEnum(SelectConditionEnum.USER_ID);//设计查询条件
                List<SelectFieldEnum> selectFieldEnumList1 = new ArrayList<>();//设置查询返回结果

                selectFieldEnumList1.add(SelectFieldEnum.NICKNAME);

                userInfoRequestDto.setSelectFieldEnumList(selectFieldEnumList);
                UserInfoResponseDto userInfoResponseDto1 = userClientProxy.selectUserInfo(userInfoRequestDto);//跨域查询
//                getSellerListResponseVo.setHandlersNikname(userInfoRequestDto1 == null ? null : userInfoResponseDto1.getNickname());
            }


            //获取上商家剩余未兑付网币金额
            list.add(getSellerListResponseVo);

        }
        return list;
    }

    public boolean subtractCredit(BusinessManagementRequestDto request) throws Exception{
        //查询商家表里是否有对应商家
        Seller seller =  sellerService.getSeller(request.getSellerId(),SellerStatusEnum.NORMAL.getCode(),"0");
        if (seller == null) {
            throw new Exception("商家不存在");
        }

        //获取注册商家乐观锁
        UserInfoRequestDto userInfoDto = new UserInfoRequestDto();
        userInfoDto.setSelectConditionEnum(SelectConditionEnum.USER_ID);
        userInfoDto.setSelectData(seller.getUserId());
        List<SelectFieldEnum> list = new ArrayList<>();
        list.add(SelectFieldEnum.NICKNAME);
        list.add(SelectFieldEnum.LOCK_VERSION);
        userInfoDto.setSelectFieldEnumList(list);
        UserInfoResponseDto responseDto = userClientProxy.selectUserInfo(userInfoDto);
        if (responseDto == null) {
            logger.error("获取乐观锁失败!");
        }

        //根据商家Id判断商家是否有发布商品，如果否扣减信用值5分
        Integer integer = productService.getProductCountBySellerIdAndStatus( request.getSellerId(), ProductStatusEnum.UP.getCode());

        //扣减商家信用5分
        if (integer < 1) {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", seller.getUserId());
            map.put("lookVersion", responseDto.getLockVersion());
            map.put("relatableType", Seller.class.getSimpleName());
            map.put("relatableId", "0");
            map.put("credit", -5);
            map.put("description", "商家注册后30天未发货，扣减信用值5分");
            if(!wangMingClientAction.addCreditRecord(map)){
                logger.error("扣除5积分失败!");
            }
        }

        //获取当前时间30天后时间戳
        long start = DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(new Date().getTime(), 30, 2);
        //开启30天后判断商家是否有发布商品的定时器
//        businessQuartzService.autoBusinessManagement(seller.getUserId(), start);
        //TODO 定时任务
        return true;
    }

    private List<GetSellerListResponseVo> queryBusiness(SellerSearchQuery searchQuery){
        List<SellerSearchResponse> sellerSearchResponses = searchServiceProvider.getSellerSearchService().querySellers(searchQuery);
        List<GetSellerListResponseVo> sellerListResponseVos = new ArrayList<>();
        for (SellerSearchResponse searchResponse: sellerSearchResponses) {
            GetSellerListResponseVo getSellerListResponseVo = new GetSellerListResponseVo();
            VoPoConverter.copyProperties(searchResponse, getSellerListResponseVo);
            List<String> logoImagesUrl = searchResponse.getLogoImagesUrl();
            if(logoImagesUrl != null && logoImagesUrl.size()>0){
                getSellerListResponseVo.setLogoImagesUrl(productAction.updateImnagesUrl(logoImagesUrl.get(0)));
            }
            List<String> categoryNames = searchResponse.getCategoryNames();
            List<String> categoryPids = searchResponse.getCategoryParentIds();
            List<String> tages = new ArrayList<>();
            List<String> categorys = new ArrayList<>();
            if(categoryNames != null && categoryPids != null && categoryNames.size() > 0 && categoryPids.size() > 0){
                for(int i=0; i<categoryPids.size(); i++){
                    if("0".equals(categoryPids.get(i))){
                        tages.add(categoryNames.get(i));
                    }else{
                        categorys.add(categoryNames.get(i));
                    }
                }
            }
            getSellerListResponseVo.setTages(new ArrayList(new HashSet(tages)));
            getSellerListResponseVo.setCategories(new ArrayList(new HashSet(categorys)));
            sellerListResponseVos.add(getSellerListResponseVo);
        }

        return sellerListResponseVos;
    }

    public List<GetSellerListResponseVo> sellerListByDealAmount(PageRequestDto request,BigDecimal lat,BigDecimal lon){

        List<GetSellerListResponseVo> resultList = new ArrayList<>();

        Integer current = (request.getCurrent() - 1) * request.getSize();
        List<String> sellerIds = productOrderService.getSellerIds(current, request.getSize());
        if (sellerIds.size() > 0) {
            for (String id : sellerIds) {
                Seller seller = sellerAction.getSellerAndChangeSellerImageUrl(id);
                GetSellerListResponseVo getSellerListResponseVo = new GetSellerListResponseVo();
                BeanUtils.copyProperties(seller, getSellerListResponseVo);
                //String categoryName =  sellerCategoryAction.getCategoryName(seller.getId())==null?null:sellerCategoryAction.getCategoryName(seller.getId());
                /*getSellerListResponseVo.setCategoryName(categoryName);
                getSellerListResponseVo.setGoodsNum(sellerAction.getGoodsNumBySellerId(seller.getId()));
                getSellerListResponseVo.setSuccessNum(sellerAction.getSuccessNum(seller.getId()));
                getSellerListResponseVo.setSumOrderAmount(productOrderAction.getSumOrderAmountBySellerId(seller.getId()));*/

                //根据userId查询用户昵称、等级、性别
                UserInfoResponseDto userInfoResponseDto = new UserInfoResponseDto();
                UserInfoRequestDto userInfoRequestDto = new UserInfoRequestDto();
                userInfoRequestDto.setSelectData(seller.getUserId());//输入查询数据
                userInfoRequestDto.setSelectConditionEnum(SelectConditionEnum.USER_ID);//设计查询条件
                List<SelectFieldEnum> selectFieldEnumList = new ArrayList<>();//设置查询返回结果
                selectFieldEnumList.add(SelectFieldEnum.LV);
                selectFieldEnumList.add(SelectFieldEnum.NICKNAME);
                selectFieldEnumList.add(SelectFieldEnum.SEX);
                selectFieldEnumList.add(SelectFieldEnum.CREDIT);
                userInfoRequestDto.setSelectFieldEnumList(selectFieldEnumList);
                userInfoResponseDto = userClientProxy.selectUserInfo(userInfoRequestDto);//跨域查询
                /*getSellerListResponseVo.setNickname(userInfoResponseDto == null ? null : userInfoResponseDto.getNickname());
                getSellerListResponseVo.setLv(userInfoResponseDto == null ? null : userInfoResponseDto.getLv());
                getSellerListResponseVo.setSex(userInfoResponseDto == null ? null : userInfoResponseDto.getSex());
                getSellerListResponseVo.setCredit(userInfoResponseDto == null ? null : userInfoResponseDto.getCredit());
                getSellerListResponseVo.setAge(userInfoResponseDto == null ? null : userInfoResponseDto.getAge());*/

                //判断是否要计算用户与商家的距离
                if (lat != null && lon != null) {
                    if (seller.getLat() != null && seller.getLon() != null) {
                        double distance = DistrictUtil.calcDistance(lat, lon, seller.getLat(), seller.getLon());
                        //保留小数点后两位小数
                        String dstr = String.valueOf(distance);
                        BigDecimal bd = new BigDecimal(dstr);
                        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                        //判断值大于999.99默认为999.99
                        if (bd.compareTo(new BigDecimal(999.99)) > 0) {
                            bd = new BigDecimal(999.99);
                        }
                        getSellerListResponseVo.setDistance(bd.doubleValue());
                    }
                }

                resultList.add(getSellerListResponseVo);
            }
        }


        return resultList;
    }

    public void messagePush(String sellerId){
        Seller seller = sellerService.selectById(sellerId);
        //向注册者、法人、收银推送信息
        MessageFormat messageFormat = new MessageFormat("{0}商家的注册有效期将于{1}  24:00时到期，请及时续费，以免影响正常使用");
        //获取法人代表和收银人员userId
        UserInfoRequestDto userInfoRequestDto = new UserInfoRequestDto();

        userInfoRequestDto.setSelectData(seller.getVerifyNetworkNum());//输入查询数据
        userInfoRequestDto.setSelectConditionEnum(SelectConditionEnum.USER_NUMBER);//设计查询条件
        List<SelectFieldEnum> selectFieldEnumList = new ArrayList<>();//设置查询返回结果
        selectFieldEnumList.add(SelectFieldEnum.USER_ID);
        userInfoRequestDto.setSelectFieldEnumList(selectFieldEnumList);
        UserInfoResponseDto userInfoResponseDto = userClientProxy.selectUserInfo(userInfoRequestDto);//跨域查询
        String verifyUserId = userInfoResponseDto.getUserId()==null?"":userInfoResponseDto.getUserId();

        SellerCashier cashier = cashierAction.selectById(seller.getSellerCashierId());
        userInfoRequestDto.setSelectData(cashier.getMoneyNetworkNum());//输入查询数据
        userInfoRequestDto.setSelectConditionEnum(SelectConditionEnum.USER_NUMBER);//设计查询条件
        userInfoRequestDto.setSelectFieldEnumList(selectFieldEnumList);
        UserInfoResponseDto userInfoResponseDto1 = userClientProxy.selectUserInfo(userInfoRequestDto);//跨域查询
        String moneyUserId = userInfoResponseDto1.getUserId()==null?"":userInfoResponseDto1.getUserId();

        //获取10天后的日期
        long start = DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(new Date().getTime(), 10, 2);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String tenDateAfter = simpleDateFormat.format(start);
        String alertMsg = messageFormat.format(new String[]{seller.getName(), tenDateAfter});

        //去重后发推送信息
        List<String> list = new ArrayList<>();
        list.add(seller.getUserId());
        list.add(verifyUserId);
        list.add(moneyUserId);
        List<String> list1 = sellerAction.getUntqueuUserId(list);
        for (String string : list1) {
            sendMessage(alertMsg, string, seller.getId());
        }

        //启动10天后修改商家缴费状态
        //获取10天后的日期24:00时间戳
        Long l = DateTimestampUtil.getStartOrEndOfTimestamp(start, 2);
        //启动定时器，是否要更改商家缴费状态
//        businessQuartzService.changeSellerPayStatus(sellerId, l);
        //TODO  定时任务
    }

    public void changeSellerPayStatus(String sellerId){
        Seller seller = sellerService.selectById(sellerId);
        //判断商家在10天内是否有缴费
        //获取10天前时间
        long tenDayAgo = DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(new Date().getTime(), 10, 1);
        if (seller.getFeeTime().getTime() < tenDayAgo)//没有
        {
            seller.setPayStatus(2);
            sellerService.updateById(seller);
            //向注册者、法人、收银推送信息
            MessageFormat messageFormat = new MessageFormat("{0}商家的注册有效期已到期，不能再生成订单，请在{1}前完成缴费，逾期未缴，商家自动下架");
            //获取法人代表和收银人员userId
            UserInfoRequestDto userInfoRequestDto = new UserInfoRequestDto();

            userInfoRequestDto.setSelectData(seller.getVerifyNetworkNum());//输入查询数据
            userInfoRequestDto.setSelectConditionEnum(SelectConditionEnum.USER_NUMBER);//设计查询条件
            List<SelectFieldEnum> selectFieldEnumList = new ArrayList<>();//设置查询返回结果
            selectFieldEnumList.add(SelectFieldEnum.USER_ID);
            userInfoRequestDto.setSelectFieldEnumList(selectFieldEnumList);
            UserInfoResponseDto userInfoResponseDto = userClientProxy.selectUserInfo(userInfoRequestDto);//跨域查询
            String verifyUserId = userInfoResponseDto.getUserId()==null?"":userInfoResponseDto.getUserId();

            SellerCashier cashier = cashierAction.selectById(seller.getSellerCashierId());
            userInfoRequestDto.setSelectData(cashier.getMoneyNetworkNum());//输入查询数据
            userInfoRequestDto.setSelectConditionEnum(SelectConditionEnum.USER_NUMBER);//设计查询条件
            userInfoRequestDto.setSelectFieldEnumList(selectFieldEnumList);
            UserInfoResponseDto userInfoResponseDto1 = userClientProxy.selectUserInfo(userInfoRequestDto);//跨域查询
            String moneyUserId = userInfoResponseDto1.getUserId()==null?"":userInfoResponseDto1.getUserId();

            //获取10天后的日期
            long start = DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(new Date().getTime(), 10, 2);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String tenDateAfter = simpleDateFormat.format(start);
            String alertMsg = messageFormat.format(new String[]{seller.getName(), tenDateAfter});

            //去重后发推送信息
            List<String> list = new ArrayList<>();
            list.add(seller.getUserId());
            list.add(verifyUserId);
            list.add(moneyUserId);
            List<String> list1 = sellerAction.getUntqueuUserId(list);
            for (String string : list1) {
                sendMessage(alertMsg, string, seller.getId());
            }

            //定时器，10天后是否更改商家状态并在接下来这10天内交了续费，启动下一次定时器发送信息提醒商家续缴费
//            businessQuartzService.changeSellerStatus(sellerId, start);
            //TODO  定时任务
        } else {
            //定时任务，下次到期前10天的提醒商家交费提醒信息
            //获取下次缴费前10天时间戳
            Integer i = seller.getEffectiveTime();
            if (i == 0)//终身
            {

                logger.info("终身");
            } else if (i == 1)//1年
            {
                long start = DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(new Date().getTime(), 355, 2);
//                businessQuartzService.messagePush(sellerId, start);
                //TODO 定时任务

            } else {//3年
                long start = DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(new Date().getTime(), 365 * 3 - 10, 2);
//                businessQuartzService.messagePush(sellerId, start);

                //TODO 定时任务

            }
        }
    }

    public Seller manageFee(SellerManageFeeRequestDto request){
        Seller seller = sellerService.selectById(request.getSellerId());
        if (seller == null) {
            return null;
        }
        seller.setId(request.getSellerId());
        seller.setEffectiveTime(request.getEffectiveTime());
        seller.setPayStatus(0);
        seller.setFeeTime(new Date());
        seller.setStatus(SellerStatusEnum.NORMAL.getCode());
        Integer effectiveTime = request.getEffectiveTime();
        seller.setEndTime(sellerAction.createEndTime(new Date(), effectiveTime));
        sellerService.updateById(seller);

        //启动商家缴费到期前10天提醒续缴费定时器

        if (effectiveTime == 0)//终身
        {
            logger.info("终身");
        } else {
            long start = DateTimestampUtil.addDateStartOrEndOfDate(new Date(), 1, -10, 0, effectiveTime).getTime();
//            businessQuartzService.messagePush(request.getSellerId(), start);
            //TODO 定时任务
        }

        //启动商家管理定时任务，注册成功后30天不发商品扣减信用值，90天无成交记录扣减信用值
        //获取当前时间30天后时间戳
        long start = DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(new Date().getTime(), 30, 2);
        //开启30天后判断商家是否有发布商品的定时器
//        businessQuartzService.autoBusinessManagement(seller.getUserId(), start);
        //TODO 定时任务

        //获取90天后同一时间戳
        long start1 = DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(new Date().getTime(), 90, 2);
        //开启90天后定时器
//        businessQuartzService.autoBusinessManagement1(seller.getUserId(), start1);
        //TODO 定时任务

        return sellerService.selectById(request.getSellerId());

    }

    public void changSellerStatus(String sellerId){
        //判断商家缴费状态是否已经缴费
        Seller seller = sellerService.selectById(sellerId);
        if (seller.getPayStatus() != 0) {//修改商家状态为下架
            seller.setStatus(SellerStatusEnum.BACK.getCode());
            sellerService.updateById(seller);
            //获取商家对应所有商品并下架
            productService.downSellerAllGoods(sellerId);

        } else //启动下一次定时器发送信息提醒商家续缴费
        {
            //获取下次缴费前10天时间戳
            Integer i = seller.getEffectiveTime();
            //获取缴费时间戳
            long feeTime = seller.getFeeTime().getTime() * 1000;
            if (i == 0)//终身
            {

                logger.info("终身");
            } else if (i == 1)//1年
            {
                long start = DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(feeTime, 355, 2);
//                businessQuartzService.messagePush(sellerId, start);
                //TODO 定时任务
            } else {//3年
                long start = DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(feeTime, 365 * 3 - 10, 2);
//                businessQuartzService.messagePush(sellerId, start);
                //TODO 定时任务
            }
        }

    }

    public void sendMessage(String alertMsg, String userId, String sellerId) {
        if (userId != null) {
            messagePushProxy.messagePushJump(MessageTypeEnum.PRODUCT_TYPE,alertMsg, "注册管理费提醒通知", userId, PushMessageDocTypeEnum.GoodsOrderDetail, sellerId);
        }
    }

    public List<GetSellerListResponseVo> list(GetSellerListRequestDto request, double lat, double lon){
        return queryBusiness(sellerAction.getSellerSearchQuery(request,lat,lon));
    }


    public List<GetSellerListResponseVo> searchSeller(SearchSelleRequestDto requestDto){
        // 构造商家搜索条件
        SellerSearchQuery sellerSearchQuery = new SellerSearchQuery();

        if (requestDto.getLat() != null && requestDto.getLon() != null){
            String geoHash = DistrictUtil.encodeLatLon(requestDto.getLat(), requestDto.getLon());
            GeoPoint geoPoint=GeoPoint.fromGeohash(geoHash);
            sellerSearchQuery.setCenterGeoPoint(geoPoint);
        }
        sellerSearchQuery.setMaxDistance(requestDto.getDistance());
        return queryBusiness(sellerSearchQuery);
    }


    /**
     * 根据用户ID获取他最新经营的商家 （只查询返回一条数据）
     */
    public GetSellerByUserIdVo getManageSellerByUserId(String userId) throws Exception{
        //获取与用户有关联的商家id
        String sellerIds = productOrderFuseAction.getRelatedSellersNotMoneyUserByUserId(userId);
        if (StringUtils.isEmpty(sellerIds)) {
            return new GetSellerByUserIdVo();
        }
        Seller manageSeller = sellerService.getSellerBySellerIds(sellerIds);
        if (manageSeller != null) {
            GetSellerByUserIdVo manage = sellerAction.getSeller(manageSeller.getId());
            return manage;
        }
        return new GetSellerByUserIdVo();
    }

    public GetRelatedSellerMessageResponseVo getMessage(String userId) throws Exception{
        GetRelatedSellerMessageResponseVo result = new GetRelatedSellerMessageResponseVo();

        //获取有关联的商家
        String sellerIds = productOrderFuseAction.getRelatedSellersNotMoneyUserByUserId(userId);
        if (StringUtils.isEmpty(sellerIds)) {
            result.setManageSellers(0);
        } else {
            result.setManageSellers( sellerService.getSellerCount(sellerIds));
        }
        result.setCollectSellers(sellerCollectService.getSellerCollectCount(userId));

        return result;
    }


    public GetSellerByUserIdVo getSellerByUserId(GetSellerByUserIdRequestDto request) throws Exception{
        if (request.getIndex() == 3) {
            return sellerAction.getRegisterSellerByUserId(request.getUserId());
        }
        if (request.getIndex() == 2) {
            return sellerAction.getCollectSellerByUserId(request.getUserId());
        }
        if (request.getIndex() == 1) {
            return this.getManageSellerByUserId(request.getUserId());
        }
        return new GetSellerByUserIdVo();
    }

    public GetSellerListResponseVo newgetManageSellerByUserId(String userId){
        //根据userId获取经纬度
        User user = userAction.getUserService().selectById(userId);
        if (user != null){
            double lat = user.getLat().doubleValue();
            double lon = user.getLon().doubleValue();

            GetListByUserIdDto getListByUserIdDto = new GetListByUserIdDto();
            getListByUserIdDto.setUserId(userId);
            getListByUserIdDto.setStatus(1);
            getListByUserIdDto.setCurrent(1);
            getListByUserIdDto.setSize(1);

            List<GetSellerListResponseVo> list = getMyManageSeller(getListByUserIdDto,lat,lon);
            if (list != null && list.size() >0){
                return list.get(0);
            }
        }
        return new GetSellerListResponseVo();
    }

    /**
     * 当status=1根据用户id获取经营的商家
     * 当status=2根据用户id获取收藏的商家
     * @param getListByUserIdDto
     * @param lat
     * @param lon
     * @return
     */
    public List<GetSellerListResponseVo> getMyManageSeller(GetListByUserIdDto getListByUserIdDto, double lat,double lon){
        List<GetSellerListResponseVo> resList = new ArrayList<>();
        List<Seller> sellerList = null;
        if(getListByUserIdDto.getStatus() == 1) {
            sellerList = productOrderFuseAction.getMyManageSeller(getListByUserIdDto);
        }else if(getListByUserIdDto.getStatus() == 2){
            sellerList = productOrderFuseAction.getMyFavoriteSeller(getListByUserIdDto);
        }
        if (sellerList != null){
            for (Seller seller : sellerList){
                GetSellerListResponseVo getSellerListResponseVo = new GetSellerListResponseVo();
                seller = sellerAction.updateImages(seller);
                BeanUtils.copyProperties(seller, getSellerListResponseVo);
                BigDecimal firstRate = packetSetService.getFirstRate(seller.getId());
                if(firstRate != null){
                    getSellerListResponseVo.setFirstRate(firstRate);
                }
//                List<String> categoryIds = sellerCategoryService.getCategoryIdBySellerId(seller.getId());
//                if(categoryIds!=null && categoryIds.size()>0){
//                    List<String> categories = categoryService.getTagesByProductId(categoryIds,false);
//                    getSellerListResponseVo.setCategories(new ArrayList(new HashSet(categories)));
//                    List<String> tages = categoryService.getTagesByProductId(categoryIds,true);
//                    if(tages == null || tages.size()<=0){
//                        List<String> pids = categoryService.getPidByCategoryIds(categoryIds);
//                        if(pids != null && pids.size()>0) {
//                            tages = categoryService.getPidById(pids);
//                        }
//                    }
//                    getSellerListResponseVo.setTages(new ArrayList(new HashSet(tages)));
//                }
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
                    getSellerListResponseVo.setDistance(bd.doubleValue());
                }
                Map<String, Object> map = userService.getCreditAndNicknameByUserId(seller.getUserId());
                if(map != null && map.size() > 0){
                    getSellerListResponseVo.setCredit(new Integer(map.get("credit").toString()));
                    getSellerListResponseVo.setNickname(map.get("nickname").toString());
                }
                resList.add(getSellerListResponseVo);
            }
        }
        return resList;
    }

    public Map<String,Object> getSellerList(GetSellerWhiteRequestDto request){
        Page<Map<String, Object>> page = sellerService.getSellerList(request.getName(), request.getCurrentPage(), request.getSize(),request.getStatus());
        List<Map<String, Object>> records = page.getRecords();
        createRedis();
        if(records!=null && records.size()>0){
            for (Map<String,Object> map: records) {
                String userId = (String)map.get("userId");
                RedisKeyName redisKeyName = new RedisKeyName("userInfo", RedisTypeEnum.OBJECT_TYPE,userId);
                User user = (User) redisCache.get(redisKeyName.getUserKey());
                if(user!=null){
                    map.put("nickname",user.getNickname());
                    map.put("sex",user.getSex());
                    map.put("credit",user.getCredit());
                    map.put("lv",user.getLv());
                }else{
                    throw new RuntimeException("该商家注册者不存在");
                }
            }
        }
        Map map = new HashMap();
        map.put("total",page.getTotal());
        map.put("records",records);
        return map;
    }
}
