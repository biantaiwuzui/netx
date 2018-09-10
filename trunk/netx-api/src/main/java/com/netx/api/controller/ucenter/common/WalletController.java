package com.netx.api.controller.ucenter.common;

import com.netx.api.controller.BaseController;
import com.netx.common.redis.model.UserGeo;
import com.netx.common.vo.common.*;
import com.netx.fuse.biz.ucenter.WalletFuseAction;
import com.netx.ucenter.biz.common.BillAction;
import com.netx.ucenter.biz.common.WalletAction;
import com.netx.ucenter.model.common.CommonWallet;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.service.common.BillService;
import com.netx.ucenter.service.common.CommonServiceProvider;
import com.netx.ucenter.service.user.UserService;
import com.netx.utils.json.ApiCode;
import com.netx.utils.json.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import com.netx.utils.money.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Create by wongloong on 17-9-15
 */
@Api(description = "钱包相关")
@RestController
@RequestMapping("/api/wallet")
public class WalletController extends BaseController{
    private Logger logger = LoggerFactory.getLogger(WalletController.class);
    @Autowired
    private WalletAction walletAction;
    @Autowired
    private WalletFuseAction walletFuseAction;
    @Autowired
    private CommonServiceProvider commonServiceProvider;
    @Autowired
    private BillAction billAction;
    @Autowired
    private UserService userService;
    /*
    @ApiOperation(value = "修改提现账户",notes = "该接口已经不再使用")
    @PostMapping("/modifyAccount")
    public JsonResult modifyWalletAccount(@RequestBody @Valid WalletModifyAccountRequestDto dto, BindingResult bindingResult,HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId = null;
        try {
            userId = getUserId(request);
            if(userId==null) {
                return JsonResult.fail("用户id不能为空");
            }
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage());
        }
        try {
            walletAction.modifyAccount(userId , dto);
            return JsonResult.success();
        } catch (Exception e) {
            logger.error("修改提现账户异常："+e.getMessage(), e);
            return JsonResult.fail("修改提现账户异常");
        }
    }*/

//    @ApiOperation("获取修改提现账户验证码")
//    @PostMapping("/modifyAliAccount")
//    public Result getCodeAccount(@RequestBody @Valid WalletCodeAccountRequestDto request, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return Result.newFailure(ErrorCode.ERROR_VALIDATION_FAIL);
//        }
//        try {
//
//            return Result.newSuccess();
//        } catch (Exception e) {
//            logger.error("修改支付宝提现账户异常", e);
//            return Result.newException(e);
//        }
//    }

