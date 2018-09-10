package com.netx.boss.web.controller.shoppingmall.bussiness;


import com.netx.shopping.biz.merchantcenter.MerchantExpressAction;
import com.netx.shopping.model.merchantcenter.MerchantExpress;
import com.netx.shopping.vo.QueryExpressRequestDto;
import com.netx.shopping.vo.UpdateExpressRequestDto;
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

@Api(description = "物流公司相关接口")
@RestController
@RequestMapping("/business/express")
public class ExpressController {

    private Logger logger = LoggerFactory.getLogger(ExpressController.class);

    @Autowired
    private MerchantExpressAction merchantExpressAction;

    @ApiOperation(value = "更新快递公司信息", notes = "更新快递公司信息")
    @PostMapping("/add")
    public JsonResult add(){
        try {
            merchantExpressAction.add();
            return JsonResult.success();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("更新快递公司表失败！");
        }
    }

    @ApiOperation(value = "模糊分页查询快递公司列表", notes = "模糊分页查询快递公司列表")
    @PostMapping("/queryExpressList")
    public JsonResult queryExpressList(@Valid @RequestBody QueryExpressRequestDto requestDto, BindingResult bindingResult){
        if( bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            List<MerchantExpress> res = merchantExpressAction.queryMerchantExpressList(requestDto);
            return JsonResult.success().addResult("res", res);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("获取列表异常！");
        }
    }

    @ApiOperation(value = "修改快递热门度")
    @PostMapping("/updateHot")
    public JsonResult updateHot(@Valid @RequestBody UpdateExpressRequestDto requestDto, BindingResult bindingResult){
        if( bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            Boolean res = merchantExpressAction.updateHot(requestDto.getId(), requestDto.getHot());
            if(res) {
                return JsonResult.success();
            }
            return JsonResult.fail("修改快递热门度失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("修改快递热门度异常！");
        }
    }

    @ApiOperation(value = "禁用/解除禁用-快递公司")
    @PostMapping("/disable")
    public JsonResult disable(@RequestParam("expressId") String expressId){
        try {
            Boolean res = merchantExpressAction.disable(expressId);
            if(res) {
                return JsonResult.success();
            }
            return JsonResult.fail("禁用/解除禁用失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("禁用/解除禁用异常！");
        }
    }
}
