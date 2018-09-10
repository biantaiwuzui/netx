package com.netx.api.controller.shoppingmall.merchantcenter;

import com.netx.api.controller.BaseController;
import com.netx.api.controller.shoppingmall.business.SellerController;
import com.netx.common.redis.model.UserGeo;
import com.netx.common.user.util.DateTimestampUtil;
import com.netx.common.vo.business.BusinessManagementRequestDto;
import com.netx.common.vo.business.GetRelatedSellerMessageResponseVo;
import com.netx.common.vo.business.GetSellerListRequestDto;
import com.netx.common.vo.business.GetSellersBuyIdResponseVo;
import com.netx.common.vo.currency.GetCanCurrencyMessageResquesDto;
import com.netx.common.vo.currency.GetCurrencyMessageResponseVo;
import com.netx.common.vo.currency.GetSellersBuyIdRequestDto;
import com.netx.fuse.biz.shoppingmall.merchantcenter.MerchantFuseAction;
import com.netx.fuse.biz.shoppingmall.merchantcenter.MerchantRegisterFuseAction;
import com.netx.shopping.biz.merchantcenter.MerchantAction;
import com.netx.shopping.biz.merchantcenter.MerchantFavoriteAction;
import com.netx.shopping.model.merchantcenter.Merchant;
import com.netx.shopping.vo.*;
import com.netx.utils.json.ApiCode;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created By CHENQIAN
 * Description: 网商控制层
 * Date: 2018-05-09
 */
