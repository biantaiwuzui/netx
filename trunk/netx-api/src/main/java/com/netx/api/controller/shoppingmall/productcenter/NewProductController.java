package com.netx.api.controller.shoppingmall.productcenter;

import com.netx.api.controller.BaseController;
import com.netx.common.user.enums.StatScoreEnum;
import com.netx.common.vo.business.*;
import com.netx.fuse.biz.shoppingmall.productcenter.ProductFuseAction;
import com.netx.shopping.biz.productcenter.ProductAction;
import com.netx.shopping.biz.productcenter.ProductFavoriteAction;
import com.netx.shopping.model.productcenter.Product;
import com.netx.shopping.model.productcenter.Sku;
import com.netx.shopping.vo.*;
import com.netx.ucenter.biz.router.ScoreAction;
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
import java.util.List;

/**
 * Created By CHENQIAN
 * Description: 商品控制层
 * Date: 2018-05-09
 */
@Api(description = "新 · 网商商品相关接口")
@RestController("newProductController")
@RequestMapping("/api/business/product")
public class NewProductController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(NewProductController.class);

    @Autowired
    private ProductFuseAction productFuseAction;
    @Autowired
    private ProductAction productAction;
    @Autowired
    private ProductFavoriteAction productFavoriteAction;
    @Autowired
    private ScoreAction scoreAction;


    @ApiOperation(value = "发布商品或编辑商品", notes = "符合条件的用户，即可发布单品。每个用户可以发布多个商品," +
            "不符合条件者，需提示其因何原因暂不具备发布商品的资格")
    @PostMapping("/releaseGoods")
    public JsonResult releaseGoods(@Valid @RequestBody AddGoodsRequestDto request, BindingResult bindingResult, HttpServletRequest requestDto){
        if(bindingResult.hasErrors()){
            return JsonResult.fail( bindingResult.getAllErrors().get(0).getDefaultMessage());
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
        try {
            Product res = productAction.addPuduct(request, userId);
            if(res == null){
                return JsonResult.fail("发布商品或编辑商品失败！");
            }
            if(StringUtils.isBlank(request.getId())){
                int count = productAction.getAllUpProductCount(userId);
                if(count>3){
                    scoreAction.addScore(userId, StatScoreEnum.SS_PRODUCT.score());
                }
                count = productAction.getProductCountByMerchantId(request.getMerchantId());
                if(count==3){
                    scoreAction.addScore(userId, StatScoreEnum.SS_MERCHANT);
                }
            }
            return JsonResult.success().addResult("res", res);
        }catch (RuntimeException e){
            logger.warn(e.getMessage());
            return JsonResult.fail(e.getMessage());
        }catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("发布商品或编辑商品异常！");
        }
    }

    @ApiOperation(value = "商品详情", notes = "商品详情")
    @PostMapping("/get")
    public JsonResult get(@Valid @RequestBody GetGoodsRequestDto request, BindingResult bindingResult, HttpServletRequest requestDto){
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
            GetProductDetailResponseVo responseVo = productFuseAction.getProductDetail(request, getLat(requestDto), getLon(requestDto));
            if (responseVo != null){
                return JsonResult.success().addResult("responseVo", responseVo);
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
            List<GetProductListVo> list = productFuseAction.list(request, getLat(requestDto), getLon(requestDto));
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
            userId = getUserId(userId, requestDto);
            if(org.springframework.util.StringUtils.isEmpty(userId)){
                return JsonResult.fail("用户id不能为空");
            }
            GetProductListVo res = productFuseAction.getNewestProductByUserId(userId, new BigDecimal(getLat(requestDto)), new BigDecimal(getLon(requestDto)));
            if (res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("查询最新发布商品失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询最新发布商品异常！");
        }
    }

    @ApiOperation(value = "根据userid查询成功发布的商品/经营商品总数/关注商品的数量", notes = "根据userid查询成功发布的商品/经营商品总数/关注商品的数量")
    @PostMapping("/getRelatedGoodsMessage")
    public JsonResult getRelatedGoodsMessage(String userId, HttpServletRequest requestDto){
        try {
            userId = getUserId(userId, requestDto);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail("用户id不能为空");
            }
            GetRelatedGoodsMessageResponseVo res = productFuseAction.getProductCount(userId);
            if (res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("查询发布商品数失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询发布商品数异常！");
        }
    }

    @ApiOperation(value = "删除商品", notes = "删除商品接口，只能删除自己发布的商品")
    @PostMapping("/delete")
    public JsonResult delete(@Valid @RequestBody DeleteGoodsRequestDto request, BindingResult bindingResult, HttpServletRequest requestDto){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(org.springframework.util.StringUtils.isEmpty(request.getUserId())){
            try {
                request.setUserId(getUserId(request.getUserId(),requestDto));
                if(org.springframework.util.StringUtils.isEmpty(request.getUserId())){
                    return JsonResult.fail("用户id不能为空");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try {
            Boolean res = productAction.deleteById(request.getId());
            return getResult(res,"删除商品失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("删除商品异常！");
        }
    }

    @ApiOperation(value = "根据商家id获取商品ids", notes = "根据商家id获取商品ids")
    @PostMapping("/getGoodsBySellerId")
    public JsonResult getGoodsBySellerId(@RequestParam("merchantId") String merchantId){
        if(org.springframework.util.StringUtils.isEmpty(merchantId)){
            return JsonResult.fail("商家id不能为空");
        }
        try {
            List<String> res = productAction.getProductIdByMerchantIds(merchantId);
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
    public JsonResult collect(@Valid @RequestBody AddCollectGoodsRequestDto request, BindingResult bindingResult, HttpServletRequest requestDto){
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
            String res = productFavoriteAction.addOrCancel(request.getGoodsId(), request.getUserId());
            return getResult(res==null,res);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("关注或取消关注操作异常！");
        }
    }

    @ApiOperation(value = "是否已发布过这个商品名")
    @PostMapping("/isHaveThisName")
    public JsonResult isHaveThisName(@RequestBody @Valid IsHaveThisNameRequestDto request, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return JsonResult.fail( bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            boolean res = productAction.isHaveThisName(request.getName(), request.getMerchantId());
            return getResult(res,"商品名已存在！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("校验商品名异常！");
        }

    }

    @ApiOperation(value = "商品上架/下架", notes = "商品上架/下架")
    @PostMapping("/optGoods")
    public JsonResult optGoods(@RequestParam("productId") String productId){
        try {
            boolean res = productAction.optProduct(productId);
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
    public JsonResult getProductListByUserId(@Valid @RequestBody GetListByUserIdDto request, BindingResult bindingResult, HttpServletRequest requestDto){
        if (bindingResult.hasErrors()) {
            return JsonResult.fail( bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(StringUtils.isEmpty(request.getUserId())){
            try {
                request.setUserId(getUserId(request.getUserId(), requestDto));
                if(StringUtils.isEmpty(request.getUserId())){
                    return JsonResult.fail("用户id不能为空");
                }
            }catch (Exception e){
                return JsonResult.fail(e.getMessage());
            }
        }
        try {
            List<GetProductListVo> res = productFuseAction.getProductListByUserId(request, new BigDecimal(getLat(requestDto)), new BigDecimal(getLon(requestDto)));
            return JsonResult.success().addResult("res", res);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询商品异常！");
        }
    }

    @ApiOperation(value="根据商家id获取商品列表")
    @PostMapping("/getProductListBySellerId")
    public JsonResult getProductListBySellerId(@Valid @RequestBody QueryProductListRequestDto requestDto, BindingResult bindingResult, HttpServletRequest request){
        if (bindingResult.hasErrors()) {
            return JsonResult.fail( bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            List<GetProductAndSpecListVo> res = productFuseAction.getProductListByMerchantId(requestDto, new BigDecimal(getLat(request)), new BigDecimal(getLon(request)));
            return JsonResult.success().addResult("res", res);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询商品异常！");
        }
    }

    @ApiOperation(value="获取SKU-库存/价格等")
    @PostMapping("/getSkuByValueIds")
    public JsonResult getSkuByValueIds(@Valid @RequestBody ValueIdsRequestDto requestDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return JsonResult.fail( bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            Sku res = productFuseAction.getSku(requestDto.getValueIds(), requestDto.getProductId());
            return JsonResult.success().addResult("res", res);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询商品异常！");
        }
    }
}