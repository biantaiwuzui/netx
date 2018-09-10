package com.netx.fuse.biz.ucenter;

import com.netx.common.common.enums.AliyunBucketType;
import com.netx.common.redis.RedisInfoHolder;
import com.netx.common.router.dto.request.UserAndSellersRequestDto;
import com.netx.common.router.dto.select.*;
import com.netx.common.user.dto.user.UserInfoAndHeadImg;
import com.netx.common.user.enums.StatScoreEnum;
import com.netx.common.user.util.AddImgUrlPreUtil;
import com.netx.common.user.util.DateTimestampUtil;
import com.netx.common.vo.business.*;
import com.netx.common.vo.currency.GetCurrencyTokenRequestDto;
import com.netx.common.vo.currency.TokenCurrencyBaseAndNewlyVo;
import com.netx.common.vo.currency.TokenCurrencyVo;
import com.netx.common.wz.dto.common.CommonPageDto;
import com.netx.fuse.biz.worth.MeetingFuseAction;
import com.netx.fuse.biz.worth.WishFuseAction;
import com.netx.utils.cache.RedisCache;
import com.netx.utils.cache.RedisKeyName;
import com.netx.utils.cache.RedisTypeEnum;
import com.netx.worth.service.WishService;
import com.netx.worth.service.WishSupportService;
import com.netx.worth.vo.DemandListVo;
import com.netx.fuse.client.shoppingmall.GoodsClientAction;
import com.netx.fuse.client.shoppingmall.OrderClientAction;
import com.netx.fuse.client.shoppingmall.SellerClientAction;
import com.netx.fuse.client.worth.WzDataClientAction;
import com.netx.fuse.proxy.ClientProxy;
import com.netx.ucenter.biz.router.RouterAction;
import com.netx.ucenter.biz.user.ArticleAction;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.utils.DistrictUtil;
import com.netx.utils.money.Money;
import com.netx.worth.model.Demand;
import com.netx.worth.model.Skill;
import com.netx.worth.model.Wish;
import com.netx.worth.service.WorthServiceprovider;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

@Service
public class RouterFuseAction {
    private Logger logger = LoggerFactory.getLogger(RouterFuseAction.class);
    @Autowired
    private AddImgUrlPreUtil addImgUrlPreUtil;

    @Autowired
    private ClientProxy clientProxy;

    @Autowired
    private RouterAction routerAction;

    @Autowired
    ArticleAction articleAction;

    @Autowired
    GoodsClientAction goodsClientAction;

    @Autowired
    WzDataClientAction wzDataClientAction;

    @Autowired
    SellerClientAction sellerClientAction;

    @Autowired
    WalletFuseAction walletFuseAction;

    @Autowired
    OrderClientAction orderClientAction;
    
    @Autowired
    MeetingFuseAction meetingFuseAction;

    @Autowired
    WishFuseAction wishFuseAction;

    @Autowired
    private WorthServiceprovider worthServiceprovider;

    @Autowired
    private WishSupportService wishSupportService;

    @Autowired
    private WishService wishService;

    @Autowired
    UserAction userAction;

    private RedisCache redisCache;

    @Autowired
    private RedisInfoHolder redisInfoHolder;

    private RedisCache clientCacha(){
        redisCache = new RedisCache(redisInfoHolder.getHost(),redisInfoHolder.getPassword(),redisInfoHolder.getPort());
        return redisCache;
    }

    private CommonPageDto initCommonPageDto(int page, int size){
        CommonPageDto commonPageDto = new CommonPageDto();
        commonPageDto.setSize(size);
        commonPageDto.setCurrentPage(page);
        return commonPageDto;
    }

    public Boolean deleteUser(String userId) throws Exception{
        routerAction.deleteUser(userId);
        walletFuseAction.deleteWalletByUserId(userId);
        try {
            clientProxy.clean(userId);
        }catch (Exception e){
            throw new RuntimeException("网值数据清除失败："+e.getMessage(),e);
        }try {
            //sellerClientAction.cancelBuyUserId(userId);
        }catch (Exception e){
            throw new RuntimeException("网商数据清除失败："+e.getMessage(),e);
        }
        return true;
    }

    public Boolean checkDeleteUser(String userId) throws Exception {
        return orderClientAction.checkUpHaveCompleteGoodsOrder(userId) && clientProxy.checkNetx(userId);
    }

