package com.netx.ucenter.biz.common;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jiguang.common.resp.ResponseWrapper;
import cn.jmessage.api.JMessageClient;
import cn.jmessage.api.common.model.RegisterInfo;
import cn.jmessage.api.group.CreateGroupResult;
import cn.jmessage.api.group.GroupInfoResult;
import cn.jmessage.api.user.UserGroupsResult;
import cn.jmessage.api.user.UserStateResult;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.push.model.notification.PlatformNotification;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.common.enums.MessageTypeEnum;
import com.netx.common.common.enums.PushMessageDocTypeEnum;
import com.netx.common.common.vo.message.GroupDto;
import com.netx.common.common.vo.message.JpushDto;
import com.netx.common.config.PushCodeConfig;
import com.netx.common.redis.RedisInfoHolder;
import com.netx.common.user.util.VoPoConverter;
import com.netx.common.vo.common.MessageResponseDto;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.ucenter.model.common.CommonJpushMessage;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.service.common.MessagePushService;
import com.netx.utils.cache.RedisCache;
import com.netx.utils.cache.RedisKeyName;
import com.netx.utils.cache.RedisTypeEnum;
import com.netx.utils.sign.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Create by wongloong on 17-8-22
 */
@Service
public class MessagePushAction{
    private Logger logger = LoggerFactory.getLogger(MessagePushAction.class);

    @Autowired
    private MessagePushService messagePushService;

    @Value("${jiguang.app-key}")
    public String appKey;
    @Value("${jiguang.secret}")
    public String secret;

    @Autowired
    protected UserAction userAction;

    private RedisCache redisCache;

    @Autowired
    RedisInfoHolder redisInfoHolder;

    private RedisCache clientRedis(){
        redisCache = RedisCache.getRedisCache(redisInfoHolder.getHost(),redisInfoHolder.getPassword(),redisInfoHolder.getPort());
        return redisCache;
    }

    public void sendMessageAlias(JpushDto jpushDto)  throws Exception{
        sendMessageAlias(jpushDto.getType(),jpushDto.getFromUserId(), jpushDto.getAlertMsg(),jpushDto.getTitle(),jpushDto.getUserId(),jpushDto.getPushParams(),jpushDto.getDocType());
    }

    public void delete(String userId){
        JMessageClient client = new JMessageClient(appKey, secret);
        try {
            client.deleteUser(userId);
        }catch (Exception e){
            logger.warn(e.getMessage());
        }
        messagePushService.deleteMessage(userId);
    }

    /**
     * 系统发送推送
     * @param alertMsg
     * @param title
     * @param userId
     * @param pushParams
     * @param docType
     * @throws Exception
     */
    public void sendMessageAlias(MessageTypeEnum typeEnum,String alertMsg, String title,String userId,Map pushParams, String docType) throws Exception{
        jiguangSendMessage(createMessage(typeEnum,null,userId,alertMsg,title,pushParams,docType));
    }

    /**
     * 用户发给用户
     * @param typeEnum
     * @param fromUserId
     * @param alertMsg
     * @param title
     * @param userId
     * @param pushParams
     * @param docType
     * @throws Exception
     */
    public void sendMessageAlias(MessageTypeEnum typeEnum,String fromUserId, String alertMsg, String title,String userId,Map pushParams, String docType) throws Exception{
        jiguangSendMessage(createMessage(typeEnum,fromUserId,userId,alertMsg,title,pushParams,docType));
    }

    /**
     * 返回极光注册密码
     * @param userId
     * @return
     */
    public String addUser(String userId){
        JMessageClient client = new JMessageClient(appKey, secret);
        try {
            client.deleteUser(userId);
        }catch (Exception e){
            logger.warn(e.getMessage());
        }
        RegisterInfo.Builder builder = new RegisterInfo.Builder();
        builder.setUsername(userId);
        String password = createPassword();
        builder.setPassword(password);
        try {
            client.registerUsers(new RegisterInfo[]{builder.build()});
            return password;
        }catch (Exception e){
            logger.warn("此用户id（"+userId+"）已注册："+e.getMessage());
            return null;
        }
    }

