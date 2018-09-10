package com.netx.ucenter.biz.user;

import com.netx.ucenter.model.user.UserBlacklistLog;
import com.netx.ucenter.service.user.SystemBlacklistLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 黑名单操作日志流水表(关联主表，取最新的一条，即为拉黑或释放理由) 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-27
 * 已归纳到系统黑名单中:BXT
 */
@Service
public class SystemBlacklistLogAction{
    @Autowired
    private SystemBlacklistLogService systemBlacklistLogService;

//    public boolean insertSystemBlacklistLog(String userId, String createUserId, String systemBlacklistId, String reason) throws Exception {
//        //再插入流水表
//        UserBlacklistLog systemBlacklistLog = new UserBlacklistLog();
//        systemBlacklistLog.setCreateUserId(createUserId);
//        systemBlacklistLog.setUserId(userId);
//        systemBlacklistLog.setSystemBlackListId(systemBlacklistId);
//        systemBlacklistLog.setReason(reason);
//        return systemBlacklistLogService.insert(systemBlacklistLog);
//    }

}
