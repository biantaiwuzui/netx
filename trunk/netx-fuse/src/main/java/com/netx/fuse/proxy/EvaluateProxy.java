package com.netx.fuse.proxy;

import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.vo.common.EvaluateQueryRequestDto;
import com.netx.ucenter.biz.common.EvaluateAction;
import com.netx.ucenter.model.common.CommonEvaluate;
import org.apache.commons.beanutils.BeanMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;

@Component
public class EvaluateProxy {

    private Logger logger = LoggerFactory.getLogger(EvaluateProxy.class);

    @Autowired
    EvaluateAction evaluateAction;

    public Page<CommonEvaluate> queryList(EvaluateQueryRequestDto request){
        try {
            return evaluateAction.queryPage(request);
        } catch (Exception e) {
            logger.error("查询评价出错");
            return new Page<>();
        }
    }

    /**
     * 返回未评价其它人的用户id
     *
     * @param userIds
     * @param typeId
     * @return
     */
    public List<String> notEvaluateUsers(List<String> userIds, String typeId) {
        //首先去重复
        Set<String> set = new HashSet<String>(userIds);//Set是不重复的集合
        userIds.clear();
        userIds.addAll(set);
        EvaluateQueryRequestDto request = new EvaluateQueryRequestDto();
        request.setTypeId(typeId);
        request.setSize(999999999);
        List<String> notEvaluateUsers = new ArrayList<>();
        List list = queryList(request).getRecords();
        Map<String, List<String>> evaluateFromMap = new HashMap<>();
        for (Object o : list) {
            Map rs = new BeanMap(o);
            String fromUserId = (String) rs.get("fromUserId");
            String toUserId = (String) rs.get("toUserId");
            if (evaluateFromMap.get(fromUserId) == null) {
                evaluateFromMap.put(fromUserId, Arrays.asList(toUserId));
            } else {
                evaluateFromMap.get(fromUserId).add(toUserId);
            }
        }
        //取出每个用户的评价对象，并对比，看看是否全包含
        userIds.forEach(userId -> {
            List<String> toUserIds = evaluateFromMap.get(userId);
            if(toUserIds == null){
                toUserIds = new ArrayList<>();
            }
            toUserIds.add(userId);//把自己加进去好比较
            if (!toUserIds.containsAll(userIds)) notEvaluateUsers.add(userId);
        });
        return notEvaluateUsers;
    }
}