package com.netx.fuse.biz.ucenter;

import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.common.enums.*;
import com.netx.common.redis.RedisInfoHolder;
import com.netx.common.user.dto.article.*;
import com.netx.common.user.dto.article.ArticlePayForCompanyDto;
import com.netx.common.user.dto.user.UserInfoAndHeadWatch;
import com.netx.common.user.enums.ArticleAmountTypeEnum;
import com.netx.common.user.enums.ArticleStatusCodeEnum;
import com.netx.common.user.enums.PayTypeEnum;
import com.netx.common.user.enums.StatScoreEnum;
import com.netx.common.user.util.AddImgUrlPreUtil;
import com.netx.common.user.util.ComputeAgeUtils;
import com.netx.common.user.util.VoPoConverter;
import com.netx.fuse.biz.job.JobFuseAction;
import com.netx.fuse.biz.shoppingmall.merchantcenter.MerchantFuseAction;
import com.netx.fuse.biz.shoppingmall.productcenter.ProductFuseAction;
import com.netx.fuse.biz.worth.DemandFuseAction;
import com.netx.fuse.biz.worth.MeetingFuseAction;
import com.netx.fuse.biz.worth.SkillFuseAction;
import com.netx.fuse.biz.worth.WishFuseAction;
import com.netx.shopping.vo.GetMerchantListVo;
import com.netx.ucenter.biz.common.CostAction;
import com.netx.ucenter.biz.user.*;
import com.netx.ucenter.model.common.CommonTags;
import com.netx.ucenter.model.common.CommonWalletFrozen;
import com.netx.ucenter.model.user.Article;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.service.common.CommonServiceProvider;
import com.netx.ucenter.service.user.UserServiceProvider;
import com.netx.utils.cache.RedisCache;
import com.netx.utils.cache.RedisKeyName;
import com.netx.utils.cache.RedisTypeEnum;
import com.netx.utils.money.Money;
import com.netx.worth.biz.common.GiftAction;
import com.netx.worth.biz.common.InvitationAction;
import com.netx.worth.biz.match.MatchApplyAction;
import com.netx.worth.biz.match.MatchCreateAction;
import com.netx.worth.model.Wish;
import com.netx.worth.vo.DemandListVo;
import com.netx.worth.vo.MatchEventSimpleVo;
import com.netx.worth.vo.MeetingDetailSendDto;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ArticleFuseAction {

    private Logger logger = LoggerFactory.getLogger(ArticleFuseAction.class);

    @Autowired
    private WallerFrozenFuseAction wallerFrozenFuseAction;

    @Autowired
    private CostAction costAction;

    @Autowired
    private ArticleAction articleAction;

    @Autowired
    private InvitationAction invitationAction;

    @Autowired
    private GiftAction giftAction;

    @Autowired
    private UserPhotoAction userPhotoAction;

    @Autowired
    private UserAction userAction;

    @Autowired
    private CommonServiceProvider commonServiceProvider;

    @Autowired
    private JobFuseAction jobFuseAction;

    @Autowired
    private AddImgUrlPreUtil addImgUrlPreUtil;

    @Autowired
    private UserServiceProvider userServiceProvider;

    @Autowired
    private RedisInfoHolder redisInfoHolder;

    //    @Autowired
    private RedisCache redisCache;
    @Autowired
    ArticleTagsAction articleTagsAction;
    @Autowired
    private MatchApplyAction matchApplyAction;

    @Autowired
    private DemandFuseAction demandAction;

    @Autowired
    private WishFuseAction wishAction;

    @Autowired
    private SkillFuseAction skillAction;

    @Autowired
    private MeetingFuseAction meetingAction;

    @Autowired
    private MatchCreateAction matchCreateAction;

    @Autowired
    private MerchantFuseAction merchantFuseAction;

    @Autowired
    private ProductFuseAction productFuseAction;

    private RedisCache clientRedis() {
        redisCache = new RedisCache(redisInfoHolder.getHost(), redisInfoHolder.getPassword(), redisInfoHolder.getPort());
        return redisCache;
    }

    @Transactional(rollbackFor = Exception.class)
    public String publishArticle(PublishArticleRequestDto request, String userId, Double lon, Double lat) throws Exception {

        //插入图文,返回图文id
        String articleId = articleAction.publishArticle(request, userId, lon, lat);
        //拆串
        String[] tagNames = request.getTagNames().split(",");
        //获取id集
        List<CommonTags> commonTags = articleTagsAction.queryArticleTags(tagNames, "0");
        //设置标签-图文关系表
        for (CommonTags tagId : commonTags) {
            if (!articleTagsAction.insertArticleTagsAll(articleId, tagId.getId(), userId)) {
                return null;
            }
        }
        //创建图文在24小时内是否支付押金
        if (articleId != null) {
            RedisKeyName redisKeyName = new RedisKeyName("articlePublish", RedisTypeEnum.HSET_TYPE, userId);
            //判断文字和图文是否符合加分规则
            boolean isAddScore = request.getLength() >= 140 && StringUtils.isNotBlank(request.getPic());
            //图文排行redis
            clientRedis().hSet(redisKeyName.getUserKey(), articleId, isAddScore);
            if (isAddScore) {
                redisKeyName = new RedisKeyName("userScore", RedisTypeEnum.ZSET_TYPE, null);
                redisCache.zincrby(redisKeyName.getUserKey(), userId, StatScoreEnum.SS_PUBLISH_ARTICLE.score());
            }
            /*Date date = DateTimestampUtil.addDayStartOrEndOfDate(new Date(),0,1);
            Boolean flag = jobFuseAction.addJob(JobEnum.ARTICLE_DELETED_JOB,articleId,articleId, "图文监控："+articleId,date, AuthorEmailEnum.ZI_AN);
            logger.warn("创建删除图文定时任务"+(flag?"成功":"失败"));*/
        }

        return articleId;
    }

    @Transactional(rollbackFor = Exception.class)
    public String checkArticle(String userId, String title) {
        String message = "";
        List<ArticleCreateTimeDto> articleCreateTimeDtoList = articleAction.queryArticleCreateListByuserId(userId);
        if (articleCreateTimeDtoList.size()==0) {
            return message;
        } else {
            for (int i = 0; i < articleCreateTimeDtoList.size(); i++) {
                if (articleCreateTimeDtoList.get(i).getTitle().equals(title)) {
                    message = "不能发布自己已发布过的标题！";
                    return message;
                }
            }
            ArticleCreateTimeDto articleCreateTimeDto= articleCreateTimeDtoList.get(0);
            SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            Date articleDate=articleCreateTimeDto.getCreateTime();
            long between = (date.getTime() - articleDate.getTime())/1000;
            long min = between / 60;
            if (min <= 5) {
                message = "五分钟内不可以发布两次哦！";
            }
        }
        return message;
    }

    @Transactional(rollbackFor = Exception.class)
    public String editArticle(EditArticleRequestDto dto, String userId) {
        String result = articleAction.editArticle(dto, userId);
        if (result == null) {
            RedisKeyName redisKeyName = new RedisKeyName("articlePublish", RedisTypeEnum.HSET_TYPE, userId);
            boolean isAddScore = getIsAddScore((Boolean) clientRedis().hGet(redisKeyName.getUserKey(), dto.getId()));
            if (dto.getLength() >= 140 && StringUtils.isNotBlank(dto.getPic())) {
                if (!isAddScore) {
                    redisCache.hSet(redisKeyName.getUserKey(), dto.getId(), true);
                    redisKeyName = new RedisKeyName("userScore", RedisTypeEnum.ZSET_TYPE, null);
                    redisCache.zincrby(redisKeyName.getUserKey(), userId, StatScoreEnum.SS_PUBLISH_ARTICLE.score());
                }
            } else {
                if (isAddScore) {
                    redisCache.hSet(redisKeyName.getUserKey(), dto.getId(), false);
                    redisKeyName = new RedisKeyName("userScore", RedisTypeEnum.ZSET_TYPE, null);
                    redisCache.zincrby(redisKeyName.getUserKey(), userId, -StatScoreEnum.SS_PUBLISH_ARTICLE.score());
                }
            }
        }
        return result;
    }

    private boolean getIsAddScore(Boolean isAddScore) {
        return isAddScore == null ? false : isAddScore;
    }

    @Transactional(rollbackFor = Exception.class)
    public SelectArticleOtherInfoResponseDto selectArticleOtherInfo(String articleId) throws Exception {
        Article article = userServiceProvider.getArticleService().selectById(articleId);
        SelectArticleOtherInfoResponseDto response = new SelectArticleOtherInfoResponseDto();
        if (article != null) {
            response.setLikesNumber(userServiceProvider.getArticleLikesService().selectLikesNumber(article.getId()));
            response.setCommentNumber(getCommentNumber(articleId));
            response.setInvitationNumber(getInvitationNumber(article.getUserId(), articleId));
            response.setGiftNumber(getGiftNumber(article.getUserId(), articleId));
        }
        return response;
    }

    private int getLikesNumber(String articleId) {
        return userServiceProvider.getArticleLikesService().selectLikesNumber(articleId);
    }

    private int getCommentNumber(String articleId) {
        return commonServiceProvider.getEvaluateService().getEvaluateNums(articleId, EvaluateTypeEnum.ARTICLE_EVALUATE);
    }

    private int getInvitationNumber(String articleId, String userId) {
        return invitationAction.getSendCount(userId, articleId);
    }

    private int getGiftNumber(String articleId, String userId) {
        return giftAction.getSendCount(userId, articleId);
    }

    /**
     * 重构 Article 变为 SelectArticleResponseDto
     * 1、根据发布者的设置判断使用者是否有查看咨讯权限，将不能查看的咨讯过滤掉，返回 null
     * 2、判断发布者是否有设置匿名、不能显示位置等。
     *
     * @param userId  使用者的 userId
     * @param article 咨讯实体（必须带有发布者的 userId 值）
     * @return 若不能公开此咨讯，则返回 null。
     */
    private SelectArticleResponseDto refactorEntityToDto(String userId, Article article) throws Exception {
        //检查使用者是否有权限查看咨讯
        if (!articleAction.isCheckOutArticle(userId, article)) {
            return null;
        }
        SelectArticleResponseDto selectArticleResponseDto = VoPoConverter.copyProperties(article, SelectArticleResponseDto.class);
        selectArticleResponseDto.setPic(addImgUrlPreUtil.addImgUrlPres(article.getPic(), AliyunBucketType.UserBucket));
        User user = userServiceProvider.getUserService().selectById(article.getUserId());
        if (user == null) {
            throw new RuntimeException("发布者不存在");
        }
        selectArticleResponseDto.setAnonymity(article.getAnonymity());
        selectArticleResponseDto.setSex(user.getSex());
        selectArticleResponseDto.setAge(ComputeAgeUtils.getAgeByBirthday(user.getBirthday()));
        selectArticleResponseDto.setCredit(user.getCredit());
        selectArticleResponseDto.setNickname(user.getNickname());
        selectArticleResponseDto.setHeadImg(userPhotoAction.selectHeadImg(article.getUserId()));
        //2、判断显示位置状态
        if (!article.getShowLocation()) selectArticleResponseDto.setLocation(null);
        //其他信息
        selectArticleResponseDto.setInvitationNumber(getInvitationNumber(article.getId(), article.getUserId()));
        selectArticleResponseDto.setGiftNumber(getGiftNumber(article.getId(), article.getUserId()));
        selectArticleResponseDto.setCommentNumber(getCommentNumber(article.getId()));
        selectArticleResponseDto.setLikesNumber(getLikesNumber(article.getId()));
        selectArticleResponseDto.setHits((long) userServiceProvider.getArticleClickHistoryService().getClickCountByArticleId(article.getId()));
        return selectArticleResponseDto;
    }

    public List<SelectArticleResponseDto> selectArticleListInPublication(SelectArticleListRequestDto request, String userId) throws Exception {
        String publishUserId = request.getUserId();//发布者
        //分页查询
        Page<Article> articlePage = new Page<>(request.getCurrent(), request.getSize());
        List<Article> poList = userServiceProvider.getArticleService().getArticlesByUserId(articlePage, userId, request.getIsArticleType(), false, publishUserId);
        return refactorList(userId, poList, new ArrayList<>());
    }

    public Map<String, Object> selectArticleListInTop(SelectArticleListInTopRequestDto request, String userId) throws Exception {
        //查询置顶信息，置顶过期的、封禁的不查
        Page<Article> page = new Page<Article>(request.getCurrent(), request.getSize());
        List<Article> list = userServiceProvider.getArticleService().getArticlePage(page, request.getArticleTopTypeEnum().getValue(), System.currentTimeMillis(), false);
        List<SelectArticleResponseDto> topList = this.refactorList(userId, list, new ArrayList<>());
        Map<String, Object> map = new HashMap();
        map.put("topList", topList);
        return map;
    }

    public List<SelectArticleResponseDto> selectArticleListInLikes(SelectArticleListRequestDto request, String userId) throws Exception {
        String publishUserId = request.getUserId();//发布者(其实是“别人的个人中心”的userId)
        //查询点赞过的咨讯id（无法区分图文和音视，需要下面来区分）
        List<String> articleIdList = (List<String>) (List) userServiceProvider.getArticleLikesService().selectArticleIdByUserId(publishUserId);
        return queryArticles(userId, articleIdList, new Page(request.getCurrent(), request.getSize()), request.getIsArticleType());
    }

    private List<SelectArticleResponseDto> queryArticles(String userId, List<String> articleIdList, Page page, Boolean isArticleType) throws Exception {
        List<SelectArticleResponseDto> list = new ArrayList<>();
        if (articleIdList != null && articleIdList.size() > 0) {
            List<Article> articleList = userServiceProvider.getArticleService().getArticleListById(articleIdList, page, isArticleType);
            return refactorList(userId, articleList, list);
        }
        return list;
    }

    public List<SelectArticleResponseDto> selectCollectArticle(SelectArticleListRequestDto request, String userId) throws Exception {
        String publishUserId = request.getUserId();//发布者(其实是“别人的个人中心”的userId)
        //查询收藏过的图文id
        List<String> articleIdList = (List<String>) (List) userServiceProvider.getArticleCollectService().selectArticleIdByUserId(publishUserId);
        return queryArticles(userId, articleIdList, new Page(request.getCurrent(), request.getSize()), request.getIsArticleType());
    }

    /**
     * 返回咨讯列表需用此方法重构
     * 将 Article 列表重构为 SelectArticleResponseDto 列表
     * 重构 ArticleList，为图文列表添加头像
     * 详情见 SelectArticleResponseDto 类
     *
     * @param userId 使用者的 userId
     * @param list   咨讯列表
     * @return
     */
    private List<SelectArticleResponseDto> refactorList(String userId, List<Article> list, List<SelectArticleResponseDto> articleList) throws Exception {
        if (list != null && list.size() > 0) {
            list.forEach(article -> {
                try {
                    SelectArticleResponseDto selectArticleResponseDto = refactorEntityToDto(userId, article);
                    if (selectArticleResponseDto != null) {
                        articleList.add(selectArticleResponseDto);
                    }
                } catch (Exception e) {
                    logger.error(article.getId() + "：" + e.getMessage(), e);
                }
            });
        }
        return articleList;
    }

    private Boolean checkPay(Long cent) {
        List<CostSettingEnum> costSettingEnums = new ArrayList<>();
        costSettingEnums.add(CostSettingEnum.PIC_AND_VOICE_PUBLISH_DEPOSIT);
        Map<String, Object> map = costAction.query(costSettingEnums);
        return cent - Money.YuanToCent(map.get(CostSettingEnum.PIC_AND_VOICE_PUBLISH_DEPOSIT.name()).toString()) >= 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> selectArticleDetailMap(String userId, String articleId,Double lon,Double lat) throws Exception {
        Map<String, Object> map = new HashMap<>();
        //根据图文id找出图文详情
        Article article = userServiceProvider.getArticleService().selectById(articleId);
        if (article != null) {
            //获取图文发布者id
            String publishUserId = article.getUserId();
            //获取用户头像
            UserInfoAndHeadWatch userInfoAndHeadWatch;
            //判断图文发布者id和登陆者id是否相等
            if (!publishUserId.equals(userId)) {
                //不相同
                userInfoAndHeadWatch = getUserInfoAndHeadWatch(publishUserId, article.getAnonymity(), userId);
            } else {
                //相同
                userInfoAndHeadWatch = userAction.getUserInfoAndHeadWatch(publishUserId, userId);
            }
            map.put("userInfo", userInfoAndHeadWatch);
            SelectArticleDetailResponseDto detailResponseDto = VoPoConverter.copyProperties(article, SelectArticleDetailResponseDto.class);
            detailResponseDto.setLikesNumber(getLikesNumber(articleId));
            detailResponseDto.setInvitationNumber(getInvitationNumber(article.getId(), article.getUserId()));
            detailResponseDto.setGiftNumber(getGiftNumber(article.getId(), article.getUserId()));
            detailResponseDto.setCommentNumber(getCommentNumber(article.getId()));
            detailResponseDto.setIsLike(userServiceProvider.getArticleLikesService().getLike(userId, articleId));
            detailResponseDto.setIsCollect(userServiceProvider.getArticleCollectService().checkCollect(articleId, userId));
            detailResponseDto.setCollectNumber(userServiceProvider.getArticleCollectService().countNum(articleId, true));
            if(StringUtils.isNotBlank(article.getWorthIds())) {
                String[] ids=article.getWorthIds().split(",");
                if(article.getWorthType().equals(WorthTypeEnum.MATCH_TYPE.getName())) {
                    List<Object> objects=new ArrayList<>();
                    for (String id:
                    ids) {
                        List<MatchEventSimpleVo> matchEventSimpleVos=matchApplyAction.selectActiveMatchEventById(id);
                        if(matchEventSimpleVos!=null) {
                            if(matchEventSimpleVos.size()>0){
                                matchEventSimpleVos.get(0).setMatchImageUrl(matchCreateAction.pictures(matchEventSimpleVos.get(0).getMatchImageUrl()));
                                objects.add(matchEventSimpleVos.get(0));
                            }
                        }
                    }
                    detailResponseDto.setObjects(objects);
                }else if(article.getWorthType().equals(WorthTypeEnum.SKILL_TYPE.getName())) {
                    List<Object> objects=new ArrayList<>();
                    for (String id:
                            ids) {
                        objects.add(skillAction.getSkillById(id,lat,lon));
                    }
                    detailResponseDto.setObjects(objects);
                }else if(article.getWorthType().equals(WorthTypeEnum.WISH_TYPE.getName())) {
                    List<Object> objects=new ArrayList<>();
                    for (String id:
                            ids) {

                        Map wish =wishAction.getWishById(id);
                        objects.add(wish);
                    }
                    detailResponseDto.setObjects(objects);
                }else if(article.getWorthType().equals(WorthTypeEnum.DEMAND_TYPE.getName())) {
                    List<Object> objects=new ArrayList<>();
                    for (String id:
                            ids) {
                        Map demandListVo =demandAction.getDemandById(id,lon,lat);
                        objects.add(demandListVo);
                    }
                    detailResponseDto.setObjects(objects);
                }else if(article.getWorthType().equals(WorthTypeEnum.MEETING_TYPE.getName())) {
                    List<Object> objects=new ArrayList<>();
                    for (String id:
                            ids) {
                        MeetingDetailSendDto meetingDetailSendDto =meetingAction.getMeetingById(id,lat,lon);
                        objects.add(meetingDetailSendDto);
                    }
                    detailResponseDto.setObjects(objects);
                }else if(article.getWorthType().equals(WorthTypeEnum.MERCHANT_TYPE.getName())) {
                    List<Object> objects=new ArrayList<>();
                    for (String id:
                            ids) {
                        GetMerchantListVo merchantListVo=merchantFuseAction.getMerchantById(id,lon,lat);
                        objects.add(merchantListVo);
                    }
                    detailResponseDto.setObjects(objects);
                }else if(article.getWorthType().equals(WorthTypeEnum.PRODUCT_TYPE.getName())) {
                    List<Object> objects=new ArrayList<>();
                    for (String id:
                            ids) {
                        objects.add(productFuseAction.getProductById(id,new BigDecimal(lat),new BigDecimal(lon)));
                    }
                    detailResponseDto.setObjects(objects);
                }else {
                    List<Object> objects=new ArrayList<>();
                    detailResponseDto.setObjects(objects);
                }
            }
            map.put("detail", detailResponseDto);
            articleAction.deductClickFee(userId, article);
        }
        return map;
    }

    private UserInfoAndHeadWatch getUserInfoAndHeadWatch(String userId, boolean isMy, String toUserId) {
        //传的是图文发布者id
        UserInfoAndHeadWatch userInfoAndHeadWatch;
        //匿名发布显示
        if (isMy) {
            User user = userAction.queryUser(userId);
            userInfoAndHeadWatch = new UserInfoAndHeadWatch();
            userInfoAndHeadWatch.setNickname("匿名");
            userInfoAndHeadWatch.setCredit(user.getCredit());
            userInfoAndHeadWatch.setId(user.getId());
            //非匿名发布显示
        } else {
            userInfoAndHeadWatch = userAction.getUserInfoAndHeadWatch(toUserId, userId);
        }
        return userInfoAndHeadWatch;
    }

    @Transactional(rollbackFor = Exception.class)
    public String payAmount(ArticlePayForCompanyDto request, String userId) {
        //BigDecimal amount = request.getAmount();
        Money money = new Money(request.getAmount());
        String articleId = request.getTypeId();//事件id，在这个地方即是咨讯id
        PayTypeEnum tradeType = request.getTradeType();
        //System.out.println("押金标准："+wzCommonCostSetting.getPicAndVoicePublishDeposit());
        if (!checkPay(money.getCent())) {
            return "支付的费用低于押金最低标准, 支付失败";
        }
        //添加押金（或续费）到咨讯表
        Article article = userServiceProvider.getArticleService().selectById(articleId);
        if (article != null && StringUtils.isNotBlank(article.getUserId()) && userId.equals(article.getUserId())) {
            if (article.getStatusCode() == ArticleStatusCodeEnum.EXCEPTION.getValue()) {
                return "该咨讯异常，禁止支付或续费押金";
            }
            //零钱、微信、支付宝实质为零钱支付
            if (tradeType.getTradeType().equals("0")) {
                article.setAmountType(ArticleAmountTypeEnum.CHANGE.getValue());
            }

            //冻结金额操作，如果不成功，则支付失败
            if (!this.frozenPayOperation(userId, articleId, money.getCent(), tradeType, null)) return "支付失败";

            article.setUpdateUserId(userId);
            article.setId(articleId);
            long amount = article.getAmount() + money.getCent();
            article.setAmount(amount);
            //若原来的状态是代交押金的状态（说明此次操作不是续费），改变状态值为待审核，否则不变。
            if (amount >= 0l) {
                article.setIsLock(0);
            }
            if (article.getStatusCode() == ArticleStatusCodeEnum.UNPAID_AMOUNT.getValue()) {
                article.setStatusCode(ArticleStatusCodeEnum.PENDING.getValue());
            }
            //若原来的状态是押金不足的状态，则经过这次的押金续费，应该对此咨讯进行解锁，并更改状态为正常
            if (article.getStatusCode() == ArticleStatusCodeEnum.NOT_SUFFICIENT_FUNDS.getValue()) {
                article.setStatusCode(ArticleStatusCodeEnum.NORMAL.getValue());
                Boolean B = jobFuseAction.removeJob(JobEnum.ARTICLE_DELETED_JOB, articleId, "图文监控：" + articleId, articleId);
                if (!B) {
                    return "更改状态异常, 支付失败";
                }
            }
            return userServiceProvider.getArticleService().updateById(article) ? null : "支付失败";
        } else {
            return "支付人与发布人不是同一人, 支付失败";
        }
    }

    /**
     * 支付押金（或续费操作）
     *
     * @param userId     支付人
     * @param articleId  支付人发布的图文
     * @param cent       金额
     * @param tradeType  支付类型
     * @param currencyId 网币
     * @return true：支付成功    false：支付失败
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    boolean frozenPayOperation(String userId, String articleId, Long cent, PayTypeEnum tradeType, String currencyId) {
        CommonWalletFrozen frozen = new CommonWalletFrozen();

        //支付方式、网币id都需要前端传值
        frozen.setCreateTime(new Date());
        frozen.setBak1(tradeType.getTradeType());//支付方式
        frozen.setBak2(currencyId);//网币id

        frozen.setFrozenType("Article");//消费渠道活动
        frozen.setAmount(cent);
        frozen.setUserId(userId);
        frozen.setToUserId("999");//收钱用户id（公司）
        frozen.setDescription("支付咨讯押金");
        frozen.setTypeId(articleId);
        //若冻结失败，则支付失败
        return wallerFrozenFuseAction.addFrozenAndBill(frozen);
    }

    private Long getCent(BigDecimal money) {
        return new Money(money).getCent();
    }


    /**
     * 根据比赛查看article
     * @param userId
     * @param matchId
     * @param page
     * @return
     * @throws Exception
     */
    public List<SelectArticleResponseDto> getArticlesByMatchId(String userId, String matchId, Page<Article> page ) throws Exception {
        if (StringUtils.isBlank(userId)||StringUtils.isBlank(matchId)) {
            throw new RuntimeException("请选定比赛");
        }
        List<SelectArticleResponseDto> list = new ArrayList<>();
        List<Article> articleList = userServiceProvider.getArticleService().selectArticlePageByMatchId(matchId, page).getRecords();
        if (articleList != null && articleList.size() > 0) {
            return refactorList(userId, articleList, list);
        }
        return list;
    }
}
