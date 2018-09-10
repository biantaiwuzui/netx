package com.netx.api.controller.ucenter.common;

import com.netx.api.controller.BaseController;
import com.netx.common.vo.common.AuditExamineFinanceDto;
import com.netx.common.vo.common.ExamineFinanceDto;
import com.netx.ucenter.biz.common.ExamineFinanceAction;
import com.netx.ucenter.model.common.CommonExamineFinance;
import com.netx.ucenter.service.common.CommonServiceProvider;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;


@Api(description = "财务其它管理")
@RestController
@RequestMapping("/api/financeOtherManager")
public class ExamineFinanceController extends BaseController{
    private Logger logger = LoggerFactory.getLogger(WalletBillController.class);

    @Autowired
    private ExamineFinanceAction examineFinanceAction;

    @Autowired
    private CommonServiceProvider commonServiceProvider;

    @ApiOperation("审批提交，普通管理员提交，需要审核")
    @PostMapping("/submit")
    public JsonResult submitExamineFinance(@RequestBody @Valid ExamineFinanceDto request, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            return super.getResult(examineFinanceAction.submitExamineFinance(request),"操作失败");
        }catch (Exception e){
            logger.error("添加财务审批记录："+e.getMessage(), e);
            return JsonResult.fail("添加财务审批记录");
        }
    }

    @ApiOperation("审批提交，超级管理员可操作")
    @PostMapping("/submitSuper")
    public JsonResult submitExamineFinanceSuper(@RequestBody @Valid ExamineFinanceDto request, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            CommonExamineFinance examineFinance = new CommonExamineFinance();
            BeanUtils.copyProperties(request,examineFinance);
            examineFinance.setStatus(1);
            examineFinance.setCreateTime(new Date());
            commonServiceProvider.getExamineFinanceService().insert(examineFinance);
            return JsonResult.success("提交成功");
        }catch (Exception e){
            logger.error("添加财务管理异常："+e.getMessage(), e);
            return JsonResult.fail("添加财务管理异常");
        }
    }

    @ApiOperation("查询财务未审批记录")
    @PostMapping("/query")
    public JsonResult queryExamineFinance(){
        try {
            return JsonResult.success().addResult("result", examineFinanceAction.queryExamineFinancesNotAudit());
        }catch (Exception e){
            logger.error("查询异常："+e.getMessage(), e);
            return JsonResult.fail("查询异常");
        }
    }

    @ApiOperation("根据id获取财务管理记录")
    @PostMapping("/get")
    public JsonResult getExamineFinanceById( @RequestBody @NotNull String id){
        try {
            return JsonResult.success().addResult("result", commonServiceProvider.getExamineFinanceService().selectById(id));
        }catch (Exception e){
            logger.error("根据id获取财务管理记录异常："+e.getMessage(), e);
            return JsonResult.fail("查询异常");
        }
    }

    @ApiOperation("审批财务记录")
    @PostMapping("/audit")
    public JsonResult auditExamineFinance(@RequestBody @Valid AuditExamineFinanceDto request, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            return super.getResult(examineFinanceAction.auditExamineFinances(request),"记录不存在");
        }catch (Exception e){
            logger.error("审批财务记录异常："+e.getMessage(), e);
            return JsonResult.fail("审批财务记录异常");
        }
    }


}
