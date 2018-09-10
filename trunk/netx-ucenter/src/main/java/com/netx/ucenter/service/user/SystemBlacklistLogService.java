package com.netx.ucenter.service.user;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.ucenter.mapper.user.UserBlacklistLogMapper;
import com.netx.ucenter.model.user.UserBlacklistLog;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 黑名单操作日志流水表(关联主表，取最新的一条，即为拉黑或释放理由) 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-27
 */
@Service
public class SystemBlacklistLogService extends ServiceImpl<UserBlacklistLogMapper, UserBlacklistLog>{
    /**
     * BXT 18.6.12
     */
    public boolean insertSystemBlacklistLog(String createUserName, String systemBlacklistId, String reason) throws Exception {
        //再插入流水表
        UserBlacklistLog systemBlacklistLog = new UserBlacklistLog();
        systemBlacklistLog.setCreateUserName(createUserName);
        systemBlacklistLog.setSystemBlackListId(systemBlacklistId);
        systemBlacklistLog.setReason(reason);
        return this.insert(systemBlacklistLog);
    }

    public UserBlacklistLog selectSystemBlacklistLog(String SystemBlackListId) throws Exception{
        Wrapper<UserBlacklistLog> wrapper = new EntityWrapper<>();
        wrapper.where("system_black_list_id = {0}", SystemBlackListId);
        return selectOne(wrapper);
    }

    public Boolean updateSystemBlacklistLogByUserId(UserBlacklistLog systemBlacklistLog,String SystemBlackListId){
        Wrapper<UserBlacklistLog> systemBlacklistLogWrapper = new EntityWrapper();
        systemBlacklistLogWrapper.where("system_black_list_id = {0}", SystemBlackListId);
        return update(systemBlacklistLog, systemBlacklistLogWrapper);
    }
}
