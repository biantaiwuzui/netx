package com.netx.shopping.biz.business;

import com.netx.common.vo.business.UpadteRedpacketPoolRequestDto;
import com.netx.shopping.model.business.SellerRedpacketPool;
import com.netx.shopping.model.business.SellerRedpacketPoolRecord;
import com.netx.shopping.service.business.RedpacketPoolRecordService;
import com.netx.shopping.service.business.RedpacketPoolService;
import com.netx.utils.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service("oldredpacketPoolAction")
public class RedpacketPoolAction{
    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Autowired
    RedpacketPoolService redpacketPoolService;

    @Autowired
    RedpacketPoolRecordService redpacketPoolRecordService;

    
    public boolean upadteRedpacketPool(UpadteRedpacketPoolRequestDto request){
        judgeRedpacketPool();
        Long amount = DatebaseMoney(request.getAmount());
        SellerRedpacketPool sellerRedpacketPool = redpacketPoolService.get();
        if(sellerRedpacketPool==null){
            return false;
        }
        Long total = checkLong(sellerRedpacketPool.getTotalAmount());
        total += request.getWay()==1? amount : -amount;
        //将金额加入红包池
        SellerRedpacketPool redpacketPool = new SellerRedpacketPool();
        redpacketPool.setUpdateUserId(request.getSourceId());
        redpacketPool.setTotalAmount(total);
        redpacketPoolService.update(redpacketPool);
        //添加红包池记录
        SellerRedpacketPoolRecord redpacketPoolRecord = new SellerRedpacketPoolRecord();
        redpacketPoolRecord.setAmount(amount);
        redpacketPoolRecord.setCreateUserId(request.getSourceId());
        redpacketPoolRecord.setSourceId(request.getSourceId());
        redpacketPoolRecord.setDeleted(0);
        redpacketPoolRecord.setSource(request.getSource());
        redpacketPoolRecord.setWay(request.getWay());
        return redpacketPoolRecordService.insert(redpacketPoolRecord);
    }

    private Long checkLong(Long cent){
        return cent==null?0:cent;
    }

    private Long DatebaseMoney(BigDecimal bigDecimal){
        return bigDecimal==null?0:new Money(bigDecimal).getCent();
    }

    /**
     * 判断红包池是否为空，是则插入一条记录
     */
    public void judgeRedpacketPool(){
        if (redpacketPoolService.get()==null) {
            SellerRedpacketPool redpacketPool = new SellerRedpacketPool();
            redpacketPool.setCreateUserId("999");
            redpacketPoolService.insert(redpacketPool);
        }
    }
}