package com.netx.ucenter.biz.user;

import com.netx.common.common.enums.MessageTypeEnum;
import com.netx.common.common.vo.message.JpushDto;
import com.netx.common.user.dto.wangMing.AddScoreRecordRequestDto;
import com.netx.common.user.enums.StatScoreEnum;
import com.netx.common.user.model.UserSynopsisData;
import com.netx.common.user.util.VoPoConverter;
import com.netx.ucenter.biz.common.MessagePushAction;
import com.netx.ucenter.biz.friend.FriendsAction;
import com.netx.ucenter.biz.router.ScoreAction;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.model.user.UserWatch;
import com.netx.ucenter.service.friend.FriendServiceProvider;
import com.netx.ucenter.service.user.UserPhotoService;
import com.netx.ucenter.service.user.UserService;
import com.netx.ucenter.service.user.UserWatchService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户关注表 服务实现类
 * </p>
 *
 * @author 逍遥游
 * @since 2017-08-27
 */
@Service
public class UserWatchAction{

    @Autowired
    private UserAction userAction;
    @Autowired
    private UserService userService;
    @Autowired
    private UserPhotoService userPhotoService;
    @Autowired
    private UserScoreAction userScoreAction;
    @Autowired
    private FriendsAction friendsAction;
    @Autowired
    private MessagePushAction messagePushAction;
    @Autowired
    private UserWatchService userWatchService;
    @Autowired
    private FriendServiceProvider friendServiceProvider;
    @Autowired
    private ScoreAction scoreAction;

    private Logger logger = LoggerFactory.getLogger(UserWatchAction.class);

    //======================== 黎子安 start ========================
    
    public void deleteWatch(String userId) throws Exception{
        userWatchService.deleteWatch(userId);
        friendsAction.delFriends(userId);
    }
    
    @Transactional(readOnly = true)
    public List<UserSynopsisData> selectUserByType(String userId,String options,Double lon,Double lat) throws Exception {
        List<String> userIdList= userWatchService.selectUserIdsByType(userId,options);
        return userIdList.size()==0?null:userAction.getUserSynopsisDataList(userIdList,lon,lat,userId);
    }

    @Transactional(readOnly = false)
    public String addUserWatch(String userId,String toUserId) throws Exception{
        User user = userService.selectById(userId);
        if(user == null) return "你是非法用户";
        User toUser = userService.selectById(toUserId);
        if(toUser==null) return "你关注的这个用户不存在！";
        //获取关注信息
        UserWatch userWatch = checkWatch(userId,toUserId);
        if(userWatch!=null){
            //if(userWatch.getWachType()==1)return 2;
            userWatch.setWachType(1);
            userWatch.setWatch(userWatch.getWatch()?false:true);
            if(userWatchService.updateById(userWatch)) {
                buildFriend(user,toUser,userWatch.getWatch());
                updateWatchNum(user,toUser,false);
                return userWatch.getWatch()?"关注成功":"取消关注成功";
            }
        }else{
            if(addWatch(userId,toUserId)){
                updateWatchNum(user,toUser,true);
                buildFriend(user,toUser,true);
                return "关注成功";
            }
        }
        return "失败";
    }

    private void buildFriend(User user,User toUser,Boolean type) throws Exception{
        if(type){
            UserWatch userWatch = checkWatch(toUser.getId(),user.getId());
            if(userWatch!=null && userWatch.getWatch()){
                //建立好友关系
                Integer count = 0;
                if(buildWatchFriend(user.getId(),toUser,toUser.getNickname())){
                    count++;
                }
                if(buildWatchFriend(toUser.getId(),user,user.getNickname())){
                    count++;
                }
                if(count>1){
                    messagePushAction.addFriend(user.getId(), toUser.getId());
                }
            }
        }
    }

    private Boolean buildWatchFriend(String userId,User toUser,String toNickname){
        if(!friendServiceProvider.getFriendsService().checkFriendOne(userId, toUser.getId())){
            if(friendsAction.addFriends(userId, toUser.getId())){
                sendFriendMessage(userId, toNickname,toUser.getId());
                if(StringUtils.isNotBlank(toUser.getRealName()) && StringUtils.isNotBlank(toUser.getMobile()) && StringUtils.isNotBlank(toUser.getIdNumber())){
                    scoreAction.addScore(userId, StatScoreEnum.SS_VERIFY_FRIEND);
                }
                return true;
            }
        }
        return false;
    }

