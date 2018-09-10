package com.netx.api.controller.ucenter.common;

import com.netx.api.controller.BaseController;
import com.netx.common.vo.common.WzCommonWalletFrozenResponseDto;
import com.netx.common.vo.common.*;
import com.netx.fuse.biz.ucenter.WallerFrozenFuseAction;
import com.netx.ucenter.biz.common.WalletFrozenAction;
import com.netx.ucenter.model.common.CommonWalletFrozen;
import com.netx.utils.json.ApiCode;
import com.netx.utils.json.JsonResult;
import com.netx.utils.money.Money;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * Create by wongloong on 17-9-17
 */
@Api(description = "钱包冻结操作")
@RestController
@RequestMapping("/api/frozen")
public class WalletForzenController extends BaseController{
    private Logger logger = LoggerFactory.getLogger(WalletForzenController.class);
    @Autowired
    private WalletFrozenAction frozenService;
    @Autowired
    private WallerFrozenFuseAction wallerFrozenFuseAction;

    @PostMapping("/list")
    @ApiOperation(value = "查询冻结记录",notes = "返回值key:list 类型:List<WzCommonWalletFrozenResponseDto>")
    public JsonResult queryList(@RequestBody @Valid FrozenQueryRequestDto requestDto, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            requestDto.setUserId(getUserId(requestDto.getUserId(),request));
            if(StringUtils.isBlank(requestDto.getUserId())){
                return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
            }
        }catch (Exception e){
            return JsonResult.fail(ApiCode.NO_AUTHORIZATION);
        }
        try {
            List<WzCommonWalletFrozenResponseDto> resultList = frozenService.selectPageAndUserDate(requestDto);
            return JsonResult.success().addResult("list",resultList);
        } catch (Exception e) {
            logger.error("查询冻结记录异常："+e.getMessage(), e);
            return JsonResult.fail("查询冻结记录异常");
        }

    }

    @PostMapping("/add")
    @ApiOperation("添加冻结记录")
    public JsonResult add(@RequestBody @Valid FrozenAddRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            CommonWalletFrozen frozen = new CommonWalletFrozen();
            BeanUtils.copyProperties(requestDto, frozen);
            frozen.setAmount(Money.YuanToCent(requestDto.getAmount().toString()));
            frozen.setCreateTime(new Date());
            frozen.setBak1(requestDto.getTradeType().toString());
            frozen.setBak2(requestDto.getCurrencyId());
            return super.getResult(wallerFrozenFuseAction.addFrozenAndBill(frozen),"操作失败");
        } catch (Exception e) {
            logger.error("添加冻结记录异常："+e.getMessage(), e);
            return JsonResult.fail("添加冻结记录异常："+e.getMessage());
        }
    }

    @ApiOperation(value = "撤销冻结金额", notes = "此金额将会加到冻结人的账户中并生成流水")
    @PostMapping("/repeal")
    public JsonResult repeal(@RequestBody @Valid FrozenOperationRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            return super.getResult(frozenService.repeal(requestDto), "操作失败");
        } catch (Exception e) {
            logger.error("撤销冻结金额异常："+e.getMessage(), e);
            return JsonResult.fail("撤销冻结金额异常");
        }
    }


    @ApiOperation(value = "使用冻结金额", notes = "使用此金额进行消费,冻结金额会进入toUserId的钱包流水中")
    @PostMapping("/pay")
    public JsonResult pay(@RequestBody @Valid FrozenOperationRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            return super.getResult(frozenService.pay(requestDto),"操作失败");
        } catch (Exception e) {
            logger.error("使用冻结金额异常："+e.getMessage(), e);
            return JsonResult.fail("使用冻结金额异常");
        }
    }

    @ApiOperation(value = "分次扣取资讯押金", notes = "资讯押金消费，产生消费流水")
    @PostMapping("/cashDeposit")
    public JsonResult cashDeposit(@RequestBody @Valid FrozenCashDepositRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            return super.getResult(frozenService.cashDeposit(requestDto.getTypeId(),requestDto.getUserId()),"操作失败");
        } catch (Exception e) {
            logger.error("扣取押金异常："+e.getMessage(), e);
            return JsonResult.fail("扣取押金异常");
        }
    }

    @ApiOperation(value = "撤销资讯押金", notes = "此金额将会加到冻结人的账户中并生成流水")
    @PostMapping("/repealDeposit")
    public JsonResult repealDeposit(@RequestBody @Valid FrozenOperationRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            return super.getResult(frozenService.repealDeposit(requestDto),"操作失败");
        } catch (Exception e) {
            logger.error("撤销冻结金额异常："+e.getMessage(), e);
            return JsonResult.fail("撤销冻结金额异常");
        }
    }
}

