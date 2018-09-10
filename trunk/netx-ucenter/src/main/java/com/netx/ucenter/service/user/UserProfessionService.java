package com.netx.ucenter.service.user;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.ucenter.model.user.UserProfession;
import com.netx.ucenter.mapper.user.UserProfessionMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;


/**
 * <p>
 * 用户职业经历表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-27
 */
@Service
public class UserProfessionService extends ServiceImpl<UserProfessionMapper, UserProfession> {
    
    public void deleteByUserId(String userId) throws Exception{
        Wrapper<UserProfession> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id",userId);
        this.delete(wrapper);
    }

    public Integer countProfessionByUserId(String userId){
        Wrapper<UserProfession> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id",userId);
        return selectCount(wrapper);
    }

    public List<Object> selectUserProfessionOneByUserId(String userId,String contidion){
        if(StringUtils.isEmpty(contidion)){
            return null;
        }
        Wrapper<UserProfession> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect(contidion);
        wrapper.eq("user_id",userId);
        return selectObjs(wrapper);
    }

    public Integer getMaxPositionByUserId(String userId){
        Wrapper<UserProfession> wrapper = new EntityWrapper<UserProfession>();
        wrapper.setSqlSelect("max(position)");
        wrapper.where("user_id = {0}", userId);
        return (Integer) this.selectObj(wrapper);
    }

    public List<UserProfession> selectUserProfessionListByUserId(String userId, Page page){
        Wrapper<UserProfession> wrapper = new EntityWrapper<UserProfession>();
        wrapper.setSqlSelect("id, company, department, top_profession as topProfession, year, position");
        wrapper.where("user_id = {0}", userId);
        return page==null?selectList(wrapper):selectPage(page,wrapper).getRecords();
    }

    public List<UserProfession> getUserProfessionListByUserId(String userId,Integer startPosition) throws Exception{
        Wrapper<UserProfession> wrapper = new EntityWrapper<UserProfession>();
        wrapper.setSqlSelect("id, position");
        wrapper.where("user_id = {0} AND position > {0}", userId, startPosition);
        return this.selectList(wrapper);
    }

    public Integer selectUserProfessionCount(String userId){
        Wrapper<UserProfession> wrapper = new EntityWrapper<UserProfession>();
        wrapper.where("user_id = {0} and deleted = 0",userId);
        return this.selectCount(wrapper);
    }
}
