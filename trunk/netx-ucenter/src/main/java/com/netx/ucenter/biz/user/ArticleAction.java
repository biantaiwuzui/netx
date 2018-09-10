package com.netx.ucenter.biz.user;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.common.enums.AliyunBucketType;
import com.netx.common.common.enums.FrozenTypeEnum;
import com.netx.common.common.enums.MessageTypeEnum;
import com.netx.common.common.enums.PushMessageDocTypeEnum;
import com.netx.common.redis.RedisInfoHolder;
import com.netx.common.router.dto.bean.UserInfoResponseDto;
import com.netx.common.router.dto.request.GeomRequestDto;
import com.netx.common.user.dto.article.ArticleCreateTimeDto;
import com.netx.common.user.dto.article.EditArticleRequestDto;
import com.netx.common.user.dto.article.PublishArticleRequestDto;
import com.netx.common.user.dto.user.UserInfoAndHeadImg;
import com.netx.common.user.dto.wangMing.AddCreditRecordRequestDto;
import com.netx.common.user.enums.*;
import com.netx.common.user.util.AddImgUrlPreUtil;
import com.netx.common.user.util.VoPoConverter;
import com.netx.common.vo.common.*;
import com.netx.searchengine.SearchServiceProvider;
import com.netx.searchengine.model.ArticleSearchResponse;
import com.netx.searchengine.query.ArticleSearchQuery;
import com.netx.ucenter.biz.common.*;
import com.netx.ucenter.biz.router.ScoreAction;
import com.netx.ucenter.biz.router.StatAction;
import com.netx.ucenter.model.common.CommonArticleLimit;
import com.netx.ucenter.model.common.CommonSensitive;
import com.netx.ucenter.model.user.Article;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.model.user.queryArticleClickHistoryCountData;
import com.netx.ucenter.service.common.SensitiveService;
import com.netx.ucenter.service.friend.FriendsService;
import com.netx.ucenter.service.user.ArticleCollectService;
import com.netx.ucenter.service.user.ArticleLikesService;
import com.netx.ucenter.service.user.ArticleService;
import com.netx.ucenter.service.user.UserPhotoService;
import com.netx.ucenter.util.ListToString;
import com.netx.ucenter.util.SearchProcessing;
import com.netx.ucenter.vo.request.QueryArticleListRequestDto;
import com.netx.ucenter.vo.response.QueryArticleListResponseDto;
import com.netx.ucenter.vo.response.SelectUserSettingResponseDto;
import com.netx.ucenter.vo.response.UserStatData;
import com.netx.utils.cache.RedisCache;
import com.netx.utils.money.Money;
import org.elasticsearch.common.geo.GeoPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.netx.ucenter.util.TupleToList;
import redis.clients.jedis.Tuple;

/**
 * <p>
 * 资讯表（图文、音视） 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-09-14
 */
@Service
public class ArticleAction {
    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Autowired
    private UserAction userAction;
    @Autowired
    private UserPhotoService userPhotoService;
    @Autowired
    private UserCreditAction userCreditAction;
    @Autowired
    private ArticleLikesService articleLikesService;
    @Autowired
    private ArticleLimitedAction articleLimitedAction;
    @Autowired
    private SensitiveService sensitiveService;
    @Autowired
    private WalletFrozenAction walletFrozenAction;
    @Autowired
    private CostAction costAction;
    @Autowired
    private FriendsService friendsService;
    @Autowired
    private UserWatchAction userWatchAction;
    @Autowired
    private AddImgUrlPreUtil addImgUrlPreUtil;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private SearchServiceProvider searchServiceProvider;
    @Autowired
    private WzCommonImHistoryAction wzCommonImHistoryAction;
    @Autowired
    private ArticleClickHistoryAction articleClickHistoryAction;
    @Autowired
    private ArticleCollectService articleCollectService;
    @Autowired
    private StatAction statAction;
    @Autowired
    private ScoreAction scoreAction;
    @Autowired
    private WalletAction walletAction;
    @Autowired
    private RedisInfoHolder redisInfoHolder;

    private RedisCache redisCache;

    private RedisCache clientRedis() {
        redisCache = RedisCache.getRedisCache(redisInfoHolder.getHost(), redisInfoHolder.getPassword(), redisInfoHolder.getPort());
        return redisCache;
    }

    private final int maxCount = 50;

    public void deleteByUserId(String userId) {
        List<String> articles = articleService.queryArticleIdsByUserId(userId);
        articleService.deleteByUserId(userId);
        articleClickHistoryAction.getArticleClickHistoryService().deleteClickHistory(userId, articles);
        articleLikesService.deleteArticleByUserId(userId, articles);
        articleCollectService.deleteCollectByUserId(userId, articles);
    }


