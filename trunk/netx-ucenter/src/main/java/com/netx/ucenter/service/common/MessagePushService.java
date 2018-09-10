package com.netx.ucenter.service.common;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.netx.common.common.enums.MessageTypeEnum;
import com.netx.common.common.enums.PushMessageDocTypeEnum;
import com.netx.ucenter.mapper.common.CommonJpushMessageMapper;
import com.netx.ucenter.model.common.CommonJpushMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Create by wongloong on 17-8-22
 */
@Service
public class MessagePushService  extends ServiceImpl<CommonJpushMessageMapper,CommonJpushMessage> {
    private Logger logger = LoggerFactory.getLogger(MessagePushService.class);

    @Autowired
    CommonJpushMessageMapper commonJpushMessageMapper;

    public CommonJpushMessageMapper getCommonJpushMessageMapper(){
        return commonJpushMessageMapper;
    }

    public Boolean addMessagePush(CommonJpushMessage wzCommonJpushMessage) throws Exception{
        return this.insert(wzCommonJpushMessage);
    }

    public List<CommonJpushMessage> selectMessageByState(){
        Wrapper<CommonJpushMessage> wrapper = new EntityWrapper<>();
        wrapper.where("state = 0 and deleted = 0");
        wrapper.orderBy("create_time desc");
        return this.selectList(wrapper);
    }

    public void deleteMessage(String userId){
        commonJpushMessageMapper.deleteByUserId(userId);//删除极光信息
    }

    public List<CommonJpushMessage> queryMessageHistoryByUserId(MessageTypeEnum typeEnum,String userId, Integer state, PushMessageDocTypeEnum docTypeEnum, Page page){
        Wrapper<CommonJpushMessage> wrapper = new EntityWrapper<>();
        wrapper.where("to_user_id = {0}",userId);
        if(typeEnum!=null){
            wrapper.and("type = {0}",typeEnum.getName());
        }
        if(state!=null){
            wrapper.and("state = {0}",state);
        }
        if(docTypeEnum!=null){
            wrapper.and("doc_type = {0}",docTypeEnum.getValue());
        }
        return this.selectPage(page,wrapper).getRecords();
    }

}
