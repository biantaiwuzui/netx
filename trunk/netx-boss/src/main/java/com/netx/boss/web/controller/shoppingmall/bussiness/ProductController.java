package com.netx.boss.web.controller.shoppingmall.bussiness;

import com.netx.fuse.biz.shoppingmall.productcenter.ProductFuseAction;
import com.netx.shopping.biz.productcenter.ProductAction;
import com.netx.shopping.model.productcenter.Product;
import com.netx.shopping.model.productcenter.ProductManagement;
import com.netx.shopping.vo.ProductForceUpRequestDto;
import com.netx.shopping.vo.QueryBusinessProductRequestDto;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Created By BXT on 18-5-24
 */
@Api(description = "网商商品相关接口")
@RestController
@RequestMapping("/business/product")
public class ProductController {

    private Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    ProductAction productAction;

    @Autowired
    ProductFuseAction productFuseAction;

    @ApiOperation(value = "模糊分页查询商品列表")
    @PostMapping("/queryProductList")
    public JsonResult queryProductList(@Valid @RequestBody QueryBusinessProductRequestDto requestDto, BindingResult bindingResult){
        if( bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            Map<String, Object> map = productFuseAction.queryMerchantProductList(requestDto);
            return JsonResult.successJsonResult(map);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取列表异常！");
        }
    }

    @ApiOperation(value = "商品上架")
    @PostMapping("/up")
    public JsonResult up(@Valid @RequestBody ProductForceUpRequestDto productForceUpRequestDto, BindingResult bindingResult){
        if( bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            if (productAction.forceUpProduct(productForceUpRequestDto)){
                return JsonResult.success();
            }
            return JsonResult.fail("商品上架失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("商品上架异常！");
        }
    }

    @ApiOperation(value = "强制下架")
    @PostMapping("/coercionDown")
    public JsonResult coercionDown(@Valid @RequestBody ProductForceUpRequestDto productForceUpRequestDto , BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            if (productAction.forceDownProduct(productForceUpRequestDto)){
                return JsonResult.success();
            }
            return JsonResult.fail("强制下架失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("强制下架异常！");
        }
    }

}
