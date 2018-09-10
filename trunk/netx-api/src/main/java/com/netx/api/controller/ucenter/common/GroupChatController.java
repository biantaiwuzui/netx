package com.netx.api.controller.ucenter.common;

import com.netx.api.controller.BaseController;
import com.netx.common.common.vo.message.*;
import com.netx.common.redis.model.UserGeo;
import com.netx.common.vo.common.SendMessageToGroupChatUsersDto;
import com.netx.ucenter.biz.common.GroupAction;
import com.netx.ucenter.model.common.CommonGroup;
import com.netx.ucenter.service.common.CommonServiceProvider;
import com.netx.ucenter.vo.request.*;
import com.netx.utils.json.ApiCode;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;

/**
 * Create on 17-11-14
 *
 * @author wongloong
 */
@RestController
@Api(description = "群组相关接口")
@RequestMapping("/api/groupChat")
public class GroupChatController extends BaseController{
    private Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private GroupAction groupAction;

    @Autowired
    private CommonServiceProvider commonServiceProvider;

   /* @PostMapping("/add")
    @ApiOperation("添加群组人员")
    public JsonResult add(@RequestBody @Valid GroupChatAddRequest request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            String result = groupAction.addMember(request.getGroupId(),request.getUserId());
            return super.getResult(result==null,result);
        } catch (Exception e) {
            logger.error("添加群组成员异常："+e.getMessage(), e);
            return JsonResult.fail("添加群组成员异常");
        }
    }*/


