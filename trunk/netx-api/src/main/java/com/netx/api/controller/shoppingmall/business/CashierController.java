package com.netx.api.controller.shoppingmall.business;

import com.netx.api.controller.BaseController;
import com.netx.common.vo.business.AddBusinessCashierRequestDto;
import com.netx.common.vo.business.DeleteBusinessCashierRequestDto;
import com.netx.shopping.biz.business.CashierAction;
import com.netx.shopping.model.business.SellerCashier;
import com.netx.shopping.vo.BusinessCashierListResponseVo;
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
 * Created By liwei
 * Description: 网商收银人员控制层
 * Date: 2017-09-07
 */
@Api(description = "网商收银人员相关接口")
@RestController
@RequestMapping("/api/business/cashier")
public class CashierController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(CashierController.class);

    @Autowired
    CashierAction cashierAction;

    @ApiOperation(value = "添加或修改收银人", notes = "添加或修改收银人")
    @PostMapping("/addOrUpdate")
    public JsonResult addOrUpdate(@Valid @RequestBody AddBusinessCashierRequestDto request, BindingResult bindingResult,HttpServletRequest requestDto){
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
            SellerCashier res = cashierAction.addOrUpdate(request);
            if(res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("添加或修改收银人操作失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("添加或修改收银人操作异常！");
        }
    }

    @ApiOperation(value = "删除收银人", notes = "删除收银人")
    @PostMapping("/delete")
    public JsonResult delete(@Valid @RequestBody DeleteBusinessCashierRequestDto request, BindingResult bindingResult){
        if( bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            boolean res = cashierAction.delete(request.getId());
            if(!res){
                return JsonResult.fail("删除收银人失败！");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("删除收银人异常！");
        }
        return JsonResult.success();
    }

    @ApiOperation(value = "获取网商收银人列表", notes = "获取网商收银人列表")
    @PostMapping("/list")
    public JsonResult list(String userId,HttpServletRequest requestDto){
        try {
            userId = getUserId(userId,requestDto);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail("用户id不能为空");
            }
            List<BusinessCashierListResponseVo> list = cashierAction.list(userId);
            if (list != null){
                return JsonResult.success().addResult("list",list);
            }
            return JsonResult.fail("查询收银人失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询收银人异常！");
        }
    }

    @ApiOperation(value = "根据收银id获取收银人",notes = "根据收银id获取收银人")
    @PostMapping("/getCashier")
    public JsonResult getCashier(@Valid @RequestBody String cashierId , BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try{
            SellerCashier cashier= cashierAction.getCashier(cashierId);
            if (cashier != null){
                return JsonResult.success().addResult("cashier",cashier);
            }
            return JsonResult.fail("查询收银人失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询收银人异常！");
        }
    }
}
