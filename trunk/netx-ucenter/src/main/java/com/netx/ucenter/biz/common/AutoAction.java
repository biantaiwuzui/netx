package com.netx.ucenter.biz.common;

import com.netx.ucenter.service.common.BillService;
import com.netx.ucenter.service.common.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Create by wongloong on 17-9-30
 */
@Service
public class AutoAction{
    @Autowired
    private BillAction billAction;

    public void autoRunWithdrawBill() {
        billAction.autoRunWithdrawBill();
    }

    public void autoCheckAliRechargeBill() {
        billAction.autoRunAliRechargeBill();
    }

    public void autoCheckWechatRechargeBill() {
        billAction.autoRunWechatRechargeBill();
    }
}
