package com.netx.boss.web.controller.ucenter;

import com.netx.common.user.dto.integration.SelectUserEducationResponse;
import com.baomidou.mybatisplus.plugins.Page;
import com.netx.common.user.dto.integration.*;
import com.netx.common.user.dto.userManagement.OperateUserVerifyRequest;
import com.netx.common.user.dto.userManagement.OperateSystemBlacklistRequestDto;
import com.netx.common.user.dto.userManagement.SelectUserInSystemBlacklistRequest;
import com.netx.common.user.dto.userManagement.SelectUserVerifyResponse;
import com.netx.common.user.enums.VerifyStatusEnum;
import com.netx.common.user.util.AddImgUrlPreUtil;
import com.netx.common.vo.business.*;
import com.netx.ucenter.vo.request.EditUserRequestDto;
import com.netx.common.vo.common.*;
import com.netx.fuse.biz.ucenter.RouterFuseAction;
import com.netx.ucenter.biz.user.*;
import com.netx.ucenter.model.user.UserProfession;
import com.netx.ucenter.model.user.UserProfile;
import com.netx.ucenter.vo.request.QueryUserVerifyListRequestDto;
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
import java.util.Map;

@Api(description = "Boss用户接口")
@RestController
public class UserController {


    private Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    UserAdminAction userAdminAction;

    @Autowired
    UserAction userAction;

    @Autowired
    AddImgUrlPreUtil addImgUrlPreUtil;

    @Autowired
    UserProfileAction userProfileAction;

    @Autowired
    UserPhotoAction userPhotoAction;

    @Autowired
    UserEducationAction userEducationAction;

    @Autowired
    UserProfessionAction userProfessionAction;

    @Autowired
    UserInterestAction userInterestAction;

    @Autowired
    RouterFuseAction routerFuseAction;

    @Autowired
    UserVerifyAction userVerifyAction;

    @Autowired
    SystemBlacklistAction systemBlacklistAction;

    @ApiOperation(value = "查询用户列表")
    @PostMapping(value = "/getUserList")
    JsonResult getUserList(@Valid @RequestBody GetUserListRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            Map map = userAction.getUserListAndCount(request);
            if (map != null) {
                return JsonResult.success().addResult("data", map);
            }
            return JsonResult.fail("查询用户列表失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return JsonResult.fail("erro查询用户列表失败！");
        }
    }

//    @ApiOperation(value = "查询用户详情")
//    @PostMapping(value = "/selectProfile")
//    JsonResult selectProfile(@Valid @RequestBody CommonUserIdRequestDto request, BindingResult bindingResult){
//        if( bindingResult.hasErrors()){
//            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
//        }
//        try {
//            JsonResult jsonResult = JsonResult.success();
//            Map<String,Object> map = new HashMap<>();
//            map.put("bean",userAction.changeUserByVerify(request.getUserId()));
//            map.put("profile",userProfileAction.getUserProfileByUserId(request.getUserId()));
//            map.put("photo",userPhotoAction.selectUserPhotos(request.getUserId()));
//            map.put("education",userEducationAction.selectUserEducation(request.getUserId()));
//            map.put("profession",userProfessionAction.selectUserProfession(request.getUserId()));
//            map.put("interest",userInterestAction.selectUserInterest(request.getUserId()));
//            jsonResult.setResult(map);
//            return jsonResult;
//        }catch (Exception e){
//            logger.error(e.getMessage(), e);
//            e.printStackTrace();
//            return JsonResult.fail("出现异常");
//        }
//    }

