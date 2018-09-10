package com.netx.api.controller.ucenter.user;

import com.netx.api.controller.BaseController;
import com.netx.common.user.dto.integration.*;
import com.netx.ucenter.biz.user.UserEducationAction;
import com.netx.ucenter.biz.user.UserInterestAction;
import com.netx.ucenter.biz.user.UserProfessionAction;
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
import java.util.Map;

@Api(description = "用户综合信息模块")
@RestController
@RequestMapping("/api/UserIntegration/")
public class UserIntegrationController extends BaseController{
    private Logger logger = LoggerFactory.getLogger(UserIntegrationController.class);
    @Autowired
    private UserEducationAction userEducationAction;
    @Autowired
    private UserProfessionAction userProfessionAction;
    @Autowired
    private UserInterestAction userInterestAction;

    @ResponseBody
    @GetMapping("selectIntegration")
    @ApiOperation("根据用户id查询综合信息")
    public JsonResult selectIntegration(String userId,HttpServletRequest request){
        try {
            userId=getUserId(userId,request);
        }catch (Exception e){
            return JsonResult.fail(e.getMessage());
        }
        if(StringUtils.isEmpty(userId)){
            return JsonResult.fail("用户id不能为空");
        }
        try{
            Map<String,Object> map = new HashMap<>();
            map.put("education", userEducationAction.selectUserEducation(userId));
            map.put("profession", userProfessionAction.selectUserProfession(userId,null));
            map.put("interest", userInterestAction.selectUserInterest(userId));
            return successResult(map);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail(userId+"查询操作出现异常");
        }
    }


