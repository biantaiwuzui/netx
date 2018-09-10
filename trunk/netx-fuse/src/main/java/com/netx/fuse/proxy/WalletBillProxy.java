package com.netx.fuse.proxy;

import com.netx.common.vo.common.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.netx.fuse.client.ucenter.WalletClientAction;
import com.netx.fuse.client.ucenter.WalletBillClientAction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WalletBillProxy {

    private Logger logger = LoggerFactory.getLogger(WalletBillProxy.class);

    @Autowired
    WalletBillClientAction walletBillClient;

    @Autowired
    WalletClientAction walletClient;

    public Boolean addBill(String userId,BillAddRequestDto requestDto ) {
        return walletBillClient.addBill(userId,requestDto);
    }

    public List<BigDecimal> searchMonthBillCount(List<String> recommendUserIds) {
        SearchMonthBillCountRequestDto requestDto =new SearchMonthBillCountRequestDto();
        requestDto.setRecommendUserIds(recommendUserIds);
        return walletBillClient.searchMonthBillCount(requestDto);
    }

    /**
     * 获取用户消费金额
     * @param request
     * @return
     */
    public BigDecimal queryBillAmountList(BillStatisticsRequestDto request){
//        List<BillListResponseDto> result=walletBillClient.queryList(userId,request);
        BigDecimal result = walletBillClient.getBillAction().statisticBill(request);
//        BigDecimal res=new BigDecimal(0);
//        logger.info(request+"*****");
//        if (result!=null){
//            for (int i=0;i<result.size();i++){
//                res=res.add(result.get(i).getAmount());
//            }
//        }
//        return res;
        return  result;
    }
}