    /**
     * 按用户id查询音频数、图文数、邀请数、礼物数
     * @param userId
     * @return SelectUserOtherNumResponseDto
     */
    public SelectUserOtherNumResponseDto otherNumByUserId(String userId) throws Exception{
        SelectUserOtherNumResponseDto selectUserOtherNumResponse=new SelectUserOtherNumResponseDto();
        selectUserOtherNumResponse.setGiftNum(getNum(wzDataClientAction.giftCount(userId)));
        selectUserOtherNumResponse.setInvitationNum(getNum(wzDataClientAction.invitationCount(userId)));
        selectUserOtherNumResponse.setImageWriteNum(articleAction.selectArticleCountByUserId(userId,null));
        //selectUserOtherNumResponse.setVedioNum(articleAction.selectArticleCountByUserId(userId,ArticleTypeEnum.AUDIO_VIDEO));
        selectUserOtherNumResponse.setVedioNum(0);
        return selectUserOtherNumResponse;
    }

    private SelectCurrencyDetailDataResponseDto getSelectCurrencyDetailDataResponseDto(GetCurrencyTokenRequestDto getCurrencyTokenRequestDto) throws Exception{
        SelectCurrencyDetailDataResponseDto selectCurrencyDetailDataResponseDto = new SelectCurrencyDetailDataResponseDto();
        //TODO 增加网信统计
        TokenCurrencyBaseAndNewlyVo tokenCurrencyBaseAndNewlyVo = new TokenCurrencyBaseAndNewlyVo();
        selectCurrencyDetailDataResponseDto.setTokenCurrencyBaseAndNewlyVo(tokenCurrencyBaseAndNewlyVo);
        if(tokenCurrencyBaseAndNewlyVo!=null && tokenCurrencyBaseAndNewlyVo.getTokenCurrencyVo()!=null){
            //数据统计
            TokenCurrencyVo tokenCurrencyVo = tokenCurrencyBaseAndNewlyVo.getTokenCurrencyVo();
            selectCurrencyDetailDataResponseDto.setSelectCurrencyUserDataDto(getSelectCurrencyUserDataDto(tokenCurrencyVo.getUserId(),tokenCurrencyVo.getSellerIds()));
        }
        return selectCurrencyDetailDataResponseDto;
    }

    /**
     * 根据用户id(userId)与网币使用范围的商家id集(sellerIds)
     * @param userAndSellersRequestDtos
     * @return
     */
    public List<SelectCurrencyUserDataDto> selectCurrencyUserDataDtos(List<UserAndSellersRequestDto> userAndSellersRequestDtos) throws Exception{
        List<SelectCurrencyUserDataDto> selectCurrencyUserDataDtos = new ArrayList<>();
        for(UserAndSellersRequestDto userAndSellersRequestDto:userAndSellersRequestDtos){
            selectCurrencyUserDataDtos.add(getSelectCurrencyUserDataDto(userAndSellersRequestDto.getUserId(),userAndSellersRequestDto.getSellerIds()));
        }
        return selectCurrencyUserDataDtos;
    }

    public Map<String,SelectCurrencyUserDataDto> selectCurrencyUserDataMap(List<UserAndSellersRequestDto> userAndSellersRequestDtos) throws Exception{
        Map<String,SelectCurrencyUserDataDto> dataDtoMap = new HashMap<>();
        for (UserAndSellersRequestDto requestDto:userAndSellersRequestDtos){
            if(!StringUtils.isEmpty(requestDto.getUserId())){
                dataDtoMap.put(requestDto.getUserId(),getSelectCurrencyUserDataDto(requestDto.getUserId(),requestDto.getSellerIds()));
            }
        }
        return dataDtoMap;
    }

