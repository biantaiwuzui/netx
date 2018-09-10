package com.netx.api.controller.ucenter.user;

import com.netx.api.controller.BaseController;
import com.netx.common.user.dto.wangMing.*;
import com.netx.common.vo.common.PageRequestDto;
import com.netx.ucenter.biz.user.*;
import com.netx.ucenter.model.user.UserCredit;
import com.netx.ucenter.model.user.UserScore;
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
import java.util.List;

@Api(description = "网名模块")
@RestController
@RequestMapping("/api/WangMing/")
public class WangMingController extends BaseController{

    @Autowired
    private UserAction userAction;
    @Autowired
    private UserCreditAction userCreditAction;
    @Autowired
    private UserContributionAction userContributionAction;
    @Autowired
    private UserValueAction userValueAction;
    @Autowired
    private UserScoreAction userScoreAction;
    @Autowired
    private UserIncomeAction userIncomeAction;

    private Logger logger = LoggerFactory.getLogger(WangMingController.class);
/*
    @ResponseBody
    @PostMapping ("selectWangMingList")
    @ApiOperation(value="根据网名类型（身价、收益、贡献、信用、积分）分页查询用户数据", notes = "按总额由高到低，相同时按距离由近及远")
    public JsonResult selectWangMingList(@Valid @RequestBody SelectWangMingListRequestDto request, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try{
            List<SelectWangMingListBaseResponseDto> list = userAction.selectWangMingList(request);
            return JsonResult.success().addResult("list",list);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询出现异常");
        }
    }*/

    @ResponseBody
    @PostMapping ("addValueRecord")
    @ApiOperation(value="添加身价流水")
    public JsonResult addValueRecord(@Valid @RequestBody AddValueRecordRequestDto request, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try{
            return super.getResult(userValueAction.addValueRecord(request),"操作失败");
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("出现异常");
        }
    }

    @ResponseBody
    @PostMapping ("addIncomeRecord")
    @ApiOperation(value="添加收益流水")
    public JsonResult addIncomeRecord(@Valid @RequestBody AddIncomeRecordRequestDto request, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try{
            return super.getResult(userIncomeAction.addIncomeRecord(request),"操作失败");
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("出现异常");
        }
    }

    @ResponseBody
    @PostMapping ("addContributionRecord")
    @ApiOperation(value="添加贡献流水")
    public JsonResult addContributionRecord(@Valid @RequestBody AddContributionRequestDto request, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try{
            return super.getResult(userContributionAction.addContributionRecord(request),"操作失败");
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("出现异常");
        }
    }

    @ResponseBody
    @PostMapping ("addCreditRecord")
    @ApiOperation(value="添加信用流水")
    public JsonResult addCreditRecord(@Valid @RequestBody AddCreditRecordRequestDto request, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try{
            return super.getResult(userCreditAction.addCreditRecord(request),"操作失败");
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("添加信用流水异常");
        }
    }

    @ResponseBody
    @PostMapping ("addScoreRecord")
    @ApiOperation(value="添加积分流水")
    public JsonResult addScoreRecord(@Valid @RequestBody AddScoreRecordRequestDto request, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try{
            return super.getResult(userScoreAction.addScoreRecord(request),"操作失败");
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("添加积分流水异常");
        }
    }

    @ResponseBody
    @PostMapping ("selectScoreRecordList")
    @ApiOperation(value="分页查询积分流水记录")
    public JsonResult selectScoreRecordList(@Valid @RequestBody PageRequestDto dto, BindingResult bindingResult, HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId = null;
        try {
            userId = getUserId(request);
            if(userId==null){
                return JsonResult.fail("用户id不能为空");
            }
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage());
        }
        try{
            List<UserScore> list = userScoreAction.selectScoreRecordList(userId,dto.getCurrent(),dto.getSize());
            return JsonResult.success().addResult("list",list);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询异常");
        }
    }

    @ResponseBody
    @PostMapping ("selectCreditRecordList")
    @ApiOperation(value="分页查询信用流水记录")
    public JsonResult selectCreditRecordList(@Valid @RequestBody PageRequestDto dto, BindingResult bindingResult,HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId = null;
        try {
            userId = getUserId(request);
            if(userId==null){
                return JsonResult.fail("用户id不能为空");
            }
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage());
        }
        try{
            List<UserCredit> list = userCreditAction.selectCreditRecordList(userId,dto.getCurrent(),dto.getSize());
            return JsonResult.success().addResult("list",list);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
            return JsonResult.fail("查询异常");
        }
    }
}
