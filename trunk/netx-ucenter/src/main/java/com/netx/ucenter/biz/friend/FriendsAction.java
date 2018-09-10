package com.netx.ucenter.biz.friend;

import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.redis.RedisInfoHolder;
import com.netx.common.user.enums.StatScoreEnum;
import com.netx.common.user.model.UserSynopsisData;
import com.netx.common.user.util.AddImgUrlPreUtil;
import com.netx.common.user.util.ComputeAgeUtils;
import com.netx.common.vo.common.StatDataDto;
import com.netx.common.vo.friends.FriendsListResponseVo;
import com.netx.ucenter.biz.common.MessagePushAction;
import com.netx.ucenter.biz.router.ScoreAction;
import com.netx.ucenter.biz.router.StatAction;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.ucenter.biz.user.UserVerifyAction;
import com.netx.ucenter.model.common.CommonOtherSet;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.service.common.OtherSetService;
import com.netx.ucenter.service.friend.FriendsService;
import com.netx.ucenter.service.user.UserPhotoService;
import com.netx.ucenter.service.user.UserProfileService;
import com.netx.ucenter.util.ListToString;
import com.netx.ucenter.util.TupleToList;
import com.netx.ucenter.vo.response.UserStatData;
import com.netx.utils.cache.RedisCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Tuple;

import java.math.BigDecimal;
import java.util.*;

/**
 * Create by wongloong on 17-8-27
 */
@Service
public class FriendsAction {
    private Logger logger = LoggerFactory.getLogger(FriendsAction.class);
    @Autowired
    private UserAction userAction;
    @Autowired
    private OtherSetService otherSetService;
    @Autowired
    private MessagePushAction messagePushAction;
    @Autowired
    private FriendsService friendsService;
    @Autowired
    private UserPhotoService userPhotoService;
    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    private ScoreAction scoreAction;
    @Autowired
    private StatAction statAction;
    @Autowired
    private RedisInfoHolder redisInfoHolder;
    @Autowired
    private UserVerifyAction userVerifyAction;
    @Autowired
    private AddImgUrlPreUtil addImgUrlPreUtil;

    private RedisCache redisCache;

    private RedisCache clientRedis() {
        redisCache = RedisCache.getRedisCache(redisInfoHolder.getHost(), redisInfoHolder.getPassword(), redisInfoHolder.getPort());
        return redisCache;
    }

    private Integer getAge(Date birthday) {
        return ComputeAgeUtils.getAgeByBirthday(birthday);
    }

    public Map<String, Object> getFriendsListByMasterId(String userId, Integer current, Integer size, boolean friendVerify, int credit, Double lon, Double lat) throws Exception {
        //获取好友id
        List<String> userIds = friendsService.queryFriendsByUserId(userId);
        Map<String, Object> map = new HashMap<>();
        List<String> userVerifyIds = new ArrayList<>();
        if (userIds != null && userIds.size() > 0 && !friendVerify && credit == 0) {
            Page page = new Page(current, size);
            return getFriendMap(userAction.getUserSynopsisDataList(userIds, page, userId, lon, lat), page.getTotal());
        } else if (userIds != null && userIds.size() > 0 && friendVerify && credit >= 0) {
            //获取认证的好友，根据好友id
            List<User> userVerifyList = userAction.getUserService().queryRealUserList(userIds);
            if (userVerifyList.size() != 0) {
                Page page = new Page(current, size);
                //获取网信值大于credit取值
                userVerifyList.forEach(user -> {
                    int resultCredit = userAction.checkUserCreditByUserIdAndCredit(user.getId(), credit);
                    if (resultCredit == 1) {
                        userVerifyIds.add(user.getId());
                    }
                });
                return getFriendMap(userAction.getUserSynopsisDataList(userVerifyIds, page, userId, lon, lat), page.getTotal());
            } else {
                return getFriendMap(null, 1);
            }
        } else {
            return getFriendMap(new ArrayList<>(), 0);
        }
    }

