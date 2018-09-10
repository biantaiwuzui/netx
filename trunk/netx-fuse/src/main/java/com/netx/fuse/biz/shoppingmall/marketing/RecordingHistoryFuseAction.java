package com.netx.fuse.biz.shoppingmall.marketing;

import com.netx.fuse.client.shoppingmall.SellerClientAction;
import com.netx.shopping.model.business.Seller;
import com.netx.shopping.model.marketing.SellerRecordingHistory;
import com.netx.shopping.service.marketing.RecordingHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class RecordingHistoryFuseAction {

    @Autowired
    RecordingHistoryService recordingHistoryService;

    @Autowired
    SellerClientAction sellerClientAction;

    public Boolean addRecording(Seller seller, Seller toSeller, Integer type, Integer money){
        SellerRecordingHistory recordingHistory = new SellerRecordingHistory();
        long moneyOne = (long)money;
        recordingHistory.setMoney(moneyOne);
        recordingHistory.setSellerId(seller.getId());
        recordingHistory.setToSellerId(toSeller.getId());
        recordingHistory.setType(type);
        recordingHistoryService.insert(recordingHistory);
        sellerClientAction.addBill("恭喜你获得由"+seller.getName()+"提供给你的"+(type==0?"直":"间")+"接提成",new BigDecimal(money),toSeller.getUserId());
        return true;
    }
}
