package com.netx.api.controller.ucenter.common;

import com.netx.api.controller.BaseController;
import com.netx.common.vo.common.PageRequestDto;
import com.netx.common.vo.common.SuggestionAddRequestDto;
import com.netx.common.vo.common.SuggestionReplyRequestDto;
import com.netx.ucenter.biz.common.SuggestionAction;
import com.netx.ucenter.model.common.CommonSuggestion;
import com.netx.ucenter.service.common.CommonServiceProvider;
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
import java.util.Date;

/**
 * Create by wongloong on 17-9-9
 */
@RestController
@Api(description = "意见与建议操作")
@RequestMapping("/api/suggestion")
public class SuggestionController extends BaseController{
    private Logger logger = LoggerFactory.getLogger(SuggestionController.class);
    @Autowired
    private SuggestionAction suggestionAction;

    @Autowired
    private CommonServiceProvider commonServiceProvider;

    @PostMapping("add")
    @ApiOperation(value = "添加建议")
    public JsonResult addSuggestion(@Valid @RequestBody SuggestionAddRequestDto request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            return super.getResult(suggestionAction.insert(request),"操作失败");
        } catch (Exception e) {
            logger.error("添加建议异常："+e.getMessage(), e);
            return JsonResult.fail("添加建议异常");
        }
    }

    @PostMapping("/reply")
    @ApiOperation(value = "回复意见")
    public JsonResult replySuggestion(@Valid @RequestBody SuggestionReplyRequestDto request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            CommonSuggestion wzCommonSuggestion = commonServiceProvider.getSuggestionService().selectById(request.getId());
            if (null == wzCommonSuggestion) {
                logger.warn("未找到该记录id:" + request.getId());
                return JsonResult.fail("操作失败");
            }
            wzCommonSuggestion.setReplacerId(request.getReplyUserId());
            wzCommonSuggestion.setReplyContent(request.getReplyContent());
            wzCommonSuggestion.setUpdateTime(new Date());
            wzCommonSuggestion.setUpdateUserId(request.getReplyUserId());
            return super.getResult(commonServiceProvider.getSuggestionService().updateById(wzCommonSuggestion),"操作失败");
        } catch (Exception e) {
            logger.error("回复意见异常："+e.getMessage(), e);
            return JsonResult.fail("回复意见异常");
        }
    }

    @PostMapping("/list")
    @ApiOperation(value = "管理员查看意见")
    public JsonResult queryPageList(@RequestBody PageRequestDto request) {
        try {
            return JsonResult.success().addResult("list", suggestionAction.getListByPage(request.getCurrent(),request.getSize()));
        } catch (Exception e) {
            logger.error("管理员查看意见出错："+e.getMessage(), e);
            return JsonResult.fail("管理员查看意见出错");
        }
    }
}