    public Map<String, Object> queryArticleStat(String fromUserId, int start, int end) {
        //取图文排行redis
        Set<Tuple> set = clientRedis().zrevrangeWithScores("ArticleStat", 0, -1);
        //存返回数据的map
        Map<String, Object> result = new HashMap<>();
        //如果redis取到了数据
        if (set.size() > 0) {
            result.put("list", TupleToList.tupleToList(set, fromUserId, result, start, end));
        }
        //程序开始时间
        long startTime = System.nanoTime();
        //如果从redis没取到数据，那么从数据库重新读入写入redis，并返回前端
        if (set.size() == 0) {
            //获取每个用户的分数
            List<queryArticleClickHistoryCountData> im=articleClickHistoryAction.getArticleClickHistoryService().queryArticleClickHistoryCount();
            Map<String,Integer> baseMap=new HashMap<>();
            for (int j=0;j<im.size();j++){
                baseMap.put(im.get(j).getUserId(),im.get(j).getSorce());
            }
            //获取所有用户的信息
            List<UserStatData> userStatDatas = userAction.getUserStatData();
            int a=userStatDatas.size();
            if (userStatDatas != null && userStatDatas.size() > 0) {
                userStatDatas.forEach(userStatData -> {
                    userStatData.setHeadImg(addImgUrlPreUtil.getUserImgPre(userStatData.getHeadImg()));
                    if (baseMap.get(userStatData.getId())!=null){
                        userStatData.setNum(new BigDecimal(baseMap.get(userStatData.getId())));
                    }
                    else {
                        userStatData.setNum(BigDecimal.ZERO);
                    }
                    clientRedis().zaddOne("ArticleStat", userStatData.getNum().doubleValue(), ListToString.objectToJson(userStatData), 600);
                });
            }
            Set<Tuple> set1 = clientRedis().zrevrangeWithScores("ArticleStat", 0, -1);
            if (set1.size() > 0) {
                result.put("list", TupleToList.tupleToList(set1, fromUserId, result, start, end));
            }
        }
        //获取结束时间
        long endTime = System.nanoTime();
        System.out.println("ArticleStat排行榜遍历sql程序运行时间： " + (endTime - startTime) + "ns"+(endTime - startTime)/1000000000+"s");
        return result;
    }

    private void addHit(String userId, String artcleId, Map<String, Integer> map) {
        Integer hit = map.get(userId);
        int nowHit = articleLikesService.selectLikesNumber(artcleId);
        if (hit == null) {
            map.put(userId, nowHit);
        } else {
            map.put(userId, hit + nowHit);
        }
    }

    public List<Article> selectPageByStatusCode(Integer current, Integer size, Integer statusCode) throws Exception {
        Page<Article> page = new Page<>();
        page.setSize(size);
        page.setCurrent(current);
        return articleService.selectPageByStatusCode(page, statusCode).getRecords();
    }

