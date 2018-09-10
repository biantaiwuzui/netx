package com.netx.ucenter.biz.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.common.enums.MessageTypeEnum;
import com.netx.common.common.enums.PushMessageDocTypeEnum;
import com.netx.common.common.vo.message.ImDto;
import com.netx.common.user.model.UserInfo;
import com.netx.common.user.model.UserSynopsisData;
import com.netx.common.user.util.VoPoConverter;
import com.netx.common.vo.common.MessageResponseDto;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.ucenter.model.common.CommonImHistory;
import com.netx.ucenter.model.common.CommonJpushMessage;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.service.common.WzCommonImHistoryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 黎子安
 * @since 2018-02-07
 */
@Service
public class  WzCommonImHistoryAction{

    @Autowired
    private WzCommonImHistoryService wzCommonImHistoryService;

    @Autowired
    private UserAction userAction;

    /**
     * 添加动态
     * @param imDto
     * @return
     */
    public Boolean add(ImDto imDto){
        CommonImHistory wzCommonImHistory = VoPoConverter.copyProperties(imDto,CommonImHistory.class);
        if(imDto.getTypeEnum()!=null){
            wzCommonImHistory.setType(imDto.getTypeEnum().getName());
        }
        if(imDto.getDocTypeEnum()!=null){
            wzCommonImHistory.setDocType(imDto.getDocTypeEnum().getValue());
        }
        if(imDto.getPushParamsMap()!=null && imDto.getPushParamsMap().size()>0){
            wzCommonImHistory.setPushParams(JSON.toJSONString(imDto.getPushParamsMap()));
        }
        if(imDto.getSendTime()==null){
            wzCommonImHistory.setSendTime(new Date());
        }
        wzCommonImHistory.setRead(false);
        return wzCommonImHistoryService.insert(wzCommonImHistory);
    }

    /**
     * 添加动态
     * @param fromUserId
     * @param toUserId
     * @param message
     * @param typeEnum
     * @param docType
     * @param param
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean add(String fromUserId, String toUserId, String message,String typeId, MessageTypeEnum typeEnum, PushMessageDocTypeEnum docType, Map<String,Object> param){
        CommonImHistory wzCommonImHistory = new CommonImHistory();
        wzCommonImHistory.setFromUserId(fromUserId);
        wzCommonImHistory.setToUserId(toUserId);
        wzCommonImHistory.setMessagePayload(message);
        wzCommonImHistory.setTypeId(typeId);
        wzCommonImHistory.setRead(false);
        if(typeEnum!=null){
            wzCommonImHistory.setType(typeEnum.getName());
        }
        if(docType!=null){
            wzCommonImHistory.setDocType(docType.getValue());
        }
        if(param!=null && param.size()>0){
            wzCommonImHistory.setPushParams(JSON.toJSONString(param));
        }
        wzCommonImHistory.setSendTime(new Date());
        return wzCommonImHistoryService.insert(wzCommonImHistory);
    }

    /**
     * 查询动态
     * @param typeEnum
     * @param lon
     * @param lat
     * @param userId
     * @param docType
     * @param current
     * @param size
     * @return
     * @throws Exception
     */
    public Map<String,Object> getHistoryMessage(MessageTypeEnum typeEnum,Double lon,Double lat,String userId,  PushMessageDocTypeEnum docType, Integer current, Integer size) throws Exception{
        List<CommonImHistory> list = wzCommonImHistoryService.queryCommonImHistoryByUserId(typeEnum,userId,docType,new Page(current,size));
        Map<String,Object> map = new HashMap<>();
        List<MessageResponseDto> messages = new ArrayList<>();
        if(list!=null && list.size()>0){
            List<String> fromUserIds = new ArrayList<>();
            Boolean flag = false;
            for(CommonImHistory commonImHistory:list){
                messages.add(createMessageResponseDto(commonImHistory));
                if(StringUtils.isNotBlank(commonImHistory.getFromUserId())){
                    if(commonImHistory.getFromUserId().equals("999")){
                        flag = true;
                    }else {
                        fromUserIds.add(commonImHistory.getFromUserId());
                    }
                }
            }
            Map<String,UserSynopsisData> userData = new HashMap<>();
            if(fromUserIds.size()>0){
                userData = userAction.getUserSynopsisDataMap(fromUserIds,lon,lat,userId);
            }
            if(flag){
                userData.put("999",userAction.getSystemUser());
            }
            map.put("userData",userData);
        }
        map.put("list",messages);
        return map;
    }

    /**
     * 重载getHistoryMessage 方法，查询多个docTypes的动态
     * @param typeEnum
     * @param lon
     * @param lat
     * @param userId
     * @param docTypes
     * @param current
     * @param size
     * @return
     * @throws Exception
     */
    public Map<String,Object> getHistoryMessage(MessageTypeEnum typeEnum,Double lon,Double lat,String userId,  List<PushMessageDocTypeEnum> docTypes, Integer current, Integer size) throws Exception{
        List<CommonImHistory> list = wzCommonImHistoryService.queryCommonImHistoryByUserId(typeEnum,userId,docTypes,new Page(current,size));
        Map<String,Object> map = new HashMap<>();
        List<MessageResponseDto> messages = new ArrayList<>();
        if(list!=null && list.size()>0){
            List<String> fromUserIds = new ArrayList<>();
            Boolean flag = false;
            for(CommonImHistory commonImHistory:list){
                messages.add(createMessageResponseDto(commonImHistory));
                if(StringUtils.isNotBlank(commonImHistory.getFromUserId())){
                    if(commonImHistory.getFromUserId().equals("999")){
                        flag = true;
                    }else {
                        fromUserIds.add(commonImHistory.getFromUserId());
                    }
                }
            }
            Map<String,UserSynopsisData> userData = new HashMap<>();
            if(fromUserIds.size()>0){
                userData = userAction.getUserSynopsisDataMap(fromUserIds,lon,lat,userId);
            }
            if(flag){
                userData.put("999",userAction.getSystemUser());
            }
            map.put("userData",userData);
        }
        map.put("list",messages);
        return map;
    }



    private MessageResponseDto createMessageResponseDto(CommonImHistory commonJpushMessage){
        MessageResponseDto messageResponseDto = VoPoConverter.copyProperties(commonJpushMessage,MessageResponseDto.class);
        messageResponseDto.setUserId(commonJpushMessage.getFromUserId());
        messageResponseDto.setISRead(commonJpushMessage.getRead());
        if(StringUtils.isNotBlank(commonJpushMessage.getPushParams())){
            messageResponseDto.setPushParamsMap(JSONObject.parseObject(commonJpushMessage.getPushParams(),Map.class));
        }
        return messageResponseDto;
    }

    public void updateRead(String id,String userId){
        CommonImHistory commonImHistory = wzCommonImHistoryService.query(id,userId);
        if(commonImHistory!=null && !commonImHistory.getRead()){
            commonImHistory.setRead(true);
            wzCommonImHistoryService.updateById(commonImHistory);
        }
    }
    
    /* 仅供网能使用 **/
    public CommonImHistory getCommonNewsReadStatus(String userId, String typeId, PushMessageDocTypeEnum docTypeEnum){
        CommonImHistory commonImHistory = wzCommonImHistoryService.getNewsReadStatus(userId,typeId,docTypeEnum);
        if(commonImHistory==null){
            return null;
        }
        return commonImHistory;
    }
}
