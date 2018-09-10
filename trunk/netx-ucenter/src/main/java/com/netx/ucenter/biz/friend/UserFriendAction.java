package com.netx.ucenter.biz.friend;

import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.common.enums.MessageTypeEnum;
import com.netx.common.common.enums.PushMessageDocTypeEnum;
import com.netx.common.user.enums.StatScoreEnum;
import com.netx.common.user.model.UserSynopsisData;
import com.netx.common.vo.common.DisposeAddFriendMessageRequestDto;
import com.netx.common.vo.common.WzCommonAddFriendAddRequestDto;
import com.netx.ucenter.biz.common.MessagePushAction;
import com.netx.ucenter.biz.common.WzCommonImHistoryAction;
import com.netx.ucenter.biz.router.ScoreAction;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.ucenter.biz.user.UserWatchAction;
import com.netx.ucenter.model.friend.CommonAddFriend;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.service.friend.UserFriendService;
import com.netx.ucenter.service.friend.FriendsService;
import com.netx.ucenter.service.user.UserBlacklistService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by wongloong on 17-8-26
 */
@Service
public class UserFriendAction{
    private Logger logger = LoggerFactory.getLogger(UserFriendAction.class);
    @Autowired
    private UserFriendService userFriendService;
    @Autowired
    private MessagePushAction messagePushAction;
    @Autowired
    private FriendsService friendsService;
    @Autowired
    private UserAction userAction;
    @Autowired
    private UserBlacklistService userBlacklistService;
    @Autowired
    private WzCommonImHistoryAction wzCommonImHistoryAction;
    @Autowired
    private FriendsAction friendsAction;
    @Autowired
    private ScoreAction scoreAction;
    @Autowired
    private UserWatchAction userWatchAction;

    public Boolean isSendFriendAdd(String fromUserId, String toUserId){
        return userFriendService.getWzCommonAddFriend(fromUserId, toUserId) != null;
    }

    public String saveAddFriendMessage(String fromUserId,String userId) throws Exception{
        User fromUser = userAction.getUserService().selectById(fromUserId);
        if(fromUser == null){
            return "你不是此平台用户，请重新登录";
        }
        User user = userAction.getUserService().selectById(userId);
        if(user == null){
            return "你所添加的用户不存在";
        }
        //判断是否已是好友
        if(friendsService.checkFriendOne(fromUserId,userId)){
            return "发送请求失败,你已添加此好友";
        }
        //判断是否已经发送了请求
        if (isSendFriendAdd(fromUserId,userId)) {
            return "你已发送好友请求，无需重复请求";
        }
        if(userFriendService.insert(twoUserToWzCommonAddFriend(fromUser,user))){
            wzCommonImHistoryAction.add(fromUserId,userId,"请求添加您为好友",fromUserId,MessageTypeEnum.USER_TYPE, PushMessageDocTypeEnum.ADD_FRIEND,null);
            return null;
        }
        return "发送好友请求失败";
    }

    private CommonAddFriend twoUserToWzCommonAddFriend(User fromUser,User toUser){
        CommonAddFriend wzCommonAddFriend = new CommonAddFriend();
        wzCommonAddFriend.setUserId(fromUser.getId());
        wzCommonAddFriend.setToUserId(toUser.getId());
        wzCommonAddFriend.setSendTime(new Date());
        wzCommonAddFriend.setCreateTime(new Date());
        wzCommonAddFriend.setDeleted(0);
        wzCommonAddFriend.setHasRead(0);
        wzCommonAddFriend.setDisposeState(0);
        return wzCommonAddFriend;
    }

    private CommonAddFriend requestDtoToWzCommonAddFriend(WzCommonAddFriendAddRequestDto request){
        CommonAddFriend wzCommonAddFriend = new CommonAddFriend();
        wzCommonAddFriend.setUserId(request.getFromUserId());
        wzCommonAddFriend.setToUserId(request.getToUserId());
        wzCommonAddFriend.setSendTime(new Date());
        wzCommonAddFriend.setCreateTime(new Date());
        wzCommonAddFriend.setDeleted(0);
        wzCommonAddFriend.setHasRead(0);
        wzCommonAddFriend.setDisposeState(0);
        return wzCommonAddFriend;
    }

