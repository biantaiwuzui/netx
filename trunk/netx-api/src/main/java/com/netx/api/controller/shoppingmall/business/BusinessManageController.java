package com.netx.api.controller.shoppingmall.business;
import com.netx.api.controller.BaseController;
import com.netx.common.vo.business.AddBusinessManageRequestDto;
import com.netx.common.vo.business.DeleteBusinessManageRequestDto;
import com.netx.shopping.biz.business.ManageAction;
import com.netx.shopping.model.business.SellerManage;
import com.netx.shopping.vo.BusinessManageListResponseVo;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * Created By wj.liu
 * Description: 网商业务主管控制层
 * Date: 2017-09-07
 */
@Api(description = "网商业务主管相关接口")
@RestController
@RequestMapping("/api/business/manage")
public class BusinessManageController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(BusinessManageController.class);

    @Autowired
    ManageAction manageAction;

    @ApiOperation(value = "添加或修改主管", notes = "添加或修改主管")
    @PostMapping("/addOrUpdate")
    public JsonResult addOrUpdate(@Valid @RequestBody AddBusinessManageRequestDto request, BindingResult bindingResult,HttpServletRequest requestDto){
        if( bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(StringUtils.isEmpty(request.getUserId())){
            try {
                request.setUserId(getUserId(request.getUserId(),requestDto));
                if(StringUtils.isEmpty(request.getUserId())){
                    return JsonResult.fail("用户id不能为空");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try {
            SellerManage res = manageAction.addOrUpdate(request);
            if(res != null){
                return JsonResult.success().addResult("res",res);
            }
           return JsonResult.fail("添加或修改主管操作失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("添加或修改主管操作异常！");
        }
    }

    @ApiOperation(value = "删除主管", notes = "删除主管")
    @PostMapping("/delete")
    public JsonResult delete(@Valid @RequestBody DeleteBusinessManageRequestDto request, BindingResult bindingResult){
        if( bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            boolean res = manageAction.delete(request.getId());
            if(!res){
                return JsonResult.fail("删除主管失败！");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("删除主管异常！");
        }
        return JsonResult.success();
    }

    @ApiOperation(value = "获取网商主管列表", notes = "获取网商主管列表")
    @PostMapping("/list")
    public JsonResult list(String userId,HttpServletRequest requestDto){
        try {
            userId = getUserId(userId,requestDto);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail("用户id不能为空");
            }
            List<BusinessManageListResponseVo> list = manageAction.list(userId);
            if (list != null){
                return JsonResult.success().addResult("list",list);
            }
            return JsonResult.fail("查询主管失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询主管异常！");
        }
    }

    @ApiOperation(value = "根据主管id获取主管",notes = "根据主管id获取主管")
    @PostMapping("/getManage")
    public JsonResult getManage(@Valid @RequestBody String manageId , BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try{
            SellerManage manage= manageAction.getManage(manageId);
            if (manage != null){
                return JsonResult.success().addResult("manage",manage);
            }
            return JsonResult.fail("查询主管失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询主管异常！");
        }
    }
}