    /**
     * 获取用户统计数据（图文、音视、活动、商家、商品、技能）
     * @param userId
     * @return
     */
    public SelectCurrencyUserDataDto getSelectCurrencyUserDataDto(String userId, String sellerIds) throws Exception{
        if (userId.isEmpty())return null;
        SelectCurrencyUserDataDto selectCurrencyUserDataDto = new SelectCurrencyUserDataDto();
        selectCurrencyUserDataDto.setImageWriteNum(articleAction.selectArticleCountByUserId(userId,null));
        selectCurrencyUserDataDto.setVedioNum(0);
        //selectCurrencyUserDataDto.setVedioNum(articleAction.selectArticleCountByUserId(userId,ArticleTypeEnum.AUDIO_VIDEO));
        if(!TextUtils.isEmpty(sellerIds)){
            GetSellerGoodsQuantityResponseVo getSellerGoodsQuantityResponseVo = goodsClientAction.getSellerGoodsQuantity(sellerIds);
            if(getSellerGoodsQuantityResponseVo!=null){
                selectCurrencyUserDataDto.setShopNum(getSellerGoodsQuantityResponseVo.getRangeSellers());
                selectCurrencyUserDataDto.setGoodsNum(getSellerGoodsQuantityResponseVo.getRangeGoods());
            }
        }
        selectCurrencyUserDataDto.setSkillNum(worthServiceprovider.getSkillService().getSkillCountByUserId(userId));
        selectCurrencyUserDataDto.setActivityNum(meetingFuseAction.getSendMeetingCount(userId));
        return selectCurrencyUserDataDto;
    }

    /**
     * 活动数、技能数
     * @param commonPageDto
     * @param type
     * @return
     */
    private int getNum(CommonPageDto commonPageDto,boolean type){
        Map<String, Object> result = type?wzDataClientAction.sendSkillList(commonPageDto):wzDataClientAction.sendMeetingList(commonPageDto);
        int total = 0;
        if(result != null){
            total = Integer.parseInt(result.get("total").toString());
        }
        return total;
    }

    /**
     * 获取网币的适用范围商品数
     * @param sellerIds
     * @return
     */
    private GetSellerGoodsQuantityResponseVo getSellerGoodsQuantityResponseVo(String sellerIds) throws Exception{
        return goodsClientAction.getSellerGoodsQuantity(sellerIds);
    }

    public Map<String,Object> getUserProductOne(String userId, BigDecimal lat, BigDecimal lon) throws Exception{
        CommonUserIdRequestDto commonUserIdRequestDto = initCommonUserIdRequestDto(userId);
        Map<String,Object> map = new HashMap<>();
        map.put("num", goodsClientAction.getRelatedGoodsMessage(commonUserIdRequestDto));//网商的发行商品数量
        map.put("list", goodsClientAction.getNewestGoodsMessage(userId, lat, lon));//网商的最新商品
        return map;
    }

    /**
     *
     * @param userId
     * @param fromUserId
     * @return
     * @throws Exception
     */
    public Map<String,Object> getUserBeanDataMap(String userId, String fromUserId) throws Exception{
        Map<String,Object> map = new HashMap<>();
        map.put("otherNum",otherNumByUserId(userId));//用户统计数据
        map.put("detail",routerAction.selectUserBeanByUserId(userId,fromUserId));//用户基本信息
        if(fromUserId!=null && !fromUserId.equals(userId)){
            long date = DateTimestampUtil.getStartOrEndOfDate(new Date(),1).getTime();
            RedisKeyName redisKeyName = new RedisKeyName("userHistory", RedisTypeEnum.HSET_TYPE,userId);
            Long lastDate = (Long) clientCacha().hGet(redisKeyName.getUserKey(),fromUserId);
            if(lastDate==null){
                //浏览记录加一
                redisCache.hSet(redisKeyName.getUserKey(),fromUserId,date);
                redisKeyName = new RedisKeyName("userHits",RedisTypeEnum.ZSET_TYPE,null);
                //点击量加一
                redisCache.zincrby(redisKeyName.getUserKey(),userId,1);
                redisKeyName = new RedisKeyName("userScore", RedisTypeEnum.ZSET_TYPE,null);
                //积分增加
                redisCache.zincrby(redisKeyName.getUserKey(),userId, StatScoreEnum.SS_USER_DETAIL.score());
            }
        }
        return map;
    }

