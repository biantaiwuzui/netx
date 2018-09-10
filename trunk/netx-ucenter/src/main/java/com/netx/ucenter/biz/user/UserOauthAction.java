package com.netx.ucenter.biz.user;

import com.netx.ucenter.model.user.UserOauth;
import com.netx.ucenter.service.user.UserOauthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 * 第三方授权登录表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-27
 */
@Service
public class UserOauthAction{

    @Autowired
    UserOauthService userOauthService;

    @Autowired
    UserAction userAction;

    public UserOauthService getUserOauthService() {
        return userOauthService;
    }

    public void delOauth(String userId) throws Exception{
        userOauthService.delOauth(userId);
    }

    public UserOauth checkOtherUser(String openId,int oauthType) {
        return userOauthService.checkOtherUser(openId,oauthType,1);
    }

    @Transactional(rollbackFor = Exception.class)
    public UserOauth queryOauth(String openId,int oauthType){
        UserOauth userOauth = userOauthService.checkOtherUser(openId,oauthType,1);
        if(userOauth==null){
            userOauth = addOauthService(null,openId,oauthType,1);
        }
        return userOauth;
    }

    @Transactional(rollbackFor = Exception.class)
    public UserOauth addOauthService(String userId,String openId,int oauthType,int state){
        UserOauth userOauth=new UserOauth();
        userOauth.setCreateUserId(userId);
        userOauth.setOpenId(openId);
        userOauth.setOauthType(oauthType);
        userOauth.setUserId(userId);
        userOauth.setStatus(state);
        return userOauthService.insert(userOauth)?userOauth:null;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updateOauth(String userId,UserOauth userOauth){
        userOauth.setUserId(userId);
        userOauth.setUpdateUserId(userId);
        return userOauthService.updateById(userOauth);
    }

    public Integer updateOauth(String userId,String openId,int oauthType) throws Exception{
        UserOauth oauth = userOauthService.getUserOauthOne(userId,oauthType,1);
        if(oauth == null){
            return addOauthService(userId,openId,oauthType,1)!=null?1:0;
        }
        oauth.setOpenId(openId);
        oauth.setUpdateUserId(userId);
        oauth.setUpdateTime(new Date());
        return userOauthService.updateById(oauth)?2:3;
    }
}
