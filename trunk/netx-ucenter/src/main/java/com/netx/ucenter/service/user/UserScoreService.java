package com.netx.ucenter.service.user;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.ucenter.model.user.UserScore;
import com.netx.ucenter.mapper.user.UserScoreMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户积分表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-27
 */
@Service
public class UserScoreService extends ServiceImpl<UserScoreMapper, UserScore>{

    public void delScoreRecord(String userId) throws Exception{
        Wrapper<UserScore> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id",userId);
        this.delete(wrapper);
    }
    
    public Page<UserScore> selectScoreRecordList(Page page,String userId,Boolean isAsc) throws Exception{
        Wrapper<UserScore> wrapper = new EntityWrapper<UserScore>();
        wrapper.setSqlSelect("description, score, create_time as createTime")
                .where("user_id = {0}", userId)
                .orderBy("create_time"+isAscStr(isAsc));
        return selectPage(page, wrapper);
    }

    private String isAscStr(Boolean isAsc){
        return isAsc?"":" desc";
    }

    public Integer countByUserAndCode(String userId,Integer code,Long startTimestamp,Long endTimestamp){
        Wrapper<UserScore> wrapper = new EntityWrapper<UserScore>();
        wrapper.where("user_id = {0} AND code = {1}", userId, code)
                .between("create_time", startTimestamp, endTimestamp);
        return this.selectCount(wrapper);
    }
}