@Api(description = "新 · 网商商家相关接口")
@RestController
@RequestMapping("/api/business/merchant")
public class MerchantController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(SellerController.class);

    @Autowired
    MerchantRegisterFuseAction merchantRegisterFuseAction;
    @Autowired
    MerchantFuseAction merchantFuseAction;
    @Autowired
    MerchantFavoriteAction merchantFavoriteAction;
    @Autowired
    MerchantAction merchantAction;

    @ApiOperation(value = "商家注册", notes = "商家注册，符合条件的用户，即可注册商家。允许同一用户注册多个商家，" +
            "不符合条件者，需提示其因何原因暂不具备注册商家的资格。")
    @PostMapping("/register")
    public JsonResult register(@Valid @RequestBody RegisterMerchantRequestDto request, BindingResult bindingResult, HttpServletRequest requestDto){
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
            Merchant merchant = merchantRegisterFuseAction.register(request);
            if(merchant == null){
                return JsonResult.fail("注册失败！");
            }
            return JsonResult.success().addResult("merchant", merchant);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("注册异常！"+e.getMessage());
        }
    }

    @ApiOperation(value = "商家编辑", notes = "商家编辑")
    @PostMapping("/edit")
    public JsonResult edit(@Valid @RequestBody RegisterMerchantRequestDto request, BindingResult bindingResult){
        if( bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            Merchant merchant = merchantRegisterFuseAction.edit(request);
            if(merchant == null){
                return JsonResult.fail("编辑失败！");
            }
            return JsonResult.success().addResult("merchant", merchant);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("编辑异常！"+e.getMessage());
        }
    }

    @ApiOperation(value = "商家加盟费处理", notes = "注册或续费商家加盟费之后调用")
    @PostMapping("/manageFee")
    public JsonResult manageFee(@Valid @RequestBody MerchantManageFeeRequestDto request, BindingResult bindingResult, HttpServletRequest requestDto){
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
        if(request.getAmount().doubleValue()<0){
            return JsonResult.fail("加盟费最少为0");
        }
        try{
            Merchant res = merchantRegisterFuseAction.manageFee(request);
            if(res == null){
                return JsonResult.fail("商家管理费处理操作失败！");
            }
            return JsonResult.success();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("商家管理费处理操作异常："+e.getMessage());
        }
    }

    @ApiOperation(value = "客服引荐人代码验证", notes = "客服引荐人代码验证")
    @PostMapping("/isHaveReferralServiceCode")
    public JsonResult isHaveReferralServiceCode(@RequestParam("referralServiceCode") String referralServiceCode){
        if(StringUtils.isEmpty(referralServiceCode)){
            return JsonResult.fail("客服引荐人代码不能为空");
        }
        try{
            boolean b = merchantRegisterFuseAction.isHaveReferralServiceCode(referralServiceCode);
            if(!b){
                return JsonResult.fail("客服引荐人代码无效！");
            }
            return JsonResult.success();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("验证客服引荐人代码异常！");
        }
    }

    @ApiOperation(value = "商家详情", notes = "商家详情")
    @PostMapping("/get")
    public JsonResult get(@Valid @RequestBody GetMerchantRequestDto request, BindingResult bindingResult, HttpServletRequest requestDto){
        if( bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId = null;
        try {
            userId = getUserId(requestDto);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }catch (Exception e){
            return JsonResult.fail(e.getMessage(), ApiCode.NO_AUTHORIZATION);
        }
        try {
            request.setUserId(userId);
            GetMerchantResponseVo object = merchantFuseAction.get(request, getLat(requestDto), getLon(requestDto));
            if (object != null){
                return JsonResult.success().addResult("object",object);
            }
            return JsonResult.fail("获取商家详情失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取商家详情异常！");
        }
    }

    @ApiOperation(value = "获取商家对象", notes = "获取商家对象，供远程调用")
    @PostMapping("/getSellerById")
    public JsonResult getSellerById(@RequestParam("merchantId") String merchantId, HttpServletRequest requestDto){
        if(!StringUtils.isNotBlank(merchantId)){
            return JsonResult.fail( "商家id不能为空");
        }
        try {
            GetMerchantListVo getMerchantListVo = merchantFuseAction.getMerchantById(merchantId , new BigDecimal(getLat(requestDto)), new BigDecimal(getLon(requestDto)));
            if (getMerchantListVo != null){
                List<GetMerchantListVo> list = new ArrayList<>();
                list.add(getMerchantListVo);
                return JsonResult.success().addResult("merchant", list);
            }
            return JsonResult.fail("获取商家对象失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取商家对象异常！");
        }
    }

    @ApiOperation(value = "收藏或取消收藏", notes = "收藏或取消收藏")
    @PostMapping("/collect")
    public JsonResult collect(@Valid @RequestBody AddMerchantFavoriteRequestDto request, BindingResult bindingResult,HttpServletRequest requestDto){
        if(bindingResult.hasErrors()){
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
        try{
            boolean res = merchantFavoriteAction.addMerchantFavorite(request);
            if(!res){
                return JsonResult.fail("操作失败！");
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("操作异常！");
        }
        return JsonResult.success();
    }

    @ApiOperation(value = "根据userId查询商家详情",notes = "返回收藏商家详情和经营商家详情")
    @PostMapping("/getSellerByUserId")
    public JsonResult getSellerByUserId(@Valid @RequestBody GetMerchantByUserIdRequestDto request,BindingResult bindingResult,HttpServletRequest requestDto){
        if(bindingResult.hasErrors()){
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
        try{
            GetMerchantListVo res = merchantFuseAction.getMerchantByUserId(request, getLat(requestDto), getLon(requestDto));
            if (res != null){
                return JsonResult.success().addResult("res", res);
            }
            return JsonResult.fail("查询商家详情失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询商家详情异常！");
        }
    }

    @ApiOperation(value = "根据UseuId查询经营、收藏商店数", notes = "根据UseuId查询经营、收藏商店数")
    @PostMapping("/getRelatedSellerMessage")
    public JsonResult getRelatedSellerMessage(String userId, HttpServletRequest requestDto) {
        try {
            userId = getUserId(userId,requestDto);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail("用户id不能为空");
            }
            GetRelatedSellerMessageResponseVo res = merchantFuseAction.getMerchatCountByUserId(userId);
            if (res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("查询经营、收藏商店数失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询经营、收藏商店数异常！");
        }
    }

    @ApiOperation(value = "根据UseuId查询现有注册的商店数、总经营的商店数", notes = "根据UseuId查询现有注册的商店数、总经营的商店数")
    @PostMapping("/getRegisterSellerMessage")
    public JsonResult getRegisterSellerMessage(String userId,HttpServletRequest requestDto){
        try {
            userId = getUserId(userId,requestDto);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail("用户id不能为空");
            }
            GetRegisterMerchantCountResponseVo res = merchantFuseAction.getRegisterMerchantCountByUserId(userId);
            if (res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("查询现有注册的商店数、总注册过的商店数失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询现有注册的商店数、总注册过的商店数异常！");
        }
    }

    @ApiOperation(value = "注销商家", notes = "注销商家，用户只能注销自己的商家")
    @PostMapping("/delete")
    public JsonResult delete(@RequestParam String merchantId, BindingResult bindingResult,HttpServletRequest requestDto){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            Boolean res = merchantAction.delete(merchantId);
            if(!res){
                return JsonResult.fail("注销商家失败！");
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("注销商家异常！");
        }
        return JsonResult.success();
    }

    @ApiOperation(value = "根据UseuId查询现有注册商家", notes = "根据UseuId查询现有注册商家")
    @PostMapping("/getSellersByUserId")
    public JsonResult getSellersByUserId(String userId,HttpServletRequest requestDto){
        try {
            userId = getUserId(userId,requestDto);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail("用户id不能为空");
            }
            List<GetMerchantListVo> res = merchantFuseAction.getRegister(userId);
            if (res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("查询注册商家失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询注册商家异常！");
        }
    }



    @ApiOperation(value = "根据商家id查询商家详情", notes = "根据商家id查询商家详情，供跨域调用")
    @PostMapping("/getSellersBuyId")
    public JsonResult getSellersBuyId(@RequestBody GetSellersBuyIdRequestDto request) {
        if (request.getIds().size()<1) {
            return JsonResult.fail("商家id不能为空");
        }
        try {
            List<GetSellersBuyIdResponseVo> res= merchantAction.getMerchantsBuyId(request.getIds());
            if (res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("查询商家详情失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询商家详情异常！");
        }
    }

    @ApiOperation(value = "生成商家二维码", notes = "生成商家二维码")
    @PostMapping("/generateSellerQrcode")
    public JsonResult generateSellerQrcode(@RequestParam("merchantId") String merchantId) {
        if (merchantId == null) {
            return JsonResult.fail( "商家id不能为空");
        }
        try {
            merchantRegisterFuseAction.generateMerchantQrcode(merchantId);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("生成异常！");
        }
        return JsonResult.success();
    }

    @ApiOperation(value = "是否已注册过这个商家名")
    @PostMapping("/isHaveThisName")
    public JsonResult isHaveThisName(@RequestParam("name") String name){
        try {
            if(StringUtils.isEmpty(name)){
                return JsonResult.fail("商家名不能为空");
            }
            boolean res = merchantAction.isHaveName(name);
            return JsonResult.success().addResult("res",res);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("判断是否已注册过此商家名异常！");
        }

    }

    @ApiOperation(value = "注销用户在网商模块的相关数据")
    @PostMapping("/cancelBuyUserId")
    public JsonResult cancelBuyUserId(String userId, HttpServletRequest requestDto){
        try {
            userId = getUserId(userId, requestDto);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail("用户id不能为空");
            }
            boolean res = merchantAction.cancelBuyUserId(userId);
            if (!res){
                return JsonResult.fail("注销用户在网商模块的相关数据失败！");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("注销用户在网商模块的相关数据异常！");
        }
        return JsonResult.success();
    }

    @ApiOperation(value = "根据userId查询经营商家或收藏商家", notes = "当status=1根据用户id获取经营的商家\n当status=2根据用户id获取收藏的商家")
    @PostMapping("/getSellerListByUserId")
    public JsonResult getSellerListByUserId(@Valid @RequestBody GetMerchantListByUserIdDto getMerchantListByUserIdDto, HttpServletRequest request){
        try{
            getMerchantListByUserIdDto.setUserId(getUserId(getMerchantListByUserIdDto.getUserId(), request));
        }catch (Exception e){
            logger.warn(e.getMessage());
        }
        try {
            List<GetMerchantListVo> res = merchantFuseAction.getMyMerchant(getMerchantListByUserIdDto, getLat(request), getLon(request));
            if (res != null){
                return JsonResult.success().addResult("res", res);
            }
            return JsonResult.fail("查询商家失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询商家异常！");
        }

    }

    @ApiOperation(value = "商家列表", notes = "通用的商家列表接口，根据不同条件返回")
    @PostMapping("/list")
    public JsonResult list(@Valid @RequestBody GetSellerListRequestDto request, BindingResult bindingResult, HttpServletRequest requestDto){
        if( bindingResult.hasErrors()){
            return JsonResult.fail( bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            List<GetMerchantListVo> list =  merchantFuseAction.list(request, getLat(requestDto), getLon(requestDto));
            if (list != null){
                return JsonResult.success().addResult("list", list);
            }
            return JsonResult.fail("获取商家列表失败！");
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("获取商家列表异常！");
        }
    }

}