    private String createPassword(){
        String token = UUID.randomUUID().toString().replace("-", "").substring(0,8);
        logger.info("转码前："+token);
        return Base64.encode(token.getBytes()).replace("[B@", "");
    }

    public void deleteUser(String userId) throws Exception{
        JMessageClient client = new JMessageClient(appKey, secret);
        client.deleteUser(userId);
    }

    public void sendMessage() throws Exception{
        List<CommonJpushMessage> list = messagePushService.selectMessageByState();
        final JPushClient jpushClient = createJPushClient();
        new Thread(new SendJMpush(jpushClient,list,null)).start();
    }

    private CommonJpushMessage createMessage(JpushDto jpushDto){
        return this.createMessage(jpushDto.getType(),jpushDto.getFromUserId(),jpushDto.getUserId(),jpushDto.getAlertMsg(),jpushDto.getTitle(),jpushDto.getPushParams(),jpushDto.getDocType());
    }

    private void jiguangSendMessage(CommonJpushMessage message) throws Exception{
        userAction.checkUserId(message.getToUserId());
        messagePushService.addMessagePush(message);
    }

    private CommonJpushMessage createMessage(MessageTypeEnum typeEnum, String fromUserId, String userId, String msg, String title, Map pushParams, String docType){
        Date time = new Date();
        CommonJpushMessage message = new CommonJpushMessage();
        message.setPushParams(JSON.toJSONString(pushParams));
        message.setAlertMsg(msg);
        message.setTitle(title);
        message.setFromUserId(fromUserId);
        message.setToUserId(userId);
        if(typeEnum!=null){
            message.setType(typeEnum.getName());
        }
        message.setCreateTime(time);
        message.setUpdateTime(time);
        message.setDocType(docType);
        message.setState(0);
        return message;
    }

    private JPushClient createJPushClient(){
        ClientConfig clientConfig = ClientConfig.getInstance();
        return new JPushClient(secret, appKey, null, clientConfig);
    }

    public void pushMessageForAll(String msg) {
        final JPushClient jpushClient = createJPushClient();
        PushPayload payload = buildPushObjectForAll(msg);
        new Thread(new SendJMpush(jpushClient, null,payload)).start();
    }

    class SendJMpush implements Runnable{
        private JPushClient jPushClient;
        private List<CommonJpushMessage> wzCommonJpushMessages;
        private PushPayload pushPayload;
        private JMessageClient client;

        public SendJMpush(JPushClient jPushClient, List<CommonJpushMessage> wzCommonJpushMessages, PushPayload pushPayload) {
            this.jPushClient = jPushClient;
            this.wzCommonJpushMessages = wzCommonJpushMessages;
            this.pushPayload = pushPayload;
            this.client = new JMessageClient(appKey, secret);
        }

        @Override
        public void run() {
            if(this.pushPayload!=null){
                try {
                    sendMessage(this.pushPayload);
                }catch (Exception e){
                    logger.error(e.getMessage(),e);
                }
                return;
            }
            int len = wzCommonJpushMessages==null? 0:wzCommonJpushMessages.size();
            if(len>0){
                clientRedis();
                RedisKeyName redisKeyName = new RedisKeyName("userInfo",RedisTypeEnum.OBJECT_TYPE,null);
                int i = 0;
                logger.info("本次需要发送"+len+"条信息");
                for (CommonJpushMessage message:wzCommonJpushMessages) {
                    logger.info("现在发送第"+(++i)+"信息，其信息id为："+message.getId()+"；内容为："+message.getAlertMsg());
                    if(StringUtils.isNotBlank(message.getToUserId())){
                        redisKeyName.setId(message.getToUserId());
                        User user = (User) redisCache.get(redisKeyName.getUserKey());
                        if(user==null){
                            user = userAction.getUserService().selectById(message.getToUserId());
                        }
                        if(user==null){
                            message.setDeleted(1);
                        }else{
                            //判断用户是否已经极光注册
                            if(checkMessageUser(client,user.getId())){
                                //判断用户极光注册后是否未进行数据库修改
                                if(!user.getRegJMessage()){
                                    String password = createPassword();
                                    if(updateJMessagePWD(client,user.getId(),password)){
                                        user.setJmessagePassword(password);
                                        updateMessageUser(user,redisKeyName.getUserKey());
                                    }
                                }
                            }else{
                                //未注册进行注册
                                String password = addUser(user.getId());
                                if(password!=null){
                                    user.setJmessagePassword(password);
                                    updateMessageUser(user,redisKeyName.getUserKey());
                                }
                            }
                            try {
                                sendMessage(buildPushObjectForAlis(message));
                                message.setState(1);
                                logger.info("第" + i + "条发送成功");
                            }catch (Exception e){
                                logger.error("第"+i+"条发送给"+message.getToUserId()+"失败，原因："+e.getMessage());
                                message.setDeleted(1);
                            }
                        }
                    }else {
                        message.setDeleted(1);
                    }
                    messagePushService.updateById(message);
                }
            }
        }

