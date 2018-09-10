package com.netx.ucenter.service.user;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.user.dto.information.SelectUserListByDetailRequestDto;
import com.netx.common.user.dto.information.UpdateUserProfileRequest;
import com.netx.common.user.dto.integration.SelectUserEducationResponse;
import com.netx.common.user.dto.integration.SelectUserInterestResponse;
import com.netx.common.user.dto.integration.SelectUserProfessionResponse;
import com.netx.common.user.dto.wangMing.AddScoreRecordRequestDto;
import com.netx.common.user.enums.UserInformationScoreEnum;
import com.netx.common.user.enums.VerifyStatusEnum;
import com.netx.common.user.enums.VerifyTypeEnum;
import com.netx.common.vo.business.UpdateUserDetailsRequestDto;
import com.netx.ucenter.aop.CalculateProfileScore;
import com.netx.ucenter.mapper.user.UserProfileMapper;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.model.user.UserPhoto;
import com.netx.ucenter.model.user.UserProfile;
import com.netx.ucenter.model.user.UserVerify;
import com.netx.ucenter.service.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户详情表
凡是标签，均以字符形式存，以逗号分隔 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-27
 */
@Service
public class UserProfileService extends ServiceImpl<UserProfileMapper, UserProfile> {

     /**
     * 定制查询用户详情信息
     * @param userIdList 子查询条件的用户id列表
     * @param field 字段
     * @return
     */
     public List<UserProfile> selectUserProfile(List<String> userIdList, StringBuilder field){
        Wrapper<UserProfile> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect(field.toString()).orderBy("user_id");
        wrapper.in("user_id", userIdList);
        return selectList(wrapper);
    }

    public List<UserProfile> selectUserProfileByUserId(List<String> userIdList) throws Exception {
        Wrapper<UserProfile> wrapper = new EntityWrapper<UserProfile>();
        wrapper.in("user_id", userIdList);
        return selectList(wrapper);
    }

    public Boolean updateUserProfile(String userId,UserProfile userProfile){
        Wrapper wrapper = new EntityWrapper<UserProfile>();
        wrapper.where("user_id = {0}", userId);
        return this.update(userProfile,wrapper);
    }

    /**
     * 根据用户id查询用户单一基本信息
     * @param userId
     * @return
     */
    public String getUserTag(String userId,String type){
        if(type.isEmpty()){
            return null;
        }
        Wrapper<UserProfile> userProfileWrapper = new EntityWrapper<>();
        userProfileWrapper.setSqlSelect(type).eq("user_id",userId);
        return (String) this.selectObj(userProfileWrapper);
    }

    public List<Map<String,Object>> getUsersTag(List<String> ids,String type) throws Exception{
        if(type.isEmpty()){
            return null;
        }
        List<String> stringList = new ArrayList<>();
        Wrapper<UserProfile> userProfileWrapper = new EntityWrapper<>();
        userProfileWrapper.setSqlSelect("user_id",type).in("user_id",ids);
        return this.selectMaps(userProfileWrapper);
    }


    public List<String> getUserTag(List<String> ids,String type){
        if(type.isEmpty()){
            return null;
        }
        List<String> stringList = new ArrayList<>();
        Wrapper<UserProfile> userProfileWrapper = new EntityWrapper<>();
        userProfileWrapper.setSqlSelect("user_id",type).in("user_id",ids);
        List<Map<String,Object>> maps=this.selectMaps(userProfileWrapper);
        for(String id:ids){
            String tag = null;
            for(Map<String,Object> temp:maps){
                if(!temp.isEmpty() && temp.get(type)!=null && temp.get("userId").equals(id)){
                    tag=temp.get(type).toString();
                    break;
                }
            }
            stringList.add(tag);
        }
        return stringList;
    }

    private Wrapper<UserProfile> createWrapperByUserId(String userId){
        Wrapper<UserProfile> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id",userId);
        return wrapper;
    }

    
    public UserProfile getUserProfileByUserId(String userId){
        return this.selectOne(createWrapperByUserId(userId));
    }

    
    public void delUserProfile(String userId) throws Exception{
        this.delete(createWrapperByUserId(userId));//正常删除
    }

    public List<String> selectUserProfileByUser(List<String> ids,SelectUserListByDetailRequestDto request) throws Exception{
        Wrapper<UserProfile> wrapper =selectUserProfileByUserWrapper(ids, request);
        return (List<String>)(List)selectObjs(wrapper);
    }

    public List<UserProfile> selectUserProfileList(List<String> ids,SelectUserListByDetailRequestDto request) throws Exception{
        Wrapper<UserProfile> wrapper =selectUserProfileByUserWrapper(ids, request);
        Page page = new Page(request.getPage().getCurrentPage(), request.getPage().getSize());
        return selectPage(page, wrapper).getRecords();
    }

    public Wrapper<UserProfile> selectUserProfileByUserWrapper(List<String> ids,SelectUserListByDetailRequestDto request) throws Exception{
        Wrapper wrapper = new EntityWrapper();
        wrapper.in("user_id", ids);
        wrapper.setSqlSelect("user_id")
                .like("emotion", request.getEmotion())
                .like("nation", request.getNation())
                .like("animal_signs", request.getAnimalSigns())
                .like("star_sign", request.getStarSign());
        wrapper.andNew("deleted = 0");
        if (request.getMinHeight() != 0 || request.getMaxHeight() != 0) {
            if (request.getMaxHeight() == 0) {
                request.setMaxHeight(Integer.MAX_VALUE);
            }
            wrapper.between("height", request.getMinHeight(), request.getMaxHeight());
        }
        if (request.getMinWeight() != 0 || request.getMaxWeight() != 0) {
            if (request.getMaxWeight() == 0) {
                request.setMaxWeight(Integer.MAX_VALUE);
            }
            wrapper.between("weight", request.getMinWeight(), request.getMaxWeight());
        }
        if (request.getMinIncome() != 0 || request.getMaxIncome() != 0) {
            if (request.getMaxIncome() == 0) {
                request.setMaxIncome(Integer.MAX_VALUE);
            }
            wrapper.andNew("(income between {0} and {1}) or (max_income between {0} and {1})",request.getMinIncome(), request.getMaxIncome());
        }
        return wrapper;
        /*//对用户详情实体进行分页查询
        Page page = new Page(request.getPage().getCurrentPage(), request.getPage().getSize());
        return selectPage(page, wrapper).getRecords();*/
    }
}
