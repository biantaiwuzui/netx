package com.netx.api.controller.shoppingmall.back;

import com.netx.common.vo.business.*;
import com.netx.fuse.biz.shoppingmall.business.SellerFuseAction;
import com.netx.fuse.biz.shoppingmall.product.ProductFuseAction;
import com.netx.shopping.biz.business.SellerAction;
import com.netx.shopping.biz.product.ProductAction;
import com.netx.shopping.vo.GetProductResponseVo;
import com.netx.shopping.vo.GetSellerListResponseVo;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * Created By liwei
 * Description: 网商后台管理控制层
 * Date: 2017-09-17
 */
@Api(description = "网商后台管理相关接口")
@RestController
@RequestMapping("/api/business/businessBackManage")
public class BusinessBackManageController {

    private Logger logger = LoggerFactory.getLogger(BusinessBackManageController.class);

    @Autowired
    SellerAction sellerAction;

    @Autowired
    ProductAction productAction;

    @Autowired
    SellerFuseAction sellerFuseAction;

    @Autowired
    ProductFuseAction productFuseAction;

    @ApiOperation(value = "商家列表", notes = "商家列表，黑名单列表/白名单列表，可以根据名称模糊查询。")
    @PostMapping("/sellerList")
    public JsonResult sellerList(@RequestBody @Valid BackManageSellerListRequestDto request, BindingResult bindingResult){
        if( bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            List<GetSellerListResponseVo> sellerList = sellerFuseAction.sellerList(request);
            if (sellerList != null){
                return JsonResult.success().addResult("sellerList",sellerList);
            }
            return JsonResult.fail("查询商家失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询商家失败！");
        }
    }

    @ApiOperation(value = "操作商家", notes = "操作商家, 拉黑或者解除拉黑。")
    @PostMapping("/optSeller")
    public JsonResult optSeller(@RequestBody @Valid BackManageOptSellerRequestDto request, BindingResult bindingResult){
        if( bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            boolean res = sellerAction.optSeller(request);
            if(!res){
                return JsonResult.fail("操作商家失败！");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("操作商家失败！");
        }
        return JsonResult.success();

    }

    @ApiOperation(value = "商品列表", notes = "商品列表，黑名单列表/白名单列表，可以根据名称模糊查询。")
    @PostMapping("/goodsList")
    public JsonResult goodsList(@Valid @RequestBody BackManageGoodsListRequestDto request, BindingResult bindingResult){
        if( bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            List<GetProductResponseVo> goodsList = productFuseAction.goodsList(request);
            if (goodsList != null){
                return JsonResult.success().addResult("goodsList",goodsList);
            }
            return JsonResult.fail("查询商品失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询商品失败！");
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

    @ApiOperation(value = "商家搜索",notes = "商家搜索")
    @PostMapping("/searchSeller")
    public JsonResult searchSeller(@Valid @RequestBody SearchSelleRequestDto requestDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try{
            List<GetSellerListResponseVo> list= sellerFuseAction.searchSeller(requestDto);
            if (list != null){
                return JsonResult.success().addResult("list",list);
            }
            return JsonResult.fail("商家搜索失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("商家搜索失败！");
        }
    }

    @ApiOperation(value = "商品搜索",notes = "商品搜索")
    @PostMapping("/searchGoods")
    public JsonResult searchGoods(@Valid @RequestBody SearchGoodsRequestDto requestDto,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try{
            List<GetProductResponseVo> list= productFuseAction.searchGoods(requestDto);
            if (list != null){
                return JsonResult.success().addResult("list",list);
            }
           return JsonResult.fail("商品搜索失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("商品搜索失败！");
        }
    }
}
