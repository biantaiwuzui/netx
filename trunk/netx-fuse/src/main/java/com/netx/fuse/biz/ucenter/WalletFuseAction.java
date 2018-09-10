package com.netx.fuse.biz.ucenter;

import com.netx.common.vo.business.UpadteRedpacketPoolRequestDto;
import com.netx.common.vo.common.WalletFindUserAmontResponseDto;
import com.netx.common.vo.currency.GetUserCurrencyAmountRequestDto;
import com.netx.fuse.client.shoppingmall.RedpacketPoolClientAction;
import com.netx.shopping.biz.redpacketcenter.RedpacketRecordAction;
import com.netx.ucenter.biz.common.WalletAction;
import com.netx.ucenter.model.common.CommonWallet;
import com.netx.ucenter.service.common.CommonServiceProvider;
import com.netx.utils.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WalletFuseAction {

    @Autowired
    private RedpacketRecordAction redpacketRecordAction;

    @Autowired
    private RedpacketPoolClientAction redpacketPoolClientAction;

    @Autowired
    private CommonServiceProvider commonServiceProvider;

    @Autowired
    private WalletAction walletAction;

    public WalletFindUserAmontResponseDto getUserAmount(String userId) throws Exception {

        GetUserCurrencyAmountRequestDto gd=new GetUserCurrencyAmountRequestDto();
        gd.setUserId(userId);
        gd.setUserId(userId);
        BigDecimal redpacketSend = redpacketRecordAction.seeWalletRedpacket(userId);
        BigDecimal walletAmount = walletAction.getAmount(userId);

        WalletFindUserAmontResponseDto wd=new WalletFindUserAmontResponseDto();
        wd.setCurrencyAmount(new BigDecimal(0));
        wd.setRedpacketAmount(redpacketSend);
        wd.setWalletAmount(walletAmount);
        return wd;
    }

    public void deleteWalletByUserId(String userId) throws Exception{
        CommonWallet wzCommonWallet = commonServiceProvider.getWalletService().selectByUserId(userId);
        if(wzCommonWallet!=null){
            BigDecimal amount=new BigDecimal(0);
            if (wzCommonWallet!=null){
                amount=new BigDecimal(Money.getMoneyString(wzCommonWallet.getTotalAmount()));
            }
            UpadteRedpacketPoolRequestDto dto=new UpadteRedpacketPoolRequestDto();
            dto.setAmount(amount);
            dto.setSource(10);
            dto.setWay(1);
            redpacketPoolClientAction.upadteRedpacketPool(dto);
        }
    }

}
