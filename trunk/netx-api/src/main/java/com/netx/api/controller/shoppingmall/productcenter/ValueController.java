package com.netx.api.controller.shoppingmall.productcenter;


import com.netx.shopping.biz.productcenter.ValueAction;
import com.netx.shopping.model.productcenter.Value;
import com.netx.shopping.vo.AddValueRequestDto;
import com.netx.shopping.vo.UpdateValueRequestDto;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created By CHENQIAN
 * Description: 属性值控制层
 * Date: 2018-05-09
 */
@Api(description = "属性值相关接口")
@RestController
@RequestMapping("/api/business/value")
public class ValueController {

    private Logger logger = LoggerFactory.getLogger(ValueController.class);

    @Autowired
    private ValueAction valueAction;

    @ApiOperation(value = "添加属性值", notes = "添加属性值")
    @PostMapping("/addValue")
    public JsonResult addValue(@Valid @RequestBody AddValueRequestDto request, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail( bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            Value res = valueAction.addValue(request);
            if(res == null){
                return JsonResult.fail("添加属性值失败！");
            }
            return JsonResult.success().addResult("res",res);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("添加属性值异常！");
        }
    }

    @ApiOperation(value = "修改属性值", notes = "修改属性值")
    @PostMapping("/updateValue")
    public JsonResult updateValue(@Valid @RequestBody UpdateValueRequestDto request, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail( bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            Value res = valueAction.updateValue(request);
            if(res == null){
                return JsonResult.fail("修改属性值失败！");
            }
            return JsonResult.success().addResult("res", res);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("修改属性值异常！");
        }
    }

    @ApiOperation(value = "修改属性值（单属性值）", notes = "修改属性值（单属性值）")
    @PostMapping("/updateValueOne")
    public JsonResult updateValueOne(@Valid @RequestBody UpdateValueRequestDto request, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail( bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            Value res = valueAction.updateValueOne(request);
            if(res == null){
                return JsonResult.fail("修改属性值失败！");
            }
            return JsonResult.success().addResult("res", res);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("修改属性值异常！");
        }
    }

    @ApiOperation(value = "删除属性值", notes = "删除属性值")
    @PostMapping("/deleteValue")
    public JsonResult deleteValue(@RequestParam("valueId") String valueId){
        try {
            if(valueId == null){
                return JsonResult.fail("属性值id不能为空！");
            }
            String res = valueAction.deleteValue(valueId);
            if(res != null){
                return JsonResult.fail("删除属性值失败！"+res);
            }
            return JsonResult.success();
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("删除属性值异常！");
        }
    }

    @ApiOperation(value = "删除属性值（单属性值）", notes = "删除属性值（单属性值）")
    @PostMapping("/deleteValueOne")
    public JsonResult deleteValueOne(@RequestParam("valueId") String valueId,@RequestParam("skuId") String skuId){
        try {
            if(valueId == null){
                return JsonResult.fail("属性值id不能为空！");
            }
            boolean res = valueAction.deleteValueOne(valueId, skuId);
            if(!res){
                return JsonResult.fail("删除属性值失败！");
            }
            return JsonResult.success();
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("删除属性值异常！");
        }
    }

}


