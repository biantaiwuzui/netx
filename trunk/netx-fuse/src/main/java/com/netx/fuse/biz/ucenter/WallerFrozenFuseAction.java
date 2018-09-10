package com.netx.fuse.biz.ucenter;

import com.netx.common.vo.common.BillAddRequestDto;
import com.netx.common.vo.common.ReceivablesOrderPayRequestDto;
import com.netx.common.vo.currency.CurrencyOperateRequestDto;
import com.netx.common.vo.currency.GetUserCurrencyAmountRequestDto;
import com.netx.common.vo.currency.GetUserCurrencyAmountVo;
import com.netx.ucenter.biz.common.BillAction;
import com.netx.ucenter.biz.common.ReceivablesOrderAction;
import com.netx.ucenter.biz.common.WalletAction;
import com.netx.ucenter.biz.common.WalletFrozenAction;
import com.netx.ucenter.model.common.CommonBill;
import com.netx.ucenter.model.common.CommonReceivablesOrder;
import com.netx.ucenter.model.common.CommonWallet;
import com.netx.ucenter.model.common.CommonWalletFrozen;
import com.netx.ucenter.service.common.CommonServiceProvider;
import com.netx.utils.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class WallerFrozenFuseAction {

    private Logger logger = LoggerFactory.getLogger(WallerFrozenFuseAction.class);

    @Autowired
    private BillAction billAction;

    @Autowired
    private CommonServiceProvider commonServiceProvider;

    @Autowired
    private WalletFrozenAction walletFrozenAction;

    @Autowired
    private WalletAction walletAction;

    @Autowired
    private ReceivablesOrderAction receivablesOrderAction;

    public WalletFrozenAction getWalletFrozenAction() {
        return walletFrozenAction;
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(String userId, BillAddRequestDto requestDto){
        //网币支付
        if (requestDto.getPayChannel() != 2) { //交易支付方式
            CommonWallet wzCommonWallet = walletAction.queryWalletByUserId(userId);
            Long amount = getCent(requestDto.getAmount());
            //Long amount = wzCommonWallet.getTotalAmount();
            CommonBill wzCommonBill = new CommonBill();
            CommonBill toUserBill = new CommonBill();
            wzCommonBill.setCreateTime(new Date());
            toUserBill.setCreateTime(new Date());
            wzCommonBill.setWallerId(wzCommonWallet.getId());
            //设置交易对象id
            wzCommonBill.setBak1(requestDto.getToUserId());
            toUserBill.setBak1(userId);
            wzCommonBill.setAmount(amount);
            BeanUtils.copyProperties(requestDto, wzCommonBill);
            wzCommonBill.setTradeType(0);
            wzCommonBill.setUserId(userId);
            BeanUtils.copyProperties(requestDto, toUserBill);
            toUserBill.setAmount(amount);
           /* if (amount.compareTo(requestDto.getAmount()) < 0) {//支出金额大于账户余额
                throw new Exception("账户余额不足");
            }*/
            if(wzCommonWallet.getTotalAmount()-amount<0){
                throw new RuntimeException("账户余额不足");
            }
            wzCommonWallet.setTotalAmount(wzCommonWallet.getTotalAmount()-amount);
            wzCommonWallet.setUpdateTime(new Date());
            commonServiceProvider.getWalletService().updateById(wzCommonWallet);
            wzCommonBill.setTotalAmount(wzCommonWallet.getTotalAmount());
            commonServiceProvider.getBillService().insert(wzCommonBill);
            //添加收款人的流水及钱包
            toUserBill.setTradeType(1);
            toUserBill.setUserId(requestDto.getToUserId());
            CommonWallet toUserWallet = walletAction.queryWalletByUserId(requestDto.getToUserId());
            toUserBill.setWallerId(toUserWallet.getId());
            toUserWallet.setTotalAmount(toUserWallet.getTotalAmount()+amount);
            commonServiceProvider.getWalletService().updateById(toUserWallet);
            toUserBill.setTotalAmount(toUserWallet.getTotalAmount());
            commonServiceProvider.getBillService().insert(toUserBill);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public boolean addFrozenAndBill(CommonWalletFrozen frozen){
        CommonBill bill = new CommonBill();
        //支出用户id
        bill.setUserId(frozen.getUserId());
        //支出
        bill.setTradeType(0);
        bill.setDescription(frozen.getDescription() + "，其冻结金额：" + (Money.CentToYuan(frozen.getAmount()).getAmount())+"元");
        bill.setAmount(frozen.getAmount());
        bill.setCreateTime(new Date());
        //零钱支付
        if (frozen.getBak1().equals("0")) {
            return walletFrozenAction.addFrozenAndBillByChange(bill,frozen);
            //网信支付
        }
        return false;
    }

    private Long getCent(BigDecimal money){
        return new Money(money).getCent();
    }
}