    private Integer getNum(Integer count){
        return count==null?0:count;
    }
//心愿个人中心
    public Map<String,Object> getWishOne(String userId,boolean isPublishUser){
        Map<String,Object> map = new HashMap<>();
        SelectNumResponseDto selectNumResponseDto = new SelectNumResponseDto();
        selectNumResponseDto.setNum(wishService.getWishOrderCountByUserId(userId));//成功数
        selectNumResponseDto.setTotal(wishService.getWishCountByUserId(userId));//总数

        Wish wish = wishService.getNewWish(userId,isPublishUser);
        if (wish!=null){
            //处理图片
            wish.setWishImagesUrl ( addImgUrlPreUtil.addImgUrlPres ( wish.getWishImagesUrl (), AliyunBucketType.ActivityBucket ) );
            wish.setWishImagesTwoUrl ( addImgUrlPreUtil.addImgUrlPres ( wish.getWishImagesTwoUrl (), AliyunBucketType.ActivityBucket ) );
            //处理金额
            if(wish.getAmount ()!=null) {
                wish.setAmount ( Money.CentToYuan ( wish.getAmount () ).getAmount ().longValue ());
            }
            if(wish.getCurrentAmount ()!=null) {
                wish.setCurrentAmount ( Money.CentToYuan ( wish.getCurrentAmount () ).getAmount ().longValue ());
            }
            if(wish.getCurrentApplyAmount ()!=null) {
                wish.setCurrentApplyAmount ( Money.CentToYuan ( wish.getCurrentApplyAmount () ).getAmount ().longValue ());
            }

//            支持者数
            selectNumResponseDto.setApplyNum ( wishSupportService.getWishSupportNumByWishId(wish.getId () ));
            //获得距离
//            selectNumResponseDto.setDistance ( DistrictUtil.calcDistance ( wish ().doubleValue (), wish.getLon ().doubleValue (), lat, lon ) );
        }
        //获取用户的信息
        UserInfoAndHeadImg userData = userAction.getUserInfoAndHeadImg ( userId );
        map.put("list",wish);
        map.put("num",selectNumResponseDto);
        map.put("userDate",userData);
//        map.put("list",wishFuseAction.getNewWish (userId));//最新一条
        return map;
    }
    
    //活动个人用户中心
    public Map<String,Object> getMeetingDto(String userId,Double lon,Double lat){
        Map<String,Object> map = new HashMap<>();
        SelectNumResponseDto numResponseDto = new SelectNumResponseDto();
        numResponseDto.setNum(meetingFuseAction.getSuccessMeetingCount(userId));
        numResponseDto.setTotal(meetingFuseAction.getSendMeetingCount(userId));
        map.put("num",numResponseDto);
        map.put("list",meetingFuseAction.getLatestNewsMeeting(userId,lat,lon));
        return map;
    }
    
    public Map<String,Object> getSkillOne(String userId, BigDecimal lat, BigDecimal lon){
        Map<String,Object> map = new HashMap<>();
        Skill skill =worthServiceprovider.getSkillService().getNewSkill(userId);
        //处理图片
        SelectNumResponseDto selectNumResponseDto = new SelectNumResponseDto();
        selectNumResponseDto.setNum(worthServiceprovider.getSkillOrderService().getSkillOrderCountByUserId(userId));//成功数
        selectNumResponseDto.setTotal(worthServiceprovider.getSkillService().getSkillCountByUserId(userId));//总数
        //获取用户的信息
        UserInfoAndHeadImg UserData = userAction.getUserInfoAndHeadImg ( userId );
        if(skill!=null){
            //处理图片
            skill.setSkillImagesUrl( addImgUrlPreUtil.addImgUrlPres ( skill.getSkillImagesUrl(), AliyunBucketType.ActivityBucket ) );
            skill.setSkillDetailImagesUrl( addImgUrlPreUtil.addImgUrlPres ( skill.getSkillDetailImagesUrl(), AliyunBucketType.ActivityBucket ) );
            //处理金额
            if(skill.getAmount()!=null) {
                skill.setAmount( Money.CentToYuan ( skill.getAmount() ).getAmount ().longValue ());
            }
            //获得距离
            selectNumResponseDto.setDistance (DistrictUtil.calcDistance(skill.getLat(),skill.getLon(),lat,lon));
        }

        map.put("num",selectNumResponseDto);
        map.put("list",skill);
        map.put ("userData", UserData);
        return map;
    }

