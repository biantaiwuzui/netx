package com.netx.ucenter.biz.router;

import com.netx.common.redis.RedisInfoHolder;
import com.netx.common.user.enums.StatScoreEnum;
import com.netx.common.user.util.AddImgUrlPreUtil;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.ucenter.biz.user.UserPhotoAction;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.util.IsNumber;
import com.netx.ucenter.util.ListToString;
import com.netx.ucenter.util.TupleToList;
import com.netx.ucenter.vo.response.UserStatData;
import com.netx.utils.cache.RedisCache;
import com.netx.utils.cache.RedisKeyName;
import com.netx.utils.cache.RedisTypeEnum;
import com.sun.javafx.collections.MappingChange;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Tuple;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ScoreAction {

    private Logger logger = LoggerFactory.getLogger(ScoreAction.class);

    @Autowired
    private RedisInfoHolder redisInfoHolder;

    private RedisCache redisCache;

    @Autowired
    private StatAction statAction;

    @Autowired
    private UserAction userAction;

    @Autowired
    UserPhotoAction userPhotoAction;

    @Autowired
    AddImgUrlPreUtil addImgUrlPreUtil;

    private RedisCache clientRedis(){
        redisCache = RedisCache.getRedisCache(redisInfoHolder.getHost(),redisInfoHolder.getPassword(),redisInfoHolder.getPort());
        return redisCache;
    }

    public void addScore(String userId, StatScoreEnum statScoreEnum){
        addScore(userId,statScoreEnum.score());
    }

    public void addScore(String userId,double score){
        RedisKeyName redisKeyName = new RedisKeyName("userScore", RedisTypeEnum.ZSET_TYPE,null);
        clientRedis().zincrby(redisKeyName.getUserKey(),userId,score);
    }

    public Map<String,Object> getUserStat(String fromUserId,int start,int end){
        RedisKeyName redisKeyName = new RedisKeyName("userHits",RedisTypeEnum.ZSET_TYPE,null);
        Set<Tuple> set = clientRedis().zrevrangeWithScores(redisKeyName.getUserKey(),0,-1);
        Set<Tuple> set1 = clientRedis().zrevrangeWithScores("UserStat", 0, -1);
        //存返回数据的map
        Map<String, Object> result = new HashMap<>();
        //如果redis取到了数据
        if (set1.size() > 0) {
            result.put("list", TupleToList.tupleToList(set1, fromUserId, result, start, end));
            return result;
        }
            return query(set,fromUserId,start,end,"UserStat",result);
    }

    public Map<String,Object> queryScoreStat(String fromUserId,int start,int end){
        Set<Tuple> set1 = clientRedis().zrevrangeWithScores("ScoreStat", 0, -1);
        //存返回数据的map
        Map<String, Object> result = new HashMap<>();
        //如果redis取到了数据
        if (set1.size() > 0) {
            result.put("list", TupleToList.tupleToList(set1, fromUserId, result, start, end));
            return result;
        }
        RedisKeyName redisKeyName = new RedisKeyName("userScore", RedisTypeEnum.ZSET_TYPE,null);
        Set<Tuple> set = clientRedis().zrevrangeWithScores(redisKeyName.getUserKey(),0,-1);
        return query(set,fromUserId,start,end,"ScoreStat",result);
    }

    private Map<String,Object> query(Set<Tuple> set,String fromUserId,int start,int end,String type,Map<String,Object> result){
        Map<String,BigDecimal> map = tupleToMap(set);
        //List<User> users = userAction.getUserService().getAllUserList();
        if (result.size()==0){
            long startTime = System.nanoTime();
            //获取所有用户的信息+url
            List<UserStatData> userStatDatas = userAction.getUserStatData();
            if(userStatDatas!=null && userStatDatas.size()>0){
                //List<Map<String,Object>> list = new ArrayList<>();
                userStatDatas.forEach(userStatData -> {
                    userStatData.setHeadImg(addImgUrlPreUtil.getUserImgPre(userStatData.getHeadImg()));
                    //UserStatData userStatData=userAction.queryUserStatData(user);
                    userStatData.setNum(getNum(map.get(userStatData.getId())));
                    clientRedis().zaddOne(type, userStatData.getNum().doubleValue(), ListToString.objectToJson(userStatData), 600);
                });
                Set<Tuple> set1 = clientRedis().zrevrangeWithScores(type, 0, -1);
                if (set1.size() > 0) {
                    result.put("list", TupleToList.tupleToList(set1, fromUserId, result, start, end));
                }
            }
            //获取结束时间
            long endTime = System.nanoTime();
            System.out.println(type+"排行榜遍历sql程序运行时间： " + (endTime - startTime) + "ns"+(endTime - startTime)/1000000000+"s");
        }
        return result;
    }

    private BigDecimal getNum(BigDecimal num){
        return num==null?BigDecimal.ZERO:num;
    }

    private Map<String,BigDecimal> tupleToMap(Set<Tuple> set){
        Map<String,BigDecimal> map = new HashMap<>();
        if(set!=null && set.size()>0){
            set.forEach(tuple -> {
                map.put(tuple.getElement(),new BigDecimal(tuple.getScore()));
            });
        }
        return map;
    }
}
