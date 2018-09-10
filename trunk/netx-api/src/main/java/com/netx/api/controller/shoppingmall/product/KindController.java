package com.netx.api.controller.shoppingmall.product;

import com.netx.api.controller.BaseController;
import com.netx.common.vo.business.AddKindRequestDto;
import com.netx.common.vo.business.DeleteKindRequestDto;
import com.netx.shopping.biz.product.KindAction;
import com.netx.shopping.model.product.SellerKind;
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
 * Description: 商品自选分类控制层
 * Date: 2017-09-04
 */
@Api(description = "网商商品自选分类相关接口")
@RestController
@RequestMapping("/api/business/kind")
public class KindController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(KindController.class);

    @Autowired
    KindAction kindAction;

    @ApiOperation(value = "新增或修改商品类别", notes = "新增或修改商品类别")
    @PostMapping("/addOrUpdate")
    public JsonResult addOrUpdate(@Valid @RequestBody AddKindRequestDto request, BindingResult bindingResult,HttpServletRequest requestDto){
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
            SellerKind res = kindAction.addOrUpdate(request);
            if(!StringUtils.isNotEmpty(res.getName())){
                return JsonResult.fail("新增或修改商品类别失败！");
            }
            return JsonResult.success().addResult("res",res);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("新增或修改商品类别异常！");
        }
    }

    @ApiOperation(value = "删除分类类别", notes = "删除分类类别")
    @PostMapping("/delete")
    public JsonResult delete(@Valid @RequestBody DeleteKindRequestDto request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            boolean res = kindAction.delete(request.getId());
            return getResult(res,"删除分类类别失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("删除分类类别异常！");
        }
    }

    @ApiOperation(value = "获取商品分类类别列表", notes = "获取商品分类类别列表")
    @PostMapping("/list")
    public JsonResult list(String userId,HttpServletRequest requestDto){
        try {
            userId = getUserId(userId,requestDto);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail("用户id不能为空");
            }
            List<SellerKind> list = kindAction.list(userId);
            if (list != null){
                return JsonResult.success().addResult("list",list);
            }
            return JsonResult.fail("获取商品分类类别列表失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取商品分类类别列表异常！");
        }
    }
}
