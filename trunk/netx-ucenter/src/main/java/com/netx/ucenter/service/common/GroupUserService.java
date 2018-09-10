package com.netx.ucenter.service.common;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.ucenter.mapper.common.CommonGroupUserMapper;
import com.netx.ucenter.model.common.CommonGroupUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Create on 17-11-14
 * @author wongloong
 */
@Service
public class GroupUserService extends ServiceImpl<CommonGroupUserMapper, CommonGroupUser>{

    private Logger logger = LoggerFactory.getLogger(GroupUserService.class);

    public List<CommonGroupUser> getListByGroupId(String groupId) throws Exception{
        return selectList(getWrapperByCondition("group_id",groupId));
    }

    public Boolean updateName(String groupId,String name,String userId){
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("group_id = {0} and user_id = {1}",groupId,userId);
        CommonGroupUser commonGroupChat = new CommonGroupUser();
        commonGroupChat.setUserName(name);
        commonGroupChat.setUpdateUserId(userId);
        return update(commonGroupChat,wrapper);
    }

    public Integer getCount(CommonGroupUser commonGroupUser){
        return selectCount(new EntityWrapper<>(commonGroupUser));
    }

    public List<String> getUserIdsByGroupId(String groupId){
        EntityWrapper wrapper=new EntityWrapper();
        wrapper.setSqlSelect("user_id").where("group_id={0}",groupId);
        return this.selectObjs(wrapper);
    }

    public Boolean delByCondition(String key,String value) throws Exception{
        return delete(getWrapperByCondition(key,value));
    }

    private Wrapper getWrapperByCondition(String key,String value){
        Wrapper wrapper = new EntityWrapper();
        wrapper.eq(key,value);
        return wrapper;
    }

    public Boolean deleteByGroupId(String userId,String groupId) throws Exception{
        Wrapper wrapper = getWrapperByCondition("user_id",userId);
        wrapper.eq("group_id", groupId);
        return delete(wrapper);
    }

    public CommonGroupUser getWzCommonGroupChatByGroupId(String userId,String groupId){
        Wrapper wrapper = getWrapperByCondition("group_id",groupId);
        wrapper.eq("user_id", userId);
        return selectOne(wrapper);
    }

    public List<CommonGroupUser> getWzCommonGroupChatByGroupId(String[] userId,String groupId){
        Wrapper wrapper = getWrapperByCondition("group_id",groupId);
        wrapper.in("user_id", userId);
        return selectList(wrapper);
    }

    public List<String> getIdsByGroupId(String[] userId,String groupId){
        Wrapper wrapper = getWrapperByCondition("group_id",groupId);
        wrapper.in("user_id", userId);
        wrapper.setSqlSelect("id");
        return (List<String>)(List)selectObjs(wrapper);
    }

    public Boolean delectByIds(List<String> ids){
        Wrapper wrapper = new EntityWrapper();
        wrapper.in("id",ids);
        return delete(wrapper);
    }

    public String getUserNameByGroupId(String userId,String groupId){
        Wrapper wrapper = getWrapperByCondition("group_id",groupId);
        wrapper.eq("user_id", userId);
        wrapper.setSqlSelect("user_name");
        return (String)selectObj(wrapper);
    }
}
