package com.netx.fuse.biz.worth;

import com.netx.common.redis.RedisInfoHolder;
import com.netx.fuse.biz.FuseBaseAction;
import com.netx.ucenter.biz.router.StatAction;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.ucenter.util.ListToString;
import com.netx.ucenter.util.TupleToList;
import com.netx.ucenter.vo.response.UserStatData;
import com.netx.utils.cache.RedisCache;
import com.netx.utils.money.Money;
import com.netx.worth.biz.skill.SkillRegisterAction;
import com.netx.worth.service.WorthServiceprovider;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Tuple;

import java.math.BigDecimal;
import java.util.*;

@Service
public class WorthFuseAction extends FuseBaseAction {

    private Logger logger = LoggerFactory.getLogger(WorthFuseAction.class);

    @Autowired
    private UserAction userAction;

    @Autowired
    private WorthServiceprovider worthServiceprovider;

    @Autowired
    private SkillRegisterAction skillRegisterAction;

    @Autowired
    private RedisInfoHolder redisInfoHolder;

    private RedisCache redisCache;

    private RedisCache clientRedis() {
        redisCache = RedisCache.getRedisCache(redisInfoHolder.getHost(), redisInfoHolder.getPassword(), redisInfoHolder.getPort());
        return redisCache;
    }

    public Map<String, Object> queryEQStat(String fromUserId, int start, int end) {
        Set<Tuple> set = clientRedis().zrevrangeWithScores("EQStat", 0, -1);
        Map<String, Object> result = new HashMap<>();
        //如果redis取到了数据
        if (set.size() > 0) {
            result.put("list", TupleToList.tupleToList(set, fromUserId, result, start, end));
        } else {
            long startTime = System.nanoTime();
            Map<String, UserStatData> userStatDataMap = userAction.queryUserStatData();
            if (userStatDataMap != null && userStatDataMap.size() > 0) {
                queryEQ(userStatDataMap);
            }
            Set<Tuple> set1 = clientRedis().zrevrangeWithScores("EQStat", 0, -1);
            if (set1.size() > 0) {
                result.put("list", TupleToList.tupleToList(set1, fromUserId, result, start, end));
            }
            //获取结束时间
            long endTime = System.nanoTime();
            System.out.println("EQStat排行榜遍历sql程序运行时间： " + (endTime - startTime) + "ns"+(endTime - startTime)/1000000000+"s");
        }
        return result;
    }

    /**
     * 红人排行榜
     *
     * @param fromUserId
     * @param start
     * @param end
     * @return
     */
    public Map<String, Object> queryWorthStat(String fromUserId, int start, int end) {
        Set<Tuple> set = clientRedis().zrevrangeWithScores("WorthStat", 0, -1);
        Map<String, Object> result = new HashMap<>();
        //如果redis取到了数据
        if (set.size() > 0) {
            result.put("list", TupleToList.tupleToList(set, fromUserId, result, start, end));
        }
        else {
            long startTime = System.nanoTime();
            Map<String, UserStatData> userStatDataMap = userAction.queryUserStatData();
            if (userStatDataMap != null && userStatDataMap.size() > 0) {
                queryWorth(userStatDataMap);
            }
            Set<Tuple> set1 = clientRedis().zrevrangeWithScores("WorthStat", 0, -1);
            if (set1.size() > 0) {
                result.put("list", TupleToList.tupleToList(set1, fromUserId, result, start, end));
            }
            //获取结束时间
            long endTime = System.nanoTime();
            System.out.println("ArticleStat排行榜遍历sql程序运行时间： " + (endTime - startTime) + "ns"+(endTime - startTime)/1000000000+"s");
        }

        return result;
    }

    private void sort(Map<String, UserStatData> userStatDataMap, String fromUserId, int start, int end, List<Map<String, Object>> temp, List<Map<String, Object>> list, Map<String, Object> myStat, Map<String, Object> map) {
        boolean flag = StringUtils.isNotBlank(fromUserId);
        if (temp != null && temp.size() > 0) {
            sortList(temp, "total");
            int no = 1;
            for (Map<String, Object> mapWorth : temp) {
                mapWorth.put("no", no);
                if (flag && fromUserId.equals(mapWorth.get("userId").toString())) {
                    myStat = mapWorth;
                }
                if (no >= start) {
                    if (no <= end) {
                        list.add(mapWorth);
                    }
                }
                if (no >= end && myStat != null) {
                    break;
                }
                no++;
            }
        }
        if (myStat == null) {
            myStat = getMap(userStatDataMap.get(fromUserId), fromUserId, BigDecimal.ZERO);
            myStat.put("no", null);
        }
        map.put("my", myStat);
    }

    /**
     * 网能消费
     *
     * @param userStatDataMap
     * @return
     */
    private List<UserStatData> queryEQ(Map<String, UserStatData> userStatDataMap) {
        List<UserStatData> list = new ArrayList<>();
        Set<String> userIds = userStatDataMap.keySet();
        Map<String, Long> wish = getWishConsume(userIds);
        Map<String, Long> skill = getSkillConsume(userIds);
        Map<String, Long> meeting = getMeetingConsume(userIds);
//        userStatDataMap.forEach((userId, userStatData) -> {
//            long total = check(wish.get(userId))+check(skill.get(userId))+check(meeting.get(userId));
//            list.add(getMap(userStatData,userId,Money.CentToYuan(total).getAmount()));
//        });
//        return list;
        for (Map.Entry<String, UserStatData> entry : userStatDataMap.entrySet()) {
//        userStatDataMap.forEach((userId, userStatData) -> {
            //总分数
            //no++;
            long total = check(wish.get(entry.getKey())) + check(skill.get(entry.getKey())) + check(meeting.get(entry.getKey()));
            UserStatData userStatData = getUserStatData(entry.getValue(), entry.getKey(), Money.CentToYuan(total).getAmount());
            list.add(userStatData);
            clientRedis().zaddOne("EQStat", userStatData.getNum().doubleValue(), ListToString.objectToJson(userStatData), 600);
        }
        return list;
    }

