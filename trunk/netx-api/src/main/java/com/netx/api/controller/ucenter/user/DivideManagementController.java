package com.netx.api.controller.ucenter.user;

import com.netx.api.controller.BaseController;
import com.netx.common.user.dto.common.CommonListDto;
import com.netx.common.user.dto.divideManagement.OperateDivideManagementRequestDto;
import com.netx.common.user.dto.divideManagement.SelectAdminstratorListResponseDto;
import com.netx.common.user.dto.divideManagement.SelectUserByUserNumberResponseDto;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "分工管理模块")
@RestController
@RequestMapping("/api/divideManagement/")
public class DivideManagementController extends BaseController{

    @Autowired
    private UserAction userAction;

    private Logger logger = LoggerFactory.getLogger(DivideManagementController.class);

    @ResponseBody
    @PostMapping("selectAdministratorList")
    @ApiOperation(value="查询管理员列表", response = SelectAdminstratorListResponseDto.class)
    public JsonResult selectAdministratorList(@RequestBody CommonListDto request) {
        try {
            List<SelectAdminstratorListResponseDto> list = userAction.selectAdministratorList(request);
            return JsonResult.success().addResult("list",list);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询操作出现异常");
        }
    }

    @ResponseBody
    @PostMapping("selectUserByUserNumber")
    @ApiOperation(value="根据网号查询用户信息（仅适用分工管理）")
    public JsonResult selectUserByUserNumber(String userNumber) {
        if(!StringUtils.hasText(userNumber)){
            return JsonResult.fail("网号不能为空");
        }
        try {
            SelectUserByUserNumberResponseDto response = userAction.selectUserByUserNumber(userNumber);
            if(response.getAdminUser()){
                return JsonResult.fail("此用户已经是管理员");
            }
            if(response.getRealName() == null){
                return JsonResult.fail("此用户没有认证身份，拒绝对此用户进行任何的分工操作");
            }
            return JsonResult.success().addResult("result",response);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询操作出现异常");
        }
    }

    @ResponseBody
    @PostMapping("addManagement")
    @ApiOperation(value="新增分工")
    public JsonResult addManagement(@RequestBody OperateDivideManagementRequestDto request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            return super.getResult(userAction.addManagement(request),"添加分工失败");
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("添加分工操作出现异常");
        }
    }

    @ResponseBody
    @PostMapping("trimManagement")
    @ApiOperation(value="调整分工")
    public JsonResult trimManagement(@RequestBody OperateDivideManagementRequestDto request, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            return super.getResult(userAction.trimManagement(request),"调整分工失败");
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("调整分工操作出现异常");
        }
    }

    @ResponseBody
    @PostMapping("deleteManagement")
    @ApiOperation(value="删除管理员", notes = "若用户id列表有不存在的id，则被忽略，其他存在的id都会成功操作")
    public JsonResult deleteManagement(@RequestBody List<String> userId) {
        if(userId.isEmpty()){
            return JsonResult.fail("用户id列表不能为空");
        }
        try {
            return super.getResult(userAction.deleteManagement(userId),"删除管理员失败");
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("删除操作出现异常");
        }
    }
}
