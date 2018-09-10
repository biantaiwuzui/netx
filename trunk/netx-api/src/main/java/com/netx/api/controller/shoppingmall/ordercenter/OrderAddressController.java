package com.netx.api.controller.shoppingmall.ordercenter;

import com.netx.api.controller.BaseController;
import com.netx.common.vo.business.AddressRequestDto;
import com.netx.common.vo.business.UpAddressRequestDto;
import com.netx.shopping.biz.ordercenter.UserOrderAddressAction;
import com.netx.utils.json.ApiCode;
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

@Api(description = "用户地址相关接口")
@RestController
@RequestMapping("/api/business/userOrderAddress")
public class OrderAddressController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(OrderAddressController.class);

    @Autowired
    UserOrderAddressAction userOrderAddressAction;

    @ApiOperation(value = "增加或修改收货地址", notes = "增加或修改收货地址")
    @PostMapping("/add")
    public JsonResult add(@Valid @RequestBody UpAddressRequestDto request, BindingResult bindingResult, HttpServletRequest requestDto){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId = null;
        try {
            userId = getUserId(requestDto);
            if(StringUtils.isBlank(userId)){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        if(StringUtils.isNotBlank(request.getMobile()) && !request.getMobile().matches("^((0\\d{2,3}(-)?\\d{7,8})|(1[3-9]\\d{9}|9[2-8]\\d{9}))$")){
            return JsonResult.fail("联系方式格式错误");
        }
        try {
            userOrderAddressAction.addorUpdate(request,userId);
            return JsonResult.success();
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("增加或修改收货地址异常！");
        }
    }

    @ApiOperation(value = "删除收货地址", notes = "删除收货地址")
    @PostMapping("/delete")
    public JsonResult delete(int index, HttpServletRequest requestDto){
        String userId = null;
        try {
            userId = getUserId(requestDto);
            if(StringUtils.isBlank(userId)){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            boolean res = userOrderAddressAction.delete(index,userId);
            return getResult(res,"删除收货地址失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("删除收货地址异常！");
        }
    }

    @ApiOperation(value = "获取用户地址列表", notes = "获取用户地址列表")
    @PostMapping("/addressList")
    public JsonResult addressList(HttpServletRequest requestDto){
        String userId = null;
        try {
            userId = getUserId(requestDto);
            if(StringUtils.isBlank(userId)){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            return JsonResult.success().addResult("list",userOrderAddressAction.getUserOrderAddressList(userId));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取用户地址列表异常！");
        }
    }
}
