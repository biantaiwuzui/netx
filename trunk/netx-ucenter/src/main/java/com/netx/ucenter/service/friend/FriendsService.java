package com.netx.ucenter.service.friend;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.ucenter.biz.common.MessagePushAction;
import com.netx.ucenter.mapper.friend.CommonFriendsMapper;
import com.netx.ucenter.model.friend.CommonFriends;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Create by wongloong on 17-8-27
 */
@Service
public class FriendsService extends ServiceImpl<CommonFriendsMapper, CommonFriends>{
    private Logger logger = LoggerFactory.getLogger(FriendsService.class);
    @Autowired
    CommonFriendsMapper wzCommonFriendsMapper;

    public CommonFriendsMapper getWzCommonFriendsMapper() {
        return wzCommonFriendsMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean addFriends(String masterId, String friendId) {
        CommonFriends friends = new CommonFriends();
        friends.setCreateTime(new Date());
        friends.setFriendId(friendId);
        friends.setMasterId(masterId);
        return this.insert(friends);
    }

    public List<Map<String,Object>> queryAllFriends(){
        Wrapper<CommonFriends> wrapper = new EntityWrapper();
        wrapper.setSqlSelect("master_id as userId,GROUP_CONCAT(friend_id) as friendId,count(*) as friendNum");
        wrapper.where("deleted=0");
        wrapper.orderBy("friendNum");
        wrapper.groupBy("userId");
        return selectMaps(wrapper);
    }

    public List<String> queryFriendsByUserIdStat(String userId){
        Wrapper<CommonFriends> wrapper = new EntityWrapper();
        wrapper.where("master_id={0} and deleted=0 and create_time between '2018-6-05 00:00:00' and '2018-9-30 00:00:00'",userId);
        wrapper.setSqlSelect("friend_id");
        return (List<String>)(List)selectObjs(wrapper);
    }

    public List<String> queryFriendsByUserId(String userId){
        Wrapper<CommonFriends> wrapper = new EntityWrapper();
        wrapper.where("master_id={0} and deleted=0",userId);
        wrapper.setSqlSelect("friend_id");
        return (List<String>)(List)selectObjs(wrapper);
    }

    public Boolean checkFriend(String userId,String toUserId){
        return checkFriends(userId,toUserId)>0;
    }

    private Integer checkFriends(String userId,String toUserId){
        Wrapper wrapper = new EntityWrapper();
        String sql = "master_id = {0} AND friend_id = {1}";
        wrapper.where(sql,userId,toUserId);
        wrapper.orNew(sql,toUserId,userId);
        return selectCount(wrapper);
    }

    public Boolean checkFriendOne(String userId,String toUserId){
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("master_id = {0} AND friend_id = {1}",userId,toUserId);
        return checkCount(selectCount(wrapper));
    }

    private Boolean checkCount(Integer count){
        return count!=null && count>0;
    }

    /**
     * 删除好友关系
     *
     * @param masterId
     * @param friendId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean delFriends(String masterId, String friendId) throws Exception{
        String sql = "master_id={0} and friend_id={1}";
        EntityWrapper mas = new EntityWrapper(new CommonFriends());
        mas.where(sql, masterId, friendId);
        EntityWrapper fri = new EntityWrapper(new CommonFriends());
        fri.where(sql, friendId, masterId);
        return this.delete(mas) && this.delete(fri);
    }

    public List<String> getFriendIds(String userId) throws Exception{
        Wrapper<CommonFriends> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("friend_id");
        wrapper.eq("master_id",userId);
        return (List<String>)(List)this.selectObjs(wrapper);
    }

    public Page<CommonFriends> getFriends(String userId, Page page) throws Exception{
        Wrapper<CommonFriends> wrapper = new EntityWrapper<>();
        wrapper.eq("master_id",userId);
        return selectPage(page,wrapper);
    }
}
