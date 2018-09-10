package com.netx.ucenter.biz.user;

import com.netx.common.user.dto.integration.EditUserLabelRequest;
import com.netx.common.user.dto.integration.InsertUserInterestDetailRequest;
import com.netx.common.user.dto.integration.SelectUserInterestDetailResponse;
import com.netx.common.user.dto.integration.SelectUserInterestResponse;
import com.netx.common.user.util.VoPoConverter;
import com.netx.ucenter.aop.CalculateProfileScore;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.model.user.UserInterest;
import com.netx.ucenter.service.user.UserInterestService;
import com.netx.ucenter.service.user.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
public class UserInterestAction{
    @Autowired
    private UserService userService;
    @Autowired
    private UserProfileAction userProfileAction;
    @Autowired
    UserInterestService userInterestService;

    public UserInterestService getUserInterestService() {
        return userInterestService;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean editUserInterestLabel(EditUserLabelRequest request){
        User user = userService.selectById(request.getUserId());
        user.setUpdateUserId(request.getUserId());
        user.setInterestLabel(request.getLabel());
        return userProfileAction.updateUserProfileScore(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean addOrEditUserEducationDetail(InsertUserInterestDetailRequest request){
        if(StringUtils.isBlank(request.getId())){
            return add(request);
        }
        UserInterest userInterest = userInterestService.selectById(request.getId());
        if(userInterest==null){
            return add(request);
        }else{
            userInterest = create(userInterest,request);
            userInterest.setUpdateUserId(request.getUserId());
            return userInterestService.updateById(userInterest);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean add(InsertUserInterestDetailRequest request){
        UserInterest userInterest = create(new UserInterest(),request);
        Integer maxPosition = userInterestService.getMaxPositionByUserId(request.getUserId());
        maxPosition = maxPosition != null ? maxPosition : 0;
        userInterest.setPosition(maxPosition + 1);
        if(userInterestService.insert(userInterest)){
            userProfileAction.updateUserProfileScore(request.getUserId());
            return true;
        }
        return false;
    }

    private UserInterest create(UserInterest userInterest,InsertUserInterestDetailRequest request){
        userInterest.setCreateUserId(request.getUserId());
        userInterest.setUserId(request.getUserId());
        userInterest.setInterestType(request.getInterestType());
        userInterest.setInterestDetail(request.getInterestDetail());
        return userInterest;
    }

    public SelectUserInterestResponse selectUserInterest(String userId){
        User user = userService.selectById(userId);
        if(user==null){
            throw new RuntimeException("此用户不存在："+userId);
        }
        SelectUserInterestResponse response = new SelectUserInterestResponse();
        response.setInterestLabel(user.getInterestLabel());
        //查询兴趣爱好的详情
        List<UserInterest> detailPoList = userInterestService.selectUserInterestListByUserId(userId);
        List<SelectUserInterestDetailResponse> list = new ArrayList<>();
        if(detailPoList.size()>0){
            detailPoList.forEach(userInterest -> {
                list.add(create(userInterest));
            });
        }
        response.setList(list);
        return response;
    }

    private SelectUserInterestDetailResponse create(UserInterest userInterest){
        SelectUserInterestDetailResponse response = new SelectUserInterestDetailResponse();
        response.setId(userInterest.getId());
        response.setInterestDetail(userInterest.getInterestDetail());
        response.setInterestType(userInterest.getInterestType());
        response.setPosition(userInterest.getPosition());
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUserInterestDetail(String id,String userId){
        //查询出需要删除的对象，进行删除操作
        UserInterest deleteInterest = userInterestService.selectById(id);
        if(deleteInterest != null && deleteInterest.getUserId()!=null && deleteInterest.getUserId().equals(userId)) {
            Integer startPosition = deleteInterest.getPosition();//获取位置
            Boolean flag = userInterestService.deleteById(id);
            if(flag){
                //进行重排操作
                List<UserInterest> list = userInterestService.getUserInterestListByUserId(userId, startPosition);
                if (!list.isEmpty()) {//如果列表为空，则不做重排操作
                    for (int i = 0; i < list.size(); i++) {
                        UserInterest userInterest = list.get(i);
                        userInterest.setPosition(userInterest.getPosition() - 1);
                    }
                    if (!userInterestService.updateBatchById(list)) {
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
        userInterestService.deleteByUserId(userId);
    }

}
