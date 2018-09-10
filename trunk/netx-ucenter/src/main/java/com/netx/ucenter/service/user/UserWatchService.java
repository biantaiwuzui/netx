package com.netx.ucenter.service.user;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.common.vo.message.JpushDto;
import com.netx.common.user.dto.wangMing.AddScoreRecordRequestDto;
import com.netx.common.user.model.UserSynopsisData;
import com.netx.common.user.util.VoPoConverter;
import com.netx.ucenter.mapper.user.UserWatchMapper;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.model.user.UserWatch;
import com.netx.ucenter.service.common.MessagePushService;
import com.netx.ucenter.service.friend.FriendsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户关注表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-27
 */
@Service
public class UserWatchService extends ServiceImpl<UserWatchMapper, UserWatch> {
    private Logger logger = LoggerFactory.getLogger(UserWatchService.class);

    //======================== 黎子安 start ========================
    
    public void deleteWatch(String userId) throws Exception{
        Wrapper<UserWatch> wrapper = new EntityWrapper<UserWatch>();
        wrapper.where("from_user_id = {0} or to_user_id = {1}",userId,userId);
        this.delete(wrapper);
    }

    
    @Transactional(readOnly = true)
    public List<String> selectUserIdsByType(String userId,String options) throws Exception {
        Wrapper<UserWatch> userWatchWrapper = new EntityWrapper<UserWatch>();
        if(options.equals("user"))
            userWatchWrapper.setSqlSelect("to_user_id").where("from_user_id = {0}", userId);
        else if(options.equals("toUser"))
            userWatchWrapper.setSqlSelect("from_user_id").where("to_user_id = {0}", userId);
        else
            return null;
        userWatchWrapper.and("is_watch = 1");
        return (List<String>)(List)this.selectObjs(userWatchWrapper);
    }

    public UserWatch getUserWatchByUserId(String fromUserId,String toUserId){
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("from_user_id = {0} and to_user_id = {1}",fromUserId,toUserId);
        return this.selectOne(wrapper);
    }

    /**
     * 获取未关注的用户【逗号隔开】
     * @param userId
     * @return
     * @throws Exception
     */
    public String getNoWatchUserId(String userId) throws Exception{
        Wrapper<UserWatch> userWatchWrapper = new EntityWrapper<UserWatch>();
        userWatchWrapper.setSqlSelect("GROUP_CONCAT(to_user_id) as ids").where("from_user_id = {0} and is_watch = 1", userId);
        return (String) this.selectObj(userWatchWrapper);
    }
}
