package com.netx.api.controller.shoppingmall.ordercenter;

import com.netx.api.controller.BaseController;
import com.netx.shopping.biz.cartcenter.CartAction;
import com.netx.shopping.biz.cartcenter.CartItemAction;
import com.netx.shopping.vo.*;
import com.netx.ucenter.biz.user.UserAction;
import com.netx.ucenter.model.user.User;
import com.netx.utils.json.ApiCode;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Api(description = "新 · 网商购物车接口")
@RestController
@RequestMapping("/api/business/cart")
public class CartController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    CartAction cartAction;

    @Autowired
    CartItemAction cartItemAction;

    @Autowired
    UserAction userAction;

    @ApiOperation(value = "加入购物车", notes = "购物车")
    @PostMapping("/add")
    public JsonResult addCart(@Valid @RequestBody AddCartItemRequestDto requestDto, BindingResult bindingResult, HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId = null;
        try {
            userId=getUserId(request);
            if(StringUtils.isBlank(userId)){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            User user = userAction.queryUser(userId);
            return getResult(cartAction.addCart(userId,requestDto,user.getUserNumber()),"加入购物车失败");
        }catch (RuntimeException e){
            logger.info(e.getMessage());
            return JsonResult.fail(e.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("加入购物车异常");
        }
    }

    @ApiOperation(value = "购物车移除商品", notes = "购物车：按详情id【cartItemId】删除")
    @PostMapping("/removeProduct")
    public JsonResult removeProduct(@Valid @RequestBody DelCartProductRequestDto requestDto, BindingResult bindingResult, HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId = null;
        try {
            userId=getUserId(request);
            if(StringUtils.isBlank(userId)){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            return getResult(cartAction.remove(userId,requestDto.getMerchantId(),requestDto.getCartItemId()),"移除商品失败");
        }catch (RuntimeException e){
            logger.info(e.getMessage());
            return JsonResult.fail(e.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("移除商品异常");
        }
    }

    @ApiOperation(value = "清空购物车", notes = "购物车")
    @PostMapping("/remove")
    public JsonResult remove(HttpServletRequest request){
        String userId = null;
        try {
            userId=getUserId(request);
            if(StringUtils.isBlank(userId)){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            return getResult(cartAction.removeByUserId(userId),"移除商品失败");
        }catch (RuntimeException e){
            logger.info(e.getMessage());
            return JsonResult.fail(e.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("移除商品异常");
        }
    }

    @ApiOperation(value = "购物车结算", notes = "购物车")
    @PostMapping("/finish")
    public JsonResult finish(@RequestBody @Valid List<FinishCartRequestDto> requestDtos, BindingResult bindingResult, HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(requestDtos==null || requestDtos.size()<1){
            return JsonResult.fail("结算的商品不能为空");
        }
        String userId = null;
        try {
            userId=getUserId(request);
            if(StringUtils.isBlank(userId)){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            User user = userAction.queryUser(userId);
            return JsonResult.success().addResult("orderId",cartAction.finish(userId,user.getNickname(),requestDtos));
        }catch (RuntimeException e){
            logger.info(e.getMessage());
            return JsonResult.fail(e.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("订单生成异常");
        }
    }

    @ApiOperation(value = "购物车详情", notes = "购物车")
    @PostMapping("/list")
    public JsonResult list(HttpServletRequest request){
        String userId = null;
        try {
            userId=getUserId(request);
            if(StringUtils.isBlank(userId)){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            List<CartMerchantResponseDto> list = cartAction.list(userId,getLat(request),getLon(request));
            return JsonResult.success().addResult("list",list);
        }catch (RuntimeException e){
            logger.info(e.getMessage());
            return JsonResult.fail(e.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("购物车详情异常");
        }
    }


}