    @ApiOperation(value = "根据Id删除用户")
    @PostMapping(value = "/deleteUserById")
    JsonResult deleteUserById(@Valid @RequestBody CommonUserIdRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            Boolean flag = routerFuseAction.deleteUser(request.getUserId());
            return flag ? JsonResult.success("注销成功") : JsonResult.fail("注销失败");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("注销异常");
        }
    }

    @ApiOperation(value = "获取用户详细信息")
    @PostMapping(value = "/getUserProfile")
    JsonResult getUserProfile(@Valid @RequestBody CommonUserIdRequestDto request) {
        if (StringUtils.isEmpty(request.getUserId())) {
            return JsonResult.fail("userId不能为空");
        }
        try {
            UserProfile userProfile = userProfileAction.getUserProfileByUserId(request.getUserId());
            if (userProfile != null) {
                return JsonResult.success().addResult("userProfile", userProfile);
            }
            return JsonResult.fail("获取用户详细信息失败");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("获取用户详细信息异常");
        }
    }

    @ApiOperation(value = "修改用户详细信息")
    @PostMapping(value = "/updateUserDetails")
    JsonResult updateUserDetails(@Valid @RequestBody UpdateUserDetailsRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            request.setUpdateUserId(userAdminAction.queryAdmin(request.getUpdateUserId()).getId());
            Boolean userProfile = userProfileAction.updateUserDetails(request);
            if (userProfile) {
                return JsonResult.success("修改用户成功");
            }
            return JsonResult.fail("修改用户详细信息失败");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("修改用户详细信息异常");
        }
    }

    @ApiOperation(value = "修改用户信息")
    @PostMapping(value = "/editUser")
    JsonResult editUser(@Valid @RequestBody EditUserRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            Boolean bl = userAction.editUserById(request);
            userAction.getUserService().checkUser(request.getId());
            if (bl) {
                return JsonResult.success("修改用户信息成功");
            } else {
                return JsonResult.fail("修改用户信息失败");
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("修改用户信息异常");
        }
    }

    @ApiOperation(value = "获取用户教育背景")
    @PostMapping(value = "/getUserEducation")
    JsonResult getUserEducation(@Valid @RequestBody CommonUserIdRequestDto request) {
        if (StringUtils.isEmpty(request.getUserId())) {
            return JsonResult.fail("userId不能为空");
        }
        try {
            SelectUserEducationResponse selectUserEducationResponse = userEducationAction.selectUserEducation(request.getUserId());
            if (selectUserEducationResponse != null) {
                return JsonResult.success().addResult("userEducation", selectUserEducationResponse);
            }
            return JsonResult.fail("获取用户教育背景失败");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail(request.getUserId() + "获取用户教育背景异常");
        }
    }

    @ApiOperation(value = "修改用户教育背景")
    @PostMapping(value = "/editUserEducationInfo")
    JsonResult editUserEducationInfo(@Valid @RequestBody UpdateUserEducationRequestDto request) {
        if (StringUtils.isEmpty(request.getId())) {
            return JsonResult.fail("userId不能为空");
        }
        try {
            request.setUpdateUserId(userAdminAction.queryAdmin(request.getUpdateUserId()).getId());
            Boolean a = userEducationAction.editUserEducationInfo(request);
            if (a) {
                return JsonResult.success("修改用户教育背景成功");
            }
            return JsonResult.fail("修改用户教育背景失败");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail(request.getId() + "修改用户教育背景异常");
        }
    }

    @ApiOperation(value = "删除用户教育背景")
    @PostMapping(value = "/deleteUserEducation")
    JsonResult deleteUserEducation(@Valid @RequestBody DeleteUserEducationRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            if (userEducationAction.deleteUserEducation(request.getId(), userAdminAction.queryAdmin(request.getUpdateUserId()).getId())) {
                return JsonResult.success("删除用户教育背景成功");
            }
            return JsonResult.fail("删除用户教育背景失败");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail(request.getId() + "删除用户教育背景异常");
        }
    }

    @ApiOperation(value = "获取用户工作经验")
    @PostMapping(value = "/getUserProfession")
    JsonResult getUserProfession(@Valid @RequestBody GetUserProfessionRequestDto request) {
        if (StringUtils.isEmpty(request.getUserId())) {
            return JsonResult.fail("userId不能为空");
        }
        try {
            Page<UserProfession> page = new Page<UserProfession>(request.getCurrentPage(), request.getSize());
            SelectUserProfessionResponse response = userProfessionAction.selectUserProfession(request.getUserId(), page);
            return JsonResult.success().addResult("result", response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询操作出现异常");
        }
    }

    @ApiOperation(value = "修改用户工作经验信息")
    @PostMapping(value = "/editUserWorkExperience")
    JsonResult editUserWorkExperience(@Valid @RequestBody EditUserWorkExperienceRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            request.setUpdateUserId(userAdminAction.queryAdmin(request.getUpdateUserId()).getId());
            if (userProfessionAction.EditUserWorkExperience(request)) {
                return JsonResult.success("修改用户工作经验信息成功");
            }
            return JsonResult.fail("修改用户工作经验信息失败");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail(request.getId() + "修改用户工作经验信息异常");
        }
    }

    @ApiOperation(value = "根据详情id删除工作经历详情")
    @PostMapping(value = "/deleteUserProfessionDetail")
    JsonResult deleteUserProfessionDetail(@Valid @RequestBody CommonIdRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            Boolean flag = userProfessionAction.deleteUserProfessionDetail(requestDto.getId());
            return flag ? JsonResult.success("删除工作经历详情成功") : JsonResult.fail("删除工作经历详情失败");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("删除工作经历详情异常");
        }
    }

    @ApiOperation(value = "新增工作经历的详情")
    @PostMapping(value = "/insertUserProfessionDetail")
    JsonResult insertUserProfessionDetail(@Valid @RequestBody InsertUserProfessionDetailRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            Boolean flag = userProfessionAction.addOrEditUserEducationDetail(request);
            return flag ? JsonResult.success("增加工作经历详情成功") : JsonResult.fail("增加工作经历详情失败");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("增加工作经历详情异常");
        }
    }

    @ApiOperation(value = "获取用户兴趣爱好")
    @PostMapping(value = "/selectUserInterest")
    JsonResult selectUserInterest(@Valid @RequestBody CommonUserIdRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            SelectUserInterestResponse selectUserInterestResponse = userInterestAction.selectUserInterest(request.getUserId());
            if (selectUserInterestResponse != null) {
                return JsonResult.success().addResult("result", selectUserInterestResponse);
            }
            return JsonResult.fail("获取用户兴趣爱好失败");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("获取用户兴趣爱好异常");
        }
    }

    @ApiOperation(value = "删除用户兴趣爱好")
    @PostMapping(value = "/deleteUserInterestDetail")
    JsonResult deleteUserInterestDetail(@Valid @RequestBody CommonIdAndUserIdRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            Boolean flag = userInterestAction.deleteUserInterestDetail(request.getId(), request.getUserId());
            return flag ? JsonResult.success("删除用户爱好成功") : JsonResult.fail("删除用户爱好失败");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("删除用户爱好异常");
        }
    }

    @ApiOperation(value = "增加用户兴趣爱好")
    @PostMapping(value = "/insertUserInterestDetail")
    JsonResult insertUserInterestDetail(@Valid @RequestBody InsertUserInterestDetailRequest request, BindingResult bindingResult, HttpServletRequest httpRequest) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            Boolean flag = userInterestAction.addOrEditUserEducationDetail(request);
            return flag ? JsonResult.success("增加用户兴趣爱好成功") : JsonResult.fail("增加用户兴趣爱好失败");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("增加用户兴趣爱好异常");
        }
    }

    @ApiOperation("查询用户认证列表")
    @PostMapping("selectUserVerifyList")
    public JsonResult selectUserVerifyList(@Valid @RequestBody QueryUserVerifyListRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            Map<String, Object> list = userVerifyAction.selectUserVerifyList(request);
            return JsonResult.success().addResult("list", list);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询出现异常");
        }
    }

    @ApiOperation("根据id查询用户认证信息")
    @PostMapping("selectUserVerify")
    JsonResult selectUserVerify(@Valid @RequestBody CommonUserIdRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            SelectUserVerifyResponse response = userVerifyAction.selectUserVerify(request.getUserId());
            return JsonResult.success().addResult("result", response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询操作出现异常");
        }
    }

    @ApiOperation(value = "根据认证id和用户id审核用户认证内容操作",
            notes = "支持批量通过认证操作且认证用户必须是同一对象，多个认证id以逗号隔开，若某认证里的用户id与传递的用户id不符合，会出现异常")
    @PostMapping("operateUserVerify")
    JsonResult operateUserVerify(@Valid @RequestBody OperateUserVerifyRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if (request.getStatus() == VerifyStatusEnum.REJECTIVE_AUTHENTICATION.getValue()) {
            if (request.getReason() == null) {
                return JsonResult.fail("拒绝原因不能为空");
            }
        }
        try {
            Boolean flag = userVerifyAction.operateUserVerify(request);
            return flag ? JsonResult.success("审核操作成功") : JsonResult.fail("审核操作失败");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return JsonResult.fail("认证操作异常");
        }
    }

    @ApiOperation(value = "根据网号模糊并分页查询系统黑名单/白名单里的用户")
    @PostMapping(value = "/queryUserInSystemBlacklist")
    public JsonResult queryUserInSystemBlacklist(@Valid @RequestBody SelectUserInSystemBlacklistRequest requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            Map map = systemBlacklistAction.selectUserInSystemBlacklist(requestDto);
            return JsonResult.success().addResult("data", map);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询出现异常");
        }
    }

    public JsonResult getResult(boolean success, String msg) {
        if (success) {
            return JsonResult.success();
        } else {
            return JsonResult.fail(msg);
        }
    }

    @ResponseBody
    @ApiOperation(value = "根据用户id和操作类型对用户进行拉黑或释放处理", notes = "operateType:操作类型（0：释放，1：拉黑）")
    @PostMapping(value = "operateSystemBlacklist")
    public JsonResult operateSystemBlacklist(@Valid @RequestBody OperateSystemBlacklistRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            return this.getResult(systemBlacklistAction.operateSystemBlacklist(requestDto.getUserId(), userAdminAction.queryAdmin(requestDto.getCreateUserName()).getRealName(), requestDto.getReason(), requestDto.getOperateType()), "拉黑或释放操作失败，可能已被拉黑或释放");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("操作出现异常");
        }
    }
}
//    public int updateUserDetails(updateUserRequestDto updateUserRequestDto){
//
//    }