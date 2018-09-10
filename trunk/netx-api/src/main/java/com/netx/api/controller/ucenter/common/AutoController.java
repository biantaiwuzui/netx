package com.netx.api.controller.ucenter.common;

import com.netx.ucenter.biz.common.AutoAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Create by wongloong on 17-10-6
 */
@Api(description = "跑批任务")
@RestController
@RequestMapping("/api/common/auto")
public class AutoController {
    @Autowired
    private AutoAction autoAction;

    @ApiOperation("提现跑批")
    @PostMapping("/withdraw")
    public void autoRunWithdrawBill() {
        autoAction.autoRunWithdrawBill();
    }

    @ApiOperation("阿里充值跑批")
    @PostMapping("aliRecharge")
    public void autoCheckAliRechargeBill() {
        autoAction.autoCheckAliRechargeBill();
    }

    @ApiOperation("微信充值跑批")
    @PostMapping("wechatRecharge")
    public void autoCheckWechatRechargeBill() {
        autoAction.autoCheckWechatRechargeBill();
    }
}
