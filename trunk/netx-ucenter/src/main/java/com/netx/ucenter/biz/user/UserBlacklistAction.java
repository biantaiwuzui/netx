package com.netx.ucenter.biz.user;

import com.netx.common.redis.model.UserGeo;
import com.netx.common.redis.service.GeoService;
import com.netx.ucenter.model.user.UserBlacklist;
import com.netx.ucenter.service.friend.FriendsService;
import com.netx.ucenter.service.user.UserBlacklistService;
import com.netx.ucenter.service.user.UserService;
import com.netx.ucenter.vo.response.SelectUserCommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户拉黑表（拉黑后对本人是黑名单，不影响其他人） 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-27
 */
@Service
public class UserBlacklistAction{
    @Autowired
    FriendsService friendService;
    @Autowired
    UserService userService;
    @Autowired
    GeoService geoService;
    @Autowired
    UserAction userAction;
    @Autowired
    UserBlacklistService userBlacklistService;

    private Logger logger = LoggerFactory.getLogger(UserBlacklistAction.class);

    public boolean addUserBlackList(String userId,String targetId) throws Exception {
        UserBlacklist userBlacklist=new UserBlacklist();
        userBlacklist.setUserId(userId);
        userBlacklist.setTargetId(targetId);
        userBlacklist.setUserId(userId);
        return userBlacklistService.insert(userBlacklist);
    }

    
    @Transactional(readOnly = false)
    public String addUserBlack(String userId,String targetId) throws Exception{
        if(userService.selectById(userId)==null){
            return "拉黑者不存在";
        }
        if(userService.selectById(targetId)==null){
            return "被拉黑者不存在";
        }
        if(checkBlack(userId, targetId)){
            return "你已经拉黑此用户";
        }
        Boolean flag = addUserBlackList(userId,targetId);
        if(flag){
            friendService.delFriends(userId,targetId);
        }
        return flag?null:"拉黑失败";
    }

    public Boolean checkBlack(String userId,String targetId){
        return userBlacklistService.checkBlacklistByUserId(userId, targetId)>0;
    }

    
    @Transactional(readOnly = false)
    public boolean deleteUserBlack(String id) throws Exception{
        return userBlacklistService.deleteById(id);
    }

    
    @Transactional(readOnly = true)
    public List<SelectUserCommonResponse> selectUserBlackListByUserId(String userId){
        List<SelectUserCommonResponse> selectUserBlackResponses=new ArrayList<>();
        List<UserBlacklist> userBlacklists=userBlacklistService.selectUserBlacklistByUserId(userId);
        UserGeo userGeo = geoService.getUserGeo(userId);
        for (UserBlacklist userBlacklist:userBlacklists){
            selectUserBlackResponses.add(createSelectUserBlackResponse(userBlacklist,userGeo.getLon(),userGeo.getLat(),userId));
        }
        return selectUserBlackResponses;
    }

    private SelectUserCommonResponse createSelectUserBlackResponse(UserBlacklist userBlackList,Double lon ,Double lat,String userId){
        SelectUserCommonResponse selectUserBlackResponse=new SelectUserCommonResponse();
        selectUserBlackResponse.setId(userBlackList.getId());
        try {
            selectUserBlackResponse.setSynopsisData(userAction.getUserSynopsisData(userBlackList.getTargetId(),lon,lat,userId));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return selectUserBlackResponse;
    }

    public void deleteByUserId(String userId) throws Exception{
        userBlacklistService.deleteByUserId(userId);
    }
}
