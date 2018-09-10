package com.netx.boss.web.controller.shoppingmall.bussiness;

import com.netx.common.user.dto.common.CommonListDto;
import com.netx.common.vo.common.GetKidTagsRequestDto;
import com.netx.shopping.biz.productcenter.CategoryAction;
import com.netx.shopping.model.productcenter.Category;
import com.netx.shopping.vo.AddCategoryRequestDto;
import com.netx.shopping.vo.DeleteRequestDto;
import com.netx.shopping.vo.QueryCategoryRequestDto;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Api(description = "网商标签相关接口")
@RestController
@RequestMapping(value ="/business/category")
public class CategoryController {

    private Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    CategoryAction categoryAction;

    @ApiOperation(value = "添加或修改网商类目", notes = "添加或修改网商类目")
    @PostMapping("/addOrUpdate")
    public JsonResult addOrUpdate(@Valid @RequestBody AddCategoryRequestDto request, BindingResult bindingResult){
        if( bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            Category res = categoryAction.addOrUpdate(request);
            if(res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("添加或修改商家类目失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            e.printStackTrace();
            return JsonResult.fail("添加或修改商家类目异常！");
        }
    }

    @ApiOperation(value = "获取网商类目列表", notes = "获取网商类目列表")
    @PostMapping("/list")
    public JsonResult list(@Valid @RequestBody QueryCategoryRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }try {
            Map<String, Object> map = categoryAction.queryCategroyList(request);
            return JsonResult.successJsonResult(map);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取网商类目异常！");
        }
    }

    @ApiOperation(value = "禁用网商类目", notes = "禁用网商类目")
    @PostMapping("/delete")
    public JsonResult delete(@Valid @RequestBody DeleteRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            Boolean res = categoryAction.deleteById(requestDto.getId());
            if(res){
                return JsonResult.success();
            }
            return JsonResult.fail("禁用网商类目失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("禁用网商类目异常！");
        }
    }

    @ApiOperation(value = "恢复网商类目", notes = "恢复网商类目")
    @PostMapping("/recovery")
    public JsonResult recovery(@Valid @RequestBody DeleteRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            String res = categoryAction.recoveryById(requestDto.getId());
            if(StringUtils.isBlank(res)){
                return JsonResult.success();
            }
            return JsonResult.fail(res);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("恢复网商类目异常！");
        }
    }


}