    //------ 文化教育部分 -------
    @ResponseBody
    @PostMapping("editUserEducationLabel")
    @ApiOperation("编辑文化教育的概况")
    public JsonResult editUserEducationLabel(@Valid @RequestBody EditUserLabelRequest request, BindingResult bindingResult,HttpServletRequest httpRequest){
        if(bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(StringUtils.isEmpty(request.getUserId())){
            try {
                request.setUserId(getUserId(httpRequest));
                if(request.getUserId()==null){
                    return JsonResult.fail("用户Id不能为空");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try{
            return super.getResult(userEducationAction.editUserEducationLabel(request),"编辑操作失败");
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("编辑操作出现异常");
        }
    }


    @ResponseBody
    @PostMapping("editEducationDetail")
    @ApiOperation(value="编辑文化教育的详情")
    public JsonResult editEducationDetail(@Valid @RequestBody InsertUserEducationDetailRequest request, BindingResult bindingResult,HttpServletRequest httpRequest){
        if(bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(StringUtils.isEmpty(request.getUserId())){
            try {
                request.setUserId(getUserId(httpRequest));
                if(request.getUserId()==null){
                    return JsonResult.fail("用户Id不能为空");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try{
            return super.getResult(userEducationAction.addOrEditUserEducationDetail(request),"新增操作失败");
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("新增操作出现异常");
        }
    }

    @ResponseBody
    @PostMapping("insertUserEducationDetail")
    @ApiOperation(value="新增或编辑文化教育的详情")
    public JsonResult insertUserEducationDetail(@Valid @RequestBody InsertUserEducationDetailRequest request, BindingResult bindingResult,HttpServletRequest httpRequest){
        if(bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(StringUtils.isEmpty(request.getUserId())){
            try {
                request.setUserId(getUserId(httpRequest));
                if(request.getUserId()==null){
                    return JsonResult.fail("用户Id不能为空");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try{
            return super.getResult(userEducationAction.addOrEditUserEducationDetail(request),"操作失败");
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("操作出现异常");
        }
    }

    @ResponseBody
    @GetMapping("selectUserEducation")
    @ApiOperation("根据用户id查询文化教育")
    public JsonResult selectUserEducation(String userId,HttpServletRequest request){
        try {
            userId=getUserId(userId,request);
        }catch (Exception e){
            return JsonResult.fail(e.getMessage());
        }
        if(StringUtils.isEmpty(userId)){
            return JsonResult.fail("用户id不能为空");
        }
        try{
            SelectUserEducationResponse response = userEducationAction.selectUserEducation(userId);
            return JsonResult.success().addResult("result",response);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询操作出现异常");
        }
    }

    @ResponseBody
    @PostMapping("deleteUserEducationDetail")
    @ApiOperation("根据详情id删除文化教育详情")
    public JsonResult deleteUserEducationDetail(String id){
        if(StringUtils.isEmpty(id)){
            return JsonResult.fail("详情id不能为空");
        }
        try{
            return super.getResult(userEducationAction.deleteUserEducationDetail(id),"删除操作失败");
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("删除操作出现异常");
        }
    }

    //------ 工作经历部分 ------
    @ResponseBody
    @PostMapping("editUserProfessionLabel")
    @ApiOperation("编辑工作经历的职业")
    public JsonResult editUserProfessionLabel(@Valid @RequestBody EditUserLabelRequest request, BindingResult bindingResult,HttpServletRequest httpRequest){
        if(bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(StringUtils.isEmpty(request.getUserId())){
            try {
                request.setUserId(getUserId(httpRequest));
                if(request.getUserId()==null){
                    return JsonResult.fail("用户Id不能为空");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try{
            return super.getResult(userProfessionAction.editUserProfessionLabel(request),"编辑操作失败");
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("编辑操作出现异常");
        }
    }
    @ResponseBody
    @PostMapping("insertUserProfessionDetail")
    @ApiOperation("新增或编辑工作经历的详情")
    public JsonResult insertUserProfessionDetail(@Valid @RequestBody InsertUserProfessionDetailRequest request, BindingResult bindingResult,HttpServletRequest httpRequest){
        if(bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(StringUtils.isEmpty(request.getUserId())){
            try {
                request.setUserId(getUserId(httpRequest));
                if(request.getUserId()==null){
                    return JsonResult.fail("用户Id不能为空");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try{
            return super.getResult(userProfessionAction.addOrEditUserEducationDetail(request),"操作失败");
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("操作出现异常");
        }
    }
    @ResponseBody
    @GetMapping("selectUserProfession")
    @ApiOperation("根据用户id查询工作经历")
    public JsonResult selectUserProfession(String userId,HttpServletRequest request){
        try {
            userId=getUserId(userId,request);
        }catch (Exception e){
            return JsonResult.fail(e.getMessage());
        }
        if(StringUtils.isEmpty(userId))
            return JsonResult.fail("用户id不能为空");
        try{
            SelectUserProfessionResponse response = userProfessionAction.selectUserProfession(userId,null);
            return JsonResult.success().addResult("result",response);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询操作出现异常");
        }
    }
    @ResponseBody
    @PostMapping("deleteUserProfessionDetail")
    @ApiOperation("根据详情id删除工作经历详情")
    public JsonResult deleteUserProfessionDetail(@RequestParam("id") String id){
        if(!StringUtils.hasText(id)){
            return JsonResult.fail("详情id不能为空");
        }
        try{
            return super.getResult(userProfessionAction.deleteUserProfessionDetail(id),"删除操作失败");
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("删除操作出现异常");
        }
    }

    //------ 兴趣爱好部分 ------
    @ResponseBody
    @PostMapping("editUserInterestLabel")
    @ApiOperation("编辑兴趣爱好的概述")
    public JsonResult editUserInterestLabel(@Valid @RequestBody EditUserLabelRequest request, BindingResult bindingResult,HttpServletRequest httpRequest){
        if(bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(StringUtils.isEmpty(request.getUserId())){
            try {
                request.setUserId(getUserId(httpRequest));
                if(request.getUserId()==null){
                    return JsonResult.fail("用户Id不能为空");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try{
            return super.getResult(userInterestAction.editUserInterestLabel(request),"编辑操作失败");
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("编辑操作出现异常");
        }
    }
    @ResponseBody
    @PostMapping("insertUserInterestDetail")
    @ApiOperation("新增或编辑兴趣爱好的详情")
    public JsonResult insertUserInterestDetail(@Valid @RequestBody InsertUserInterestDetailRequest request, BindingResult bindingResult,HttpServletRequest httpRequest){
        if(bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(StringUtils.isEmpty(request.getUserId())){
            try {
                request.setUserId(getUserId(httpRequest));
                if(request.getUserId()==null){
                    return JsonResult.fail("用户Id不能为空");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try{
            return super.getResult(userInterestAction.addOrEditUserEducationDetail(request),"操作失败");
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail( "操作出现异常");
        }
    }
    @ResponseBody
    @GetMapping("selectUserInterest")
    @ApiOperation("根据用户id查询兴趣爱好")
    public JsonResult selectUserInterest(String userId,HttpServletRequest request){
        try {
            userId=getUserId(userId,request);
        }catch (Exception e){
            return JsonResult.fail(e.getMessage());
        }
        if(StringUtils.isEmpty(userId))
            return JsonResult.fail("用户id不能为空");
        try{
            SelectUserInterestResponse response = userInterestAction.selectUserInterest(userId);
            return JsonResult.success().addResult("result",response);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询操作出现异常");
        }
    }
    @ResponseBody
    @PostMapping("deleteUserInterestDetail")
    @ApiOperation("根据详情id删除兴趣爱好详情")
    public JsonResult deleteUserInterestDetail(@RequestParam("id") String id,HttpServletRequest request){
        String userId = null;
        if(StringUtils.isEmpty(id)){
            return JsonResult.fail("详情id不能为空");
        }
        try {
            userId = getUserId(request);
        }catch (Exception e){
            return JsonResult.fail(e.getMessage());
        }
        if(userId==null){
            return JsonResult.fail("请先登录");
        }
        try{
            return super.getResult(userInterestAction.deleteUserInterestDetail(id,userId),"删除操作失败");
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("删除操作出现异常");
        }
    }
}
