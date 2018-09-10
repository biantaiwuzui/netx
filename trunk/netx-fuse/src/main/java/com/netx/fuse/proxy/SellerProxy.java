package com.netx.fuse.proxy;


import com.netx.common.vo.business.CommonUserIdRequestDto;
import com.netx.common.vo.business.GetSellerByUserIdVo;
import com.netx.common.vo.common.BillAddRequestDto;
import com.netx.fuse.client.shoppingmall.SellerClientAction;
import com.netx.fuse.client.ucenter.WalletBillClientAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class SellerProxy {

    private Logger logger= LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Autowired
    private SellerClientAction sellerClient;

    @Autowired
    WalletBillClientAction walletBillClient;

    public List<GetSellerByUserIdVo> getSellerByUserIdVoList(CommonUserIdRequestDto request){
        List<GetSellerByUserIdVo> result=sellerClient.getSellersByUserId(request);
        if(result!=null){
            List<GetSellerByUserIdVo> list=result;
            return list;
        }
        return null;
    }
    /**
     * 添加流水（平台给用户零钱）
     */
    public void addBill(String description, BigDecimal amount, String userId){
        BillAddRequestDto billAddRequestDto=new BillAddRequestDto();
        billAddRequestDto.setToUserId(userId);
        billAddRequestDto.setPayChannel(3);
        billAddRequestDto.setType(0);
        billAddRequestDto.setDescription(description);
        billAddRequestDto.setAmount(amount);
        walletBillClient.addBill("999",billAddRequestDto);
    }
}
