package com.netx.ucenter.service.user;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.ucenter.model.user.UserContribution;
import com.netx.ucenter.mapper.user.UserContributionMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户贡献表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-27
 */
@Service
public class UserContributionService extends ServiceImpl<UserContributionMapper, UserContribution>{

    public void deleteByUserId(String userId) throws Exception{
        Wrapper<UserContribution> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id",userId);
        delete(wrapper);
    }
}
