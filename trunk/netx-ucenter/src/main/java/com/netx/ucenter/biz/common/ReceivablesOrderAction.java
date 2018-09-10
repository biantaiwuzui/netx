package com.netx.ucenter.biz.common;

import com.netx.common.common.enums.MessageTypeEnum;
import com.netx.common.user.util.DateTimestampUtil;
import com.netx.common.vo.common.ReceivablesOrderPayRequestDto;
import com.netx.common.vo.currency.GetUserCurrencyAmountVo;
import com.netx.ucenter.model.common.CommonReceivablesOrder;
import com.netx.ucenter.model.common.CommonWalletFrozen;
import com.netx.ucenter.model.user.User;
import com.netx.ucenter.service.common.ReceivablesOrderService;
import com.netx.ucenter.service.user.UserService;
import com.netx.utils.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 网币收款订单表 服务实现类
 * </p>
 * @author allen
 * @since 2017-12-08
 */
@Service
public class ReceivablesOrderAction{
    private Logger logger = LoggerFactory.getLogger(ReceivablesOrderAction.class);

    @Autowired
    private ReceivablesOrderService receivablesOrderService;

    @Autowired
    private MessagePushAction messagePushAction;

    @Autowired
    private UserService userService;

    public CommonReceivablesOrder payReceivablesOrder(ReceivablesOrderPayRequestDto requestDto,GetUserCurrencyAmountVo currency)throws Exception{
        if(currency==null){
            throw new Exception("网币不存在");
        }
        CommonReceivablesOrder wzCommonReceivablesOrder = receivablesOrderService.selectById(requestDto.getId());
        if(wzCommonReceivablesOrder==null){
            throw new Exception("收款订单不存在");
        }
        if (wzCommonReceivablesOrder.getState()==1){
            throw new Exception("叮当已支付");
        }
        Long money = new Money(currency.getCurrencyAmount()).getCent();
        if(wzCommonReceivablesOrder.getAmount()-money<0) {
            throw new Exception("网币余额不足");
        }
        wzCommonReceivablesOrder.setUserId(requestDto.getUserId());
        wzCommonReceivablesOrder.setCreditId(requestDto.getCurrencyId());
        wzCommonReceivablesOrder.setState(1);
        return wzCommonReceivablesOrder;
    }

    public void payReceivablesOrder(CommonReceivablesOrder wzCommonReceivablesOrder)throws Exception{
        receivablesOrderService.updateById(wzCommonReceivablesOrder);
        MessageFormat mf=new MessageFormat("您于{0}时间收到{1}扫码网币支付{2}元，请注意查收!");
        User user=userService.selectById(wzCommonReceivablesOrder.getToUserId());
        String alertMsg=mf.format(new String[] {String.valueOf(DateTimestampUtil.getDateByTimestamp1(new Date().getTime())),user.getNickname(), String.valueOf(wzCommonReceivablesOrder.getAmount())});
        Map<String,Object> param=new HashMap<>();
        param.put("redirectId","20");
        messagePushAction.sendMessageAlias(MessageTypeEnum.PRODUCT_TYPE,alertMsg,"收款通知",wzCommonReceivablesOrder.getToUserId(),param,"用户扫描支付收款订单");
    }

}