    @ApiOperation("删除某一群组成员")
    @PostMapping("/deleteUserFromGroup")
    public JsonResult delete(@RequestBody @Valid GroupChatRequest request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            String result = groupAction.deleteGroupUser(request.getGroupId(),request.getUserId());
            return super.getResult(result==null,result);
        } catch (Exception e) {
            logger.error("删除群组成员异常："+e.getMessage(), e);
            return JsonResult.fail("解散群组成员异常");
        }
    }

    @ApiOperation("删除某一群组成员（批量）")
    @PostMapping("/deleteUsersFromGroup")
    public JsonResult deleteUsers(@RequestBody @Valid GroupMembersMultipleDto request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            String result = groupAction.deleteGroupUsers(request.getGroupId(),request.getUserId());
            return super.getResult(result==null,result);
        } catch (Exception e) {
            logger.error("删除群组成员异常："+e.getMessage(), e);
            return JsonResult.fail("解散群组成员异常");
        }
    }

    @ApiOperation("解散群组")
    @PostMapping("/dissolve")
    public JsonResult dissolve(Long groupId, HttpServletRequest request) {
        if(groupId==null){
            return JsonResult.fail("群id不能为空");
        }
        String userId=null;
        try {
            userId = getUserId(request);
            if(userId==null){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
           String result = groupAction.deleteGroup(groupId,userId);
            return super.getResult(result==null,result);
        } catch (Exception e) {
            logger.error("解散群组异常："+e.getMessage(), e);
            return JsonResult.fail("解散群组异常");
        }
    }

    @ApiOperation("获取群组成员列表,id为群id")
    @GetMapping("/list")
    public JsonResult list(Long groupId,HttpServletRequest request) {
        if (groupId==null) {
            return JsonResult.fail("群id不能为空");
        }
        UserGeo userGeo = new UserGeo();
        try {
            userGeo=getGeoFromRequest(request);
        }catch (Exception e){
            logger.warn(e.getMessage());
        }
        try {
            return JsonResult.successJsonResult(groupAction.getListByGroupId(groupId,userGeo.getLon(),userGeo.getLat(),userGeo.getUserId()));
        } catch (Exception e) {
            logger.error("获取群组列表异常", e);
            return JsonResult.fail("获取群组列表异常");
        }
    }

    @ApiOperation("通过密码获取群组信息")
    @PostMapping("/listByPwd")
    public JsonResult listByPwd(@RequestBody @Valid GroupChatQueryByPwdRequest request) {
        if(StringUtils.isEmpty(request.getPwd())){
            return JsonResult.fail("密码不能为空");
        }
        try {
            return JsonResult.successJsonResult(groupAction.getGroupByPwd(request.getPwd()));
        } catch (Exception e) {
            logger.error("获取群组异常："+e.getMessage(), e);
            return JsonResult.fail("获取群组异常");
        }
    }

    @ApiOperation(value = "创建群组")
    @PostMapping("/createGroup")
    public JsonResult createGroup(@RequestBody GroupDto groupDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            if(groupDto.getType()!=null && groupDto.getType().equals("faceCreate")) {
                Date time = new Date(System.currentTimeMillis()- 30 * 60 * 1000);
                int count = commonServiceProvider.getGroupService().getCount( groupDto.getTag(), time);
                if (count>0) {
                    return JsonResult.fail("半小时内已经存在此密码创建的群组");
                }
            }
            Long group = groupAction.createGroup(groupDto);
            GroupInfoDto groupInfoDto=new GroupInfoDto();
            groupInfoDto.setGroupId(group);
            groupInfoDto.setGroupName(groupDto.getGroupName());
            groupInfoDto.setUserId(groupDto.getUserId());
            return JsonResult.success().addResult("result",groupInfoDto);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("创建群组异常");
        }
    }

    @ApiOperation(value = "修改群信息")
    @PostMapping("/modifyGroup")
    public JsonResult modifyGroup(@Valid @RequestBody GroupInfoRequest groupInfoDto, BindingResult bindingResult,HttpServletRequest request) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(StringUtils.isEmpty(groupInfoDto.getUserId())){
            try{
                groupInfoDto.setUserId(getUserId(request));
                if(StringUtils.isEmpty(groupInfoDto.getUserId())){
                    return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
                }
            }catch (Exception e){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }
        try {
            return super.getResult(commonServiceProvider.getGroupService().updateGroup(groupInfoDto.getGroupId(),groupInfoDto.getName(),groupInfoDto.getUserId()),"修改成功","修改失败");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("修改群信息异常");
        }
    }

    @ApiOperation(value = "修改群备注")
    @PostMapping("/modifyGroupNote")
    public JsonResult modifyGroupNote(@Valid @RequestBody GroupInfoRequest groupInfoDto, BindingResult bindingResult, HttpServletRequest request) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(StringUtils.isEmpty(groupInfoDto.getUserId())){
            try{
                groupInfoDto.setUserId(getUserId(request));
                if(StringUtils.isEmpty(groupInfoDto.getUserId())){
                    return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
                }
            }catch (Exception e){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }
        try {
            CommonGroup commonGroup = commonServiceProvider.getGroupService().getGroupByGroupId(groupInfoDto.getGroupId());
            if(commonGroup==null){
                return JsonResult.fail("此群已不存在");
            }
            return super.getResult(commonServiceProvider.getGroupUserService().updateName(commonGroup.getId(),groupInfoDto.getName(),groupInfoDto.getUserId()),"修改成功","修改失败");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("修改成员备注异常");
        }
    }

    @ApiOperation(value = "添加群员")
    @PostMapping("/addMembers")
    public JsonResult addMembers(@RequestBody GroupMembersDto groupMembersDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            groupAction.addMember(groupMembersDto.getGroupId(),groupMembersDto.getUserId());
            return JsonResult.success();
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("添加群员异常");
        }
    }

    @ApiOperation(value = "添加群员(批量)")
    @PostMapping("/addMembersMultiple")
    public JsonResult addMembersMultiple(@RequestBody GroupMembersMultipleDto groupMembersMultipleDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            groupAction.addMembers(groupMembersMultipleDto.getGroupId(),groupMembersMultipleDto.getUserId());
            return JsonResult.success();
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("批量添加群员异常");
        }
    }

    @ApiOperation(value = "根据用户获取用户群组")
    @PostMapping("/findUserGroup")
    public JsonResult findUserGroup(@RequestBody GroupUserDto groupUserDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            return JsonResult.successJsonResult(groupAction.getGroupsByUserId(groupUserDto.getUserId()));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取群组异常");
        }
    }

    @ApiOperation(value = "根据群组ID与用户ID获取群信息与用户信息")
    @PostMapping("/findGroupAndUserId")
    public JsonResult findGroupAndUserId(@Valid @RequestBody GroupChatRequest dto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            return JsonResult.successJsonResult(groupAction.getGroup(dto.getGroupId(),dto.getUserId()));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取群组信息异常");
        }
    }

    @ApiOperation(value = "根据群组ID获取群组信息")
    @PostMapping("/findGroup")
    public JsonResult findGroup(@RequestBody GroupInfoDto groupInfoDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            return JsonResult.successJsonResult(groupAction.getGroup(groupInfoDto.getGroupId()));
        } catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取群组信息异常");
        }
    }

    @ApiOperation(value = "根据群组ID模糊获取群组信息")
    @PostMapping("/findGroups")
    public JsonResult findGroups(@RequestParam("id") Long groupId) {
        try {
            return JsonResult.success().addResult("list",groupAction.getGroupList(groupId));
        } catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取群组信息异常");
        }
    }

    @ApiOperation(value = "判断用户是否存在指定的组里面")
    @PostMapping("/whetherExisUserInGroup")
    public JsonResult whetherExisUserInGroup(@RequestBody GroupMembersDto groupMembersDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            GroupInfoResponseDto groupInfoResponseDto= groupAction.whetherExisUserInGroup(groupMembersDto.getGroupId(),groupMembersDto.getUserId());
            return JsonResult.success().addResult("result",groupInfoResponseDto);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("操作异常");
        }
    }

    @ApiOperation(value = "群聊接口,根据群id来实现群聊")
    @PostMapping("/sendMessageToGroupUsers")
    public JsonResult sendMessageToGroupUsers(@Valid @RequestBody SendMessageToGroupChatUsersDto request,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try{
            groupAction.sendMessageToGroupAllUser(request);
            return JsonResult.success();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("发送消息异常");
        }
    }
}
