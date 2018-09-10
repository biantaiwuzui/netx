package com.netx.ucenter.biz.user;

import com.netx.ucenter.model.user.UserVerifyResource;
import com.netx.ucenter.service.user.UserVerifyResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户提交认证资源表，如一些图片或文件 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-27
 */
@Service
public class UserVerifyResourceAction{

    @Autowired
    UserVerifyResourceService userVerifyResourceService;

    /**
     * 添加单条资源信息（不限认证类型）
     * @param userId
     * @param userVerifyId
     * @param url
     * @param position
     * @return
     */
    
    public boolean postUserVerifyResource(String userId, String userVerifyId, String url, Integer position) throws Exception {
        UserVerifyResource userVerifyResource = new UserVerifyResource();
        userVerifyResource.setCreateUserId(userId);
        userVerifyResource.setUserId(userId);
        userVerifyResource.setUserVerifyId(userVerifyId);
        userVerifyResource.setUrl(url);
        userVerifyResource.setPosition(position);
        return userVerifyResourceService.insert(userVerifyResource);
    }

    
    public void deleteVerfyResource(String userId) throws Exception {
        userVerifyResourceService.deleteVerfyResource(userId);
    }
}
