package com.netx.ucenter.service.user;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.ucenter.model.user.UserOauth;
import com.netx.ucenter.mapper.user.UserOauthMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 第三方授权登录表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-27
 */
@Service
public class UserOauthService extends ServiceImpl<UserOauthMapper, UserOauth>{

    public UserOauth queryOauth(String id,Integer status){
        Wrapper<UserOauth> wrapper = new EntityWrapper<>();
        wrapper.where("id={0}",id);
        if(status!=null){
            wrapper.and("status={0}",status);
        }
        return selectOne(wrapper);
    }

    public int countOauth(String id,String userId,Integer status){
        Wrapper<UserOauth> wrapper = new EntityWrapper<>();
        wrapper.where("id={0} and user_id={1}",id,userId);
        if(status!=null){
            wrapper.and("status={0}",status);
        }
        return selectCount(wrapper);
    }

    public void delOauth(String userId) throws Exception{
        Wrapper<UserOauth> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id",userId);
        this.delete(wrapper);
    }

    public UserOauth checkOtherUser(String openId,int oauthType,Integer status){
        Wrapper<UserOauth> userOauthWrapper = getUserOauthWrapper(openId,oauthType,status);
        userOauthWrapper.orderBy("create_time");
        return selectOne(userOauthWrapper);
    }

    private Wrapper getUserOauthWrapper(String openId,int oauthType,Integer status){
        Wrapper<UserOauth> wrapper = new EntityWrapper<>();
        wrapper.where("open_id = {0} and oauth_type = {1} and status = {2}",openId,oauthType,status);
        return wrapper;
    }

    public UserOauth getUserOauthOne(String openId,int oauthType,Integer status) throws Exception{
        return this.selectOne(getUserOauthWrapper(openId, oauthType, status));
    }
}
