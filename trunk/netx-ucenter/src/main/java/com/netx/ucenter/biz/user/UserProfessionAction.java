package com.netx.ucenter.biz.user;


import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.user.dto.integration.EditUserLabelRequest;
import com.netx.common.user.dto.integration.InsertUserProfessionDetailRequest;
import com.netx.common.user.dto.integration.SelectUserProfessionDetailResponse;
import com.netx.common.user.dto.integration.SelectUserProfessionResponse;
import com.netx.common.user.util.VoPoConverter;
import com.netx.common.vo.business.EditUserWorkExperienceRequestDto;
import com.netx.ucenter.aop.CalculateProfileScore;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.model.user.UserProfession;
import com.netx.ucenter.service.user.UserProfessionService;
import com.netx.ucenter.service.user.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class UserProfessionAction{

    private Logger logger = LoggerFactory.getLogger(UserProfessionAction.class);

    @Autowired
    private UserService userService;
    @Autowired
    private UserProfileAction userProfileAction;
    @Autowired
    UserProfessionService professionService;

    public UserProfessionService getProfessionService() {
        return professionService;
    }

    public void deleteByUserId(String userId) throws Exception{
        professionService.deleteByUserId(userId);
    }

    public boolean editUserProfessionLabel(EditUserLabelRequest request) throws Exception {
        User user = userService.selectById(request.getUserId());
        if(user == null){
            throw new RuntimeException("用户不存在："+request.getUserId());
        }
        user.setUpdateUserId(request.getUserId());
        user.setProfessionLabel(request.getLabel());
        return userProfileAction.updateUserProfileScore(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean addOrEditUserEducationDetail(InsertUserProfessionDetailRequest request){
        if(StringUtils.isBlank(request.getId())){
            return add(request);
        }
        UserProfession userProfession = professionService.selectById(request.getId());
        if(userProfession==null){
            return add(request);
        }else{
            userProfession = create(userProfession,request);
            userProfession.setUpdateUserId(request.getUserId());
            return professionService.updateById(userProfession);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean add(InsertUserProfessionDetailRequest request){
        UserProfession userProfession = create(new UserProfession(),request);
        Integer maxPosition = professionService.getMaxPositionByUserId(request.getUserId());
        maxPosition = maxPosition != null ? maxPosition : 0;
        userProfession.setPosition(maxPosition + 1);
        if(professionService.insert(userProfession)){
            userProfileAction.updateUserProfileScore(request.getUserId());
            return true;
        }
        return false;
    }

    private UserProfession create(UserProfession userProfession,InsertUserProfessionDetailRequest request){
        userProfession.setCreateUserId(request.getUserId());
        userProfession.setUserId(request.getUserId());
        userProfession.setCompany(request.getCompany());
        userProfession.setDepartment(request.getDepartment());
        userProfession.setTopProfession(request.getTopProfession());
        userProfession.setYear(request.getYear());
        return userProfession;
    }

    public SelectUserProfessionResponse selectUserProfession(String userId, Page page){
        //查询用户
        User user = userService.selectById(userId);
        if(user == null){
            throw new RuntimeException("用户不存在："+userId);
        }
        SelectUserProfessionResponse response = new SelectUserProfessionResponse();
        response.setProfessionLabel(user.getProfessionLabel());
        //查询工作经历的详情
        List<UserProfession> detailPoList = professionService.selectUserProfessionListByUserId(userId,page);
        List<SelectUserProfessionDetailResponse> detailVoList = VoPoConverter.copyList(detailPoList, SelectUserProfessionDetailResponse.class);
        response.setList(detailVoList);
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUserProfessionDetail(String id) throws Exception{
        //查询出需要删除的对象，进行删除操作
        UserProfession deleteProfession = professionService.selectById(id);
        if(deleteProfession != null) {
            Integer startPosition = deleteProfession.getPosition();//获取位置
            String userId = deleteProfession.getUserId();//获取用户id
            Boolean flag = professionService.deleteById(id);
            if (flag) {
                //进行重排操作
                List<UserProfession> list = professionService.getUserProfessionListByUserId(userId, startPosition);
                if (!list.isEmpty()) {//如果列表为空，则不做重排操作
                    for (int i = 0; i < list.size(); i++) {
                        UserProfession userProfession = list.get(i);
                        userProfession.setPosition(userProfession.getPosition() - 1);
                    }
                    if (professionService.updateBatchById(list)) {
                        logger.warn(userId+"的工作经历重排失败");
                    }
                }
                userProfileAction.updateUserProfileScore(userId);//资料计分
            }
            return flag;
        }
        return false;
    }

    public Boolean EditUserWorkExperience(EditUserWorkExperienceRequestDto request){
        UserProfession userProfession=new UserProfession();
        VoPoConverter.copyProperties(request,userProfession);
        return professionService.updateById(userProfession);
    }
}
