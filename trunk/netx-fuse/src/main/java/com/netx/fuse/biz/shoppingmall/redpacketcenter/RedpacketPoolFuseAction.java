package com.netx.fuse.biz.shoppingmall.redpacketcenter;

import com.netx.common.user.util.DateTimestampUtil;
import com.netx.common.vo.business.UpadteRedpacketPoolRequestDto;
import com.netx.common.vo.common.BillAddRequestDto;
import com.netx.fuse.client.ucenter.WalletBillClientAction;
import com.netx.shopping.biz.ordercenter.MerchantOrderInfoAction;
import com.netx.shopping.biz.redpacketcenter.RedpacketPoolAction;
import com.netx.shopping.biz.merchantcenter.MerchantManagerAction;
import com.netx.shopping.biz.merchantcenter.MerchantPacketSetAction;
import com.netx.shopping.model.merchantcenter.MerchantManager;
import com.netx.shopping.model.merchantcenter.MerchantPacketSet;
import com.netx.shopping.model.ordercenter.constants.OrderStatusEnum;
import com.netx.shopping.service.merchantcenter.MerchantPacketSetService;
import com.netx.ucenter.service.user.UserService;
import com.netx.utils.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RedpacketPoolFuseAction {

    @Autowired
    WalletBillClientAction walletBillClientAction;
    @Autowired
    RedpacketPoolAction redpacketPoolAction;
    @Autowired
    MerchantManagerAction merchantManagerAction;
    @Autowired
    UserService userService;
    @Autowired
    MerchantPacketSetAction merchantPacketSetAction;
    @Autowired
    MerchantPacketSetService merchantPacketSetService;
    @Autowired
    MerchantOrderInfoAction merchantOrderInfoAction;

    public void getSellerAmountToPool() throws Exception{
        //获取昨天完成订单的商家id集
        Date now = new Date();
        Date start = DateTimestampUtil.addDayStartOrEndOfDate(now,1,-1);
        Date end = DateTimestampUtil.addDayStartOrEndOfDate(now,2,-1);
        List<String> merchantIds = merchantOrderInfoAction.getMerchantOrderInfoService().getMerchantIdsByTime(OrderStatusEnum.OS_FINISH,start,end);
        if (merchantIds.size() > 0) {
            Map<String,String> map = merchantPacketSetAction.getPacketIdsByMerchantIds(merchantIds);
            if (map.size() > 0) {
                for (String merchantId : merchantIds) {
                    MerchantPacketSet packetSet = merchantPacketSetService.selectById(map.get(merchantId));
                    if (packetSet != null) {
                        if (packetSet.getStartPacket() == true) {
                            getMerchantAmountToPool(new BigDecimal(Money.getMoneyString(packetSet.getPacketMoney())), merchantId);
                        }
                    }
                }
            }
        }
    }

    public void getMerchantAmountToPool(BigDecimal amount, String merchantId) throws Exception{
        List<MerchantManager> merchantManagers = merchantManagerAction.getMerchantManagerListByMerchantId(merchantId, 0);
        if (merchantManagers == null && merchantManagers.size() < 1){
            throw new Exception("获取收银人员信息失败");
        }
        MerchantManager merchantManager = merchantManagers.get(0);
        String moneyUserId = userService.getUserIdByUserNumber(merchantManager.getUserNetworkNum());
        BillAddRequestDto billAddRequestDto = new BillAddRequestDto();
        billAddRequestDto.setToUserId("999");
        billAddRequestDto.setAmount(amount);
        billAddRequestDto.setDescription("商家每日红包启动金额");
        billAddRequestDto.setPayChannel(4);
        walletBillClientAction.addBill(moneyUserId,billAddRequestDto);
        //金额加入红包池
        UpadteRedpacketPoolRequestDto upadteRedpacketPoolRequestDto=new UpadteRedpacketPoolRequestDto();
        upadteRedpacketPoolRequestDto.setWay(1);
        upadteRedpacketPoolRequestDto.setSource(6);
        upadteRedpacketPoolRequestDto.setAmount(amount);
        upadteRedpacketPoolRequestDto.setSourceId(moneyUserId);
        redpacketPoolAction.upadteRedpacketPool(upadteRedpacketPoolRequestDto);
    }
}
