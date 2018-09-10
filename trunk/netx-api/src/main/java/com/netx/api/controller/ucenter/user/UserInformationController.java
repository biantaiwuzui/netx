package com.netx.api.controller.ucenter.user;

import com.netx.api.controller.BaseController;
import com.netx.common.redis.model.UserGeo;
import com.netx.common.user.dto.common.CommonUserBaseInfoDto;
import com.netx.common.user.dto.information.SelectUserListByDetailRequestDto;
import com.netx.common.user.dto.information.UpdateUserBaseInfoRequest;
import com.netx.common.user.dto.information.UpdateUserProfileRequest;
import com.netx.common.user.dto.information.UpdateUserSettingRequestDto;
import com.netx.common.user.enums.RegularExpressionEnum;
import com.netx.common.user.model.UserSynopsisData;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.ucenter.biz.user.UserPhotoAction;
import com.netx.ucenter.biz.user.UserProfileAction;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.model.user.UserProfile;
import com.netx.ucenter.vo.response.SelectUserSettingResponseDto;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "用户信息模块")
@RestController
@RequestMapping("/api/userInformation/")
public class UserInformationController extends BaseController{
    @Autowired
    private UserAction userAction;
    @Autowired
    private UserProfileAction userProfileAction;
    @Autowired
    private UserPhotoAction userPhotoAction;

    private Logger logger = LoggerFactory.getLogger(UserInformationController.class);