    private void sendFriendMessage(String userId,String nikeName,String fromUserId){
        JpushDto jpushDto = new JpushDto();
        jpushDto.setTitle("好友消息");
        jpushDto.setUserId(userId);
        jpushDto.setFromUserId(fromUserId);
        jpushDto.setType(MessageTypeEnum.USER_TYPE);
        jpushDto.setAlertMsg("由于你与"+nikeName + "相互关注，促使你们成为好友！");
        try {
            messagePushAction.sendMessageAlias(jpushDto);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
    }

    
    @Transactional(readOnly = true)
    public UserWatch checkWatch(String fromUserId,String toUserId){
        return userWatchService.getUserWatchByUserId(fromUserId, toUserId);
    }

    /**
     *
     * @param fromUserId 关注者
     * @param toUserId 被关注者
     * @return
     */
    public Boolean getIsWatch(String fromUserId,String toUserId){
        if(fromUserId==null){
            return null;
        }
        UserWatch userWatch = checkWatch(fromUserId, toUserId);
        return userWatch!=null && userWatch.getWatch();
    }

    public void addWatchToUser(UserSynopsisData userSynopsisData,String userId){
        userSynopsisData.setWatch(getIsWatch(userId,userSynopsisData.getId()));
    }

    private void updateWatchNum(User user,User toUser,boolean type) throws Exception{
        updateWatch(user,true,user.getCurrentWatchFrom()+(type?1:-1),type);
        updateWatch(toUser,false,toUser.getCurrentWatchTo()+(type?1:-1),type);
    }

    private void updateWatch(User user,boolean type,int count,boolean isAdd) throws Exception{
        if(type){
            user.setCurrentWatchFrom(count);
        }else{
            user.setCurrentWatchTo(count);
        }
        userService.updateById(user);
        if(count<=50){
            if(isAdd && count%5==0){
                addScore(user.getId(),user.getLockVersion(),type?10:9);
            }
        }
    }

    private void addScore(String userId,int lockVersion,int code) throws Exception{
        AddScoreRecordRequestDto addScoreRecordRequestDto = new AddScoreRecordRequestDto();
        addScoreRecordRequestDto.setUserId(userId);
        addScoreRecordRequestDto.setCode(code);
        addScoreRecordRequestDto.setRelatableId(userId);
        addScoreRecordRequestDto.setRelatableType("UserWatch");
        userScoreAction.addScoreRecord(addScoreRecordRequestDto);
    }

    /**
     * 往数据库添加一条关注信息
     * @param userId
     * @param toUserId
     * @return
     */
    public boolean addWatch(String userId,String toUserId){
        long time=System.currentTimeMillis();
        UserWatch userWatch=new UserWatch();
        userWatch.setFromUserId(userId);
        userWatch.setToUserId(toUserId);
        userWatch.setCreateUserId(userId);
        userWatch.setRelatableId(userId);
        userWatch.setWatchAt(new Date());
        userWatch.setWachType(1);
        userWatch.setWatch(true);
        return userWatchService.insert(userWatch);
    }

    
    @Transactional(readOnly = false)
    public List<UserSynopsisData> selectNoWatchUserByUserId(String userId) throws Exception{
        String ids=userWatchService.getNoWatchUserId(userId);
        if(ids==null || ids.isEmpty()){
            return null;
        }
        List<User> userList=userService.getUsersByNoIds(ids);
        if (userList.isEmpty())return null;
        List<UserSynopsisData> selectUserResponses= VoPoConverter.copyList(userList,UserSynopsisData.class);
        UserSynopsisData synopsisData;
        for(int i=0;i<selectUserResponses.size();i++){
            synopsisData=selectUserResponses.get(i);
            selectUserResponses.get(i).setHeadImgUrl(userPhotoService.selectHeadImg(synopsisData.getId()));
        }
        return selectUserResponses;
    }
}
