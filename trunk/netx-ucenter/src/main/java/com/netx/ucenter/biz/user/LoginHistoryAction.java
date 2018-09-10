package com.netx.ucenter.biz.user;

import com.netx.common.redis.model.UserGeo;
import com.netx.common.redis.model.UserGeoRadius;
import com.netx.common.user.util.ComputeAgeUtils;
import com.netx.common.user.util.VoPoConverter;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.model.user.UserLoginHistory;
import com.netx.ucenter.service.user.LoginHistoryService;
import com.netx.ucenter.service.user.UserPhotoService;
import com.netx.ucenter.vo.response.SelectNearlyUserLoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 登录记录表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-27
 */
@Service
public class LoginHistoryAction{

    private Logger logger = LoggerFactory.getLogger(LoginHistoryAction.class);

    @Autowired
    private LoginHistoryService loginHistoryService;
    @Autowired
    private UserAction userAction;
    @Autowired
    private UserPhotoService userPhotoService;

    private Integer getAge(Date birthday){
        return ComputeAgeUtils.getAgeByBirthday(birthday);
    }

    public List<UserLoginHistory> selectUserNewLoginList(Map<String,UserGeoRadius> userGeos) throws Exception {
        if(userGeos.isEmpty()){
            return new ArrayList<>();
        }
        return loginHistoryService.getUserLoginHistoryMapper().selectUserNewLoginList(userGeos.keySet());
    }

    private List<String> getIds(List<UserGeo> userGeos){
        if(userGeos==null || userGeos.size()==0){
            return null;
        }
        List<String> ids = new ArrayList<>();
        for (UserGeo userGeo:userGeos) {
            ids.add(userGeo.getUserId());
        }
        return ids;
    }

    public List<SelectNearlyUserLoginResponse> selectNearlyUserLoginList(List<UserGeoRadius> userGeos) throws Exception{
        List<SelectNearlyUserLoginResponse> result = new ArrayList<>();
        if(userGeos!=null && !userGeos.isEmpty()){
            userGeos.forEach(userGeoRadius -> {
                UserLoginHistory history = loginHistoryService.getUserLoginHistoryMapper().selectUserNewLogin(userGeoRadius.getUserId());
                if(history!=null){
                    result.add(getNearlyUserLoginList(history,userGeoRadius));
                }
            });
        }
        return result;
    }

    public List<SelectNearlyUserLoginResponse> selectNearlyUserLoginPage(List<UserGeoRadius> userGeos, int current,int size) throws Exception{
        List<SelectNearlyUserLoginResponse> result = new ArrayList<>();
        int end = current*size;
        int start = (current-1)*size;
        int i = 1;
        if(userGeos!=null && !userGeos.isEmpty()){
            for(UserGeoRadius userGeoRadius:userGeos){
                UserLoginHistory history = loginHistoryService.getUserLoginHistoryMapper().selectUserNewLogin(userGeoRadius.getUserId());
                if(history!=null){
                    i++;
                    if(i>=start && i<end){
                        result.add(getNearlyUserLoginList(history,userGeoRadius));
                    }
                    if(result.size()==size){
                        break;
                    }
                }
            }
        }
        return result;
    }

    private SelectNearlyUserLoginResponse getNearlyUserLoginList(UserLoginHistory history,UserGeoRadius userGeoRadius){
        SelectNearlyUserLoginResponse response = VoPoConverter.copyProperties(history,SelectNearlyUserLoginResponse.class);
        updateSelectNearlyUserLoginResponse(response);
        response.setDistance(userGeoRadius.getRadius());
        return response;
    }

    private void updateSelectNearlyUserLoginResponse(SelectNearlyUserLoginResponse selectNearlyUserLoginResponse){
        User user = userAction.getUserService().selectById(selectNearlyUserLoginResponse.getUserId());
        if(user!=null){
            selectNearlyUserLoginResponse.setAge(getAge(user.getBirthday()));
            selectNearlyUserLoginResponse.setLv(user.getLv());
            selectNearlyUserLoginResponse.setSex(user.getSex());
            selectNearlyUserLoginResponse.setNickName(user.getNickname());
            try {
                selectNearlyUserLoginResponse.setHeadImg(userPhotoService.selectHeadImg(user.getId()));
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }
        }
    }

    public List<String> getUserId(Collection<?> ids) throws Exception{
        return loginHistoryService.getUserId(ids);
    }

    public void deleteByUserId(String userId) throws Exception{
        loginHistoryService.deleteByUserId(userId);
    }
}
