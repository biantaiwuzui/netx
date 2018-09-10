package com.netx.ucenter.service.user;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.user.enums.SystemBlackStatusEnum;
import com.netx.ucenter.mapper.user.UserSystemBlacklistMapper;
import com.netx.ucenter.model.user.UserSystemBlacklist;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 系统黑名单，拉黑后即锁定用户，类似封禁的功能 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-27
 */
@Service
public class SystemBlacklistService extends ServiceImpl<UserSystemBlacklistMapper, UserSystemBlacklist>{

    public UserSystemBlacklist getSystemBlacklistByUserId(String userId) throws Exception{
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("user_Id = {0}", userId);
        return this.selectOne(wrapper);
    }

    /**
     * 插入到黑名单记录
     * @param userId 被操作用户
     * @param operateType 操作类型
     * @return
     */
    public UserSystemBlacklist insertSystemBlacklist(String userId, int operateType){
        //先插入到系统黑名单表
        UserSystemBlacklist systemBlacklist = new UserSystemBlacklist();
        systemBlacklist.setUserId(userId);
        systemBlacklist.setStatus(operateType);//根据operateType更新状态(这里为拉黑)
        systemBlacklist.setDeleted(0);
        insert(systemBlacklist);
        return systemBlacklist;
    }

    /**
     * 查询黑名单的所有用户id
     * @return
     */
    public List<Object> selectUserIdListInBlackList(){
        Wrapper<UserSystemBlacklist> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("user_id").where("status = {0}", SystemBlackStatusEnum.DEFRIEND.getValue());
        return selectObjs(wrapper);
    }
    public Boolean updateSystemBlackByUserId(UserSystemBlacklist systemBlacklist,String userId){
        Wrapper<UserSystemBlacklist> systemBlacklistWrapper = new EntityWrapper();
        systemBlacklistWrapper.where("user_id = {0}", userId);
        return update(systemBlacklist, systemBlacklistWrapper);
    }

    public UserSystemBlacklist selectSystemBlackId(String userId){
        Wrapper<UserSystemBlacklist> systemBlacklistWrapper = new EntityWrapper();
        systemBlacklistWrapper.where("user_id = {0} AND deleted = 0", userId);
        return selectOne(systemBlacklistWrapper);
    }

}
