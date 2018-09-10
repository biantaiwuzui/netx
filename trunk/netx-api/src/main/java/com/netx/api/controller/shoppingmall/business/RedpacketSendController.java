package com.netx.api.controller.shoppingmall.business;

import com.netx.api.controller.BaseController;
import com.netx.common.vo.business.*;
import com.netx.fuse.biz.shoppingmall.redpacketcenter.RedpacketRecordFuseAction;
import com.netx.fuse.biz.shoppingmall.redpacketcenter.RedpacketSendFuseAction;
import com.netx.shopping.biz.redpacketcenter.RedpacketRecordAction;
import com.netx.utils.json.JsonResult;
import com.netx.utils.money.Money;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@Api(description = "红包发放相关接口")
@RestController
@RequestMapping("/api/business/redpacketSend")
public class RedpacketSendController extends BaseController {
    private Logger logger= LoggerFactory.getLogger(this.getClass());

    @Autowired
    RedpacketRecordAction redpacketRecordAction;

    @Autowired
    RedpacketRecordFuseAction redpacketRecordFuseAction;

    @Autowired
    RedpacketSendFuseAction redpacketSendFuseAction;

    @ApiOperation(value = "领取红包")
    @PostMapping("/receive")
    public JsonResult receive(@Valid @RequestBody ReceiveRedPacketRequestDto request, BindingResult bindingResult,HttpServletRequest requestDto){
        if (bindingResult.hasErrors()){
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
        try{
            BigDecimal res= redpacketSendFuseAction.receive(request);
            if (res != null){
                return JsonResult.success().addResult("res",res);
            }
            //return JsonResult.fail("领取红包失败！");
            return JsonResult.fail("不满足领取红包条件！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail(e.getMessage());
        }
    }

    @ApiOperation("获取最新红包信息")
    @PostMapping("/getRedpacketInfo")
    public JsonResult getRedpacketInfo(String userId,HttpServletRequest requestDto){
        try{
            userId = getUserId(userId,requestDto);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail("用户id不能为空");
            }
            GetRedpacketInfoResponseDto getRedpacketInfoResponseDto= redpacketSendFuseAction.getRedpacketInfo(userId);
            return JsonResult.success().addResult("getRedpacketInfoResponseDto",getRedpacketInfoResponseDto);
//            if (getRedpacketInfoResponseDto != null){
//                return JsonResult.success().addResult("getRedpacketInfoResponseDto",getRedpacketInfoResponseDto);
//            }
            //return JsonResult.fail("获取最新红包信息失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            //return JsonResult.fail("获取最新红包信息异常！");
        }
        return JsonResult.success().addResult("getRedpacketInfoResponseDto", null);
    }

    @ApiOperation(value = "发放红包")
    @PostMapping("/send")
    public JsonResult send(){
        try{
            boolean res= redpacketSendFuseAction.send();
            if (!res){
                return JsonResult.fail("发放红包失败！");
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            e.printStackTrace();
            return JsonResult.fail("发放红包异常！");
        }
        return JsonResult.success();
    }

    @ApiOperation("查看明细")
    @PostMapping("/see")
    public JsonResult see(@Valid @RequestBody SeeRedpacketRecordDto dto){
        try{
            RedpacketRecordResponseDto res= redpacketRecordFuseAction.see(dto);
            if (res!=null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("查看明细失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            e.printStackTrace();
            return JsonResult.fail("查看明细异常！");
        }
    }

    @ApiOperation(value = "测试")
    @PostMapping("/test")
    public JsonResult test(@Valid @RequestBody String msg,Long time,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
//            businessQuartzService.redpacketRemind(msg,time);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonResult.fail("失败！");
        }
        return JsonResult.success();
    }

    @ApiOperation(value = "定时器，检查红包是否发放完毕")
    @PostMapping("/checkIsSendFinish")
    public JsonResult checkIsSendFinish(@Valid @RequestBody CheckIsSendFinishRequestDto dto,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            boolean res= redpacketSendFuseAction.checkIsSendFinish(dto.getRedpacketSendId());
            if (!res){
                return JsonResult.fail("定时器，检查红包是否发放完毕失败！");
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            e.printStackTrace();
            return JsonResult.fail("定时器，检查红包是否发放完毕异常！");
        }
        return JsonResult.success();
    }

    @ApiOperation(value = "查看用户红包明细")
    @PostMapping("/seeRedpacketToUser")
    public JsonResult seeRedpacketToUser(String userId,HttpServletRequest requestDto){
        try {
            userId = getUserId(userId,requestDto);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail("用户id不能为空");
            }
            List<RedpacketRecordDto> res= redpacketRecordFuseAction.seeRedpacketToUser(userId);
            if (res != null){
                return JsonResult.success().addResult("res",res);
            }
            return JsonResult.fail("查看用户红包明细失败！");
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            return JsonResult.fail("查看用户红包明细异常！");
        }
    }

    @ApiOperation(value = "根据用户id获取钱包红包金额")
    @PostMapping("/seeWalletRedpacket")
    public JsonResult seeWalletRedpacket(String userId,HttpServletRequest requestDto){
        try {
            userId = getUserId(userId,requestDto);
            if(StringUtils.isEmpty(userId)){
                return JsonResult.fail("用户id不能为空");
            }
            BigDecimal amount= redpacketRecordAction.seeWalletRedpacket(userId);
            if (amount != null){
                String res = Money.getMoneyString(amount.longValue());
                return JsonResult.success().addResult("amount",res);
            }
            return JsonResult.fail("取钱包红包金额失败！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            e.printStackTrace();
            return JsonResult.fail("取钱包红包金额异常！");
        }
    }
}
