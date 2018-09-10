package com.netx.credit.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.credit.mapper.UserCreditLikesMapper;
import com.netx.credit.model.UserCreditLikes;
import org.springframework.stereotype.Service;

/**
 * @author 梓
 * @date 2018-08-07 18:15
 */
@Service
public class UserCreditLikesService extends ServiceImpl<UserCreditLikesMapper , UserCreditLikes> {

    public UserCreditLikes getCreditLikesByUserIdAndCreditId(String userId, String creditId){
        Wrapper<UserCreditLikes> wrapper = new EntityWrapper<>();
        wrapper.where("user_id = {0} and credit_id = {1}", userId, creditId);
        return this.selectOne(wrapper);
    }
}
