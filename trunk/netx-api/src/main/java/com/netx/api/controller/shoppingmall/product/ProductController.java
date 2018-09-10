package com.netx.api.controller.shoppingmall.product;

import com.netx.api.controller.BaseController;
import com.netx.common.redis.model.UserGeo;
import com.netx.common.vo.business.*;
import com.netx.fuse.biz.shoppingmall.product.ProductFuseAction;
import com.netx.shopping.biz.product.ProductCollectAction;
import com.netx.shopping.biz.product.ProductAction;
import com.netx.shopping.model.product.Product;
import com.netx.shopping.vo.GetProductListResponseVo;
import com.netx.shopping.vo.GetProductResponseVo;
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
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created By wj.liu
 * Description: 商品控制层
 * Date: 2017-09-13
 */
@Api(description = "网商商品相关接口")
@RestController
@RequestMapping("/api/business/goods")
public class ProductController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    ProductAction productAction;

    @Autowired
    ProductCollectAction productCollectAction;

    @Autowired
    ProductFuseAction productFuseAction;

    @ApiOperation(value = "获取所有商品", notes = "所有商品")
    @PostMapping(value = "selectGoodsList")
    public JsonResult selectGoodsList() {
        try {
            List<Map<String,String>> list = productAction.selectGoodsList();
            if (list != null){
                return JsonResult.success().addResult("list",list);
            }
            return JsonResult.fail("获取所有商品失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取所有商品异常！");
        }
    }

    @ApiOperation(value = "发布商品或编辑商品", notes = "符合条件的用户，即可发布单品。每个用户可以发布多个商品," +
            "不符合条件者，需提示其因何原因暂不具备发布商品的资格")
    @PostMapping("/releaseGoods")
    public JsonResult releaseGoods(@Valid @RequestBody ReleaseGoodsRequestDto request, BindingResult bindingResult,HttpServletRequest requestDto){
        if(bindingResult.hasErrors()){
            return JsonResult.fail( bindingResult.getAllErrors().get(0).getDefaultMessage());
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
            Product res = productAction.releaseGoods(request);
            if(res==null){
                return JsonResult.fail("发布商品或编辑商品失败！");
            }
            return JsonResult.success().addResult("res",res);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("发布商品或编辑商品异常！");
        }
    }

    @ApiOperation(value = "商品详情", notes = "商品详情")
    @PostMapping("/get")
    public JsonResult get(@Valid @RequestBody GetGoodsRequestDto request, BindingResult bindingResult,HttpServletRequest requestDto){
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
        try {
            GetProductResponseVo getGoodsResponseVo = productFuseAction.get(request);
            if (getGoodsResponseVo != null){
                return JsonResult.success().addResult("getGoodsResponseVo",getGoodsResponseVo);
            }
            return JsonResult.fail("获取商品详情失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取商品详情异常！");
        }
    }

    @ApiOperation(value = "商品列表", notes = "通用的商品列表接口，根据不同的条件返回")
    @PostMapping("/list")
    public JsonResult list(@Valid @RequestBody GetGoodsListRequestDto request, BindingResult bindingResult, HttpServletRequest requestDto){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            List<GetProductListResponseVo> list = productFuseAction.list(request, getLat(requestDto), getLon(requestDto));
            if (list != null){
                return JsonResult.success().addResult("list",list);
            }
            return JsonResult.fail("获取商品列表失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取商品列表异常！");
        }
    }

    @ApiOperation(value = "根据userid查询最新发布商品", notes = "根据userid查询最新发布商品")
    @PostMapping("/getNewestGoodsMessage")
    public JsonResult getNewestGoodsMessage(String userId, HttpServletRequest requestDto){

        try {
            userId = getUserId(userId,requestDto);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail("用户id不能为空");
            }
            GetNewestGoodsMessageResponseVo res = productFuseAction.getMessage(userId);
            if (res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("查询最新发布商品失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询最新发布商品异常！");
        }
    }

    @ApiOperation(value = "根据userid查询最新发出订单", notes = "根据userid查询最新发出订单")
    @PostMapping("/getNewestOdersMessage")
    public JsonResult getNewestOdersMessage(String userId,HttpServletRequest requestDto){

        try {
            userId = getUserId(userId,requestDto);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail("用户id不能为空");
            }
            GetNewestOdersMessageResponseVo res = productAction.getMessage2(userId);
            if (res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("查询最新发出订单失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询最新发出订单异常！");
        }
    }

    @ApiOperation(value = "根据userid查询成功发布的商品/发布的商品总数", notes = "根据userid查询成功发布的商品/发布的商品总数")
    @PostMapping("/getRelatedGoodsMessage")
    public JsonResult getRelatedGoodsMessage(String userId,HttpServletRequest requestDto){
        try {
            userId = getUserId(userId,requestDto);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail("用户id不能为空");
            }
            GetRelatedGoodsMessageResponseVo res = productAction.getMessage1(userId);
            if (res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("查询发布商品数失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询发布商品数异常！");
        }
    }

    @ApiOperation(value = "根据userid查询已成功发出的订单数/发出的订单总数", notes = "根据userid查询已成功发出的订单数/发出的订单总数")
    @PostMapping("/getRelatedOdersMessage")
    public JsonResult getRelatedOdersMessage(String userId,HttpServletRequest requestDto){
        try {
            userId = getUserId(userId,requestDto);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail("用户id不能为空");
            }
            GetRelatedOdersMessageResponseVo res = productAction.getMessage4(userId);
            if (res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("查询发出订单数失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询发出订单数异常！");
        }
    }

    @ApiOperation(value = "删除商品", notes = "删除商品接口，只能删除自己发布的商品")
    @PostMapping("/delete")
    public JsonResult delete(@Valid @RequestBody DeleteGoodsRequestDto request, BindingResult bindingResult,HttpServletRequest requestDto){
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
        try {
            Boolean res = productAction.delete(request);
            return getResult(res,"删除商品失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("删除商品异常！");
        }
    }

    @ApiOperation(value = "获取网币适用范围商家商品数量", notes = "获取网币适用范围商家商品数量")
    @PostMapping("/getSellerGoodsQuantity")
    public JsonResult getSellerGoodsQuantity(@RequestBody String sellerIds){
        if(!StringUtils.hasText(sellerIds.toString()))
        {
            return JsonResult.fail("商家ID不能为空");
        }
        try {
            GetSellerGoodsQuantityResponseVo res = productAction.getMessage3(sellerIds);
            if (res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("获取网币适用范围商家商品数量失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取网币适用范围商家商品数量异常！");
        }
    }
    @ApiOperation(value = "根据商家id获取商品ids", notes = "根据商家id获取商品ids")
    @PostMapping("/getGoodsBySellerId")
    public JsonResult getGoodsBySellerId(@RequestParam("sellerId") String sellerId){
        if(StringUtils.isEmpty(sellerId)){
            return JsonResult.fail("商家id不能为空");
        }
        try {
            List<String> res = productAction.getGoodsBySellerId(sellerId);
            if (res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("获取商品ids");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取商品ids异常");
        }
    }

    @ApiOperation(value = "关注或取消关注", notes = "关注或取消关注")
    @PostMapping("/collect")
    public JsonResult collect(@Valid @RequestBody AddCollectGoodsRequestDto request, BindingResult bindingResult,HttpServletRequest requestDto){
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
            boolean res = productCollectAction.collectSeller(request);
            return getResult(res,"关注或取消关注操作失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("关注或取消关注操作异常！");
        }
    }

    @ApiOperation(value = "是否已发布过这个商品名")
    @PostMapping("/isHaveThisName")
    public JsonResult isHaveThisName(@RequestBody @Valid IsHaveThisNameRequestDto request,BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return JsonResult.fail( bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            boolean res = productAction.isHaveThisName(request.getMerchantId(),request.getName());
            return getResult(res,"校验商品名失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("校验商品名异常！");
        }

    }

    @ApiOperation(value = "是否可以操作", notes = "如果有关联的订单状态没有完成，不能操作")
    @PostMapping("/isCanHandle")
    public JsonResult isCanHandle(@RequestBody @Valid IsCanHandleRequestDto request, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return JsonResult.fail( bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            boolean res = productAction.isCanHandle(request);
            return getResult(res,"检验是否可以操作失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("检验是否可以操作异常！");
        }
    }

    @ApiOperation(value = "商品上架", notes = "商品上架")
    @PostMapping("/optGoods")
    public JsonResult optGoods(@RequestBody @Valid BackManageOptGoodsRequestDto request, BindingResult bindingResult){
        if( bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            boolean res = productAction.optGoods(request);
            if(!res){
                return JsonResult.fail("操作商品失败！");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("操作商品失败！");
        }
        return JsonResult.success();
    }

    @ApiOperation(value="根据用户id获取商品列表")
    @PostMapping("/getProductListByUserId")
    public JsonResult getProductListByUserId(@Valid @RequestBody GetListByUserIdDto getListByUserIdDto, HttpServletRequest request){
        UserGeo userGeo = new UserGeo();
        try{
            userGeo=getGeoFromRequest(request);
        }catch (Exception e){
            logger.warn(e.getMessage());
        }
        try {
            List<GetProductListResponseVo> res = productFuseAction.getProductListByUserId(getListByUserIdDto, userGeo.getLat(), userGeo.getLon());
            return JsonResult.success().addResult("res",res);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询商品异常！");
        }
    }

    @ApiOperation(value="根据商家id获取商品列表")
    @PostMapping("/getProductListBySellerId")
    public JsonResult getProductListBySellerId(@Valid @RequestBody GetListBySellerIdDto getListBySellerIdDto, HttpServletRequest request){
        if(StringUtils.isEmpty(getListBySellerIdDto.getSellerId())){
            return JsonResult.fail("商家id不能为空！");
        }
        try {
            List<GetProductListResponseVo> res = productFuseAction.getProductListBySellerId(getListBySellerIdDto, getLat(request), getLon(request));
            return JsonResult.success().addResult("res",res);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询商品异常！");
        }
    }
}