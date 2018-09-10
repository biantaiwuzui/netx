package com.netx.ucenter.service.user;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.user.enums.VerifyStatusEnum;
import com.netx.common.user.enums.VerifyTypeEnum;
import com.netx.ucenter.mapper.user.UserVerifyMapper;
import com.netx.ucenter.model.user.UserVerify;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户认证表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-27
 */
@Service
public class UserVerifyService extends ServiceImpl<UserVerifyMapper, UserVerify> {

    public Integer getVerifyState(String userId,Integer verifyType) throws Exception{
        Wrapper<UserVerify> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("status");
        wrapper.where("user_id = {0} and verify_type = {1}",userId,verifyType);
        wrapper.orderBy("create_time desc");
        return (Integer) this.selectObj(wrapper);
    }

    public Page<UserVerify> getUserVerifyByStatus(Integer status, Page page, List<String> ids, Integer verifyType){
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.where("deleted = 0");
        if(status != null) {
            wrapper.and("status = {0}", status);
        }
        if(verifyType != null){
            wrapper.and("verify_type = {0}", verifyType);
        }
        if (ids != null && ids.size() > 0){
            wrapper.in("user_id", ids);
        }
        return selectPage(page, wrapper);
    }
    
    public void deleteVerify(String userId) throws Exception{
        Wrapper<UserVerify> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id",userId);
        this.delete(wrapper);
    }

    public UserVerify checkVerifyByCar(Integer verifyType,String carBrand){
        Wrapper<UserVerify> wrapper = new EntityWrapper<>();
        wrapper.eq("verify_type",verifyType);
        wrapper.eq("description",carBrand);
        return this.selectOne(wrapper);
    }

    public UserVerify queryVerifyByUserId(String userId,VerifyTypeEnum typeEnum){
        Wrapper wrapper = new EntityWrapper<UserVerify>();
        wrapper.where("user_id = {0} and verify_type = {1}",userId,typeEnum.getValue());
        wrapper.orderBy("create_time desc");
        return selectOne(wrapper);
    }

    public List<UserVerify> selectVerifyByUserId(String userId,Integer status) throws Exception{
        Wrapper wrapper = new EntityWrapper<UserVerify>();
        wrapper.where("user_id = {0} and status = {1}", userId, status);
        return selectList(wrapper);
    }

    /**
     * 通过userid，查找已认证的用户id
     * @param userId
     * @return
     */
    public List<UserVerify> selectUsersVerifyIdByUserId(List<String> userId){
        Wrapper<UserVerify> wrapper = new EntityWrapper<>();
        wrapper.in("user_id",userId);
        wrapper.setSqlSelect("user_id as userId,status");
        return selectList(wrapper);
    }

    public Integer selectStautsByUserId(String userId){
        Wrapper wrapper = new EntityWrapper<UserVerify>();
        wrapper.setSqlSelect("status").where("user_id = {0} and deleted = {1}", userId, 0);
        return (Integer) selectObj(wrapper);
    }
    
    public String selectUserIdentity(String userId,Integer verifyType,Integer status) throws Exception{
        Wrapper<UserVerify> wrapper = createWrapperByUserId(userId, verifyType, status);
        wrapper.setSqlSelect("description");
        return (String) selectObj(wrapper);
    }

    public Integer countUserIdentity(String userId,Integer verifyType,Integer status) throws Exception{
        Wrapper<UserVerify> wrapper = createWrapperByUserId(userId, verifyType, status);
        return this.selectCount(wrapper);
    }

    public List<Map<String,Object>> countUserIdentities(String userId,Integer status){
        Wrapper<UserVerify> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("count(verify_type) level,verify_type as type");
        wrapper.where("user_id = {0} and status = {1}",userId,status);
        wrapper.groupBy("verify_type");
        wrapper.orderBy("verify_type");
        return selectMaps(wrapper);
    }

    private Wrapper<UserVerify> createWrapperByUserId(String userId,Integer verifyType,Integer status){
        Wrapper<UserVerify> wrapper = new EntityWrapper<>();
        wrapper.where("user_id = {0} and verify_type = {1} and status = {2}",
                userId, verifyType, status);
        return wrapper;
    }

    public Integer countUserVerifyByUserId(Integer verifyType, String userId) throws Exception{
        Wrapper<UserVerify> wrapper = new EntityWrapper<>();
        wrapper.where("status != {0} and verify_type = {1} and user_id = {2}",
                VerifyStatusEnum.REJECTIVE_AUTHENTICATION.getValue(),
                verifyType,
                userId
        );
        return selectCount(wrapper);
    }

    public List<UserVerify> getUserVerifiesByDescription(String description) throws Exception{
        Wrapper<UserVerify> wrapper = new EntityWrapper<>();
        wrapper.where("verify_type = {0} and status = {1}",
                VerifyTypeEnum.IDCARD_TYPE.getValue(),
                VerifyStatusEnum.PASSING_AUTHENTICATION.getValue());
        wrapper.like("description", description);
        return selectList(wrapper);
    }
}
