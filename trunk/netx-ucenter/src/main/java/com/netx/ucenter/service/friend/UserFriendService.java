package com.netx.ucenter.service.friend;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.ucenter.mapper.friend.CommonAddFriendMapper;
import com.netx.ucenter.model.friend.CommonAddFriend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Create by wongloong on 17-8-26
 */
@Service
public class UserFriendService extends ServiceImpl<CommonAddFriendMapper, CommonAddFriend>{
    private Logger logger = LoggerFactory.getLogger(UserFriendService.class);

    public boolean updateByToUserId(String userId,CommonAddFriend commonAddFriend){
        EntityWrapper<CommonAddFriend> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("to_user_id={0}",userId);
        return update(commonAddFriend,entityWrapper);
    }

    public int countRead(String userId,Boolean isRead){
        EntityWrapper<CommonAddFriend> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("to_user_id={0} and deleted=0",userId);
        if(isRead!=null){
            entityWrapper.and("has_read={0}",isRead?1:0);
        }
        entityWrapper.and("dispose_state=0");
        return selectCount(entityWrapper);
    }

    public CommonAddFriend getWzCommonAddFriend(String fromUserId,String toUserId){
        EntityWrapper<CommonAddFriend> ew = new EntityWrapper(new CommonAddFriend());
        ew.where("user_id={0}", fromUserId);
        ew.and("to_user_id={0}", toUserId);
        ew.and("dispose_state=0");
        return this.selectOne(ew);
    }

    public Page<CommonAddFriend> selectPageByUserId(String userId,Integer disposeState,Page page) throws Exception {
        EntityWrapper<CommonAddFriend> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("to_user_id={0} and dispose_state={1}", userId,disposeState);
        entityWrapper.orderBy("send_time desc");
        return selectPage(page,entityWrapper);
    }

    public Boolean updateAddFriend(String userId,String toUserId){
        EntityWrapper<CommonAddFriend> entityWrapper = new EntityWrapper<>();
        String sql = "user_id = {0} and to_user_id = {1}";
        entityWrapper.where(sql,userId,toUserId);
        entityWrapper.orNew(sql,toUserId,userId);
        CommonAddFriend commonAddFriend = new CommonAddFriend();
        commonAddFriend.setDisposeState(1);
        commonAddFriend.setDisposeTime(new Date());
        commonAddFriend.setUpdateUserId(userId);
        commonAddFriend.setUpdateTime(new Date());
        return this.update(commonAddFriend,entityWrapper);
    }
}
