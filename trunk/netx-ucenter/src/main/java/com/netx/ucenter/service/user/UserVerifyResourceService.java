package com.netx.ucenter.service.user;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.ucenter.model.user.UserVerifyResource;
import com.netx.ucenter.mapper.user.UserVerifyResourceMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户提交认证资源表，如一些图片或文件 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-27
 */
@Service
public class UserVerifyResourceService extends ServiceImpl<UserVerifyResourceMapper, UserVerifyResource> {
    
    public void deleteVerfyResource(String userId) throws Exception {
        Wrapper<UserVerifyResource> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id",userId);
        this.delete(wrapper);
    }

    public List<UserVerifyResource> selectVerifyResourceListByUserVerifyId(String userVerifyId){
        Wrapper<UserVerifyResource> wrapper = new EntityWrapper<UserVerifyResource>();
        wrapper.where("user_verify_id = {0}", userVerifyId);
        wrapper.orderBy("position");
        return selectList(wrapper);
    }
}