    @ResponseBody
    @GetMapping("selectUserBaseInfoByUserId")
    @ApiOperation(value ="根据用户id列表查询用户基础信息(包含头像)")
    public JsonResult selectUserBaseInfoByUserId(@RequestBody List<String> userIdList){
        if(userIdList.isEmpty()){
            return JsonResult.fail("用户id列表不能为空");
        }
        try{
            List<CommonUserBaseInfoDto> userBaseInfoList = userAction.selectUserBaseInfoByUserId(userIdList);
            return JsonResult.success().addResult("list",userBaseInfoList);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail(e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("selectUserProfileByUserId")
    @ApiOperation(value = "根据用户id列表查询用户详情信息")
    public JsonResult selectUserProfileByUserId(@RequestBody List<String> userIdList){
        if(userIdList.isEmpty()){
            return JsonResult.fail("用户id列表不能为空");
        }
        try{
            List<UserProfile> userProfileList = userProfileAction.selectUserProfileByUserId(userIdList);
            return JsonResult.success().addResult("list",userProfileList);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("出现异常");
        }
    }

    @ResponseBody
    @GetMapping("selectProfile")
    @ApiOperation(value = "根据用户id查询用户详情信息")
    public JsonResult selectProfile(@RequestParam("userId") String userId,HttpServletRequest request){
        if(StringUtils.isEmpty(userId)){
            try {
                userId = getUserId(request);
                if(StringUtils.isEmpty(userId)){
                    return JsonResult.fail("用户id列表不能为空");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try{
            Map<String,Object> map = new HashMap<>();
            map.put("bean",userAction.changeUserByVerify(userId));
            map.put("profile",userProfileAction.getUserProfileByUserId(userId));
            map.put("photo",userPhotoAction.selectUserPhotos(userId));
            return successResult(map);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("出现异常");
        }
    }

    @ResponseBody
    @PostMapping("selectUserListByDetail")
    @ApiOperation(value ="根据各个详细条件分页查询用户id列表（主要用在网友搜索）",
            notes="1、string类型的传递空串或传递null或不传值都表示不限。integer类型的最小值最大值同时为0表示不限，最小值不为0最大值为0表示最大值没有上限，" +
                    "例如：①minAge=0, maxAge=0，表示年龄不限；②minAge=60，maxAge=0, 表示60岁以上<br>" +
                    "2、关于在线时间，单位：分钟级，例如：若为“10分钟前”，则传递10分钟。若在线时间不限，则传递 null 或 不传值<br>" +
                    "3、关于距离，单位:km，若不限，则传递 null 或不传值")
    public JsonResult selectUserListByDetail(@Valid @RequestBody SelectUserListByDetailRequestDto dto, BindingResult bindingResult,HttpServletRequest request) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        UserGeo userGeo = new UserGeo();
        try {
            userGeo = getGeoFromRequest(request);
            if(StringUtils.isEmpty(userGeo.getUserId())){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }catch (Exception e){
            return JsonResult.fail(e.getMessage());
        }
        try{
            List<UserSynopsisData> list = userAction.searchUserListByDetail(dto,userGeo.getUserId(),userGeo.getLon(),userGeo.getLat());
            return JsonResult.success().addResult("list",list);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("出现异常");
        }
    }

    @ResponseBody
    @PostMapping("updateUserBaseInfo")
    @ApiOperation(value ="编辑用户基本信息", notes = "昵称、性别、出生年月日")
    public JsonResult updateUserBaseInfo(@Valid @RequestBody UpdateUserBaseInfoRequest dto, BindingResult bindingResult,HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(dto.getNickname()!=null && !dto.getNickname().matches(RegularExpressionEnum.NICKNAME.getValue())){
            return JsonResult.fail("昵称格式不正确");
        }
        if(StringUtils.isEmpty(dto.getUserId())){
            try {
                dto.setUserId(getUserId(dto.getUserId(),request));
                if(StringUtils.isEmpty(dto.getUserId())){
                    return JsonResult.fail("用户id不能为空");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try{
            return super.getResult(userAction.updateUserBaseInfo(dto),"编辑操作失败");
        }catch(Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail( "编辑操作出现异常");
        }
    }
    @ResponseBody
    @PostMapping("checkUserSameName")
    @ApiOperation(value = "检查昵称是否存在同名", notes="返回值：true，存在同名；false，不存在同名")
    public JsonResult checkUserSameName(@RequestParam("nickname") String nickname){
        if(!nickname.matches(RegularExpressionEnum.NICKNAME.getValue())){
            return JsonResult.fail("昵称格式不正确");
        }
        try{
            return JsonResult.success().addResult("result", userAction.checkUserSameName(nickname)!=null);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询操作出现异常");
        }
    }
    @ResponseBody
    @PostMapping("updateUserProfile")
    @ApiOperation(value="编辑用户详情信息")
    public JsonResult updateUserProfile(@Valid @RequestBody UpdateUserProfileRequest dto, BindingResult bindingResult,HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(StringUtils.isEmpty(dto.getUserId())){
            try {
                dto.setUserId(getUserId(dto.getUserId(),request));
                if(StringUtils.isEmpty(dto.getUserId())){
                    return JsonResult.fail("用户id不能为空");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try{
            if(dto.getIncome()!=null && dto.getMaxIncome()!=null && dto.getIncome()>dto.getMaxIncome()){
                return JsonResult.fail("最低收入不能低于最高收入");
            }
            return super.getResult(userProfileAction.updateUserProfile(dto),"编辑操作失败");
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("编辑操作出现异常");
        }
    }

    @ResponseBody
    @PostMapping("/updateUserSetting")
    @ApiOperation(value="编辑用户设置", notes = "不用设置的，不要设值")
    public JsonResult updateUserSetting(@Valid @RequestBody UpdateUserSettingRequestDto dto, BindingResult bindingResult,HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(StringUtils.isEmpty(dto.getUserId())){
            try {
                dto.setUserId(getUserId(dto.getUserId(),request));
                if(StringUtils.isEmpty(dto.getUserId())){
                    return JsonResult.fail("用户id不能为空");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try{
            return super.getResult(userAction.updateUserSetting(dto),"操作失败");
        }catch(Exception e){
            logger.error("编辑用户设置"+e.getMessage(), e);
            return JsonResult.fail("操作出现异常");
        }
    }

    @ResponseBody
    @GetMapping("selectUserSetting")
    @ApiOperation(value="查看用户设置")
    public JsonResult selectUserSetting(String userId,HttpServletRequest request){
        if(StringUtils.isEmpty(userId)){
            try {
                userId = getUserId(userId,request);
                if(StringUtils.isEmpty(userId)){
                    return JsonResult.fail("用户id不能为空");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try{
            SelectUserSettingResponseDto selectUserSettingResponseDto = userAction.selectUserSetting(userId);
            return JsonResult.success().addResult("result",selectUserSettingResponseDto);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("操作出现异常");
        }
    }
}