    public Map<String,Object> getDemandOne(String userId,double lat, double lon){
        Map<String,Object> map = new HashMap<>();
        SelectNumResponseDto selectNumResponseDto = new SelectNumResponseDto();
        selectNumResponseDto.setNum(worthServiceprovider.getDemandOrderService().getDemandOrderCountByUserId(userId));//成功数
        selectNumResponseDto.setTotal(worthServiceprovider.getDemandService().getDemandCountByUserId(userId));//总数
        //获得最新发布的需求
        Demand demand = worthServiceprovider.getDemandService().getNewDemand (userId);
        DemandListVo demandListVo = null;
        //获得申请人数
        if(demand!=null){
            demandListVo = new DemandListVo();
            BeanUtils.copyProperties(demand,demandListVo);
            //处理图片
            demandListVo.setImagesUrl ( addImgUrlPreUtil.addImgUrlPres ( demand.getImagesUrl (), AliyunBucketType.ActivityBucket ) );
            demandListVo.setDetailsImagesUrl ( addImgUrlPreUtil.addImgUrlPres ( demand.getDetailsImagesUrl (), AliyunBucketType.ActivityBucket ) );
            //处理金额
            if(demand.getWage ()!=null) {
                demandListVo.setWageBig(Money.CentToYuan(demand.getWage()).getAmount());
            }
            if(demand.getBail()!=null){
                demandListVo.setBailBig(Money.CentToYuan(demand.getBail()).getAmount());
            }
            if(demand.getOrderPrice()!=null){
                demandListVo.setOrderPriceBig(Money.CentToYuan(demand.getOrderPrice()).getAmount());
            }
            selectNumResponseDto.setApplyNum ( worthServiceprovider.getDemandRegisterService().getDemandApplyNumByDemandId(demand.getId () ));
            //获得距离
            selectNumResponseDto.setDistance ( DistrictUtil.calcDistance ( demand.getLat ().doubleValue (), demand.getLon ().doubleValue (), lat, lon ) );
        }
        //获取用户的信息
        UserInfoAndHeadImg UserData = userAction.getUserInfoAndHeadImg ( userId );
        map.put("list",demandListVo);//最新一条需求
        map.put("num",selectNumResponseDto);
        map.put ( "userData", UserData );
        return map;
    }

    public Map<String,Object> getSellerDetailByUserIdVo(String userId, Double lat, Double lon) throws Exception{
        Map<String,Object> map = new HashMap<>();
        map.put("num", sellerClientAction.getRegisterSellerMessage(new CommonUserIdRequestDto(userId)));
        map.put("list", sellerClientAction.newgetSellerByUserId(userId, lat, lon));
        return map;
    }

    private CommonUserIdRequestDto initCommonUserIdRequestDto(String userId){
        CommonUserIdRequestDto commonUserIdRequestDto = new CommonUserIdRequestDto();
        commonUserIdRequestDto.setUserId(userId);
        return commonUserIdRequestDto;
    }

    /**
     * 网币模块的数据(最新记录--持有或发行)
     * @param userId
     * @param hold
     * @return
     */
    public SelectCurrencyDetailDataResponseDto getCurrencyDetailData(String userId, boolean hold) throws Exception{
        return getSelectCurrencyDetailDataResponseDto(createGetCurrencyTokenRequestDto(userId, hold));
    }

    private GetCurrencyTokenRequestDto createGetCurrencyTokenRequestDto(String userId, boolean hold){
        GetCurrencyTokenRequestDto getCurrencyTokenRequestDto = new GetCurrencyTokenRequestDto();
        getCurrencyTokenRequestDto.setUserId(userId);
        getCurrencyTokenRequestDto.setIsHold(hold);
        return getCurrencyTokenRequestDto;
    }

    private long getNumFromMap(Map<String,Long> map,String key){
        return map.containsKey(key)?map.get(key):0;
    }

    private SelectUserDemandResponseDto updateSelectUserDemandResponseDto(SelectUserDemandResponseDto demandResponseDto,Map<String,Object> map){
        if(map.get("title")!=null){
            demandResponseDto.setTitle((String)map.get("title"));
        }
        if(map.get("demandLabel")!=null){
            demandResponseDto.setDemandLabel((String)map.get("demandLabel"));
        }
        if(map.get("amount")!=null){
            demandResponseDto.setAmount((int)map.get("amount"));
        }
        if(map.get("pic")!=null) {
            demandResponseDto.setPic((String)map.get("pic"));
        }
        return demandResponseDto;
    }
}
