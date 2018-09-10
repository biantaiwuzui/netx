package com.netx.fuse.client.ucenter;

import com.netx.common.vo.common.BaseDto;
import com.netx.common.vo.common.ThirdPayResponse;
import com.netx.common.vo.common.WalletCheckPasswordRequestDto;
import com.netx.common.vo.common.WalletRechargeRequestDto;
import com.netx.ucenter.biz.common.WalletAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

@Service
public class WalletClientAction {
    private Logger logger = LoggerFactory.getLogger(WalletClientAction.class);
    @Autowired
    WalletAction walletAction;
    public  ThirdPayResponse rechargeUseAli(String userId,WalletRechargeRequestDto requestDto){
        try {
            ThirdPayResponse response = new ThirdPayResponse();
            return walletAction.notifyAliClient(userId,requestDto);
        } catch (Exception e) {
            logger.error("钱包充值异常", e);
        }
        return null;
    }
    public Map<String, Object> rechargeUseWechat(String userId,WalletRechargeRequestDto requestDto,HttpServletRequest request) {
        try {
            return walletAction.notifyWechatClient(userId, requestDto, request);
        } catch (Exception e) {
            logger.error("钱包充值异常", e);
        }
        return null;
    }

    public BigDecimal getAmount(String userId){
        try {
            return walletAction.getAmount(userId);
        } catch (Exception e) {
            logger.error("查询可用零钱数异常", e);
        }
        return null;
    }
}
