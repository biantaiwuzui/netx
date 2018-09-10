package com.netx.ucenter.service.user;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.ucenter.model.user.UserInterest;
import com.netx.ucenter.mapper.user.UserInterestMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 用户兴趣 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-27
 */
@Service
public class UserInterestService extends ServiceImpl<UserInterestMapper, UserInterest> {

    public void deleteByUserId(String userId) throws Exception{
        this.delete(createWrapper(userId));
    }

    private Wrapper createWrapper(String userId){
        Wrapper<UserInterest> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id",userId);
        return wrapper;
    }

    public Integer getMaxPositionByUserId(String userId){
        Wrapper<UserInterest> wrapper = new EntityWrapper<UserInterest>();
        wrapper.setSqlSelect("max(position)");
        wrapper.where("user_id = {0}", userId);
        return (Integer) this.selectObj(wrapper);
    }

    public List<UserInterest> selectUserInterestListByUserId(String userId){
        Wrapper<UserInterest> wrapper = new EntityWrapper<UserInterest>();
        wrapper.where("user_id = {0}", userId);
        return this.selectList(wrapper);
    }

    public Integer countInterestByUserId(String userId){
        Wrapper<UserInterest> wrapper = new EntityWrapper<UserInterest>();
        wrapper.where("user_id = {0}", userId);
        return this.selectCount(wrapper);
    }

    public List<UserInterest> getUserInterestListByUserId(String userId,Integer startPosition){
        Wrapper<UserInterest> wrapper = new EntityWrapper<UserInterest>();
        wrapper.setSqlSelect("id, position");
        wrapper.where("user_id = {0} AND position > {0}", userId, startPosition);
        return this.selectList(wrapper);
    }

    public List<Object> selectInterestOneByUserId(String userId,String contidion){
        if(StringUtils.isEmpty(contidion)){
            return null;
        }
        Wrapper wrapper = createWrapper(userId);
        wrapper.setSqlSelect(contidion);
        return this.selectObjs(wrapper);
    }
}
