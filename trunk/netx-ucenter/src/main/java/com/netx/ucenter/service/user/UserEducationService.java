package com.netx.ucenter.service.user;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.ucenter.model.user.UserEducation;
import com.netx.ucenter.mapper.user.UserEducationMapper;
import com.netx.ucenter.model.user.UserProfile;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * <p>
 * 用户教育经历表
凡是标签，均以字符形式存，以逗号分隔 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-27
 */
@Service
public class UserEducationService extends ServiceImpl<UserEducationMapper, UserEducation> {

    public Integer getMaxPositionByUserId(String userId){
        Wrapper<UserEducation> wrapper = new EntityWrapper<UserEducation>();
        wrapper.setSqlSelect("max(position)");
        wrapper.where("user_id = {0}", userId);
        return (Integer) this.selectObj(wrapper);
    }

    public void deleteByUserId(String userId) throws Exception{
        Wrapper<UserEducation> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id",userId);
        this.delete(wrapper);
    }

    public List<UserEducation> selectUserEducationListByUserId(String userId){
        Wrapper<UserEducation> userEducationWrapper = new EntityWrapper<UserEducation>();
        userEducationWrapper.setSqlSelect("id, school, department, speciality, year, time, degree, position");
        userEducationWrapper.where("user_id = {0} and deleted = 0", userId);
        return this.selectList(userEducationWrapper);
    }

    public int countEducationByUserId(String userId){
        Wrapper wrapper = new EntityWrapper();
        wrapper.where("user_id = {0}",userId);
        return this.selectCount(wrapper);
    }

    public List<UserEducation> getUserEducationListByUserId(String userId,Integer startPosition){
        Wrapper<UserEducation> wrapper = new EntityWrapper<UserEducation>();
        wrapper.setSqlSelect("id, position");
        wrapper.where("user_id = {0} AND position > {0}", userId, startPosition);
        return this.selectList(wrapper);
    }

    public List<String> selectEducationByDegree(List<String> userIds,String degree){
        Wrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect("user_id");
        wrapper.like("degree", degree).groupBy("user_id");
        wrapper.in("user_id", userIds);
        return selectObjs(wrapper);
    }

    public List<Object> selectEducationOneDateByUserId(String userId,String value){
        if(StringUtils.isEmpty(value)){
            return null;
        }
        Wrapper wrapper = new EntityWrapper();
        wrapper.setSqlSelect(value);
        wrapper.eq("user_id",userId);
        return selectObjs(wrapper);
    }
}
