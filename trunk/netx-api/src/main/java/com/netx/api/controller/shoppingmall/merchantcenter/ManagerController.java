package com.netx.api.controller.shoppingmall.merchantcenter;

import com.netx.api.controller.BaseController;
import com.netx.api.controller.shoppingmall.business.BusinessManageController;
import com.netx.common.vo.business.DeleteBusinessManageRequestDto;
import com.netx.fuse.biz.shoppingmall.merchantcenter.MerchantManagerFuseAction;
import com.netx.shopping.biz.merchantcenter.MerchantManagerAction;
import com.netx.shopping.model.merchantcenter.MerchantManager;
import com.netx.shopping.vo.*;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * Created By CHENQIAN
 * Description: 网商相关人员接口
 * Date: 2018-05-09
 */
@Api(description = "新 · 网商相关人员接口")
@RestController
@RequestMapping("/api/business/merchantManager")
public class ManagerController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(BusinessManageController.class);

    @Autowired
    private MerchantManagerAction merchantManagerAction;
    @Autowired
    private MerchantManagerFuseAction merchantManagerFuseAction;

    @ApiOperation(value = "添加或修改主管/收银员/法人", notes = "添加或修改主管/收银员/法人/注册者/特权行使人/客服人员")
    @PostMapping("/addOrUpdate")
    public JsonResult addOrUpdate(@Valid @RequestBody AddManagerRequestDto request, BindingResult bindingResult, HttpServletRequest requestDto){
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
            MerchantManager res = merchantManagerAction.addManager(request);
            if(res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("添加或修改主管操作失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            e.printStackTrace();
            return JsonResult.fail("添加或修改主管操作异常！");
        }
    }


    @ApiOperation(value = "删除主管/收银员/法人/注册者/特权行使人/客服人员", notes = "删除主管/收银员/法人/注册者/特权行使人/客服人员")
    @PostMapping("/delete")
    public JsonResult delete(@Valid @RequestBody DeleteBusinessManageRequestDto request, BindingResult bindingResult){
        if( bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            boolean res = merchantManagerAction.delete(request.getId(), request.getMerchantId(), request.getMerchantUserType());
            if(!res){
                return JsonResult.fail("删除主管失败！");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("删除主管异常！");
        }
        return JsonResult.success();
    }

    @ApiOperation(value = "获取网商收银员/主管/法人/注册者/特权行使人/客服人员列表", notes = "获取网商收银员/主管/法人/注册者/特权行使人/客服人员列表")
    @PostMapping("/list")
    public JsonResult list(@Valid @RequestBody GetMerchantManagerListRequestDto request, BindingResult bindingResult){
        if( bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            List<QueryManagerResponseDto> list = merchantManagerAction.queryMerchantManagerByUserId(request.getUserId(), request.getMerchantUserType(), request.getMerchantId());
            if (list != null){
                return JsonResult.success().addResult("list",list);
            }
            return JsonResult.fail("查询相关人员列表失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询相关人员列表异常！");
        }
    }

    @ApiOperation(value = "根据id获取收银员/主管/法人/注册者/特权行使人/客服人员 详细信息",notes = "根据id获取收银员/主管/法人/注册者/特权行使人/客服人员 详细信息")
    @PostMapping("/getManage")
    public JsonResult getManage(@Valid @RequestBody QueryManagerRequestDto requestDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try{
            MerchantManager merchantManager = merchantManagerAction.getMerchantManagerById(requestDto.getId());
            if (merchantManager != null){
                return JsonResult.success().addResult("merchantManager", merchantManager);
            }
            return JsonResult.fail("查询相关人员失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询相关人员异常！");
        }
    }

    @ApiOperation(value = "根据商家id获取商家人员列表", notes = "根据商家id获取商家人员列表")
    @PostMapping("/merchantList")
    public JsonResult merchantList(@RequestParam String merchantId){
        try {
            List<ManagerListResponseVo> list = merchantManagerFuseAction.getMerchantManagerByMerchantId(merchantId);
            if (list != null){
                return JsonResult.success().addResult("list", list);
            }
            return JsonResult.fail("查询商家相关人员列表失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询商家相关人员列表异常！");
        }
    }


}