        private void sendMessage(PushPayload pushPayload) throws Exception{
            //安卓
            PushResult result = jPushClient.sendPush(pushPayload);
            logger.info("Got result - " + result);
            logger.info("发送消息:" + pushPayload.toString());
        }



        /**
         *
         * @param message
         * @return
         */
        private PushPayload buildPushObjectForAlis(CommonJpushMessage message){
            Map<String,String> map = JSON.parseObject(message.getPushParams(),Map.class);
            if(map == null){
                map = new HashMap<>();
            }
            if(message.getDocType()!=null){
                map.put("docType", message.getDocType());
            }else {
                map.put("docType","message");
            }
            PushPayload pushPayload = PushPayload.newBuilder()
                    .setPlatform(Platform.android_ios())
                    //.setOptions(Options.newBuilder().setTimeToLive(864000).build())
                    .setAudience(Audience.alias(message.getToUserId()))
                    .setNotification(Notification.newBuilder()
                                    .setAlert(message.getAlertMsg())
                                    .addPlatformNotification(createPlatformNotification(map,message.getTitle()))
                                    .addPlatformNotification(IosNotification.newBuilder().incrBadge(1).addExtras(map).build())
                                    .build())
                    .setOptions(Options.newBuilder().setApnsProduction(true).setTimeToLive(864000).build())
                    .build();
            return pushPayload;
        }
    }

    private PlatformNotification createPlatformNotification(Map<String,String> map, String title){
        AndroidNotification.Builder builder = AndroidNotification.newBuilder().addExtras(map);
        if(StringUtils.isNotBlank(title)){
            builder.setTitle(title);
        }
        return builder.build();
    }

    private void updateMessageUser(User user,String redisKeyName){
        user.setRegJMessage(true);
        userAction.getUserService().selectById(user);
        redisCache.put(redisKeyName,user);
    }

    private Boolean updateJMessagePWD(JMessageClient client,String userId,String password){
        try {
            client.updateUserPassword(userId,password);
            return true;
        }catch (Exception e){
            logger.error(userId+"极光密码修改失败："+e.getMessage(),e);
            return false;
        }
    }

    /**
     * 快捷地构建推送对象：所有平台，所有设备，内容为 alertMsg 的通知
     *
     * @param alertMsg 通知内容
     * @return
     */
    public PushPayload buildPushObjectForAll(String alertMsg) {
        return PushPayload.alertAll(alertMsg);
    }

    public void pushIdToGroup(Long groupId,String... userId) throws Exception {

        JMessageClient client = new JMessageClient(appKey, secret);
        try {
            for(String u:userId){
                checkUserId(client,u);
            }
            client.addOrRemoveMembers(groupId,userId,null);
        } catch (APIConnectionException e) {
            logger.error("Connection error. Should retry later. ", e);
            throw new Exception("推送异常："+e.getMessage());
        } catch (APIRequestException e) {
            pushError(e);
        }

    }