    private Map<String, Object> getMap(UserStatData userStatData, String userId, BigDecimal total) {
        Map<String, Object> map = new HashMap<>();
        map.put("userData", userStatData);
        map.put("userId", userId);
        map.put("total", total);
        return map;
    }

    /**
     * 网能收入
     *
     * @param userStatDataMap
     * @return
     */
    private List<UserStatData> queryWorth(Map<String, UserStatData> userStatDataMap) {
        List<UserStatData> list = new ArrayList<>();
        Set<String> userIds = userStatDataMap.keySet();
        Map<String, Long> wish = getWishIncome(userIds);
        Map<String, Long> skill = getSkillIncome(userIds);
        Map<String, Long> meeting = getMeetingIncome(userIds);
        //Integer no= new Integer(start-1);
        //int no=start-1;
        for (Map.Entry<String, UserStatData> entry : userStatDataMap.entrySet()) {
//        userStatDataMap.forEach((userId, userStatData) -> {
            //总分数
            //no++;
            long total = check(wish.get(entry.getKey())) + check(skill.get(entry.getKey())) + check(meeting.get(entry.getKey()));
            UserStatData userStatData = getUserStatData(entry.getValue(), entry.getKey(), Money.CentToYuan(total).getAmount());
            list.add(userStatData);
            clientRedis().zaddOne("WorthStat", userStatData.getNum().doubleValue(), ListToString.objectToJson(userStatData), 600);
        }
        return list;
    }

    private UserStatData getUserStatData(UserStatData userStatData, String userId, BigDecimal total) {
        // Map<String,Object> map = new HashMap<>();
        //userStatData.setNo(no);
        userStatData.setNum(total);
        // map.put("userData",userStatData);
        //  map.put("userId",userId);
        return userStatData;
    }

    private long check(Long total) {
        return total == null ? 0 : total;
    }

    /**
     * 心愿收入
     *
     * @param userIds
     * @return
     */
    private Map<String, Long> getWishIncome(Set<String> userIds) {
        List<Map<String, Object>> list = worthServiceprovider.getWishService().queryWishIncome(userIds);
        Map<String, Long> map = new HashMap<>();
        if (list != null && list.size() > 0) {
            list.forEach(wish -> {
                BigDecimal total = (BigDecimal) wish.get("total");
                map.put(wish.get("userId").toString(), total == null ? 0 : total.longValue());
            });
        }
        return map;
    }

    /**
     * 活动收入
     *
     * @param userIds
     * @return
     */
    private Map<String, Long> getMeetingIncome(Set<String> userIds) {
        List<Map<String, Object>> list = worthServiceprovider.getMeetingService().queryMeeting(userIds);
        Map<String, Long> map = new HashMap<>();
        if (list != null && list.size() > 0) {
            list.forEach(meeting -> {
                long total = 0;
                Object ids = meeting.get("ids");
                if (ids != null && StringUtils.isNotBlank(ids.toString())) {
                    total = worthServiceprovider.getMeetingRegisterService().getMeetingIncomeTotal(ids.toString().split(","));
                }
                map.put(meeting.get("userId").toString(), total);
            });
        }
        return map;
    }

    /**
     * 技能收入
     *
     * @param userIds
     * @return
     */
    private Map<String, Long> getSkillIncome(Set<String> userIds) {
        List<Map<String, Object>> skillList = worthServiceprovider.getSkillService().querySkill(userIds);
        Map<String, Long> map = new HashMap<>();
        if (skillList != null && skillList.size() > 0) {
            skillList.forEach(skill -> {
                long total = skillRegisterAction.querySkillIncome(skill.get("ids").toString());
                map.put(skill.get("userId").toString(), total);
            });
        }
        return map;
    }

    private Map<String, Long> getSkillConsume(Set<String> userIds) {
        List<Map<String, Object>> skillList = worthServiceprovider.getSkillRegisterService().querySkillRegister(userIds);
        Map<String, Long> map = new HashMap<>();
        if (skillList != null && skillList.size() > 0) {
            skillList.forEach(skill -> {
                BigDecimal total = (BigDecimal) skill.get("total");
                map.put(skill.get("userId").toString(), total == null ? 0 : total.longValue());
            });
        }
        return map;
    }

    private Map<String, Long> getMeetingConsume(Set<String> userIds) {
        List<Map<String, Object>> meetingList = worthServiceprovider.getMeetingRegisterService().queryMeetingStat(userIds);
        Map<String, Long> map = new HashMap<>();
        if (meetingList != null && meetingList.size() > 0) {
            meetingList.forEach(meeting -> {
                BigDecimal total = (BigDecimal) meeting.get("total");
                map.put(meeting.get("userId").toString(), total == null ? 0 : total.longValue());
            });
        }
        return map;
    }

    private Map<String, Long> getWishConsume(Set<String> userIds) {
        List<Map<String, Object>> wishList = worthServiceprovider.getWishSupportService().queryConsume(userIds);
        Map<String, Long> map = new HashMap<>();
        if (wishList != null && wishList.size() > 0) {
            wishList.forEach(wish -> {
                BigDecimal total = (BigDecimal) wish.get("total");
                map.put(wish.get("userId").toString(), total == null ? 0 : total.longValue());
            });
        }
        return map;
    }

}
