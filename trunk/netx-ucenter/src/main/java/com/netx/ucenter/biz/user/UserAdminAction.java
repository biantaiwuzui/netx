package com.netx.ucenter.biz.user;

import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.redis.RedisInfoHolder;
import com.netx.common.user.util.VoPoConverter;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.model.user.UserAdmin;
import com.netx.ucenter.service.user.UserAdminService;
import com.netx.ucenter.util.SearchProcessing;
import com.netx.ucenter.vo.request.AddUserAdminRequestDto;
import com.netx.ucenter.vo.request.QueryUserAdminListRequestDto;
import com.netx.ucenter.vo.request.ResetPasswordRequestDto;
import com.netx.ucenter.vo.request.UpdatePasswordRequestDto;
import com.netx.ucenter.vo.response.UserAdminListResponseDto;
import com.netx.utils.cache.RedisCache;
import com.netx.utils.cache.RedisKeyName;
import com.netx.utils.cache.RedisTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserAdminAction {

    private Logger logger = LoggerFactory.getLogger(UserAdminAction.class);

    @Autowired
    private UserAdminService userAdminService;

    private PasswordEncoder encoder;

    private RedisCache redisCache=null;

    public UserAdminService getUserAdminService() {
        return userAdminService;
    }

    @Autowired
    public UserAdminAction(RedisInfoHolder redisInfoHolder) {
        encoder = new BCryptPasswordEncoder();
        logger.info("userAdmin构造注入服务");
        redisCache = RedisCache.getRedisCache(redisInfoHolder.getHost(),redisInfoHolder.getPassword(),redisInfoHolder.getPort());
    }

    public UserAdmin queryAdmin(String username){
        RedisKeyName redisKeyName = new RedisKeyName("userAdmin", RedisTypeEnum.OBJECT_TYPE,username);
        UserAdmin user = (UserAdmin) redisCache.get(redisKeyName.getUserKey());
        if(user==null){
            //数据库查询
            user = userAdminService.getUserAdminByUserName(username,null);
            if(user!=null){
                //将数据库的存回redis
                redisCache.put(redisKeyName.getUserKey(),user);
            }
        }
        return user;
    }

    /**
     * 判断用户是登录状态
     * @param username
     * @return
     */
    public User queryUser(String username){
        RedisKeyName redisKeyName = new RedisKeyName("userAdmin", RedisTypeEnum.OBJECT_TYPE,username);
        User user = (User) redisCache.get(redisKeyName.getUserKey());
        if (user==null){
            return null;
        }
        return user;
    }
    /**
     * boss系统登录
     * @param userName
     * @param password
     * @return （user.id=null 系统异常，user=null 登录失败）除此之外 表示以获取全部用户信息
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public UserAdmin loginUser(String userName, String password) throws Exception{
        UserAdmin userAdmin = userAdminService.getUserAdminByUserName(userName, null);
        if(userAdmin != null){
            if(StringUtils.isNotBlank(password)){
                if(encoder.matches(password, userAdmin.getPassword())){
                    //进行登录成功的操作
                    logger.info("登录成功");
                    return userAdmin;
                }
            }
        }
        return null;
    }

    /**
     * 添加管理员用户
     * @param requestDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public UserAdmin addUserAdmin(AddUserAdminRequestDto requestDto){
        UserAdmin userAdmin = new UserAdmin();
        VoPoConverter.copyProperties(requestDto, userAdmin);
        userAdmin.setDeleted(0);
        if(userAdminService.getUserAdminByUserName(requestDto.getUserName(), null) == null){
            if(userAdminService.getUserAdminByUserName(requestDto.getCreateUserName(), true) != null){
                userAdmin.setPassword(encoder.encode(requestDto.getPassword()));
                userAdmin.setUpdateUserName(requestDto.getCreateUserName());
                if(userAdminService.insert(userAdmin)){
                    return userAdmin;
                }
                return null;
            }
            userAdmin.setCreateUserName(null);
            return userAdmin;
        }
        userAdmin.setUserName(null);
        return userAdmin;
    }

    /**
     * 修改密码
     * @param requestDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public UserAdmin updatePassword(UpdatePasswordRequestDto requestDto){
        UserAdmin userAdmin = userAdminService.getUserAdminByUserName(requestDto.getUserName(), null);
        if(userAdmin != null){
            if(encoder.matches(requestDto.getOldPassword(), userAdmin.getPassword())){
                userAdmin.setPassword(encoder.encode(requestDto.getNewPassword()));
                if(userAdminService.updateById(userAdmin)){
                    return userAdmin;
                }
                return null;
            }
            userAdmin.setPassword(null);
            return userAdmin;
        }
        userAdmin.setUserName(null);
        return userAdmin;
    }

    /**
     * 重置密码
     * @param requestDto
     * @return
     */
    public String resetPassword(ResetPasswordRequestDto requestDto){
        if(userAdminService.getUserAdminByUserName(requestDto.getSuperUserName(), true) != null){
            UserAdmin userAdmin = userAdminService.getUserAdminByUserName(requestDto.getUserName(), null);
            if(userAdmin != null){
                if(userAdmin.getSuperAdmin()){
                    return "只能重置普通管理员密码！";
                }
                userAdmin.setPassword(encoder.encode(requestDto.getPassword()));
                userAdmin.setUpdateUserName(requestDto.getSuperUserName());
                if(userAdminService.updateById(userAdmin)){
                    return null;
                }
                return "重置失败！";
            }
            return "该管理员用户不存在！";
        }
        return "您不是超级管理员，没有权限重置密码！";
    }

    /**
     * 获取管理员列表
     * @param requestDto
     * @return
     */
    public Map<String, Object> queryUserAdminList(QueryUserAdminListRequestDto requestDto){
        SearchProcessing searchProcessing=new SearchProcessing();
        Page page = new Page(requestDto.getCurrent(), requestDto.getSize());
        Page<UserAdmin> userAdmins = userAdminService.getUserAdmins(searchProcessing.SearchProcessing(requestDto.getUserName()), searchProcessing.SearchProcessing(requestDto.getRealName()), requestDto.getDeleted(), page);
        List<UserAdminListResponseDto> responseDtos = new ArrayList<>();
        for(UserAdmin userAdmin:userAdmins.getRecords()){
            UserAdminListResponseDto responseDto = new UserAdminListResponseDto();
            VoPoConverter.copyProperties(userAdmin, responseDto);
            responseDtos.add(responseDto);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("total", page.getTotal());
        map.put("list", responseDtos);
        return map;
    }

    /**
     * 禁用/解除禁用 管理员用户
     * @param id
     * @return
     */
    public String deleteById(String id, String userName, String reason){
        if(userAdminService.getUserAdminByUserName(userName, true) != null){
            UserAdmin userAdmin = userAdminService.selectById(id);
            if(userAdmin != null){
                if(userAdmin.getDeleted() == 0){
                    userAdmin.setDeleted(1);
                }else{
                    userAdmin.setDeleted(0);
                }
                userAdmin.setReason(reason);
                userAdmin.setUpdateUserName(userName);
                if(userAdminService.updateById(userAdmin)){
                    return null;
                }
                return "操作失败！";
            }
            return "该管理员用户不存在！";
        }
        return "您不是超级管理员，没有权限禁用！";
    }
}
