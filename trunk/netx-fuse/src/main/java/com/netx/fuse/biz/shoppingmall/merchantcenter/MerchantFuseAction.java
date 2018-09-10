package com.netx.fuse.biz.shoppingmall.merchantcenter;

import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.common.enums.JobEnum;
import com.netx.common.common.enums.MessageTypeEnum;
import com.netx.common.common.enums.PushMessageDocTypeEnum;
import com.netx.common.redis.RedisInfoHolder;
import com.netx.common.user.util.DateTimestampUtil;
import com.netx.common.user.util.VoPoConverter;
import com.netx.common.vo.business.GetRelatedSellerMessageResponseVo;
import com.netx.common.vo.business.GetSellerListRequestDto;
import com.netx.common.vo.business.GetSellerWhiteRequestDto;
import com.netx.common.vo.business.SelectSellerListByInPublicCreditDto;
import com.netx.common.vo.common.BillStatisticsRequestDto;
import com.netx.fuse.biz.job.JobFuseAction;
import com.netx.fuse.proxy.MessagePushProxy;
import com.netx.searchengine.SearchServiceProvider;
import com.netx.searchengine.model.SellerSearchResponse;
import com.netx.searchengine.query.SellerSearchQuery;
import com.netx.shopping.biz.merchantcenter.*;
import com.netx.shopping.biz.productcenter.ProductPictureAction;
import com.netx.shopping.biz.redpacketcenter.RedpacketSendAction;
import com.netx.shopping.enums.ProductStatusEnum;
import com.netx.shopping.enums.SellerStatusEnum;
import com.netx.shopping.model.merchantcenter.MerchantManager;
import com.netx.shopping.model.merchantcenter.MerchantPacketSet;
import com.netx.shopping.model.merchantcenter.constants.MerchantManagerEnum;
import com.netx.shopping.model.merchantcenter.constants.MerchantPictureEnum;
import com.netx.shopping.model.merchantcenter.Merchant;
import com.netx.shopping.model.merchantcenter.constants.MerchantStatusEnum;
import com.netx.shopping.model.ordercenter.constants.OrderStatusEnum;
import com.netx.shopping.service.merchantcenter.MerchantFavoriteService;
import com.netx.shopping.service.merchantcenter.MerchantManagerService;
import com.netx.shopping.service.merchantcenter.MerchantPacketSetService;
import com.netx.shopping.service.merchantcenter.MerchantService;
import com.netx.shopping.service.ordercenter.MerchantOrderInfoService;
import com.netx.shopping.service.productcenter.CategoryService;
import com.netx.shopping.service.productcenter.ProductService;
import com.netx.shopping.vo.*;
import com.netx.ucenter.biz.common.BillAction;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.ucenter.biz.user.UserCreditAction;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.service.user.UserService;
import com.netx.utils.DistrictUtil;
import com.netx.utils.cache.RedisCache;
import com.netx.utils.cache.RedisKeyName;
import com.netx.utils.cache.RedisTypeEnum;
import com.netx.utils.money.Money;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class MerchantFuseAction {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private MerchantManagerService merchantManagerService;
    @Autowired
    private ProductService productService;
    @Autowired
    private RedpacketSendAction redpacketSendAction;
    @Autowired
    private MerchantFavoriteService merchantFavoriteService;
    @Autowired
    private MerchantCategoryAction merchantCategoryAction;
    @Autowired
    private UserService userService;
    @Autowired
    private MerchantAction merchantAction;
    @Autowired
    private MerchantPictureAction merchantPictureAction;
    @Autowired
    private MessagePushProxy messagePushProxy;
    @Autowired
    private MerchantFavoriteAction merchantFavoriteAction;
    @Autowired
    private MerchantManagerAction merchantManagerAction;
    @Autowired
    private MerchantPacketSetService merchantPacketSetService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserCreditAction userCreditAction;
    @Autowired
    JobFuseAction jobFuseAction;
    @Autowired
    RedisInfoHolder redisInfoHolder;
    @Autowired
    BillAction billAction;
    @Autowired
    MerchantOrderInfoService merchantOrderInfoService;
    @Autowired
    SearchServiceProvider searchServiceProvider;
    @Autowired
    ProductPictureAction productPictureAction;
    @Autowired
    MerchantManagerFuseAction merchantManagerFuseAction;
    @Autowired
    MerchantRegisterAction merchantRegisterAction;
    @Autowired
    UserAction userAction;
    private RedisCache redisCache = null;

    private RedisCache clientRedis(){
        if(redisCache == null){
            redisCache = RedisCache.getRedisCache(redisInfoHolder.getHost(),redisInfoHolder.getPassword(),redisInfoHolder.getPort());
        }
        return redisCache;
    }
    /**
     * 商家详情
     * @param request
     * @param lat
     * @param lon
     * @return
     */
    public GetMerchantResponseVo get(GetMerchantRequestDto request, double lat, double lon){

        GetMerchantResponseVo result = new GetMerchantResponseVo();
        if (!StringUtils.isNotEmpty(request.getId())) {
            return result;
        }
        //商家信息
        Merchant merchant = merchantService.selectById(request.getId());
        if (merchant != null) {
            VoPoConverter.copyProperties(merchant, result);
        }
        //订单数量
        Integer orderNum = merchantOrderInfoService.getProductOrderCount(merchant.getId(), OrderStatusEnum.OS_FINISH, null, null);
        if(orderNum != null){
            result.setOrderNum(orderNum);
        }
        //订单总额
        Long sumOrderAmount = merchantOrderInfoService.getAllOrderAmountByMerchantId(merchant.getId(), OrderStatusEnum.OS_FINISH, null, null);
        if(sumOrderAmount != null){
            result.setSumOrderAmount(new BigDecimal(Money.getMoneyString(sumOrderAmount)));
        }
        //商品总数
        List<String> merchantIds = new ArrayList<>();
        merchantIds.add(request.getId());
        result.setGoodsNum(productService.getAllUpProductCount(merchantIds));
        //返回今日红包总额
        BigDecimal dto = redpacketSendAction.getLastDayAmount();
        if (dto == null) {
            result.setPacketPoolAmount(0L);
        } else {
            result.setPacketPoolAmount(dto.longValue());
        }
        //是否是收藏
        if(merchantFavoriteService.isHaveFavorite(request.getUserId(), request.getId()) != null){
            result.setIsHaveCollect(1);
        }else{
            result.setIsHaveCollect(2);
        }
        //类目
        List<String> categoryId = merchantCategoryAction.getCategoryIdByMerchantId(request.getId());
        if(categoryId != null && categoryId.size() > 0) {
            result.setCategoryId(categoryService.getParentCategory(categoryId));
            result.setTagIds(categoryService.getKidCategory(categoryId));
        }
        //图片
        List<PictureResponseVo> logoImagesUrl = merchantPictureAction.getPicture(merchant.getId(), MerchantPictureEnum.LOGO.getType());
        if(logoImagesUrl != null && logoImagesUrl.size() > 0) {
            result.setLogoImagesUrl(logoImagesUrl);
        }
        List<PictureResponseVo> merchantImagesUrl = merchantPictureAction.getPicture(merchant.getId(), MerchantPictureEnum.MERCHANT.getType());
        if(merchantImagesUrl != null && merchantImagesUrl.size() > 0) {
            result.setMerchantImagesUrl(merchantImagesUrl);
        }
        List<PictureResponseVo> certiImagesUrl = merchantPictureAction.getPicture(merchant.getId(), MerchantPictureEnum.CERTI.getType());
        if(certiImagesUrl != null && certiImagesUrl.size() > 0) {
            result.setCertiImagesUrl(certiImagesUrl);
        }
        //返回距离
        result.setDistance(DistrictUtil.calcDistance(lat,lon,merchant.getLat().doubleValue(),merchant.getLon().doubleValue()));
        //判断可编辑
        String userNumber = userService.getUserNumberByUserId(request.getUserId());
        if(userNumber != null){
            MerchantManager merchantManager = merchantManagerService.getNoCashierByUserNetworkNumAndMerchantId(userNumber, merchant.getId());
            if(merchantManager != null){
                result.setEdit(true);
            }
        }
        //人员
        List<ManagerListResponseVo> list = merchantManagerFuseAction.getMerchantManagerByMerchantId(merchant.getId());
        if(list != null && list.size() > 0){
            result.setManagerList(list);
        }
        //修改访问量
        Merchant update = new Merchant();
        update.setId(merchant.getId());
        update.setVisitCount(merchant.getVisitCount() == null ? 0 : merchant.getVisitCount() + 1);
        merchantService.updateById(update);

      //  User user = userService.selectById(merchant.getUserId());
        User user=userAction.queryUser(merchant.getUserId());
        if(user != null){
            result.setNickname(user.getNickname());
            result.setCredit(user.getCredit());
        }
        //首单红包
        MerchantPacketSet packetSet = merchantPacketSetService.selectById(merchant.getPacSetId());
        if(packetSet != null){
            result.setFirstRate(packetSet.getFirstRate());
            result.setLimitRate(packetSet.getLimitRate());
            if(packetSet.getSendTime() != null) {
                result.setSendTime(packetSet.getSendTime().getTime());
            }
        }
        return result;
    }

    /**
     * 计算用户和商家的距离
     * @param lat
     * @param lon
     * @param slat
     * @param slon
     * @return
     */
    public double getDistance(BigDecimal lat,BigDecimal lon,BigDecimal slat,BigDecimal slon){
        if (slat != null && slon != null) {
            double distance = DistrictUtil.calcDistance(lat, lon, slat, slon);
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

    /**
     * 注册管理费提醒通知
     * @param alertMsg
     * @param userId
     * @param sellerId
     */
    public void sendMessage(String alertMsg, String userId, String sellerId) {
        if (userId != null) {
            messagePushProxy.messagePushJump(MessageTypeEnum.PRODUCT_TYPE,alertMsg, "注册管理费提醒通知", userId, PushMessageDocTypeEnum.GoodsOrderDetail, sellerId);
        }
    }


    /**
     * 当status=1根据用户id获取经营的商家
     * 当status=2根据用户id获取收藏的商家
     * @param request
     * @param lat
     * @param lon
     * @return
     */
    public List<GetMerchantListVo> getMyMerchant(GetMerchantListByUserIdDto request, double lat, double lon){
        List<GetMerchantListVo> resList = new ArrayList<>();
        List<Merchant> merchantList = new ArrayList<>();
        List<String> merchantIds = new ArrayList<>();
        if(request.getStatus() == 1) {
            String userNumber = userService.getUserNumberByUserId(request.getUserId());
            List<String> ids = merchantManagerAction.getMerchantIdByUserNetworkNum(userNumber);
            merchantIds = new ArrayList<>(new HashSet<>(ids));
        }else if(request.getStatus() == 2){
            merchantIds = merchantFavoriteAction.getMerchantIdByUserId(request.getUserId());
        }
        if(merchantIds != null && merchantIds.size() > 0){
            merchantList = merchantAction.getMerChantByIds(merchantIds, request.getCurrent(), request.getSize());
        }
        if (merchantList != null){
            for (Merchant merchant : merchantList){
                resList.add(creatGetMerchantListVo(merchant, new BigDecimal(lat), new BigDecimal(lon)));
            }
        }
        return resList;
    }

    /**
     * 定时任务：注册后，判断每个月是否有发货
     * @param id
     * @return
     * @throws Exception
     */
    public Boolean subtractCredit(String id) throws Exception{
        //查询商家表里是否有对应商家
        Merchant merchant =  merchantService.queryMerchant(id, SellerStatusEnum.NORMAL.getCode(),false);
        try {
            if (merchant != null) {
                jobFuseAction.removeJob(JobEnum.MERCHANT_SUBTRACT_CREDIT_JOB,merchant.getId(),"注册后，一个月未发货处罚",merchant.getId());
                throw new RuntimeException("商家不存在");
            }
            //获取注册商家乐观锁
            User user = queryUser(merchant.getUserId());
            if (user == null) {
                throw new RuntimeException("注册者不存在");
            }
            //根据商家Id判断商家是否有发布商品，如果否扣减信用值5分
            Integer integer = productService.getProductCountByMerchantIdAndCreateTime(id, ProductStatusEnum.UP.getCode(),DateTimestampUtil.addMonthStartOrEndOfDate(new Date(),0,-1));

            //扣减商家信用5分
            if (integer < 1) {
                addCredit(id,user,-5,"商家（"+merchant.getName()+"）注册后一个月未发货，扣减信用值5分");
            }
            return true;
        }catch (RuntimeException e){
            logger.info(e.getMessage());
            return false;
        }
    }


    public void addCredit(String merchantId,User user,int credit,String description){
        if(!userCreditAction.addCreditRecord(user,credit,description,merchantId,"Merchant")){
            logger.error("扣除5信用值失败!");
        }
    }

    /**
     * 判断商家在注册后每3个月是否有交易
     * @param id
     * @return
     * @throws Exception
     */
    public boolean transactionPunish(String id) throws Exception{
        //查询商家表里是否有对应商家
        Merchant merchant =  merchantService.queryMerchant(id, MerchantStatusEnum.NORMAL.getCode(),false);
        try {
            if (merchant == null) {
                jobFuseAction.removeJob(JobEnum.MERCHANT_TRANSACTION_PUNISH_JOB,merchant.getId(),"注册后，三个月未有交易记录处罚",merchant.getId());
                throw new RuntimeException("商家不存在");
            }
            //获取注册商家乐观锁
            User user = queryUser(merchant.getUserId());
            if (user == null) {
                throw new RuntimeException("注册者不存在");
            }
            //获取当前时间戳
            long now = new Date().getTime();
            //获取90天前同一时间戳
            long old = DateTimestampUtil.getTimestampForBeforeOrAfterOfDays(new Date().getTime(), 90, 1);
            Date date = new Date();
            //根据商家id查询90天内订单成交记录
            Integer i = merchantOrderInfoService.getProductOrderCount(merchant.getId(), OrderStatusEnum.OS_FINISH,DateTimestampUtil.addMonthStartOrEndOfDate(date,0,-3), date);
            MerchantManager merchantManager = merchantManagerService.getMerchantManagerByMerchantIdAndMerchantUserType(merchant.getId(), MerchantManagerEnum.CASHIER.getName());
            if (merchantManager == null){
                throw new Exception("获取收银人员信息失败");
            }
            User userManager = queryUser(merchantManager.getUserId());
            if(userManager==null){
                throw new Exception("此收银人员不存在");
            }
            //查询90天内商家收银人员支付记录
            BillStatisticsRequestDto billQueryRequestDto = new BillStatisticsRequestDto();
            billQueryRequestDto.setQueryType(1);
            billQueryRequestDto.setStartTime(old);
            billQueryRequestDto.setEndTime(now);
            billQueryRequestDto.setUserId(merchantManager.getId());
            BigDecimal bigDecimal = billAction.statisticBill(billQueryRequestDto);
            if (i < 1 && bigDecimal.intValue() == 0) {
                addCredit(id,user,-5,"商家名（"+merchant.getName()+"）在90天内无成交记录或交易记录或支付记录，每90天扣减信用值5分");
                return true;
            }
        }catch (RuntimeException e){
            logger.info(e.getMessage());
        }
        return false;
    }

    /**
     * 根据用户id获取用户信息
     * @param userId
     * @return
     */
    public User queryUser(String userId){
        RedisKeyName redisKeyName = new RedisKeyName("userInfo", RedisTypeEnum.OBJECT_TYPE,userId);
        User user = (User) clientRedis().get(redisKeyName.getUserKey());
        if(user==null){
            user = userService.selectById(userId);
        }
        return user;
    }

    /**
     * 根据商家id获取商家列表-单个
     * @param id
     * @param lat
     * @param lon
     * @return
     */
    public GetMerchantListVo getMerchantById(String id, BigDecimal lat, BigDecimal lon){
        Merchant merchant = merchantService.selectById(id);
        if(merchant != null){
            return creatGetMerchantListVo(merchant, lat, lon);
        }
        return null;
    }

    /**
     * 单个商家列表信息
     * @param merchant
     * @param lat
     * @param lon
     * @return
     */
    public GetMerchantListVo creatGetMerchantListVo(Merchant merchant, BigDecimal lat, BigDecimal lon){
        GetMerchantListVo getMerchantListVo = new GetMerchantListVo();
        BeanUtils.copyProperties(merchant, getMerchantListVo);
        if(merchant.getSupportCredit() != null){
            getMerchantListVo.setHoldCredit(merchant.getSupportCredit());
        }
        BigDecimal firstRate = merchantPacketSetService.getFirstRateByMerchatId(merchant.getId());
        if(firstRate != null){
            getMerchantListVo.setFirstRate(firstRate);
        }
        List<String> categoryIds = merchantCategoryAction.getCategoryIdByMerchantId(merchant.getId());
        if(categoryIds!=null && categoryIds.size()>0){
            List<String> tages = categoryService.getKidCategoryName(categoryIds);
            List<String> categories = categoryService.getParentCategoryName(categoryIds);
            if(tages == null){
                tages = new ArrayList<>();
            }
            if(categories == null){
                categories = new ArrayList<>();
            }
            getMerchantListVo.setCategories(categories);
            getMerchantListVo.setTages(tages);
        }
        getMerchantListVo.setDistance(DistrictUtil.calcDistance(lat, lon, merchant.getLat(),merchant.getLon()));
        List<String> logoImagesUrl = merchantPictureAction.getPictureUrl(merchant.getId(), MerchantPictureEnum.LOGO.getType());
        if(logoImagesUrl != null && logoImagesUrl.size() > 0) {
            getMerchantListVo.setLogoImagesUrl(logoImagesUrl.get(0));
        }
        Map<String, Object> map = userService.getCreditAndNicknameByUserId(merchant.getUserId());
        if(map != null && map.size() > 0){
            if(map.get("credit")!=null){
                getMerchantListVo.setCredit(Integer.parseInt(map.get("credit").toString()));
            }
            getMerchantListVo.setNickname((String) map.get("nickname"));
        }
        return getMerchantListVo;
    }

    /**
     * 根据userId查询最新的一个商家，用于个人中心
     * @param request
     * @param lat
     * @param lon
     * @return
     * @throws Exception
     */
    public GetMerchantListVo getMerchantByUserId(GetMerchantByUserIdRequestDto request, Double lat, Double lon) throws Exception{
        Merchant merchant = new Merchant();
        List<String> merchantIds = new ArrayList<>();
        if (request.getIndex() == 1) {
            merchant = merchantService.getNewestMerchantByUserId(request.getUserId());
        }else if (request.getIndex() == 2) {
            merchantIds = merchantFavoriteService.getMerchantIdByUserId(request.getUserId());
        }else if (request.getIndex() == 3) {
            String userNumber = userService.getUserNumberByUserId(request.getUserId());
            merchantIds = merchantManagerService.getMerchantIdByUserNetworkNum(userNumber);
        }
        if(merchantIds != null && merchantIds.size() > 0) {
            merchant = merchantService.getNewestMerchantById(merchantIds);
        }
        if(merchant != null) {
            return creatGetMerchantListVo(merchant, new BigDecimal(lat), new BigDecimal(lon));
        }else {
            return null;
        }
    }

    /**
     * 获得商家信息通过id
     * @param id
     * @param lon
     * @param lat
     * @return
     */
    public GetMerchantListVo getMerchantById(String id,Double lon,Double lat) {
        Merchant merchant = merchantService.getMerchantById(id);
        if(merchant!=null) {
            return creatGetMerchantListVo(merchant, new BigDecimal(lat), new BigDecimal(lon));
        }
        return null;
    }

    /**
     * 根据UseuId查询现有注册商家
     * @param userId
     * @return
     */
    public List<GetMerchantListVo> getRegister(String userId){
        List<GetMerchantListVo> getMerchantListVoList = new ArrayList<>();
        List<Merchant> merchantList = merchantService.getMerchantByUserId(userId, MerchantStatusEnum.NORMAL.getCode());
        for(Merchant merchant : merchantList){
            getMerchantListVoList.add(creatGetMerchantListVo(merchant, merchant.getLat(), merchant.getLon()));
        }
        return getMerchantListVoList;
    }


    /**
     * 根据UseuId查询经营、收藏商店数
     * @param userId
     * @return
     */
    public GetRelatedSellerMessageResponseVo getMerchatCountByUserId(String userId){
        GetRelatedSellerMessageResponseVo responseVo = new GetRelatedSellerMessageResponseVo();
        responseVo.setManageSellers(getMerchatCount(userId, 1));
        responseVo.setCollectSellers(getMerchatCount(userId, 2));
        return responseVo;
    }

    /**
     * 根据userId获取商家数
     * status = 1：获取经营数
     * status = 2：获取收藏数
     * @param userId
     * @param status
     * @return
     */
    public Integer getMerchatCount(String userId, Integer status){
        if(status == 1){
            List<String> list = merchantFavoriteAction.getMerchantIdByUserId(userId);
            return list.size();
        }else{
            String userNumber = userService.getUserNumberByUserId(userId);
            List<String> list1 = merchantManagerAction.getMerchantIdByUserNetworkNum(userNumber);
            List<String> list2 = merchantService.getIdByUserId(userId);
            list1.addAll(list2);
            List<String> list = new ArrayList(new HashSet(list1));
            return list.size();
        }
    }

    public List<GetMerchantListVo> list(GetSellerListRequestDto request, double lat, double lon){
        return queryBusiness(merchantAction.getSellerSearchQuery(request,lat,lon));
    }

    private List<GetMerchantListVo> queryBusiness(SellerSearchQuery searchQuery){
        List<SellerSearchResponse> sellerSearchResponses = searchServiceProvider.getSellerSearchService().querySellers(searchQuery);
        List<GetMerchantListVo> getMerchantListVoList = new ArrayList<>();
        for (SellerSearchResponse searchResponse: sellerSearchResponses) {
            GetMerchantListVo responseVo = new GetMerchantListVo();
            VoPoConverter.copyProperties(searchResponse, responseVo);
            List<String> logoImagesUrl = searchResponse.getLogoImagesUrl();
            if(logoImagesUrl != null && logoImagesUrl.size()>0){
                responseVo.setLogoImagesUrl(productPictureAction.updateImnagesUrl(logoImagesUrl.get(0)));
            }
            List<String> categoryNames = searchResponse.getCategoryNames();
            List<String> categoryPids = searchResponse.getCategoryParentIds();
            List<String> tages = new ArrayList<>();
            List<String> categorys = new ArrayList<>();
            if(categoryNames != null && categoryPids != null && categoryNames.size() > 0 && categoryPids.size() > 0){
                for(int i=0; i<categoryPids.size(); i++){
                    if("0".equals(categoryPids.get(i))){
                        categorys.add(categoryNames.get(i));
                    }else{
                        tages.add(categoryNames.get(i));
                    }
                }
            }
            responseVo.setTages(new ArrayList(new HashSet(tages)));
            responseVo.setCategories(new ArrayList(new HashSet(categorys)));
            getMerchantListVoList.add(responseVo);
        }

        return getMerchantListVoList;
    }

    /**
     * 根据UseuId查询现有注册的商店数、总经营的商店数
     * @param userId
     * @return
     */
    public GetRegisterMerchantCountResponseVo getRegisterMerchantCountByUserId(String userId){
        GetRegisterMerchantCountResponseVo responseVo = new GetRegisterMerchantCountResponseVo();
        Integer nowCount = merchantService.getNowCountByUserIdAndStatus(userId, MerchantStatusEnum.NORMAL.getCode());
        if(nowCount != null){
            responseVo.setNowRegisterMerchant(nowCount);
        }
        String userNumber = userService.getUserNumberByUserId(userId);
        List<String> merchantIds = merchantManagerAction.getMerchantIdByUserNetworkNum(userNumber);
        List<String> sumCount = new ArrayList<>(new HashSet<>(merchantIds));
        if(sumCount != null && sumCount.size() > 0) {
            responseVo.setSumRegisterMerchant(sumCount.size());
        }
        return responseVo;
    }

    public Map<String,Object> getMerchantList(GetSellerWhiteRequestDto request){
        Page<Map<String, Object>> page = merchantService.getMerchantList(request.getName(), request.getCurrentPage(), request.getSize(),request.getStatus());
        List<Map<String, Object>> records = page.getRecords();
        clientRedis();
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
                    //throw new RuntimeException("该商家注册者不存在");
                }
            }
        }
        Map map = new HashMap();
        map.put("total",page.getTotal());
        map.put("records",records);
        return map;
    }

    /**
     * 网信 - 发行网信 - 获取商家列表
     */
    public List<MerchantListInPublishCreditVo> getMerchantList(SelectSellerListByInPublicCreditDto dto) {
//        dto.setHoldCredit(true);
//        dto.setPayStatus(0);
        List<Merchant> merchantList = merchantService.selectMerchantListInPublicCredit(dto);
        List<MerchantListInPublishCreditVo> merchantListInPublicCreditVos = new ArrayList<>();
        MerchantListInPublishCreditVo merchant;
        for (Merchant list : merchantList) {
            merchant = new MerchantListInPublishCreditVo();
            merchant.setId(list.getId());
            merchant.setName(list.getName());
            List<String> logoImagesUrl = merchantPictureAction.getPictureUrl(merchant.getId(), MerchantPictureEnum.LOGO.getType());
            if(logoImagesUrl != null && logoImagesUrl.size() > 0) {
                merchant.setMerchantImage(logoImagesUrl.get(0));
            }
            merchant.setCategories(merchantCategoryAction.getParentMerchantCategory(list.getId()).get(0));
            merchant.setStatus(false);
            merchantListInPublicCreditVos.add(merchant);
        }
        return merchantListInPublicCreditVos;
    }

}
