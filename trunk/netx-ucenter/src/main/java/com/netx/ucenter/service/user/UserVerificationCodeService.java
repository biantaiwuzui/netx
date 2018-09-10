package com.netx.ucenter.service.user;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.ucenter.model.user.UserVerificationCode;
import com.netx.ucenter.mapper.user.UserVerificationCodeMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户验证码 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-27
 */
@Service
public class UserVerificationCodeService extends ServiceImpl<UserVerificationCodeMapper, UserVerificationCode>{

    public UserVerificationCode getNewelyCode(String mobile,String userId,String code){
        Wrapper<UserVerificationCode> wrapper = new EntityWrapper<UserVerificationCode>();
        Map<String,Object> map = new HashMap<>();
        map.put("mobile",mobile);
        map.put("code",code);
        if(userId==null ||userId.isEmpty()){
            wrapper.isNull("user_id");
        }else{
            map.put("user_id",userId);
        }
        wrapper.allEq(map);
        wrapper.orderBy("expired_at desc");
        return this.selectOne(wrapper);
    }

    public void delMobileCode(String userId,String mobile) throws Exception{
        Wrapper<UserVerificationCode> wrapper = new EntityWrapper<>();
        if(mobile==null || mobile.trim().isEmpty()){
            wrapper.where("user_id = {0}",userId);
        }else{
            wrapper.where("user_id = {0} or mobile = {1}",userId,mobile);
        }
        this.delete(wrapper);
    }
    //======================== 黎子安  end  ========================
}
