package com.netx.boss.web.controller.worth;

import com.netx.common.wz.dto.wish.QueryWishWithdrawalsDto;
import com.netx.common.wz.dto.wish.WishWithdrawalDto;
import com.netx.fuse.biz.worth.WishFuseAction;
import com.netx.utils.json.JsonResult;
import com.netx.worth.biz.wish.WishAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Api(description = "网能心愿相关接口")
@RestController
@RequestMapping("/worth/wish")
public class WishController {

    private Logger logger = LoggerFactory.getLogger(WishController.class);

    @Autowired
    private WishAction wishAction;
    @Autowired
    private WishFuseAction wishFuseAction;

    @ApiOperation(value = "模糊分页查询申请提现列表")
    @PostMapping("/queryWishList")
    public JsonResult queryWishList(@Valid @RequestBody QueryWishWithdrawalsDto dto, BindingResult bindingResult){
        if( bindingResult.hasErrors()){
            return JsonResult.success(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            Map<String, Object> map = wishFuseAction.queryWishHistoryList(dto);
            return JsonResult.successJsonResult(map);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return JsonResult.fail("查询申请提现列表列表异常！");
        }
    }

    @ApiOperation(value = "提现成功")
    @PostMapping("/success")
    public JsonResult success(@Valid @RequestBody WishWithdrawalDto withdrawalDto, BindingResult bindingResult){
        if( bindingResult.hasErrors()){
            return JsonResult.success(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            boolean success = wishAction.withdrawalSuccess(withdrawalDto);
            if(success) {
                return JsonResult.success();
            }
            return JsonResult.fail("修改状态为【提现成功】失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("修改状态为【提现成功】异常！");
        }
    }

    @ApiOperation(value = "提现失败")
    @PostMapping("fail")
    public JsonResult fail(@Valid @RequestBody  WishWithdrawalDto withdrawalDto, BindingResult bindingResult){
        if( bindingResult.hasErrors()){
            return JsonResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            boolean fail = wishAction.withdrawalFailure(withdrawalDto);
            if(fail) {
                return JsonResult.success();
            }
            return JsonResult.fail("修改状态为【提现失败】失败！");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail("修改状态为【提现失败】异常！");
        }
    }


}
