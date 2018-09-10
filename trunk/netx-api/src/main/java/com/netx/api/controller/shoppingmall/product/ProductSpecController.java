package com.netx.api.controller.shoppingmall.product;

import com.netx.api.controller.BaseController;
import com.netx.common.vo.business.AddGoodsSpecRequertDto;
import com.netx.common.vo.business.DelectGoodsSpecImplRequestDto;
import com.netx.common.vo.business.DelectGoodsSpecRequestDto;
import com.netx.shopping.biz.product.ProductSpecItemAction;
import com.netx.shopping.biz.product.ProductSpecAction;
import com.netx.shopping.model.product.ProductSpec;
import com.netx.shopping.vo.ProductSpecResponseVo;
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
 * Description: 商品规格控制层
 * Date: 2017-09-13
 */
@Api(description = "网商商品规格相关接口")
@RestController
@RequestMapping("/api/business/ProductSpec")
public class ProductSpecController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(ProductSpecController.class);

    @Autowired
    ProductSpecAction productSpecAction;

    @Autowired
    ProductSpecItemAction productSpecItemAction;

    @ApiOperation(value = "新增商品规格", notes = "新增商品规格")
    @PostMapping("/add")
    public JsonResult add(@Valid @RequestBody AddGoodsSpecRequertDto request, BindingResult bindingResult,HttpServletRequest requestDto){
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
            ProductSpec res = productSpecAction.add(request);
            if (res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("新增商品规格失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("新增商品规格失败！");
        }
    }

    @ApiOperation(value = "删除商品规格", notes = "删除商品规格")
    @PostMapping("/delete")
    public JsonResult delete(@Valid @RequestBody DelectGoodsSpecRequestDto request, BindingResult bindingResult){
        if( bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            boolean res = productSpecAction.delete(request);
            return getResult(res,"删除商品规格失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("删除商品规格失败！");
        }
    }

    @ApiOperation(value = "删除商品规格分类项", notes = "删除商品规格分类项")
    @PostMapping("/deleteSpecImpl")
    public JsonResult deleteSpecImpl(@Valid @RequestBody DelectGoodsSpecImplRequestDto request, BindingResult bindingResult){
        if( bindingResult.hasErrors()){
            return JsonResult.fail( bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            boolean res = productSpecItemAction.delete(request);
            return getResult(res,"删除商品规格分类项失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("删除商品规格分类项失败！");
        }
    }

    @ApiOperation(value = "获取商品规格分类列表", notes = "获取商品规格分类列表，用于规格分类下拉选择")
    @PostMapping("/specList")
    public JsonResult specList(String userId,HttpServletRequest requestDto){
        try {
            userId = getUserId(userId,requestDto);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail("用户id不能为空");
            }
            List<ProductSpec> list = productSpecAction.specList(userId);
            if (list != null){
                return JsonResult.success().addResult("list",list);
            }
            return JsonResult.fail("获取商品规格分类列表失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取商品规格分类列表失败！");
        }
    }

    @ApiOperation(value = "获取商品规格列表", notes = "获取商品规格列表，用于可选规格选择")
    @PostMapping("/specItemList")
    public JsonResult specItemList(String userId,HttpServletRequest requestDto){
        try {
            userId = getUserId(userId,requestDto);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail("用户id不能为空");
            }
            List<ProductSpecResponseVo> list = productSpecAction.specItemList(userId);
            if (list != null){
                return JsonResult.success().addResult("list",list);
            }
            return JsonResult.fail("获取商品规格列表失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取商品规格列表失败！");
        }
    }
}