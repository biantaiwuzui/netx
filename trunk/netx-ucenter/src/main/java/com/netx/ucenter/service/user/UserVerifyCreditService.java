package com.netx.ucenter.service.user;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.user.enums.VerifyCreditEnum;
import com.netx.ucenter.mapper.user.UserVerifyCreditMapper;
import com.netx.ucenter.model.user.UserVerifyCredit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 黎子安
 * @since 2018-01-26
 */
@Service
public class UserVerifyCreditService extends ServiceImpl<UserVerifyCreditMapper, UserVerifyCredit> {

    @Autowired
    UserVerifyCreditMapper userVerifyCreditMapper;

    public UserVerifyCreditMapper getUserVerifyCreditMapper() {
        return userVerifyCreditMapper;
    }

    public UserVerifyCredit selectUserIdNumber(String value, VerifyCreditEnum creditEnum) throws Exception {
        Wrapper<UserVerifyCredit> wrapper = new EntityWrapper<>();
        wrapper.eq(creditEnum.getName(),value);
        return this.selectOne(wrapper);
    }
    
    public void deleteUserVerifyCreditByUserId(String userId,Integer credit) throws Exception{
        userVerifyCreditMapper.delectUserId(userId,credit);
    }

    // 根据 用户id 获取身份证号（！！！）
    public String getUserIdentityByUserId(String userId) {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("id_number");
        wrapper.where("user_id = {0}", userId);
        return (String)selectObj(wrapper);
    }
}
