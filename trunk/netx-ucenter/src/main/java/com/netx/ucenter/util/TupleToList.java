package com.netx.ucenter.util;

import com.netx.ucenter.vo.response.UserStatData;
import redis.clients.jedis.Tuple;

import java.math.BigDecimal;
import java.util.*;

public class TupleToList {

    public static List<UserStatData> tupleToList(Set<Tuple> set, String fromUserId, Map<String,Object> result, int start, int end){
        Map<String,BigDecimal> map = new HashMap<>();
        List<UserStatData> list=new ArrayList<>();
        boolean flag=false;
        if(set!=null && set.size()>0){
            int no=0;
            for(Tuple tuple:set){
                no++;
                UserStatData userStatData=ListToString.jsonToPojo(tuple.getElement(),UserStatData.class);
                if (start<=no){
                    if (no<=end){
                        userStatData.setNo(no);
                        list.add(userStatData);
                    }
                }
                if (fromUserId.equals(userStatData.getId())){
                    flag=true;
                    userStatData.setNo(no);
                    result.put("my",userStatData);
                }
                if (flag&&no>end){
                    break;
                }
            }
        }
        return list;
    }
}
