package com.netx.ucenter.service.user;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.redis.model.UserGeo;
import com.netx.common.redis.service.GeoService;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.model.user.UserBlacklist;
import com.netx.ucenter.mapper.user.UserBlacklistMapper;
import com.netx.ucenter.service.friend.FriendsService;
import com.netx.ucenter.vo.response.SelectUserCommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户拉黑表（拉黑后对本人是黑名单，不影响其他人） 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-27
 */
@Service
public class UserBlacklistService extends ServiceImpl<UserBlacklistMapper, UserBlacklist>{

    public void deleteByUserId(String userId) throws Exception{
        Wrapper<UserBlacklist> wrapper = new EntityWrapper<>();
        wrapper.where("user_id = {0} or target_id = {1}",userId,userId);
        delete(wrapper);
    }

    public List<UserBlacklist> selectUserBlacklistByUserId(String userId){
       Wrapper wrapper = new EntityWrapper<UserBlacklist>();
       wrapper.eq("user_id",userId);
       return this.selectList(wrapper);
    }

    public Integer checkBlacklistByUserId(String userId,String targetId){
        Wrapper wrapper = new EntityWrapper<UserBlacklist>();
        wrapper.where("user_id = {0} and target_id = {1}",userId,targetId);
        return selectCount(wrapper);
    }
}
