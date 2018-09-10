package com.netx.ucenter.service.user;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.ucenter.mapper.user.UserCreditMapper;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.model.user.UserCredit;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户信用表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-27
 */
@Service
public class UserCreditService extends ServiceImpl<UserCreditMapper, UserCredit>{

    public Page<UserCredit> selectUserCreditPage(String userId,Page page) throws Exception{
        Wrapper<UserCredit> wrapper = new EntityWrapper<UserCredit>();
        wrapper.setSqlSelect("credit, description, create_time createTime").
                orderBy("create_time", false).
                where("user_id = {0}", userId);
        return selectPage(page, wrapper);
    }

    public void deleteByUserId(String userId) throws Exception{
        Wrapper<UserCredit> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id",userId);
        delete(wrapper);
    }

    /*网信 - 获取用户信用详情**/
    public List<UserCredit> selectUserCreditByUserId(String userId) throws Exception{
        Wrapper<UserCredit> wrapper = new EntityWrapper<UserCredit>();
        wrapper.setSqlSelect("credit, description, create_time createTime").
                orderBy("create_time", false).
                where("user_id = {0}", userId);
        return selectList(wrapper);
    }
}
