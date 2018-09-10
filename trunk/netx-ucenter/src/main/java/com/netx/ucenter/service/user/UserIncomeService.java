package com.netx.ucenter.service.user;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.ucenter.model.user.UserIncome;
import com.netx.ucenter.mapper.user.UserIncomeMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户收益表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-27
 */
@Service
public class UserIncomeService extends ServiceImpl<UserIncomeMapper, UserIncome>{

    public void deleteByUserId(String userId) throws Exception{
        Wrapper<UserIncome> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id",userId);
        this.delete(wrapper);
    }

}