    @ApiOperation("修改支付宝提现账户")
    @PostMapping("/modifyAliAccount")
    public JsonResult modifyAliAccount(@RequestBody @Valid WalletModifyAliAccountRequestDto dto, BindingResult bindingResult,HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId = null;
        try {
            userId = getUserId(request);
            if(userId==null) {
                return JsonResult.fail("用户id不能为空");
            }
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage());
        }
        try {
            String returnStr=walletAction.modifyAliAccount(userId,dto);
            if(!returnStr.equals("success")){
                return JsonResult.fail(returnStr);
            }
            return JsonResult.success();
        } catch (Exception e) {
            logger.error("修改支付宝提现账户异常："+e.getMessage(), e);
            return JsonResult.fail("修改支付宝提现账户异常");
        }
    }

    @ApiOperation("修改微信提现账户")
    @PostMapping("/modifyWechatAccount")
    public JsonResult modifyWechatAccount(@RequestBody @Valid WalletModifyWechatAccountRequestDto dto, BindingResult bindingResult,HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId = null;
        try {
            userId = getUserId(request);
            if(userId==null) {
                return JsonResult.fail("用户id不能为空");
            }
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage());
        }
        try {
            String returnStr=walletAction.modifyWechatAccount(userId,dto);
            if(!returnStr.equals("success")){
                return JsonResult.fail(returnStr);
            }
            return JsonResult.success();
        } catch (Exception e) {
            logger.error("修改微信提现账户异常："+e.getMessage(), e);
            return JsonResult.fail("修改微信提现账户异常");
        }
    }

    @ApiOperation(value = "查询提现账户",notes = "返回值key:result 类型:WalletFindAccountResponseVo")
    @GetMapping("/findAccount")
    public JsonResult findWalletAccount(HttpServletRequest request) {
        String userId = null;
        try {
            userId = getUserId(request);
            if(userId==null) {
                return JsonResult.fail("用户id不能为空");
            }
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage());
        }
        try {
            WalletFindAccountResponseVo account = walletAction.findAccount(userId);
            return JsonResult.success().addResult("result",account);
        } catch (Exception e) {
            logger.error("查询提现账户异常："+e.getMessage(), e);
            return JsonResult.fail("查询异常");
        }
    }

    @ApiOperation(value = "获得当前可用零钱数,传userId",notes = "返回值key:result 类型:BigDecimal")
    @GetMapping("/amount")
    public JsonResult getAmount(HttpServletRequest request) {
        String userId = null;
        try {
            userId = getUserId(request);
            if(userId==null) {
                return JsonResult.fail("用户id不能为空");
            }
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage());
        }
        try {
            BigDecimal amount = walletAction.getAmount(userId);
            return JsonResult.success().addResult("result",amount);
        } catch (Exception e) {
            logger.error("查询可用零钱数异常："+e.getMessage(), e);
            return JsonResult.fail("查询可用零钱数异常");
        }
    }

    @ApiOperation(value = "充值钱包阿里支付",notes = "返回值key:result 类型:ThirdPayResponse")
    @PostMapping("/rechargeUseAli")
    public JsonResult rechargeUseAli(@RequestBody @Valid WalletRechargeRequestDto requestDto, BindingResult bindingResult,HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId = null;
        try {
            userId = getUserId(request);
            if(userId==null) {
                return JsonResult.fail("用户id不能为空");
            }
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage());
        }
        try {
            ThirdPayResponse response = new ThirdPayResponse();
            response = walletAction.notifyAliClient(userId,requestDto);
            return JsonResult.success().addResult("result",response);
        }catch (RuntimeException ex) {
            return JsonResult.fail ( ex.getMessage () );
        }catch (Exception e) {
            e.printStackTrace();
            logger.error("钱包充值异常："+e.getMessage(), e);
            return JsonResult.fail("钱包充值异常");
        }
    }

    @ApiOperation(value = "充值钱包微信支付",notes = "返回值key:map 类型: Map<String, Object>")
    @PostMapping("/rechargeUseWechat")
    public JsonResult rechargeUseWechat(@RequestBody @Valid WalletRechargeRequestDto requestDto, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if(StringUtils.isBlank(requestDto.getNickName())){
            return JsonResult.fail("微信账号昵称不能为空");
        }
        String userId = null;
        try {
            userId = getUserId(request);
            if(userId==null) {
                return JsonResult.fail("用户id不能为空");
            }
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage());
        }
        try {
            Map<String, Object> result = walletAction.notifyWechatClient(userId,requestDto, request);
            return JsonResult.success().addResult("map",result);
        }catch (RuntimeException ex) {
            return JsonResult.fail ( ex.getMessage () );
        }catch (Exception e) {
            logger.error("钱包充值异常："+e.getMessage(), e);
            return JsonResult.fail("钱包充值异常");
        }
    }

    @ApiOperation("提现")
    @PostMapping("/withdraw")
    public JsonResult withdraw(@RequestBody @Valid WalletWithdrawRequestDto requestDto, BindingResult bindingResult,HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        String userId = null;
        try {
            userId = getUserId ( request );
            if (userId == null) {
                return JsonResult.fail ( "用户id不能为空" );
            }
            User user = userService.getUserById ( userId );
            if (user.getIdNumber () == null) {
                return JsonResult.fail ( "没有实名验证,不可以提现!" );
            }
            BigDecimal commonBill =billAction.countThisDayOutcome(userId,0);
            //BigDecimal commonBill = billService.countThisDayOutcome ( userId, 0 );
            BigDecimal billCount = commonBill.add ( requestDto.getAmount ().multiply ( BigDecimal.valueOf ( 100 ) ) );
            BigDecimal count = new BigDecimal ( 200000 );
            if (billCount.compareTo ( count ) > 0) {
                throw new RuntimeException ();
            }
        }catch (RuntimeException ex) {
            //ex.printStackTrace ();
            return JsonResult.fail ( "今日提现金额已达或与此次提现金额相加超出2000元,今日不能再提现." );
        }catch (Exception e){
            return JsonResult.fail("提现异常");
        }
        try {
            requestDto.setCent(new Money(requestDto.getAmount()).getCent());
            if(requestDto.getCent()<1){
                return JsonResult.fail("提现金额最少为1分钱（0.01）");
            }
            if(!walletAction.boolIsAccountBindTrue(userId,requestDto.getType()==1?1:2)){
                return JsonResult.fail("账号验证失败,请检查清楚");
            }
            CommonWallet wallet = walletAction.queryWalletByUserId(userId);
            if(wallet==null){
                return JsonResult.fail("用户钱包信息读取异常");
            }
            if(wallet.getTotalAmount()<requestDto.getCent()){
                return JsonResult.fail("零钱只有"+wallet.getTotalAmount()/100+"元,不足已提现");
            }
            String errorStr = walletAction.withdraw(userId,requestDto,wallet);
            return super.getResult(errorStr==null,errorStr);
        }catch (RuntimeException ex){
            return JsonResult.fail ( ex.getMessage () );
        }catch (Exception e) {
            e.printStackTrace();
            logger.error("用户提现异常："+e.getMessage(), e);
            return JsonResult.fail(e.getMessage());
        }
    }

    private Long getCent(BigDecimal money){
        return new Money(money).getCent();
    }

    @ApiOperation(value = "获取用户钱包金额，红包金额，网币金额",notes = "返回值key:result 类型:WalletFindUserAmontResponseDto")
    @GetMapping("/getUserAmount")
    public JsonResult getUserAmount(HttpServletRequest request) {
        String userId = null;
        try {
            userId = getUserId(request);
            if(userId==null) {
                return JsonResult.fail("用户id不能为空");
            }
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage());
        }
        try {
            WalletFindUserAmontResponseDto userAmount = walletFuseAction.getUserAmount(userId);
            return JsonResult.success().addResult("result",userAmount);
        } catch (Exception e) {
            logger.error("获取用户钱包金额，红包金额，网币金额异常："+e.getMessage(), e);
            return JsonResult.fail("获取用户钱包金额，红包金额，网币金额异常");
        }
    }

    @ApiOperation(value = "修改微信常用账号接口",notes = "如果nickname或者openId都为空的话，不进行修改")
    @PostMapping("/changeWechatUsedAccount")
    public JsonResult changeWechatUsedAccount(@RequestBody @Valid WalletChangeWechatUsedAccountRequestDto requestDto,BindingResult bindingResult,HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        String userId = null;
        try {
            userId = getUserId(request);
            if(userId==null) {
                return JsonResult.fail("用户id不能为空");
            }
        } catch (Exception e) {
            return JsonResult.fail(e.getMessage());
        }
        try {
            boolean bRet= walletAction.changeWechatUsedAccounts(userId,requestDto.getNickname(),requestDto.getOpenId());
            return super.getResult(bRet,"修改微信常用账号取消");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("修改微信常用账号异常");
        }
    }

   /* @ApiOperation(value = "测试支付宝授权")
    @PostMapping("/aliOauthRequest")
    public Result aliOauthRequest(String userId){
        if(StringUtils.isEmpty(userId)){
            return Result.newFailure(200,"用户id不能为空");
        }
        try{
            String result=walletAction.aliOauthRequest(userId);
            return Result.newSuccess(result);
        }catch (Exception e){
            logger.error(e.getMessage());
            return Result.newException(e);
        }
    }*/
}