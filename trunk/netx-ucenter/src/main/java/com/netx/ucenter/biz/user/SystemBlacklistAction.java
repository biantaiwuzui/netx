package com.netx.ucenter.biz.user;

import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.user.dto.userManagement.SelectUserInSystemBlacklistRequest;
import com.netx.common.user.dto.userManagement.SelectUserInSystemBlacklistResponse;
import com.netx.common.user.enums.SystemBlackStatusEnum;
import com.netx.common.user.util.ComputeAgeUtils;
import com.netx.common.user.util.VoPoConverter;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.model.user.UserBlacklistLog;
import com.netx.ucenter.model.user.UserSystemBlacklist;
import com.netx.ucenter.service.user.SystemBlacklistLogService;
import com.netx.ucenter.service.user.SystemBlacklistService;
import com.netx.ucenter.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统黑名单，拉黑后即锁定用户，类似封禁的功能 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-27
 */
@Service
public class SystemBlacklistAction{

    @Autowired
    private SystemBlacklistService systemBlacklistService;

    @Autowired
    private UserAction userAction;
    @Autowired
    private UserService userService;
    @Autowired
    private SystemBlacklistLogService systemBlacklistLogService;


    @Transactional
    public Map selectUserInSystemBlacklist(SelectUserInSystemBlacklistRequest request) throws Exception {
        Map map = new HashMap();
        List<SelectUserInSystemBlacklistResponse> list = new ArrayList<>();
        Integer operateType = request.getOperateType();
        List<String> userIdListInBlackList = (List<String>)(List)systemBlacklistService.selectUserIdListInBlackList();
        //System.out.println("黑名单的所有id：" + userIdListInBlackList);
        //根据用户id到user表分页查询用户信息
        Page<User> page = new Page<User>(request.getCurrentPage(), request.getSize());
        List<User> userList = userService.pageUser(request.getUserNumber(),page,userIdListInBlackList,operateType);
        //若查询出的用户列表为空，则返回 null
        if(userList ==null || userList.isEmpty()){
            return null;
        }
        int length = userList.size();
        for (int i=0; i<length; i++){
            User user = userList.get(i);
//            if (user !=null){
//                continue;
//            }
            SelectUserInSystemBlacklistResponse response = VoPoConverter.copyProperties(user, SelectUserInSystemBlacklistResponse.class);
            response.setAge(ComputeAgeUtils.getAgeByBirthday(user.getBirthday()));
            //UserSystemBlacklist a=systemBlacklistService.selectSystemBlackId(user.getId());
            // 只有黑名单的才需要查询原因，以及操作人昵称
            if(operateType == SystemBlackStatusEnum.DEFRIEND.getValue()) {
                UserBlacklistLog systemBlacklistLog = systemBlacklistLogService.selectSystemBlacklistLog(systemBlacklistService.selectSystemBlackId(user.getId()).getId());
                if (systemBlacklistLog==null){
                    continue;
                }
//                User createUser = userService.selectById(systemBlacklistLog.getCreateUserId());
//                if (createUser==null){
//                    continue;
//                }
                response.setReason(systemBlacklistLog.getReason());
                response.setOperateUserNickname(systemBlacklistLog.getCreateUserName());
            }
            response.setId(user.getId());
            list.add(response);
        }
        map.put("userList",list);
        map.put("count",page.getTotal());
        return map;
    }


    @Transactional
    public boolean operateSystemBlacklist(String userId, String createUserName, String reason, Integer operateType) throws Exception{
        //如果操作为拉黑，要先查询黑名单表里是否存在用户数据，若不存在，做插入操作，若存在，做更新操作
        //操作：拉黑
        if(operateType == SystemBlackStatusEnum.DEFRIEND.getValue()){
            userAction.lockUser(userId, true);//锁定用户
            if(systemBlacklistService.getSystemBlacklistByUserId(userId) == null) {//如果系统黑名单表不存在此用户，插入操作
                UserSystemBlacklist systemBlacklist = systemBlacklistService.insertSystemBlacklist(userId, operateType);
                systemBlacklistLogService.insertSystemBlacklistLog(createUserName, systemBlacklist.getId(), reason);
                return true;
            } else {//如果系统黑名单表存在此用户，更新操作
                return updateSystemBlacklist(userId, createUserName, reason, operateType);
            }
        }
        //操作：释放
        if (operateType == SystemBlackStatusEnum.RELEASE.getValue()) {
            userAction.lockUser(userId, false);//解锁用户
            return updateSystemBlacklist(userId, createUserName, reason, operateType);
        }
        return false;
    }

    //------ 私有 ------

    /**
     *  更新系统黑名单表和流水表(通用私有方法)
     *  更新拉黑或释放的状态和原因
     *  @param userId
     *  @param createUserName 进行操作的用户
     *  @param reason 原因
     * @param operateType 操作类型（白名单：0， 黑名单：1）
     */
    @Transactional
    boolean updateSystemBlacklist(String userId, String createUserName, String reason, Integer operateType) throws Exception{
        //更新系统黑名单表记录
        UserSystemBlacklist systemBlacklist = new UserSystemBlacklist();
        systemBlacklist.setStatus(operateType);//根据operateType更新状态
        boolean flag1 = systemBlacklistService.updateSystemBlackByUserId(systemBlacklist, userId);
        //更新流水表记录
        UserBlacklistLog systemBlacklistLog = new UserBlacklistLog();
        systemBlacklistLog.setCreateUserName(createUserName);
        systemBlacklistLog.setReason(reason);
        boolean flag2 = systemBlacklistLogService.updateSystemBlacklistLogByUserId(systemBlacklistLog, systemBlacklistService.selectSystemBlackId(userId).getId());
        return flag1 && flag2;
    }

}
