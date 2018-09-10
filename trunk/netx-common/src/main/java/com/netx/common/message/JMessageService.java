package com.netx.common.message;


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
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.push.model.notification.PlatformNotification;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netx.common.common.exception.ClientException;
import com.netx.common.common.vo.message.GroupDto;
import com.netx.common.config.PushCodeConfig;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 阿里云物流服务类
 * @author 黎子安
 * @date 2017/10/23
 */
@Service
public class JMessageService {
    private Logger logger = LoggerFactory.getLogger(JMessageService.class);

    @Value("${jiguang.app-key}")
    public String appKey;
    @Value("${jiguang.secret}")
    public String secret;

    public String addUser(String userId){
        JMessageClient client = new JMessageClient(appKey, secret);
        RegisterInfo.Builder builder = new RegisterInfo.Builder();
        builder.setUsername(userId);
        builder.setPassword("123456");
        try {
            return client.registerUsers(new RegisterInfo[]{builder.build()});
        }catch (Exception e){
            logger.warn("此用户id（"+userId+"）已注册"+e.getMessage());
            return null;
        }
    }

    public Boolean deleteUser(String userId){
        try {
            JMessageClient client = new JMessageClient(appKey, secret);
            client.deleteUser(userId);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return false;
    }

    public void pushMessageForAll(String msg) {
        final JPushClient jpushClient = createJPushClient();
        PushPayload payload = buildPushObjectForAll(msg);
        new Thread(new SendJMpush(jpushClient, null,payload)).start();
    }

    public void sendMessage(List<Map<String,Object>> list){
        final JPushClient jpushClient = createJPushClient();
        new Thread(new SendJMpush(jpushClient,list,null)).start();
        //final PushPayload androidPayload = buildPushObjectForAlis(message.getAlertMsg(),message.getTitle(),message.getToUserId(),JSON.parseObject(message.getPushParams(),Map.class),message.getDocType());
    }

    private JPushClient createJPushClient(){
        ClientConfig clientConfig = ClientConfig.getInstance();
        return new JPushClient(secret, appKey, null, clientConfig);
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
/*
    /**
     * 构建推送对象：android
     *
     * @param jpushDto 推送Dto
     * @return
     */
/*    public static PushPayload buildPushObjectForAlis(JpushDto jpushDto) {
        return buildPushObjectForAlis(jpushDto.getAlertMsg(),jpushDto.getTitle(),jpushDto.getUserId(), jpushDto.getPushParams(),jpushDto.getDocType());
    }*/

    class SendJMpush implements Runnable{
        private JPushClient jPushClient;
        private List<Map<String,Object>> wzCommonJpushMessages;
        private PushPayload pushPayload;
        private JMessageClient jMessageClient;

/*        public SendJMpush(JPushClient jPushClient, List<WzCommonJpushMessage> wzCommonJpushMessages) {
            this.jPushClient = jPushClient;
            this.wzCommonJpushMessages = wzCommonJpushMessages;
            this.pushPayload=null;
        }*/

        public SendJMpush(JPushClient jPushClient, List<Map<String,Object>> wzCommonJpushMessages, PushPayload pushPayload) {
            this.jPushClient = jPushClient;
            this.wzCommonJpushMessages = wzCommonJpushMessages;
            this.pushPayload = pushPayload;
            this.jMessageClient = new JMessageClient(appKey, secret);
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
                int i = 0;
                logger.info("本次需要发送"+len+"条信息");
                String updateIds = "";
                for (Map<String,Object> message:wzCommonJpushMessages) {
                    try {
                        logger.info("现在发送第"+(i++)+"信息，其信息id为："+message.get("id")+"；内容为："+message.get("alertMsg"));
                        sendMessage(buildPushObjectForAlis(message));
                        message.put("state",1);
                        updateIds += message.get("id");
                        logger.info("第"+i+"条发送成功");
                    }catch (Exception e){
                        logger.info("第"+i+"条发送失败");
                        logger.error(e.getMessage(),e);
                    }
                }
                //访问用户中心接口，对极光信息进行更新
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
        private PushPayload buildPushObjectForAlis(Map<String,Object> message){
            Map<String,String> map = JSON.parseObject(message.get("pushParams").toString(),Map.class);
            if(map == null){
                map = new HashMap<>();
            }
            if(message.get("docType")!=null){
                map.put("docType", message.get("docType").toString());
            }else {
                map.put("docType","message");
            }
            String toUserId = message.get("toUserId").toString();
            checkUserState(jMessageClient,toUserId);
            PushPayload pushPayload = PushPayload.newBuilder()
                    .setPlatform(Platform.android_ios())
                    .setOptions(Options.newBuilder().setTimeToLive(864000).build())
                    .setAudience(Audience.alias(toUserId))
                    .setNotification(
                            Notification.newBuilder()
                                    .setAlert(message.get("alertMsg").toString())
                                    .addPlatformNotification(createPlatformNotification(map,message.get("title").toString()))
                                    .addPlatformNotification(IosNotification.newBuilder().incrBadge(1).addExtras(map).build())
                                    .build()
                    )
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

    public void pushIdToGroup(Long groupId,String... userId) throws ClientException {
        JMessageClient client = new JMessageClient(appKey, secret);
        try {
            for(String u:userId){
                checkUserState(client,u);
            }
            client.addOrRemoveMembers(groupId,userId,null);
        } catch (APIConnectionException e) {
            logger.error("Connection error. Should retry later. ", e);
            throw new ClientException("推送异常："+e.getMessage());
        } catch (APIRequestException e) {
            pushError(e);
        }

    }

    private void pushError(APIRequestException e) throws ClientException {
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
            throw new ClientException(e.getMessage());
        }
        throw new ClientException(message);
    }

    public Long createGroup(GroupDto groupDto) throws ClientException {
        JMessageClient client = new JMessageClient(appKey, secret);
        try {
            checkUserState(client,groupDto.getUserId());
            CreateGroupResult group = client.createGroup(groupDto.getUserId(), groupDto.getGroupName(), groupDto.getType());
            return group.getGid();
        } catch (APIConnectionException e) {
            logger.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            pushError(e);
        }
        return null;
    }

    private void checkUserState(JMessageClient client,String userId){
        try {
            UserStateResult userState = client.getUserState(userId);
        }catch (Exception e) {
            logger.info("Error Message: " + e.getMessage());
            addUser(userId);
        }
    }

    public void addFriend(String userId,String friendId) throws ClientException {
        JMessageClient client = new JMessageClient(appKey, secret);
        try {
            checkUserState(client,userId);
            checkUserState(client,friendId);
            ResponseWrapper responseWrapper = client.addFriends(userId, friendId);
        } catch (APIConnectionException e) {
            logger.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            pushError(e);
        }
    }

    public void deleteFriend(String userId,String friendId) throws Exception {
        JMessageClient client = new JMessageClient(appKey, secret);
        checkUserState(client,userId);
        checkUserState(client,friendId);
        ResponseWrapper responseWrapper = client.deleteFriends(userId,friendId);
    }

    public GroupInfoResult[] findUserGroup(String userId) throws ClientException {
        JMessageClient client = new JMessageClient(appKey, secret);
        try {
            checkUserState(client,userId);
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

    public GroupInfoResult findGroup(Long groupId) throws ClientException {
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
}