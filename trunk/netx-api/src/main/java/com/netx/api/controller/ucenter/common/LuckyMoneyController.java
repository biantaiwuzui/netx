package com.netx.api.controller.ucenter.common;

import com.netx.api.controller.BaseController;
import com.netx.common.vo.common.ExamineRedpacketResponseDto;
import com.netx.common.vo.common.LuckMoneyQueryDto;
import com.netx.common.vo.common.LuckyMoneySaveOrUpdateDto;
import com.netx.ucenter.biz.common.LuckyMoneyAction;
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

/**
 * Create by wongloong on 17-9-8
 */
@Api(description = "红包设置")
@RestController
@RequestMapping("/api/luckyMoney")
public class LuckyMoneyController extends BaseController{
    private Logger logger = LoggerFactory.getLogger(LuckyMoneyController.class);
    @Autowired
    private LuckyMoneyAction luckyMoneyAction;

    @ApiOperation(value = "添加红包设置,所有参数均不能为空")
    @PostMapping("/add")
    public JsonResult add(@RequestBody LuckyMoneySaveOrUpdateDto requestDto) {
        try {
            return super.getResult(luckyMoneyAction.add(requestDto),"操作失败");
        } catch (Exception e) {
            logger.error("添加红包设置失败："+e.getMessage(), e);
            return JsonResult.fail("添加红包设置失败");
        }
    }

    @ApiOperation(value = "获取红包设置", notes = "按照时间倒序排列,如果为空则没有数据,如果不传time的话,查询全部数据,如果传time的话,查询的是大于这个时间的红包设置")
    @PostMapping("/query")
    public JsonResult query(@RequestBody LuckMoneyQueryDto request) {
        try {
            return JsonResult.success().addResult("list",luckyMoneyAction.query(request));
        } catch (Exception e) {
            logger.error("获取红包设置出错："+e.getMessage(), e);
            return JsonResult.fail("获取红包设置出错");
        }
    }

    @ApiOperation(value = "将昨天待生效的红包设置改为今日的红包设置")
    @PostMapping("/updateLuckMoneySet")
    public JsonResult updateLuckMoneySet(){
        try{
            boolean res=luckyMoneyAction.updateLuckMoneySet();
            return super.getResult(res,"更新失败");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("更新异常");
        }

    }

    @PostMapping("/examineRedpacket")
    @ApiOperation(value = "审核红包设置")
    public JsonResult examine(@RequestBody @Valid ExamineRedpacketResponseDto request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            boolean res=luckyMoneyAction.examine(request);
            return JsonResult.success().addResult("result",res);
        } catch (Exception e) {
            logger.error("审核其他信息出错："+e.getMessage(), e);
            return JsonResult.fail("审核其他信息出错");
        }
    }
}