    public Page<CommonAddFriend> selectPage(String userId,Integer current,Integer size) throws Exception {
        Page<CommonAddFriend> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);
        return userFriendService.selectPageByUserId(userId,0,page);
    }

    public Map<String, Object> selectPageMap(String userId,Integer current,Integer size) throws Exception{
        Map<String,Object> objectMap = new HashMap<>();
        Page<CommonAddFriend> result = selectPage(userId, current, size);
        List<CommonAddFriend> list = result.getRecords();
        objectMap.put("list",list);
        Map<String,UserSynopsisData> map = new HashMap<>();
        User user = userAction.getUserService().selectById(userId);
        for(CommonAddFriend friend: list){
            try {
                addMap(friend.getUserId(),map,user.getLon(),user.getLat(),user.getId());
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }
        }
        objectMap.put("userData",map);
        return objectMap;
    }

    public Map<String, Object> selectFriendPageMap(String userId,Integer current,Integer size) throws Exception{
        Map<String,Object> objectMap = new HashMap<>();
        Page<CommonAddFriend> page = new Page<>(current,size);
        List<CommonAddFriend> list = userFriendService.selectPageByUserId(userId,1,page).getRecords();
        Map<String,UserSynopsisData> map = new HashMap<>();
        User user = userAction.getUserService().selectById(userId);
        for(CommonAddFriend friend: list){
            try {
                addMap(friend.getUserId(),map,user.getLon(),user.getLat(),user.getId());
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }
        }
        objectMap.put("list",list);
        objectMap.put("userData",map);
        return objectMap;
    }

    private void addMap(String userId, Map<String,UserSynopsisData> map, BigDecimal lon, BigDecimal lat,String watchUserId) throws Exception{
        UserSynopsisData userSynopsisData = userAction.getUserSynopsisData(userId,lon.doubleValue(),lat.doubleValue(),watchUserId);
        if(userSynopsisData!=null){
            map.put(userId,userSynopsisData);
        }
    }

    public int getReadCount(String userId,Boolean isRead){
        return userFriendService.countRead(userId,isRead);
    }

    public boolean getById(String userId){
        CommonAddFriend wzCommonAddFriend = new CommonAddFriend();
        wzCommonAddFriend.setHasRead(1);
        return userFriendService.updateByToUserId(userId,wzCommonAddFriend);
    }

    @Transactional(rollbackFor = Exception.class)
    public String disposeMessage(DisposeAddFriendMessageRequestDto request,String userId) throws Exception {
        //根据添加好友记录ID查到记录并映射到添加好友modle
        CommonAddFriend wzCommonAddFriend = userFriendService.selectById(request.getId());
        //记录消失的情况
        if(null==wzCommonAddFriend){
            return "该添加好友请求不存在";
        }
        //获取当前登录用户的信息（根据token里的id）
        User user = userAction.getUserService().selectById(userId);
        //获取请求者id
        User toUser = userAction.getUserService().selectById(wzCommonAddFriend.getUserId());
        if(user==null || toUser==null){
            return "用户已不存在";
        }
        //拒绝
        if(!request.getDispose()){
            wzCommonImHistoryAction.add(wzCommonAddFriend.getToUserId(),wzCommonAddFriend.getUserId(),"拒绝了您的好友请求",null,MessageTypeEnum.USER_TYPE, null,null);
            wzCommonAddFriend.setDisposeState(0);
            wzCommonAddFriend.setDisposeTime(new Date());
            wzCommonAddFriend.setUpdateUserId(userId);
            return userFriendService.updateById(wzCommonAddFriend)?null:"处理失败";
        }
        if(friendsService.checkFriendOne(wzCommonAddFriend.getToUserId(),wzCommonAddFriend.getUserId())){
            return "你们已是好友关系";
        }
        if(userBlacklistService.checkBlacklistByUserId(wzCommonAddFriend.getToUserId(),wzCommonAddFriend.getUserId())>0){
            return "请先释放此用户再添加他为好友";
        }
        try {
            //最终同意操作
            if (friendsAction.addFriends(wzCommonAddFriend.getToUserId(), wzCommonAddFriend.getUserId()) && friendsAction.addFriends(wzCommonAddFriend.getUserId(), wzCommonAddFriend.getToUserId())) {
                check(user,toUser.getId());
                check(toUser,user.getId());
                messagePushAction.addFriend(wzCommonAddFriend.getToUserId(),wzCommonAddFriend.getUserId());
                wzCommonImHistoryAction.add(wzCommonAddFriend.getToUserId(),wzCommonAddFriend.getUserId(),"同意了您的好友请求,并已自动关注对方",wzCommonAddFriend.getToUserId(),MessageTypeEnum.USER_TYPE, PushMessageDocTypeEnum.USER_DETAIL,null);
                if (userFriendService.updateAddFriend(wzCommonAddFriend.getToUserId(),wzCommonAddFriend.getUserId())){
                    //双边自动关注
                    userWatchAction.addUserWatch(userId,wzCommonAddFriend.getUserId());
                    userWatchAction.addUserWatch(wzCommonAddFriend.getUserId(),userId);
                    return null;
                }
                else {
                    return "添加好友失败";
                }

            }
        } catch (Exception e) {
            logger.error("添加好友关系出错," + request.toString() + "好友关系原始数据:" + wzCommonAddFriend.toString());
            logger.error(e.getMessage());
        }
        return "添加好友失败";
    }

    private void check(User user,String fromUserId){
        if(StringUtils.isNotBlank(user.getRealName()) && StringUtils.isNotBlank(user.getMobile()) && StringUtils.isNotBlank(user.getIdNumber())){
            scoreAction.addScore(fromUserId, StatScoreEnum.SS_VERIFY_FRIEND);
        }
    }
}
