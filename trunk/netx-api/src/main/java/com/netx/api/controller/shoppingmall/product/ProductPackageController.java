package com.netx.api.controller.shoppingmall.product;

import com.netx.api.controller.BaseController;
import com.netx.common.vo.business.AddGoodsPackageRequestDto;
import com.netx.common.vo.business.DeleteGoodsPackageRequestDto;
import com.netx.shopping.biz.product.ProductPackageAction;
import com.netx.shopping.model.product.ProductPackage;
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
 * Description: 商品包装明细控制层
 * Date: 2017-09-14
 */
@Api(description = "网商商品包装明细相关接口")
@RestController
@RequestMapping("/api/business/goodsPackage")
public class ProductPackageController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(ProductPackageController.class);

    @Autowired
    ProductPackageAction productPackageAction;

    @ApiOperation(value = "新增或修改商品包装明细", notes = "新增或修改商品包装明细")
    @PostMapping("/addOrUpdate")
    public JsonResult addOrUpdate(@Valid @RequestBody AddGoodsPackageRequestDto request, BindingResult bindingResult,HttpServletRequest requestDto){
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
            ProductPackage res = productPackageAction.addOrUpdate(request);
            if(!StringUtils.isNotEmpty(res.getName())){
                return JsonResult.fail("新增或修改商品包装明细失败！");
            }
            return JsonResult.success().addResult("res",res);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("新增或修改商品包装明细异常！");
        }
    }

    @ApiOperation(value = "删除商品包装明细", notes = "删除商品包装明细")
    @PostMapping("/delete")
    public JsonResult delete(@Valid @RequestBody DeleteGoodsPackageRequestDto request, BindingResult bindingResult){
        if( bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            boolean res = productPackageAction.delete(request.getId());
            if(!res){
                return JsonResult.fail("该包装明细还被使用，不能删除！");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("删除商品包装明细异常！");
        }
        return JsonResult.success();
    }

    @ApiOperation(value = "获取商品包装明细列表", notes = "获取商品包装明细列表")
    @PostMapping("/list")
    public JsonResult list(String userId,HttpServletRequest requestDto){
        try {
            userId = getUserId(userId,requestDto);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail("用户id不能为空");
            }
            List<ProductPackage> list = productPackageAction.getGoodsPackageListByUserId(userId);
            if (list != null){
                return JsonResult.success().addResult("list",list);
            }
            return JsonResult.fail("获取商品包装明细列表失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取商品包装明细列表异常！");
        }
    }
}