    public void removeUserIdToGroup(Long groupId,String... userId) throws Exception {
        JMessageClient client = new JMessageClient(appKey, secret);
        try {
            for(String u:userId){
                checkUserId(client,u);
            }
            client.addOrRemoveMembers(groupId,null,userId);
        } catch (APIConnectionException e) {
            logger.error("Connection error. Should retry later. ", e);
            throw new Exception("推送异常："+e.getMessage());
        } catch (APIRequestException e) {
            pushError(e);
        }

    }

    private Boolean checkMessageUser(JMessageClient client,String userId){
        try {
            UserStateResult userState = client.getUserState(userId);
            return true;
        }catch (Exception e) {
            logger.info("Error Message: " + e.getMessage());
            return false;
        }
    }

    private void pushError(APIRequestException e) throws Exception {
        logger.error("Error response from JPush server. Should review and fix it. ", e);
        logger.info("HTTP Status: " + e.getStatus());
        logger.info("Error Message: " + e.getMessage());
        String message=null;
        try {
            JSONObject jsonObject = JSON.parseObject(e.getMessage());
            JSONObject error = jsonObject.getJSONObject("error");
            Integer code = error.getInteger("code");
            message= PushCodeConfig.map.get(code);
        }catch (Exception ep){
            throw new Exception(e.getMessage());
        }
        throw new Exception(message);
    }

    public Long createGroup(GroupDto groupDto) throws Exception {
        JMessageClient client = new JMessageClient(appKey, secret);
        try {
            checkUserId(client,groupDto.getUserId());
            CreateGroupResult group = client.createGroup(groupDto.getUserId(), groupDto.getGroupName(), groupDto.getType());
            return group.getGid();
        } catch (APIConnectionException e) {
            logger.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            pushError(e);
        }
        return null;
    }

    private Boolean addMessageUser(String userId){
        String password = addUser(userId);
        if(StringUtils.isNotBlank(password)){
            return userAction.addMessageUser(userId,password);
        }
        return false;
    }

    private void checkUserId(JMessageClient client,String userId){
        if(!checkMessageUser(client,userId)){
            addMessageUser(userId);
        }
    }

    public void addFriend(String userId,String friendId) throws Exception {
        JMessageClient client = new JMessageClient(appKey, secret);
        try {
            checkUserId(client,userId);
            checkUserId(client,friendId);
            ResponseWrapper responseWrapper = client.addFriends(userId, friendId);
        } catch (APIConnectionException e) {
            logger.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            pushError(e);
        }
    }

    public void deleteFriend(String userId,String friendId) throws Exception {
        JMessageClient client = new JMessageClient(appKey, secret);
        ResponseWrapper responseWrapper = client.deleteFriends(userId,friendId);
    }

    public GroupInfoResult[] findUserGroup(String userId) throws Exception {
        JMessageClient client = new JMessageClient(appKey, secret);
        try {
            UserGroupsResult groupListByUser = client.getGroupListByUser(userId);
            GroupInfoResult[] groups = groupListByUser.getGroups();
            return groups;
        } catch (APIConnectionException e) {
            logger.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            pushError(e);
        }
        return null;
    }

    public GroupInfoResult findGroup(Long groupId) throws Exception {
        JMessageClient client = new JMessageClient(appKey, secret);
        try {
            GroupInfoResult groupInfo = client.getGroupInfo(groupId);
            return groupInfo;
        } catch (APIConnectionException e) {
            logger.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            pushError(e);
        }
        return null;
    }

    /**
     * 解散群
     * @param groupId
     */
    public void delGroup(Long groupId){
        JMessageClient client = new JMessageClient(appKey, secret);
        try {
            client.deleteGroup(groupId);
        }catch (APIConnectionException e) {
            logger.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            try {
                pushError(e);
            }catch (Exception em){
                logger.error(em.getMessage(),e);
            }
        }
    }

    public Boolean updateGroup(Long groupId,String groupName,String userId){
        JMessageClient client = new JMessageClient(appKey, secret);
        try {
            client.updateGroupInfo(groupId,groupName,userId);
            return true;
        }catch (APIConnectionException e) {
            logger.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            try {
                pushError(e);
            }catch (Exception em){
                logger.error(em.getMessage(),e);
            }
        }
        return false;
    }
}
