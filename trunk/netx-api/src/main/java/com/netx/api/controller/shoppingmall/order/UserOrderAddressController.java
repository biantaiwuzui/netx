package com.netx.api.controller.shoppingmall.order;

import com.netx.api.controller.BaseController;
import com.netx.common.vo.business.AddOrUpdateRequestDto;
import com.netx.common.vo.business.DelectAddressRequestDto;
import com.netx.common.vo.business.UpdateAddressRequestDto;
import com.netx.shopping.biz.order.UserOrderAddressAction;
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
 * Description: 用户订单地址控制层
 * Date: 2017-09-16
 */
@Api(description = "用户订单地址相关接口")
@RestController
@RequestMapping("/api/business/UserOrderAddressController")
public class UserOrderAddressController extends BaseController {
/*
    private Logger logger = LoggerFactory.getLogger(UserOrderAddressController.class);

    @Autowired
    UserOrderAddressAction userOrderAddressAction;

    @ApiOperation(value = "增加或修改收货地址", notes = "增加或修改收货地址")
    @PostMapping("/add")
    public JsonResult add(@Valid @RequestBody AddOrUpdateRequestDto request, BindingResult bindingResult,HttpServletRequest requestDto){
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
            List<String> address= userOrderAddressAction.addorUpdate(request);
            if (address != null){
                return JsonResult.success().addResult("address",address);
            }
            return JsonResult.fail("增加或修改收货地址失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("增加或修改收货地址异常！");
        }
    }

    @ApiOperation(value = "删除收货地址", notes = "删除收货地址")
    @PostMapping("/delete")
    public JsonResult delete(@Valid @RequestBody DelectAddressRequestDto request, BindingResult bindingResult,HttpServletRequest requestDto){
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
            boolean res = userOrderAddressAction.delete(request);
            return getResult(res,"删除收货地址失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("删除收货地址异常！");
        }
    }

    @ApiOperation(value = "获取用户订单地址列表", notes = "获取用户订单地址列表")
    @PostMapping("/addressList")
    public JsonResult addressList(String userId,HttpServletRequest requestDto){
        try {
            userId = getUserId(userId,requestDto);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail("用户id不能为空");
            }
            List<Object> res = userOrderAddressAction.getUserOrderAddressList(userId);
            if (res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("获取用户订单地址列表失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取用户订单地址列表异常！");
        }
    }

    @ApiOperation(value = "修改用户订单地址列表", notes = "修改用户订单地址列表")
    @PostMapping("/update")
    public JsonResult update(@Valid @RequestBody UpdateAddressRequestDto request , BindingResult bindingResult,HttpServletRequest requestDto){
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
            boolean res = userOrderAddressAction.update(request);
            return getResult(res,"修改用户订单地址列表失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("修改用户订单地址列表异常！");
        }
    }*/
}