    public Map<String, Object> quertNearlArticle(GeomRequestDto dto, String userId, Double lon, Double lat) {
        //ES查询查询参数类：类似于mybait-plus的查询参数类
        ArticleSearchQuery articleSearchQuery = new ArticleSearchQuery();
        //分页参数设置
        articleSearchQuery.setPage(dto.getCurrentPage(), dto.getSize());
        //将当前用户定位lat，lon设置，以获取附近质询
        articleSearchQuery.setCenterGeoPoint(new GeoPoint(lat, lon));
        //搜索正常状态的咨询0
        articleSearchQuery.setIsLock(0);
        //设置标签筛选
        if (org.apache.commons.lang.StringUtils.isNotEmpty(dto.getTagName()))
            articleSearchQuery.setTagName(dto.getTagName());
        List<ArticleSearchResponse> responseList = searchServiceProvider.getArticleSearchService().queryArticle(articleSearchQuery);
        List<String> userIds = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        if (responseList != null && responseList.size() > 0) {
            Set<String> anonymity = new HashSet<>();
            responseList.forEach(articleSearchResponse -> {
                articleSearchResponse.setPic(addImgUrlPreUtil.addImgUrlPres(articleSearchResponse.getPic(), AliyunBucketType.UserBucket));
                userIds.add(articleSearchResponse.getUserId());
            });
            map = userAction.getUsersAndHeadImg(userIds);
            //addAnonymity(map,anonymity);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("list", responseList);
        result.put("userData", map);
        return result;
    }

    private void addAnonymity(Map<String, Object> map, Set<String> anonymity) {
        UserInfoAndHeadImg userInfoAndHeadImg;
        for (String userId : anonymity) {
            userInfoAndHeadImg = new UserInfoAndHeadImg();
            userInfoAndHeadImg.setId(userId);
            userInfoAndHeadImg.setNickname("匿名");
            try {
                User user = userAction.queryUser(userId);
                userInfoAndHeadImg.setSex(user.getSex());
                userInfoAndHeadImg.setBirthday(user.getBirthday());
                userInfoAndHeadImg.setCredit(user.getCredit());
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }
            map.put(userId, userInfoAndHeadImg);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public String editArticle(EditArticleRequestDto dto, String userId) {
        Article article = articleService.selectById(dto.getId());
        if (article == null) {
            return "该图文已被删除";
        }
        if (!userId.equals(article.getUserId())) {
            return "非发布者不能修改";
        }
        if (article.getAdvertorialType().equals(ArticleAdvertorialTypeEnum.NORMAL_ARTICLE.getValue())) {
            if (article.getStatusCode().equals(ArticleStatusCodeEnum.NORMAL)) {
                return "你发布的免费图文已正常显示，无需修改";
            }
        }
        articleService.getArticleMapper().updateArticleContent(dto);
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public String publishArticle(PublishArticleRequestDto request, String userId, Double lon, Double lat) {
        //判断是否有权限
        if(whetherTheSameContentExists(request.getContent())){
            throw new RuntimeException("不能重复发布图文");
        }
        if (!articleLimitedAction.isCanReleaseArticle(userId, System.currentTimeMillis())) {
            throw new RuntimeException("此用户没有权限发布图文");
        }
        Article article = new Article();
        article.setUserId(userId);
        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.setHits(0l);
        article.setLon(new BigDecimal(lon));
        article.setLat(new BigDecimal(lat));
        article.setCreateUserId(userId);
        article.setUserId(userId);
        article.setTitle(request.getTitle());
        article.setPic(request.getPic());
        article.setAnonymity(request.getAnonymity());//是否匿名
        article.setShowLocation(request.getShowLocation());//是否显示位置
        article.setAdvertorialType(request.getIsAdvertorial() ? 1 : 0);//设置图文类型
        article.setIsLock(0);//发布后的咨讯都会锁定，用户交了押金才会解锁
        article.setStatusCode(ArticleStatusCodeEnum.UNPAID_AMOUNT.getValue());//未交押金状态
        //存储位置和经纬度
        article.setLocation(request.getLocation());
        //指定谁可以看此咨讯动态
        Integer value = whoCan(request.getWho());
        article.setWho(value);
        //只有“指定部分好友”的情况，receiver 才起作用，已经在控制器里面进行拦截不适当的访问
        article.setReceiver(request.getReceiver());
        article.setLength(request.getLength());
        if (request.getWorthTypeEnum() != null) {
            article.setWorthType(request.getWorthTypeEnum().getName());

        }
        article.setWorthIds(request.getWorthTypeIds());
        return articleService.insert(article) ? article.getId() : null;
    }

    public Integer selectArticleCountByUserId(String userId, Boolean isArticleType) throws Exception {
        return articleService.selectArticleCountByUserId(userId, isArticleType, null);
    }

    public String selectUserIdByArticleId(String articleId) {
        Article article = articleService.selectById(articleId);
        if (article == null) {
            return null;
        }
        return article.getUserId();
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteArticleByUser(String userId, String articleId) {
        Article article = articleService.selectById(articleId);
        if (article != null && article.getUserId() != null && userId.equals(article.getUserId())) {
            //删除咨讯和咨讯点赞记录
            if (!this.delete(articleId)) throw new RuntimeException("删除咨讯失败");
            //若不是“待交押金”状态，需要返还押金
            if (article.getAmount() > 0) {
                String description = "自行删除" + article.getTitle() + "图文返还" + (Money.CentToYuan(article.getAmount()).getAmount()) + "元押金";
                walletAction.pay("999", articleId, userId, article.getAmount(), 0, description, 3);
            }
            if (article.getLength()>140&&article.getPic()!=null){
                scoreAction.addScore(article.getUserId(), -15);
            }
            return true;
        } else {
            return false;
        }

    }


    /**
     * by lcx
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteArticleByBoss(String articleId) {
        Article article = articleService.selectById(articleId);
        if (article != null) {
            long amount = article.getAmount();
            //删除咨讯和咨讯点赞记录
            if (!this.delete(articleId)) throw new RuntimeException("删除咨讯失败");
            if (amount > 0) {
                String description = "系统删除" + article.getTitle() + "图文返还" + (Money.CentToYuan(amount).getAmount()) + "元押金";
                walletAction.pay("999", articleId, article.getUserId(), amount, 0, description, 3);
            }
            //如果删除符合加分规则的图文，扣15分
            if (article.getPic()!=null&&article.getLength()>140){
                scoreAction.addScore(article.getUserId(), -15);
            }
            return true;
        } else {
            return false;
        }

    }

    /**
     * 退还押金
     *
     * @param articleId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean returnAmount(String articleId) {
        Article article = articleService.selectById(articleId);
        if (article != null) {
            //若不是“待交押金”状态，需要返还押金
            if (article.getAdvertorialType().equals(ArticleAdvertorialTypeEnum.NORMAL_ARTICLE.getValue())) {
                long amount = article.getAmount();
                if (amount > 0) {
                    walletAction.pay("999", articleId, article.getUserId(), amount, 0, article.getTitle() + "审核通过，返还" + Money.CentToYuan(amount).getAmount() + "元押金", 3);
                    article.setAmount(0l);
                }
            }
            article.setStatusCode(ArticleStatusCodeEnum.NORMAL.getValue());
            if (!articleService.updateById(article)) {
                throw new RuntimeException("审核通过异常");
            }
            return true;
        }
        return false;
    }

    public boolean deleteUnpaidArticle(String articleId) {
        Article article = articleService.selectById(articleId);
        //有可能，在没交押金时，发布者自主删掉了
        if (article == null) return true;
        //若24小时后咨讯的状态还是“待交押金”，则删除
        if (article.getStatusCode() == ArticleStatusCodeEnum.UNPAID_AMOUNT.getValue()) {
            if (!articleService.deleteById(articleId)) return false;
        }
        return true;
    }

    //------ 私有 start ------

    /**
     * 删除咨讯，并且删除该咨讯所有的有关的点赞
     *
     * @param articleId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    boolean delete(String articleId) {
        //删除咨讯，根据图文id
        if (!articleService.deleteByUserId(articleId)) return false;
        //删除所有相关的点赞记录
        articleLikesService.deleteByArticleId(articleId);
        return true;
    }

    /**
     * 扣除点击费用
     *
     * @param userId  使用者（若发布者与使用者同一人，不需要扣点击费用）
     * @param article 需要查看的咨讯
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void deductClickFee(String userId, Article article) throws Exception {
        String publishUserId = article.getUserId();//发布者
        //若发布者的 userId 等于 使用者的 userId，则说明是同一人，不需要扣掉点击费
        if (!publishUserId.equals(userId)) {
            if (articleClickHistoryAction.addArticleClickHistory(article.getId(), userId)) {
                article.setHits(Long.valueOf(articleClickHistoryAction.getClickCount(article.getId())));
                Integer advertorialType = article.getAdvertorialType();
                scoreAction.addScore(publishUserId, StatScoreEnum.SS_READ_ARTICLE);
                //不是软文或违规软文，不需要扣点击费用
                if (advertorialType != ArticleAdvertorialTypeEnum.NORMAL_ARTICLE.getValue()) {
                    scoreAction.addScore(publishUserId, StatScoreEnum.SS_READ_PAY_ARTICLE);
                    // 扣除点击费用
                    CostSettingResponseDto wzCommonCostSetting = costAction.query();
                    //费用
                    BigDecimal fee = article.getIllegal() ? wzCommonCostSetting.getViolationClickFee() : wzCommonCostSetting.getClickFee();
                    Money feeMoney = new Money(fee);
                    Long yu = article.getAmount() - feeMoney.getCent();
                    // 若扣掉这次的点击费用后，押金等于或小于0，则进行对此咨讯封禁。
                    String alertMsg;
                    Map<String, Object> param = new HashMap<>();
                    param.put("title", article.getTitle());
                    param.put("id", article.getId());
                    if (yu <= 0) {
                        article.setIsLock(1);
                        //推送内容
                        alertMsg = "你发布的\"" + article.getTitle() + "\"图文目前已有" + article.getHits() + "次点击，"
                                + "预付的押金余额为 " + getMoney(yu) + "元，此图文现已被封禁，预需继续显示，请尽快续费";
                        wzCommonImHistoryAction.add("999", article.getUserId(), alertMsg, article.getId(), MessageTypeEnum.USER_TYPE, PushMessageDocTypeEnum.ARTICLE_PAY_DETAIL, param);
                        //messagePushAction.sendMessageAlias(MessageTypeEnum.USER_TYPE,alertMsg,"你发布的"+article.getTitle()+"的图文押金已经超额",publishUserId,null,null);
                    } else if (yu <= feeMoney.getCent() * maxCount) {
                        //推送内容
                        alertMsg = "你发布的\"" + article.getTitle() + "\"图文目前已有" + article.getHits() + "次点击，"
                                + "预付的押金余额为 " + getMoney(yu) + "元，预需继续发布，请尽快续费，逾期该图文将不再显示";
                        wzCommonImHistoryAction.add("999", article.getUserId(), alertMsg, article.getId(), MessageTypeEnum.USER_TYPE, PushMessageDocTypeEnum.ARTICLE_PAY_DETAIL, param);
                        //messagePushAction.sendMessageAlias(MessageTypeEnum.USER_TYPE,alertMsg,"你发布的"+article.getTitle()+"的图文押金不多了",publishUserId,null,null);
                    }
                    article.setAmount(yu);
                }
                articleService.updateById(article);
            }
        }
    }


    /**
     * 判断使用者是否有权限查看该咨讯
     *
     * @param userId  使用者
     * @param article 咨讯
     * @return true:有权限   false:没有权限
     */
    public boolean isCheckOutArticle(String userId, Article article) throws Exception {
        Integer whoValue = article.getWho();
        String publishUserId = article.getUserId();
        //若使用者与发布者是同一人
        if (userId.equals(publishUserId)) {
            return true;
        }
        //1、如果该用户是游客，则仅公开设置了“所有人”的咨讯查看。
        if (!StringUtils.hasText(userId)) {
            //System.out.println("此用户是游客");
            if (whoValue == WhoCanEnum.ALL.getValue()) {
                return true;
            } else {
                return false;
            }
        }

        //2、判断发布者的 articleSetting 开关
        SelectUserSettingResponseDto selectUserSettingResponseDto = userAction.selectUserSetting(publishUserId);
        //如果发布者设置隐私“仅好友查看咨讯”，则存在“所有好友”“指定好友”这两种情况，其中“指定好友”需要做多一次判断
        if (selectUserSettingResponseDto.getArticleSetting() == ArticleSettingEnum.FRIENDS.getValue()) {
            //是否为好友
            if (!friendsService.checkFriendOne(userId, publishUserId)) {
                return false;
            }
            //若发布者设置的是“指定好友”，则要进行多一次判断
            if (whoValue == WhoCanEnum.DESIGNED_FRIENDS.getValue()) {
                String[] receiver = article.getReceiver().split(",");
                boolean isDesignedFriends = true;//记录是否是指定好友
                for (String str : receiver) {
                    if (userId.equals(str)) {
                        break;//若是指定好友，则跳出循环，进行公开咨讯操作
                    } else {
                        isDesignedFriends = false;
                    }
                }
                if (!isDesignedFriends) return false;
            }
        }
        //3、判断是否可以公开咨讯
        if (whoValue == WhoCanEnum.ALL_FRIENDS.getValue()) {//所有好友，判断是否是好友
            if (!friendsService.checkFriendOne(userId, publishUserId)) {
                return false;
            }
        } else if (whoValue == WhoCanEnum.WATCH_PEOPLE.getValue()) {//发布者关注的人
            if (userWatchAction.checkWatch(publishUserId, userId) == null) {
                return false;
            }
        } else if (whoValue == WhoCanEnum.PEOPLE_WATCH.getValue()) {//关注发布者的人
            if (userWatchAction.checkWatch(userId, publishUserId) == null) {
                return false;
            }
        } else if (whoValue == WhoCanEnum.DESIGNED_FRIENDS.getValue()) {//指定好友
            if (!friendsService.checkFriendOne(userId, publishUserId)) {
                return false;
            }
            String[] receiver = article.getReceiver().split(",");
            boolean flag = false;//记录是否是指定好友
            for (String str : receiver) {
                if (userId.equals(str)) {
                    flag = true;
                    break;//若是指定好友，则跳出循环，进行公开咨讯操作
                }
            }
            if (!flag) return false;
        }
        return true;
    }

    /**
     * 用于判断对谁公开咨讯
     *
     * @param whoCanEnum
     * @return
     */
    private Integer whoCan(WhoCanEnum whoCanEnum) {
        Integer value = 0;
        switch (whoCanEnum) {//指定谁可以看此图文动态
            case ALL:
                value = WhoCanEnum.ALL.getValue();
                break;
            case ALL_FRIENDS:
                value = WhoCanEnum.ALL_FRIENDS.getValue();
                break;
            case WATCH_PEOPLE:
                value = WhoCanEnum.WATCH_PEOPLE.getValue();
                break;
            case PEOPLE_WATCH:
                value = WhoCanEnum.PEOPLE_WATCH.getValue();
                break;
            case DESIGNED_FRIENDS://指定部分好友才能可以看此图文
                value = WhoCanEnum.DESIGNED_FRIENDS.getValue();
                break;
        }
        return value;
    }

    //------ 私有 end ------

    public List<ArticleCommonResponseDto> selectByArticleTypeOrTitleOrUserid(SelectAriticleListRequestDto request) throws Exception {
        String userId = null;
        if (!StringUtils.isEmpty(request.getUserNumber())) {
            User user = userAction.getUserService().getUserLikeUserNumber(request.getUserNumber());
            if (user != null && !StringUtils.isEmpty(user.getId())) {
                userId = user.getId();
            }
        }
        List<Article> articles = articleService.getArticleList(request.getArticleType(), request.getTitle(), userId);
        if (articles.isEmpty()) {
            return null;
        } else {
            List<ArticleCommonResponseDto> list = new ArrayList<>();
            User u = null;
            String headUrl = null;
            for (Article article : articles) {
                ArticleCommonResponseDto one = new ArticleCommonResponseDto();
                if (!StringUtils.isEmpty(article.getUserId())) {
                    u = userAction.getUserService().selectById(article.getUserId());
                    if (u != null) {
                        VoPoConverter.copyProperties(u, one);
                        try {
                            one.setHeadImgUrl(userPhotoService.selectHeadImg(article.getUserId()));
                        } catch (Exception e) {
                            logger.warn("用户id:" + one.getUserId() + "头像没有获取到，" + e.getMessage());
                        }
                    }
                }
                VoPoConverter.copyProperties(article, one);
                list.add(one);
            }
            return list;
        }
    }


    public boolean editArticleDetail(EditArticleDetailRequestDto request) throws Exception {
        Article srcArticle = articleService.selectById(request.getId());
        if (srcArticle == null) {
            throw new RuntimeException("此图文不存在" + request.getId());
        }
        Article article = new Article();
        article.setAnonymity(request.getAnonymity());
        article.setAtta(request.getAtta());
        article.setContent(request.getContent());
        article.setTitle(request.getTitle());
        article.setShowLocation(request.getShowLocation());
        if (request.getWho().getValue() != srcArticle.getWho()) {
            article.setWho(request.getWho().getValue());
            if (request.getWho() == WhoCanEnum.DESIGNED_FRIENDS) {
                article.setReceiver(request.getReceiver());
            }
        }
        article.setAdvertorialType(request.getAdvertorial() ? 1 : 0);
        article.setId(request.getId());
        article.setUserId(request.getUserId());
        article.setUpdateTime(new Date());
        article.setUpdateUserId(request.getUserId());
        return articleService.updateById(article);
    }

    @Transactional(rollbackFor = Exception.class)
    public String deleteArticleByID(List<String> articleIds) throws Exception {
        Article article = null;
        try {
            List<Article> articleList = articleService.getArticleListById(articleIds, null, null);
            if (articleList == null || articleList.size() == 0) {
                return "异常咨询不存在";
            }
            for (Article obj : articleList) {
                //根据userid查询 创建者的信用值,然后修改值再更新
                User user = userAction.getUserService().selectById(obj.getUserId());
                if (user.getCredit() == null) {
                    logger.warn("个人用户没有信用值");
                }
                //调用添加信用流水
                AddCreditRecordRequestDto addCreditRecordRequestDto = new AddCreditRecordRequestDto();
                addCreditRecordRequestDto.setCredit(-5);
                addCreditRecordRequestDto.setRelatableType(Article.class.getSimpleName());
                addCreditRecordRequestDto.setRelatableId(obj.getId());
                addCreditRecordRequestDto.setUserId(obj.getUserId());
                addCreditRecordRequestDto.setDescription("咨询后台删除咨询扣除信用值5分");
                //冻结押金
                FrozenOperationRequestDto frozenOperationRequestDto = new FrozenOperationRequestDto();
                frozenOperationRequestDto.setTypeId(obj.getId());
                frozenOperationRequestDto.setUserId(obj.getUserId());
                frozenOperationRequestDto.setType(FrozenTypeEnum.FTZ_ARTICLE);
                obj.setAmount(0L); //把押金变为0
                obj.setUpdateTime(new Date());
                walletFrozenAction.pay(frozenOperationRequestDto);

                if (articleService.updateById(obj) && (userCreditAction.addCreditRecord(addCreditRecordRequestDto) && this.delete(obj.getId()))) {
                    /*JpushDto jpushDto = new JpushDto();
                    MessageFormat messageFormat = new MessageFormat(ArticleMessagePushEnum.DELETE_AND_RELEASE_CREDIT.getMsg());
                    jpushDto.setAlertMsg(messageFormat.format(new String[]{obj.getTitle()}));
                    jpushDto.setTitle("咨询异常删除通知");
                    jpushDto.setUserId(obj.getUserId());
                    jpushDto.setType(MessageTypeEnum.USER_TYPE);
                    messagePushAction.sendMessageAlias(jpushDto);*/
                    return null;
                } else {
                    return "删除咨询异常";
                }
            }
        } catch (NullPointerException e) {
            logger.error(e.getMessage(), e);
        }
        return "删除失败";
    }

    public boolean pushOrPopArticleException(Integer todo, ArticleExceptionPushOrPopRequestDto request) {
        Article article = articleService.selectById(request.getArticleId());
        return pushOrPopArticleException(article, todo, request);
    }


    /**
     * 该方法是列入/解除异常资讯的方法
     * 1.首先，资讯表的数据库表与model类字段的不匹配，现在假设
     *
     * @param todo 1.列入异常         2.解除异常
     *             reason：资讯原因
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean pushOrPopArticleException(Article article, Integer todo, ArticleExceptionPushOrPopRequestDto request) {
        //列入资讯异常之前，查找所有咨询
        if (article == null) {
            logger.info("异常咨询为空");
            return false;
        }
        try {
            //现在是异常资讯,列入异常的话不操作,反之也一样
            if (article.getStatusCode().intValue() == 1) {
                //解除异常操作的话,就把statusCode变成0
                if (todo == 2) {
                    article.setStatusCode(ArticleStatusCodeEnum.NORMAL.getValue());
                    article.setIsLock(0);
                    article.setReason(request.getReason());
                    if (articleService.updateById(article)) {
                        return true;
                    }
                    logger.error("解除" + article.getUserId() + "咨询异常失败");
                }
            } else {
                //列入异常资讯
                if (todo == 1) {
                    article.setStatusCode(ArticleStatusCodeEnum.EXCEPTION.getValue());
                    article.setIsLock(1);
                    article.setReason(request.getReason());
                    if (articleService.updateById(article)) {
                        return true;
                    }
                    logger.error("列入" + article.getUserId() + "咨询异常失败");
                }
            }
        } catch (NullPointerException e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }


    public UserInfoResponseDto queryByUserNetworkNumId(String userNetworkNumId) {
        UserInfoResponseDto responseVo = articleLimitedAction.queryByUserNetworkNum(userNetworkNumId);
        return responseVo;
    }


    public boolean punishAuthor(PunishAuthorTransferRequestDto request) throws Exception {
        return articleLimitedAction.punishAuthorByNetworkNum(request);
    }


    public boolean sanctionAritcle(ArticleSanctionRequestDto request) {
        return articleLimitedAction.sanctionAritcle(request);
    }

    /**
     * 执行到这里,不是列表选中就是类别选中
     *
     * @param requestDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)

    public boolean introduceToTop(ArticleIntroduceToTopRequestDto requestDto) {
        Integer top = requestDto.getAllListTopRefund() == 0 ? 1 : 2;
        String value = requestDto.getAllListTopRefund() == 0 ? requestDto.getAllListTopValue() : requestDto.getClassListTopValue();
        ArticleIntroduceDateTypeEnum dateTypeEnum = requestDto.getAllListTopRefund() == 0 ? requestDto.getAllListTopUnit() : requestDto.getClassListTopUnit();
        //匹配正整数
        Pattern pattern = Pattern.compile("^\\d*[1-9]\\d*$");
        Matcher matcher = pattern.matcher(value);
        if (value.length() > 0 && matcher.matches()) {
            List<Article> articleList = articleService.selectBatchIds(requestDto.getArticleIds());
            if (articleList == null || articleList.size() == 0) {
                System.out.println("选择的资讯不存在");
                return false;
            }
            for (Article article : articleList) {
                Long time = new Date().getTime();
                switch (dateTypeEnum) {
                    case ARTICLE_INTRODUCE_DATE_TYPE_HOUR:
                        article.setTopExpiredAt(new Date(time + Integer.parseInt(value) * 3600 * 1000));
                        break;
                    case ARTICLE_INTRODUCE_DATE_TYPE_DAY:
                        article.setTopExpiredAt(new Date(time + Integer.parseInt(value) * 3600 * 24 * 1000));
                        break;
                    case ARTICLE_INTRODUCE_DATE_TYPE_WEEK:
                        article.setTopExpiredAt(new Date(time + Integer.parseInt(value) * 3600 * 24 * 7 * 1000));
                        break;
                    case ARTICLE_INTRODUCE_DATE_TYPE_MONTH:
                        article.setTopExpiredAt(new Date(time + Integer.parseInt(value) * 3600 * 24 * 30 * 1000));
                        break;
                }
                article.setTop(top);
                if (!articleService.updateById(article)) {
                    logger.error("咨询ID为:" + article.getId() + "置顶失败!");
                }
            }
            return true;
        }
        return false;
    }


    public void commandUpdate(List<String> articleIds) throws Exception {
        List<Article> list = articleService.getArticleListById(articleIds, null, null);
        if (!list.isEmpty()) {
            for (Article article : list) {
                /*JpushDto jpushDto=new JpushDto();
                MessageFormat messageFormat=new MessageFormat(ArticleMessagePushEnum.COMMAND_UPDATE.getMsg());
                jpushDto.setAlertMsg(messageFormat.format(new String[]{article.getTitle()}));
                jpushDto.setTitle("异常资讯要求修改通知");
                jpushDto.setUserId(article.getUserId());
                jpushDto.setType(MessageTypeEnum.USER_TYPE);
                messagePushAction.sendMessageAlias(jpushDto);*/
            }
        }
    }


    public String advertorialBoolean(ArticleDeleteAndReleaseScoreRequestDto request) throws Exception {
        //查找待审核被封禁的并且选择的
        List<Article> list = articleService.getArticleList(ArticleStatusCodeEnum.PENDING.getValue(), true, request.getArticleIds());
        if (!list.isEmpty()) {
            for (Article article : list) {
                //发布者没有定义为广告软文标志
                if (article.getAdvertorialType() == 1) {
                    article.setAdvertorialType(ArticleAdvertorialTypeEnum.ILLEGAL_ADVERTORIAL.getValue());//违规软文
                }
                //发布者定义了广告软文标志
                else {
                    article.setAdvertorialType(ArticleAdvertorialTypeEnum.NORMAL_ADVERTORIAL.getValue());//正常软文
                }
                article.setUpdateTime(new Date());
                article.setUpdateUserId(request.getAuditUserId());//审核人
                if (!articleService.updateById(article)) {
                    logger.error("更新资讯:" + article.getTitle() + "为广告失败了");
                    return "部分资讯广告操作失败！";
                }
                if (!this.containSensitive(article.getId())) {//不含有敏感词
                    Article article1 = articleService.selectById(article.getId());
                    article1.setIsLock(0);
                    article1.setStatusCode(ArticleStatusCodeEnum.NORMAL.getValue());
                    articleService.updateById(article1);
                } else {
                    Article article1 = articleService.selectById(article.getId());
                    article1.setStatusCode(ArticleStatusCodeEnum.EXCEPTION.getValue());
                    article1.setUpdateTime(new Date());
                    articleService.updateById(article1);
                    return "部分资讯含有敏感词，已过滤，并加入到异常列表中！";
                }
            }
            return null;
        }
        return "没有找到资讯列表！";
    }

    @Transactional(rollbackFor = Exception.class)
    public String managementArticlePublish(ArticleDeleteAndReleaseScoreRequestDto request) throws Exception {
        //查找被封禁的并且选择的
        List<Article> list = articleService.getArticleList(ArticleStatusCodeEnum.PENDING.getValue(), true, request.getArticleIds());
        if (!list.isEmpty()) {
            for (Article article : list) {
                //article.setLock(false);//解禁资讯
                //article.setStatusCode(ArticleStatusCodeEnum.NORMAL.getValue());//正常
                if (!this.containSensitive(article.getId())) {//不含有敏感词，解锁
                    //发布者没有申明是软文
                    if (article.getAdvertorialType() == 0) {
                        //撤销资讯押金
                        FrozenOperationRequestDto requestDto = new FrozenOperationRequestDto();
                        requestDto.setTypeId(article.getId());
                        requestDto.setUserId(article.getUserId());
                        if (walletFrozenAction.repealDeposit(requestDto)) {
                            Article article1 = articleService.selectById(article.getId());
                            article1.setIsLock(0);
                            article1.setStatusCode(ArticleStatusCodeEnum.NORMAL.getValue());
                            article1.setAmount(0l);
                            article1.setUpdateTime(new Date());
                            articleService.updateById(article1);
                        }
                    } else {
                        return "部分咨询被发布者申明为软文，不能进行免费发布";//部分资讯是软文，不能免费发布，并通知审核人信息
                    }
                } else {
                    Article article1 = articleService.selectById(article.getId());
                    article1.setStatusCode(ArticleStatusCodeEnum.EXCEPTION.getValue());
                    article1.setUpdateTime(new Date());
                    articleService.updateById(article1);
                    return "部分资讯含有敏感词，已过滤，并加入到异常列表中！";//异常
                }
            }
            return null;
        }
        return "没有找到资讯列表！";
    }


    public List<ArticleLockListResponseDto> selectArticleLockList(Integer current, Integer size) throws Exception {
        Page<Article> page = new Page(current, size);
        List<Article> list = articleService.getArticlePage(page);
        if (list != null && !list.isEmpty()) {
            List<ArticleLockListResponseDto> result = new ArrayList<>();
            for (Article article : list) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                ArticleLockListResponseDto one = new ArticleLockListResponseDto();
                User user = userAction.getUserService().selectById(article.getUserId());
                if (user != null) {
                    one = VoPoConverter.copyProperties(user, ArticleLockListResponseDto.class);
                }
                one.setArticleId(article.getId());
                one.setAmount(getMoney(article.getAmount()));
                one.setHits(article.getHits());
                one.setTitle(article.getTitle());
                one.setDate(simpleDateFormat.format(article.getCreateTime()));
                result.add(one);
            }
            return result;
        }
        return null;
    }

    private BigDecimal getMoney(Long cent) {
        return new BigDecimal(Money.getMoneyString(cent));
    }

    private Long getCent(BigDecimal money) {
        return new Money(money).getCent();
    }


    public boolean containSensitive(String articleId) throws Exception {
        Article article = articleService.selectById(articleId);
        boolean isTitleTrue = false;
        boolean isContentTrue = false;
        if (article == null) {
            logger.warn("未找到对应的资讯记录.....");
            return false;
        }
        List<CommonSensitive> list = sensitiveService.getWzCommonSensitiveList();
        for (CommonSensitive wzCommonSensitive : list) {
            if (!StringUtils.isEmpty(article.getTitle())) {
                int count = 0;
                //查找到敏感词
                if (article.getTitle().contains(wzCommonSensitive.getValue())) {
                    isTitleTrue = true;
                   /* int i,index=-1,j=0;
                    index=article.getTitle().indexOf(wzCommonSensitive.getValue(),index+1);
                    while( j<article.getTitle().length()-wzCommonSensitive.getValue().length()) {
                        index=(article.getTitle().indexOf(wzCommonSensitive.getValue(), j));
                        if (index != -1) {
                            logger.info("indexing......."+index);
                            //index = article.getTitle().indexOf(wzCommonSensitive.getValue(), index + wzCommonSensitive.getValue().length());
                            j=index+wzCommonSensitive.getValue().length();
                            count++;
                        }else{
                            j++;
                        }
                    }*/
                    article.setTitle(article.getTitle().replace(wzCommonSensitive.getValue(), "*"));
                    wzCommonSensitive.setCount(wzCommonSensitive.getCount() == null ? 0 : wzCommonSensitive.getCount() + count);
                    if (!(articleService.updateById(article) && sensitiveService.updateById(wzCommonSensitive))) {
                        logger.error("更新资讯信息失败...");
                        throw new Exception("containSensitive1更新资讯信息失败...");
                    }
                }
            }
            if (!StringUtils.isEmpty(article.getContent())) {
                int count = 0;
                //查找到敏感词
                if (article.getContent().contains(wzCommonSensitive.getValue())) {
                    isContentTrue = true;
                    /*int i,index=-1,j=0;
                    index=article.getContent().indexOf(wzCommonSensitive.getValue(),j);
                    while( j<article.getContent().length()-wzCommonSensitive.getValue().length()) {
                        index=article.getContent().indexOf(wzCommonSensitive.getValue(),j);
                        if (index != -1) {
                            j = index + wzCommonSensitive.getValue().length();
                            count++;
                        }else{
                            j++;
                        }
                    }*/
                    article.setContent(article.getContent().replace(wzCommonSensitive.getValue(), "*"));
                    wzCommonSensitive.setCount(wzCommonSensitive.getCount() == null ? 0 : wzCommonSensitive.getCount() + count);
                    if (!(articleService.updateById(article) && sensitiveService.updateById(wzCommonSensitive))) {
                        throw new Exception("containSensitive2更新资讯信息失败...");
                    }
                }

            }
        }
        if (isTitleTrue || isContentTrue) {
            return true;
        }
        return false;
    }


    public boolean punishAuthorByCredit(PunishAuthorRequestDto request, String limitId) throws Exception {
        Integer limitValue = request.getParam1();
        User user = userAction.getUserService().getUserByUserNumber(request.getUserNetworkNum());
        if (user == null) {
            logger.error("没有与网号:" + request.getUserNetworkNum() + "对应的用户");
            throw new Exception("没有与网号:" + request.getUserNetworkNum() + "对应的用户");
        }
        AddCreditRecordRequestDto addCreditRecordRequestDto = new AddCreditRecordRequestDto();
        addCreditRecordRequestDto.setUserId(user.getId());
        addCreditRecordRequestDto.setRelatableType(CommonArticleLimit.class.getSimpleName());
        addCreditRecordRequestDto.setRelatableId(limitId);
        addCreditRecordRequestDto.setCredit(-limitValue);
        addCreditRecordRequestDto.setDescription("处分作者" + user.getId() + "信用值:" + limitValue);
        if (!userCreditAction.addCreditRecord(addCreditRecordRequestDto)) {
            logger.error("添加信用流水失败");
            throw new Exception("没有与网号:" + request.getUserNetworkNum() + "添加信用流水失败");
        }
        return true;
    }

    public Map<String, Object> queryArticleList(QueryArticleListRequestDto requestDto) {
        Map<String, Object> map = new HashMap<>();
        SearchProcessing searchProcessing = new SearchProcessing();
        Page page = new Page(requestDto.getCurrent(), requestDto.getSize());
        String userId=null;
        String userNumber=requestDto.getUserNumber().trim();
        if(!"".equals(userNumber)) {
            userId = userAction.getUserService().getUserIdByUserNumber(requestDto.getUserNumber());
            if (userId == null) {
                map.put("userId", null);
                return map;
            }
        }
        Page<Article> selecctPage = articleService.getAllArticle(page, searchProcessing.SearchProcessing(requestDto.getTitle()), requestDto.getStatusCode(), requestDto.getAdvertorialType(),userId);
        if (selecctPage.getRecords().size()!=0) {
            List<Article> records = selecctPage.getRecords();
            List<QueryArticleListResponseDto> responseDtos = new ArrayList<>();
            if (records != null && records.size() > 0) {
                for (Article userArticle : records) {
                    QueryArticleListResponseDto responseDto = new QueryArticleListResponseDto();
                    VoPoConverter.copyProperties(userArticle, responseDto);
                    responseDto.setPic(addImgUrlPreUtil.getUserImgPre(userArticle.getPic()) + "?x-oss-process=image/resize,h_100,w_70");
                    responseDto.setAmounts(new BigDecimal(Money.getMoneyString(userArticle.getAmount())));
                    responseDto.setNickname(userAction.getUserService().getNickNameById(userArticle.getUserId()));
                    responseDtos.add(responseDto);
                }
            }
            map.put("total", selecctPage.getTotal());
            map.put("records", responseDtos);
            map.put("userId", 1);
        }else{
            map.put("records", null);
            map.put("userId", 1);
        }
        return map;
    }

    public List<ArticleCreateTimeDto> queryArticleCreateListByuserId(String userId) {
        List<Article> articleList = articleService.selectArticleCreateTimeByUserId(userId);
        List<ArticleCreateTimeDto> articleCreateTimeDtoList = new ArrayList<>();
        ArticleCreateTimeDto articleCreateTimeDto = new ArticleCreateTimeDto();
        for (int i = 0; i < articleList.size(); i++) {
            VoPoConverter.copyProperties(articleList.get(i), articleCreateTimeDto);
            articleCreateTimeDtoList.add(articleCreateTimeDto);
        }
        return articleCreateTimeDtoList;
    }

    /**
     * 修改status根据articleId，跟要修改的status
     * @param articleId
     * @param status
     * @return
     */
    public boolean updateStatusbyUserIdAndStatus(String articleId,int status){
        return articleService.updateStatusbyUserIdAndStatus(articleId,status);
    }

    /**
     * 修改update_user_id
     * @param articleId
     * @return
     */


    public boolean updateUpdateUserId(String articleId){
        String updateUserId="9999";
        boolean result=articleService.updateUpdateUserIdByArticleId(updateUserId,articleId);
        return result;
    }

    /**
     * 检测是否有重复的图文
     * @param content
     * @return
     */
    public boolean whetherTheSameContentExists(String content) {
        return articleService.whetherTheSameContentExists(content);
    }

//    public List<Article> selectArticleByMatchId(String matchId, Page<Article> page) {
//        if (StringUtils.isEmpty(matchId)) {
//            throw new RuntimeException("请选择相应的比赛");
//        }
//        Page<Article> getArticle =  articleService.selectArticlePageByMatchId(matchId, page);
//        if (getArticle!=null) {
//            return getArticle.getRecords();
//        }
//        return new ArrayList<>();
//    }
}
