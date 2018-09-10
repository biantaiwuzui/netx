package com.netx.ucenter.service.common;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.ucenter.mapper.common.CommonWalletMapper;
import com.netx.ucenter.model.common.CommonWallet;
import org.springframework.stereotype.Service;

/**
 * Create by wongloong on 17-9-17
 */
@Service
public class WalletService extends ServiceImpl<CommonWalletMapper, CommonWallet>{

    public CommonWallet selectByUserId(String userId){
        EntityWrapper wrapper = new EntityWrapper<>();
        wrapper.where("user_id={0}", userId);
        return this.selectOne(wrapper);
    }
}