package com.netx.ucenter.service.common;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.ucenter.mapper.common.CommonGroupMapper;
import com.netx.ucenter.model.common.CommonGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Create on 17-11-14
 * @author wongloong
 */
@Service
public class GroupService extends ServiceImpl<CommonGroupMapper, CommonGroup>{

    private Logger logger = LoggerFactory.getLogger(GroupService.class);

    @Autowired
    private CommonGroupMapper commonGroupMapper;

    public CommonGroupMapper getCommonGroupMapper() {
        return commonGroupMapper;
    }

    public CommonGroup getGroupByGroupId(Long groupId){
        Wrapper wrapper =getWrapperByCondition("group_id",groupId);
        wrapper.orderBy("group_name desc");
        return selectOne(wrapper);
    }

    public List<CommonGroup> getGroupsByGroupId(Long groupId){
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("deleted=0");
        if(groupId!=null){
            wrapper.like("group_id",groupId+"");
        }
        wrapper.orderBy("group_name desc");
        return selectList(wrapper);
    }

    public Boolean updateGroup(Long groupId,String name,String userId){
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("group_id = {0}",groupId);
        CommonGroup commonGroupChat = new CommonGroup();
        commonGroupChat.setGroupName(name);
        commonGroupChat.setUpdateUserId(userId);
        return update(commonGroupChat,wrapper);
    }

    public CommonGroup getGroupByPwd(String pwd, Date time) throws Exception{
        Wrapper wrapper = getWrapperByPwd(pwd, time);
        return selectOne(wrapper);
    }

    public Integer getCount(CommonGroup wzCommonGroupChat) throws Exception{
        return selectCount(new EntityWrapper<>(wzCommonGroupChat));
    }

    public Integer getCount(String pwd,Date time) throws Exception{
        return selectCount(getWrapperByPwd(pwd, time));
    }

    private Wrapper getWrapperByPwd(String pwd,Date time){
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("pwd={0} and create_time  >= {1}", pwd,time);
        return wrapper;
    }

    public List<String> getUserIdsByGroupId(String groupId){
        EntityWrapper wrapper=new EntityWrapper();
        wrapper.setSqlSelect("user_id").where("group_id={0}",groupId);
        return this.selectObjs(wrapper);
    }

    public CommonGroup getAdminGroupByGroupId(String groupId){
        EntityWrapper wrapper=new EntityWrapper();
        wrapper.where("group_id={0} and admin=1",groupId);
        return this.selectOne(wrapper);
    }

    public Boolean delByCondition(String key,String value) throws Exception{
        return delete(getWrapperByCondition(key,value));
    }

    private Wrapper getWrapperByCondition(String key,Object value){
        Wrapper wrapper = new EntityWrapper();
        wrapper.eq(key,value);
        return wrapper;
    }

    public Boolean deleteByGroupId(String userId,String groupId) throws Exception{
        Wrapper wrapper = getWrapperByCondition("user_id",userId);
        wrapper.eq("group_id", groupId);
        return delete(wrapper);
    }

    public CommonGroup getWzCommonGroupChatByGroupId(String userId,String groupId) throws Exception{
        Wrapper wrapper = getWrapperByCondition("user_id",userId);
        wrapper.eq("group_id", groupId);
        wrapper.orderBy("group_name");
        return selectOne(wrapper);
    }

    public List<CommonGroup> getCommonGroupListByUserId(String userId){
        return selectList(getWrapperByCondition("user_id",userId));
    }
}
