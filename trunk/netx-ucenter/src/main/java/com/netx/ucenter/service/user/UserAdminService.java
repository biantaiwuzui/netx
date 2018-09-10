package com.netx.ucenter.service.user;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.ucenter.mapper.user.UserAdminMapper;
import com.netx.ucenter.model.user.UserAdmin;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;


@Service
public class UserAdminService  extends ServiceImpl<UserAdminMapper, UserAdmin> {

    /**
     * 根据用户名查询管理员用户
     * @param userName
     * @param isSuperAdmin
     * @return
     */
    public UserAdmin getUserAdminByUserName(String userName, Boolean isSuperAdmin){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.where("user_name = {0} AND deleted = 0", userName);
        if(isSuperAdmin != null){
            wrapper.and("is_super_admin = {0}", isSuperAdmin);
        }
        return selectOne(wrapper);
    }

    public Page<UserAdmin> getUserAdmins(String userName, String realName, Integer deleted, Page page){
        EntityWrapper wrapper = new EntityWrapper();
        if(deleted != null){
            wrapper.where("deleted = {0}", deleted);
        }
        if(StringUtils.isNotBlank(userName)){
            wrapper.like("user_name", userName);
        }
        if(StringUtils.isNotBlank(realName)){
            wrapper.like("real_name", realName);
        }
        return selectPage(page, wrapper);
    }

}
