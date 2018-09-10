package com.netx.ucenter.biz.router;

import com.netx.common.redis.RedisInfoHolder;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.ucenter.util.ListToString;
import com.netx.ucenter.vo.response.UserStatData;
import com.netx.utils.cache.RedisCache;
import com.netx.utils.cache.RedisKeyName;
import com.netx.utils.cache.RedisTypeEnum;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Tuple;

import java.math.BigDecimal;
import java.util.*;

@Service
public class StatAction {

    private Logger logger = LoggerFactory.getLogger(StatAction.class);

    @Autowired
    private UserAction userAction;

    @Autowired
    private RedisInfoHolder redisInfoHolder;

    private RedisCache redisCache;

    private RedisCache clientRedis(){
        redisCache = new RedisCache(redisInfoHolder.getHost(),redisInfoHolder.getPassword(),redisInfoHolder.getPort());
        return redisCache;
    }

    public void sortList(List<Map<String,Object>> mapList,String key){
        Comparator<Map<String,Object>> comparator = new Comparator<Map<String,Object>>(){
            public int compare(Map<String,Object> mapOne, Map<String,Object> mapTwo) {
                //先排年龄
                BigDecimal left = (BigDecimal) (mapOne.get(key));
                BigDecimal right = (BigDecimal) (mapTwo.get(key));
                int result = right.compareTo(left);
                if(result==0){
                    UserStatData leftUserStatData = (UserStatData) (mapOne.get("userData"));
                    UserStatData rightUserStatData = (UserStatData) (mapTwo.get("userData"));
                    return leftUserStatData.getCreateTime().compareTo(rightUserStatData.getCreateTime());
                }
                return result;
            }
        };
        mapList.sort(comparator);
    }

    public Map<String,Object> queryStat(int start,int end,String fromUserId,boolean flag,List<Map<String,Object>> list,List<Map<String,Object>> result){
        int no = 1;
        Map<String,Object> myStat = null;
        for(Map<String,Object> map:list){
            try {
                String userId = map.get("userId").toString();
                queryUserSuggest(map,userId,no);
                if(no>=start){
                    if(no>end){
                        if(myStat!=null){
                            return myStat;
                        }
                    }else{
                        result.add(map);
                    }
                }
                if(flag && userId.equals(fromUserId)){
                    myStat = map;
                    if(no>=end){
                        return myStat;
                    }
                }
                no++;
            }catch (Exception e){
                logger.warn(e.getMessage());
            }

        }
        return myStat;
    }

    public void queryUserSuggest(Map<String,Object> map,String userId,Integer no){
        map.put("userData",userAction.queryUserStatData(userId));
        map.put("no",no);
    }

    public Map<String,Object> queryScoreStat(Set<Tuple> set,String fromUserId,int start,int end,String key){
        List<Map<String,Object>> mapList = new ArrayList<>();
        Map<String,Object> myStat = null;
        Map<String,Object> scoreStat = new HashMap<>();
        boolean flag = StringUtils.isNotBlank(fromUserId);
        if(set!=null && set.size()>0){
            int no = 1;
            Map<String,Object> map;
            for(Tuple tuple:set){
                try {
                    String userId = tuple.getElement();
                    map=getTupleMap(tuple.getScore(),userId,key);
                    map.put("no",no++);
                    if(no>start){
                        if(no>end+1){
                            if(myStat!=null){
                                break;
                            }
                        }else{
                            mapList.add(map);
                        }
                    }
                    if(flag && userId.equals(fromUserId)){
                        myStat = map;
                        if(no>end){
                            break;
                        }
                    }
                }catch (Exception e){
                    logger.warn(e.getMessage());
                }
            }
        }
        if(myStat==null){
            myStat = getTupleMap(0,fromUserId,key);
            myStat.put("no",null);
        }
        scoreStat.put("my",myStat);
        scoreStat.put("list",mapList);
        return scoreStat;
    }

    private Map<String,Object> getTupleMap(double score,String userId,String key){
        Map<String,Object> map = new HashMap<>();
        map.put("userId",userId);
        map.put(key,score);
        map.put("userData",userAction.queryUserStatData(userId));
        return map;
    }
}
