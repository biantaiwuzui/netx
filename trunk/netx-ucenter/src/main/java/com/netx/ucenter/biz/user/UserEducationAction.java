package com.netx.ucenter.biz.user;

import com.netx.common.user.dto.integration.EditUserLabelRequest;
import com.netx.common.user.dto.integration.InsertUserEducationDetailRequest;
import com.netx.common.user.dto.integration.SelectUserEducationDetailResponse;
import com.netx.common.user.dto.integration.SelectUserEducationResponse;
import com.netx.common.user.util.VoPoConverter;
import com.netx.common.vo.business.UpdateUserEducationRequestDto;
import com.netx.ucenter.aop.CalculateProfileScore;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.model.user.UserEducation;
import com.netx.ucenter.service.user.UserEducationService;
import com.netx.ucenter.service.user.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
public class UserEducationAction{

    @Autowired
    UserEducationService userEducationService;
    @Autowired
    UserAction userAction;
    @Autowired
    UserProfileAction userProfileAction;

    public UserEducationService getUserEducationService() {
        return userEducationService;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean editUserEducationLabel(EditUserLabelRequest request){
        User user = userAction.getUserService().selectById(request.getUserId());
        user.setUpdateUserId(request.getUserId());
        user.setEducationLabel(request.getLabel());
        return userProfileAction.updateUserProfileScore(user);
    }

    private UserEducation create(UserEducation userEducation,InsertUserEducationDetailRequest request){
        userEducation.setCreateUserId(request.getUserId());
        userEducation.setUserId(request.getUserId());
        userEducation.setSchool(request.getSchool());
        userEducation.setDepartment(request.getDepartment());
        userEducation.setSpeciality(request.getSpeciality());
        userEducation.setYear(request.getYear());
        userEducation.setTime(request.getTime());
        userEducation.setDegree(request.getDegree());
        return userEducation;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean addOrEditUserEducationDetail(InsertUserEducationDetailRequest request){
        if(StringUtils.isBlank(request.getId())){
            return add(request);
        }
        UserEducation userEducation = userEducationService.selectById(request.getId());
        if(userEducation==null){
            return add(request);
        }else{
            userEducation = create(userEducation,request);
            userEducation.setUpdateUserId(request.getUserId());
            return userEducationService.updateById(userEducation);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean add(InsertUserEducationDetailRequest request){
        UserEducation userEducation = create(new UserEducation(),request);
        Integer maxPosition = userEducationService.getMaxPositionByUserId(request.getUserId());
        maxPosition = maxPosition != null ? maxPosition : 0;
        userEducation.setPosition(maxPosition + 1);
        if(userEducationService.insert(userEducation)){
            userProfileAction.updateUserProfileScore(request.getUserId());
            return true;
        }
        return false;
    }

    public SelectUserEducationResponse selectUserEducation(String userId){
        //查询用户是否存在
        User user = userAction.getUserService().selectById(userId);
        if(user==null){
            throw new RuntimeException("此用户不存在："+userId);
        }
        SelectUserEducationResponse response = new SelectUserEducationResponse();
        response.setEducationLabel(user.getEducationLabel());
        //查询文化教育详情
        List<UserEducation> detailPoList = userEducationService.selectUserEducationListByUserId(userId);
        List<SelectUserEducationDetailResponse> detailVoList = VoPoConverter.copyList(detailPoList, SelectUserEducationDetailResponse.class);
        response.setList(detailVoList);
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUserEducationDetail(String id){
        //查询出需要删除的对象，进行删除操作
        UserEducation deleteEducation = userEducationService.selectById(id);
        if(deleteEducation != null){//如果存在这个删除对象，则进行删除操作和重排操作
            Boolean flag = userEducationService.deleteById(id);
            if(flag){
                //进行重排操作
                Integer startPosition = deleteEducation.getPosition();
                String userId = deleteEducation.getUserId();
                List<UserEducation> list = userEducationService.getUserEducationListByUserId(userId,startPosition);
                if(!list.isEmpty()) {//如果列表为空，则不做重排操作
                    for (int i = 0; i < list.size(); i++) {
                        UserEducation userEducation = list.get(i);
                        userEducation.setPosition(userEducation.getPosition() - 1);
                    }
                    if (!userEducationService.updateBatchById(list)) {
                        throw new RuntimeException("删除异常");
                    }
                }
                userProfileAction.updateUserProfileScore(userId);//资料计分
            }
            return flag;
        }
        return false;
    }

    public void deleteByUserId(String userId) throws Exception{
        userEducationService.deleteByUserId(userId);
    }

    public Boolean editUserEducationInfo(UpdateUserEducationRequestDto request){
        UserEducation userEducation=new UserEducation();
        VoPoConverter.copyProperties(request,userEducation);
        return userEducationService.updateById(userEducation);
    }

    /**
     * 删除用户背景:状态改为deleted=1,
     * @param id
     * @param updateUserId
     * @return
     */
    public Boolean deleteUserEducation(String id,String updateUserId){
        UserEducation userEducation=new UserEducation();
        userEducation.setId(id);
        userEducation.setUpdateUserId(updateUserId);
        userEducation.setDeleted(1);
        return userEducationService.updateById(userEducation);
    }
}
