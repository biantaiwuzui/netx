package com.netx.fuse.proxy;

import com.netx.common.common.enums.MessageTypeEnum;
import com.netx.common.common.enums.PushMessageDocTypeEnum;
import com.netx.common.common.vo.message.JpushDto;
import com.netx.ucenter.biz.common.MessagePushAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 发消息工具类
 */
@Component
public class MessagePushProxy {

    private Logger logger = LoggerFactory.getLogger(MessagePushProxy.class);

    @Autowired
    MessagePushAction messagePushAction;

    public Boolean sendByAlias(JpushDto jpushDto){
        try {
            messagePushAction.sendMessageAlias(jpushDto);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return false;
        }
        return true;
    }

    public void send(String msg) {
        messagePushAction.pushMessageForAll(msg);
    }

    public Boolean sendMessage(){
        try {
            messagePushAction.sendMessage();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return false;
        }
        return true;
    }

    public void messagePush(MessageTypeEnum typeEnum,String alertMsg, String title, String userId, String docType, String redirectId){
        JpushDto jpushDto=new JpushDto();
        Map<String,Object> param=new HashMap<>();
        jpushDto.setAlertMsg(alertMsg);
        jpushDto.setTitle(title);
        jpushDto.setUserId(userId);
        param.put("redirectId",redirectId);
        jpushDto.setPushParams(param);
        jpushDto.setDocType(docType);
        jpushDto.setType(typeEnum);
        sendByAlias(jpushDto);
    }

    /**
     *
     * @param alertMsg 推送消息
     * @param title 推送标题
     * @param userId    推送的用户ID
     * @param docType   推送的类型
     * @param id    如果推送的类型是订单的话，这个ID就是订单ID，跳转要根据这个订单ID实现跳转
     */
    public void messagePushJump(MessageTypeEnum typeEnum,String alertMsg, String title, String userId, PushMessageDocTypeEnum docType, String id){
        JpushDto jpushDto=new JpushDto();
        Map param=new HashMap<>();
        jpushDto.setAlertMsg(alertMsg);
        jpushDto.setType(typeEnum);
        jpushDto.setTitle(title);
        jpushDto.setUserId(userId);
        param.put("id",id);
        jpushDto.setPushParams(param);
        jpushDto.setDocType(docType.getValue());
        sendByAlias(jpushDto);
    }

    /**
     *
     * @param alertMsg 推送消息
     * @param title 推送标题
     * @param userId    推送的用户ID
     * @param docType   推送的类型
     * @param param    如果推送的类型是订单的话，这个ID就是订单ID，跳转要根据这个订单ID实现跳转,报名申购填1
     */
    public void multipleParamMessagePushJump(MessageTypeEnum typeEnum,String alertMsg, String title, String userId, PushMessageDocTypeEnum docType, Map param){
        JpushDto jpushDto=new JpushDto();
        jpushDto.setAlertMsg(alertMsg);
        jpushDto.setTitle(title);
        jpushDto.setUserId(userId);
        jpushDto.setPushParams(param);
        jpushDto.setDocType(docType.getValue());
        jpushDto.setType(typeEnum);
        sendByAlias(jpushDto);
    }
}
