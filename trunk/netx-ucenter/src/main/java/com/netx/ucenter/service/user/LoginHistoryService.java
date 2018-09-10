package com.netx.ucenter.service.user;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.ucenter.mapper.user.UserLoginHistoryMapper;
import com.netx.ucenter.model.user.UserLoginHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 登录记录表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-27
 */
@Service
public class LoginHistoryService extends ServiceImpl<UserLoginHistoryMapper, UserLoginHistory>{

    @Autowired
    UserLoginHistoryMapper userLoginHistoryMapper;

    public UserLoginHistoryMapper getUserLoginHistoryMapper() {
        return userLoginHistoryMapper;
    }

    public List<String> getUserId(Collection<?> ids) throws Exception{
        Wrapper<UserLoginHistory> wrapper = new EntityWrapper<>();
        wrapper.in("user_id",ids);
        wrapper.setSqlSelect("distinct user_id");
        wrapper.orderBy("user_id");
        return (List<String>)(List)this.selectObjs(wrapper);
    }

    public void deleteByUserId(String userId) throws Exception{
        Wrapper<UserLoginHistory> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id",userId);
        delete(wrapper);
    }
}
