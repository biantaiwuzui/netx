package com.netx.ucenter.service.user;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.user.dto.wangMing.AddValueRecordRequestDto;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.model.user.UserValue;
import com.netx.ucenter.mapper.user.UserValueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * <p>
 * 用户身价表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-27
 */
@Service
public class UserValueService extends ServiceImpl<UserValueMapper, UserValue> {

    public void delValueRecord(String userId) throws Exception{
        Wrapper<UserValue> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id",userId);
        this.delete(wrapper);
    }
}