    private Map<String, Object> getFriendMap(List<UserSynopsisData> userSynopsisData, int total) {
        Map<String, Object> map = new HashMap<>();
        map.put("list", userSynopsisData);
        map.put("total", total);
        return map;
    }

    public Map<String, Object> queryFriendStatOne(String fromUserId, int start, int end) {
        //从redis获取分数倒叙排序的所有数据
        Set<Tuple> set = clientRedis().zrevrangeWithScores("FriendStat", 0, -1);
        Map<String, Object> result = new HashMap<>();
        //如果redis取到了数据
        if (set.size() > 0) {
            result.put("list", TupleToList.tupleToList(set, fromUserId, result, start, end));
        }
        //如果从redis没取到数据，那么从数据库重新读入写入redis，并返回前端
        long startTime = System.nanoTime();
        try {
            if (set.size() == 0) {
                //获取所有用户id，跟好友数
                List<StatDataDto> statDataDtos = friendsService.getWzCommonFriendsMapper().selectStatData();
                if (statDataDtos.size()>0) {
                    //获取所有用户的信息
                    for (StatDataDto statDataDto : statDataDtos) {
                        //将user copy到userStatData里面
                        UserStatData userStatData = userAction.queryStatDataDto(statDataDto);
                        userStatData.setNum(new BigDecimal(statDataDto.getCount()));
                        userStatData.setHeadImg(addImgUrlPreUtil.getUserImgPre(userStatData.getHeadImg()));
                        //存储到redis中
                        clientRedis().zaddOne("FriendStat", userStatData.getNum().doubleValue(), ListToString.objectToJson(userStatData), 600);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //if用户list大于0，说明redis取数据失败，将数据库查询的数据返回前端
        Set<Tuple> set1 = clientRedis().zrevrangeWithScores("FriendStat", 0, -1);
        if (set1.size() > 0) {
            result.put("list", TupleToList.tupleToList(set1, fromUserId, result, start, end));
        }
        //获取结束时间
        long endTime = System.nanoTime();
        System.out.println("FriendStat排行榜遍历sql程序运行时间： " + (endTime - startTime) + "ns" + (endTime - startTime) / 1000000000 + "s");
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean addFriends(String masterId, String friendId) {
        boolean flag = friendsService.addFriends(masterId, friendId);
        if (flag) {
            scoreAction.addScore(masterId, StatScoreEnum.SS_ADD_FRIEND);
        }
        return flag;
    }

    private FriendsListResponseVo userToFriendsListResponseVo(User user) throws Exception {
        FriendsListResponseVo friendsListResponseVo = new FriendsListResponseVo();
        friendsListResponseVo.setId(user.getId());
        friendsListResponseVo.setNickname(user.getNickname());
        friendsListResponseVo.setUserNumber(user.getUserNumber());
        friendsListResponseVo.setAge(getAge(user.getBirthday()));
        friendsListResponseVo.setSex(user.getSex());
        friendsListResponseVo.setLevel(user.getLv());
        friendsListResponseVo.setUrl(userPhotoService.selectHeadImg(user.getId()));
        friendsListResponseVo.setTags(userProfileService.getUserTag(user.getId(), "disposition"));
        return friendsListResponseVo;
    }

    private List<FriendsListResponseVo> getFriendsListByUserId(String userId, Integer current, Integer size) throws Exception {
        List<FriendsListResponseVo> friendsListResponseVos = new ArrayList<FriendsListResponseVo>();
        current = (current - 1) * size;
        List<User> friendUsers = userAction.getUserService().getUserMapper().queryUserFriendsByUserId(userId, current, size);
        if (friendUsers != null) {
            for (User user : friendUsers) {
                if (user != null) {
                    friendsListResponseVos.add(userToFriendsListResponseVo(user));
                }
            }
        }
        return friendsListResponseVos;
    }

    private List<FriendsListResponseVo> getFriendsListByMasterIdAndFilter(List<String> ids, String credit, String phone, String idCard, String voice) throws Exception {
        List<FriendsListResponseVo> friendsListResponseVos = new ArrayList<FriendsListResponseVo>();
        List<User> users = userAction.getUserService().getFilterFriendUser(ids, credit, phone, idCard, voice);
        if (users != null && users.size() > 0) {
            users.forEach(user -> {
                try {
                    friendsListResponseVos.add(userToFriendsListResponseVo(user));
                } catch (Exception e) {
                    logger.warn("过滤好友时：用户id：" + user.getId() + "的信息转换出现问题");
                }
            });
        }
        return friendsListResponseVos;
    }

    public List<FriendsListResponseVo> getFriendsListByMasterIdAndFilter(String userId, String type, Integer current, Integer size) throws Exception {
        List<FriendsListResponseVo> listResponseVos = new ArrayList<>();
        try {
            listResponseVos = getFriendsListByUserId(userId, current, size);
            if (type != null && "0".equals(type)) {//心愿过滤
                CommonOtherSet otherSet = otherSetService.getWzCommonOtherSetOne();
                //判断设置是否通过审核
                if (null != otherSet && otherSet.getCanUse().intValue() == 1) {
                    //推荐者与发布者同等资格
                    if (otherSet.getWishLimitCondition().indexOf("4") > -1) {
                        //限制为人员名单
                        if (otherSet.getWishLimitType() == 0) {
                            List<String> canWishUserIds = Arrays.asList(otherSet.getWishLimitUserIds().split(","));
                            List<FriendsListResponseVo> filterList = new ArrayList<>();
                            listResponseVos.forEach(friend -> {
                                for (String id : canWishUserIds) {
                                    if (friend.getId().equals(id)) {
                                        filterList.add(friend);
                                    }
                                }
                            });
                            return filterList;
                        } else if (otherSet.getWishLimitType() == 1) {//限制条件
                            String credit = null;
                            String phone = null;
                            String idCard = null;
                            String voice = null;
                            //限制信用值
                            if (otherSet.getRegMerchantLimitCondition().indexOf("0") > -1) {
                                credit = otherSet.getWishLimitPoint().toString();
                            }
                            if (otherSet.getRegMerchantLimitCondition().indexOf("1") > -1) {
                                phone = "1";
                            }
                            if (otherSet.getRegMerchantLimitCondition().indexOf("2") > -1) {
                                idCard = "1";
                            }
                            if (otherSet.getRegMerchantLimitCondition().indexOf("3") > -1) {
                                voice = "1";
                            }
                            List<String> ids = friendsService.getWzCommonFriendsMapper().getUserIdsByMasterId(userId);
                            return getFriendsListByMasterIdAndFilter(ids, credit, phone, idCard, voice);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return listResponseVos;
    }

    /**
     * 判断是否好友关系
     *
     * @param userId1
     * @param userId2
     * @return
     */
    public boolean isFriend(String userId1, String userId2) throws Exception {
        return friendsService.checkFriend(userId1, userId2);
    }

    /**
     * 删除好友关系
     *
     * @param masterId
     * @param friendId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean delFriends(String masterId, String friendId) throws Exception {
        boolean flag = friendsService.delFriends(masterId, friendId);
        scoreAction.addScore(masterId, -StatScoreEnum.SS_ADD_FRIEND.score());
        scoreAction.addScore(friendId, -StatScoreEnum.SS_ADD_FRIEND.score());
        try {
            messagePushAction.deleteFriend(masterId, friendId);
        } catch (Exception e) {
            logger.warn("极光好友删除失败：" + e.getMessage(), e);
        }
        return flag;
    }

    public void delFriends(String userId) throws Exception {
        List<String> list = friendsService.getFriendIds(userId);
        if (list != null) {
            list.forEach(friendId -> {
                try {
                    delFriends(userId, friendId);
                } catch (Exception e) {
                    logger.warn(userId + "删除好友" + friendId + "失败：" + e.getMessage(), e);
                }
            });
        }
    }
}
