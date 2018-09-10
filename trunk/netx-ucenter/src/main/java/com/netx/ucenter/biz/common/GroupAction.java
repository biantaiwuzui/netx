package com.netx.ucenter.biz.common;

import com.netx.common.common.enums.MessageTypeEnum;
import com.netx.common.common.vo.message.GroupDto;
import com.netx.common.common.vo.message.GroupInfoDto;
import com.netx.common.user.dto.user.UserInfoAndHeadImg;
import com.netx.common.user.model.UserSynopsisData;
import com.netx.common.user.util.VoPoConverter;
import com.netx.common.vo.common.SendMessageToGroupChatUsersDto;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.ucenter.biz.user.UserPhotoAction;
import com.netx.ucenter.model.common.CommonGroup;
import com.netx.ucenter.model.common.CommonGroupUser;
import com.netx.ucenter.service.common.GroupUserService;
import com.netx.ucenter.service.common.GroupService;
import com.netx.ucenter.vo.GroupInfo;
import com.netx.ucenter.vo.request.GroupInfoResponseDto;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Create on 17-11-14
 * @author wongloong
 */
@Service
public class GroupAction {

    private Logger logger = LoggerFactory.getLogger(GroupAction.class);

    @Autowired
    private MessagePushAction messagePushAction;

    @Autowired
    private UserPhotoAction userPhotoAction;

    @Autowired
    private UserAction userAction;

    @Autowired
    private GroupService groupsService;

    @Autowired
    private GroupUserService groupUserService;

    public String getNickname(String userId){
        return userAction.getUserService().getOneDataByUserId(userId,"nickname");
    }

    public String deleteGroupUser(Long groupId,String userId) throws Exception{
        CommonGroup commonGroup = groupsService.getGroupByGroupId(groupId);
        if(commonGroup==null){
            return "此群已不存在";
        }
        CommonGroupUser commonGroupUser = groupUserService.getWzCommonGroupChatByGroupId(userId, commonGroup.getId());
        if (commonGroupUser == null) {
            return "此用户已不在群中";
        }
        if (commonGroupUser.getAdmin()) {
            return "不能删除群主";
        }
        Boolean result = groupUserService.deleteByGroupId(userId,commonGroup.getId());
        try {
            messagePushAction.removeUserIdToGroup(groupId, userId);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return result?null:"删除失败";
    }

    public String deleteGroupUsers(Long groupId,String[] userIds) throws Exception{
        CommonGroup commonGroup = groupsService.getGroupByGroupId(groupId);
        if(commonGroup==null){
            return "此群已不存在";
        }
        for(String userId:userIds){
            if(userId.equals(commonGroup.getUserId())){
                return "不能删除群主";
            }
        }
        List<String> groupUserList = groupUserService.getIdsByGroupId(userIds,commonGroup.getId());
        if (groupUserList == null || groupUserList.size()<0) {
            return "有用户已不在群中";
        }
        Boolean result = groupUserService.delectByIds(groupUserList);
        try {
            messagePushAction.removeUserIdToGroup(groupId, userIds);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return result?null:"删除失败";
    }

    public String deleteGroup(Long groupId,String userId) throws Exception{
        CommonGroup commonGroup = groupsService.getGroupByGroupId(groupId);
        if(commonGroup==null){
            return "此群已不存在";
        }
        CommonGroupUser commonGroupUser = groupUserService.getWzCommonGroupChatByGroupId(userId, commonGroup.getId());
        if (commonGroupUser == null) {
            return "你不在群中";
        }
        if (!commonGroupUser.getAdmin()) {
            return "只有群主才能解散群组";
        }
        try {
            groupsService.delByCondition("group_id", groupId + "");
            groupUserService.delByCondition("group_id", commonGroup.getId());
            messagePushAction.delGroup(groupId);
            return null;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return "解散失败";
        }
    }

    public void delByUserId(String userId){
        //移除出群
        List<CommonGroup> commonGroups = groupsService.getCommonGroupMapper().queryGroupsByUserId(userId,0);
        if(commonGroups!=null && commonGroups.size()>0){
            commonGroups.forEach(commonGroup -> {
                try {
                    messagePushAction.removeUserIdToGroup(commonGroup.getGroupId(), userId);
                }catch (Exception e){
                    logger.error(e.getMessage(),e);
                }
            });
            try {
                groupUserService.delByCondition("user_id", userId);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }
        }
        //删除群
        commonGroups = groupsService.getCommonGroupListByUserId(userId);
        if(commonGroups!=null && commonGroups.size()>0){
            commonGroups.forEach(commonGroup -> {
                try {
                    messagePushAction.delGroup(commonGroup.getGroupId());
                }catch (Exception e){
                    logger.error(e.getMessage(),e);
                }
            });
            try {
                groupsService.delByCondition("user_id", userId);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }
        }
    }

    public Map<String,Object> getListByGroupId(Long groupId,Double lon,Double lat,String userId) throws Exception{
        CommonGroup commonGroup = groupsService.getGroupByGroupId(groupId);
        List<GroupInfo> result = new ArrayList<>();
        GroupInfoDto groupInfoDto = new GroupInfoDto();
        Map<String,Object> map = new HashMap<>();
        if(commonGroup!=null && commonGroup.getGroupId()!=null ){
            groupInfoDto = createGroupInfoDto(commonGroup);
            List<CommonGroupUser> commonGroupUsers = groupUserService.getListByGroupId(commonGroup.getId());
            if(commonGroupUsers!=null && commonGroupUsers.size()>0){
                List<String> userIds = new ArrayList<>();
                commonGroupUsers.forEach(commonGroupUser -> {
                    userIds.add(commonGroupUser.getUserId());
                });
                Map<String,UserSynopsisData> userData = userAction.getUserSynopsisDataMap(userIds,lon,lat,userId);
                commonGroupUsers.forEach(commonGroupUser -> {
                    result.add(createGroupInfo(commonGroupUser,userData));
                });
            }
        }
        map.put("list",result);
        map.put("group",groupInfoDto);
        return map;
    }

    private GroupInfo createGroupInfo(CommonGroupUser commonGroupUser,Map<String,UserSynopsisData> userData){
        GroupInfo groupInfo = new GroupInfo();
        UserSynopsisData userSynopsisData = userData.get(commonGroupUser.getUserId());
        if(userSynopsisData!=null){
            VoPoConverter.copyProperties(userSynopsisData,groupInfo);
        }
        groupInfo.setAdmin(commonGroupUser.getAdmin());
        if(StringUtils.isNotBlank(commonGroupUser.getUserName())){
            groupInfo.setNickName(commonGroupUser.getUserName());
        }
        return groupInfo;
    }

    public Long createGroup(GroupDto groupDto) throws Exception {
        Long group = messagePushAction.createGroup(groupDto);
        if(group==null){
            throw new Exception("创建群组异常");
        }
        //将群组信息保存到groupChat中
        CommonGroup wzCommonGroupChat = new CommonGroup();
        wzCommonGroupChat.setCreateTime(new Date());
        wzCommonGroupChat.setCreateUserId(groupDto.getUserId());
        wzCommonGroupChat.setGroupName(groupDto.getGroupName());
        wzCommonGroupChat.setPwd(groupDto.getTag());
        wzCommonGroupChat.setGroupId(group);
        wzCommonGroupChat.setUserId(groupDto.getUserId());
        groupsService.insert(wzCommonGroupChat);
        addGroupUser(wzCommonGroupChat.getId(),groupDto.getUserId(),true);
        return group;
    }

    public Boolean addGroupUser(String groupId,String userId,Boolean isAdmin){
        CommonGroupUser commonGroupUser = new CommonGroupUser();
        commonGroupUser.setGroupId(groupId);
        commonGroupUser.setUserId(userId);
        commonGroupUser.setAdmin(isAdmin);
        commonGroupUser.setCreateUserId(userId);
        return groupUserService.insert(commonGroupUser);
    }

    public Map<String,Object> getGroup(Long groupId){
        Map<String,Object> map = new HashMap<>();
        CommonGroup commonGroup = groupsService.getGroupByGroupId(groupId);
        if(commonGroup!=null){
            return getGroupMap(commonGroup);
        }
        return map;
    }

    public List<Map<String,Object>> getGroupList(Long groupId){
        List<CommonGroup> commonGroups = groupsService.getGroupsByGroupId(groupId);
        List<Map<String,Object>> list = new ArrayList<>();
        if(commonGroups!=null && commonGroups.size()>0){
            commonGroups.forEach(commonGroup -> {
                list.add(getGroupMap(commonGroup));
            });
        }
        return list;
    }

    public Map<String,Object> getGroupByPwd(String pwd) throws Exception{
        Map<String,Object> map = new HashMap<>();
        Date time=new Date(System.currentTimeMillis()-30*60*1000);
        CommonGroup commonGroup = groupsService.getGroupByPwd(pwd,time);
        if(commonGroup!=null){
            return getGroupMap(commonGroup);
        }
        return map;
    }

    private Map<String,Object> getGroupMap(CommonGroup commonGroup){
        Map<String,Object> map = new HashMap<>();
        GroupInfoDto groupInfoDto = new GroupInfoDto();
        UserInfoAndHeadImg userInfoAndHeadImg = new UserInfoAndHeadImg();
        groupInfoDto.setGroupId(commonGroup.getGroupId());
        groupInfoDto.setGroupName(commonGroup.getGroupName());
        if(StringUtils.isNotBlank(commonGroup.getUserId())){
            userInfoAndHeadImg = getGroupUserInfo(commonGroup.getId(),commonGroup.getUserId());
        }
        map.put("total",getTotal(commonGroup.getId()));
        map.put("groupInfo",groupInfoDto);
        map.put("userInfo",userInfoAndHeadImg);
        return map;
    }

    private UserInfoAndHeadImg getGroupUserInfo(String groupId,String userId){
        UserInfoAndHeadImg userInfoAndHeadImg = userAction.getUserInfoAndHeadImg(userId);
        if(userInfoAndHeadImg!=null){
            String name = groupUserService.getUserNameByGroupId(userId, groupId);
            if(StringUtils.isNotBlank(name)){
                userInfoAndHeadImg.setNickname(name);
            }
        }
        return userInfoAndHeadImg;
    }

    private Integer getTotal(String groupId){
        CommonGroupUser commonGroup = new CommonGroupUser();
        commonGroup.setGroupId(groupId);
        try {
            Integer total = groupUserService.getCount(commonGroup);
            return total==null?0:total;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    private GroupInfoDto createGroupInfoDto(CommonGroup commonGroup){
        GroupInfoDto groupInfoDto = new GroupInfoDto();
        groupInfoDto.setGroupId(commonGroup.getGroupId());
        groupInfoDto.setGroupName(commonGroup.getGroupName());
        groupInfoDto.setUserId(commonGroup.getUserId());
        return groupInfoDto;
    }

    public GroupInfoResponseDto whetherExisUserInGroup(Long groupId,String userId){
        CommonGroup commonGroup = groupsService.getGroupByGroupId(groupId);
        GroupInfoResponseDto groupInfoResponseDto = new GroupInfoResponseDto();
        groupInfoResponseDto.setGroupId(groupId);
        if(commonGroup!=null){
            CommonGroupUser commonGroupUser = new CommonGroupUser();
            commonGroupUser.setGroupId(commonGroup.getId());
            commonGroupUser.setUserId(userId);
            groupInfoResponseDto.setWetherExist(checkCount(groupUserService.getCount(commonGroupUser)));
        }
        return groupInfoResponseDto;
    }

    public Map<String,Object> getGroup(Long groupId,String userId){
        GroupInfoDto groupInfoDto = null;
        Map<String,Object> map = new HashMap<>();
        CommonGroup commonGroup = groupsService.getGroupByGroupId(groupId);
        Integer total = 0;
        if(commonGroup!=null){
            groupInfoDto = createGroupInfoDto(commonGroup);
            total = getTotal(commonGroup.getId());
            map.put("userInfo",getGroupUserInfo(commonGroup.getId(),userId));
        }
        map.put("total",total);
        map.put("groupInfo",groupInfoDto);
        return map;
    }

    private Boolean checkCount(Integer count){
        return count!=null && count>0;
    }

    public Map<String,Object> getGroupsByUserId(String userId){
        List<CommonGroup> commonGroups = groupsService.getCommonGroupMapper().queryGroupsByUserId(userId,null);
        Map<String,Object> map = new HashMap<>();
        List<Map<String,Object>> groupInfoDtos = new ArrayList<>();
        Map<String,Object> userInfo = new HashMap<>();
        if(commonGroups!=null && commonGroups.size()>0){
            List<String> userIds = new ArrayList<>();
            commonGroups.forEach(commonGroup -> {
                if(StringUtils.isNotBlank(commonGroup.getUserId())){
                    groupInfoDtos.add(getGroupAnTotal(commonGroup));
                    userIds.add(commonGroup.getUserId());
                }
            });
            if(userIds.size()>0){
                userInfo = userAction.getUsersAndHeadImg(userIds);
            }
        }
        map.put("userInfo",userInfo);
        map.put("list",groupInfoDtos);
        return map;
    }

    private Map<String,Object> getGroupAnTotal(CommonGroup commonGroup){
        Map<String,Object> map = new HashMap<>();
        map.put("groupInfo",createGroupInfoDto(commonGroup));
        map.put("total",getTotal(commonGroup.getId()));
        return map;
    }

    public void addMembers(Long groupId,String... userId) throws Exception{
        CommonGroup commonGroup = groupsService.getGroupByGroupId(groupId);
        if(commonGroup==null){
            throw new RuntimeException("所进的群不存在");
        }
        //将群组信息保存到groupChat中
        for(String u:userId) {
            if(getGroupUser(commonGroup.getId(),u)==null){
                messagePushAction.pushIdToGroup(groupId,u);
                addGroupUser(commonGroup.getId(),u,false);
            }
        }
    }

    public String addMember(Long groupId,String userId) throws Exception{
        CommonGroup commonGroup = groupsService.getGroupByGroupId(groupId);
        if(commonGroup==null){
            return "所进的群不存在";
        }
        //将群组信息保存到groupChat中
        if(getGroupUser(commonGroup.getId(),userId)==null) {
            messagePushAction.pushIdToGroup(groupId,userId);
            return addGroupUser(commonGroup.getId(),userId,false)?null:"添加异常";
        }else {
            return "该用户已加入该群中";
        }
    }

    public CommonGroupUser getGroupUser(String groupId, String userId){
        return groupUserService.getWzCommonGroupChatByGroupId(userId,groupId);
    }



    public UserInfoAndHeadImg getAdminUserDataByGroupId(Long groupId){
        CommonGroup commonGroup = groupsService.getGroupByGroupId(groupId);
        if(commonGroup==null || StringUtils.isEmpty(commonGroup.getUserId())){
            return null;
        }
        return userAction.getUserInfoAndHeadImg(commonGroup.getUserId());
    }

    public void sendMessageToGroupAllUser(SendMessageToGroupChatUsersDto dto) {
        CommonGroup commonGroup = groupsService.getGroupByGroupId(Long.parseLong(dto.getId()));
        if(commonGroup!=null){
            List<String> userList=groupUserService.getUserIdsByGroupId(commonGroup.getId());
            if(!userList.isEmpty()){
                for (String userId:userList){
                    if(!userId.equals(dto.getUserId())) {
                        try {
                            messagePushAction.sendMessageAlias(MessageTypeEnum.USER_TYPE,dto.getMsg(), dto.getTitle(), userId, dto.getPushParams(), dto.getDocType());
                        }catch (Exception e){
                            logger.error(e.getMessage(),e);
                        }
                    }
                }
            }
        }
    }
}
