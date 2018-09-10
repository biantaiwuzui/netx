package com.netx.shopping.biz.redpacketcenter;


import com.netx.common.vo.business.UpadteRedpacketPoolRequestDto;
import com.netx.shopping.model.redpacketcenter.RedpacketPool;
import com.netx.shopping.model.redpacketcenter.RedpacketPoolRecord;
import com.netx.shopping.service.redpacketcenter.RedpacketPoolRecordService;
import com.netx.shopping.service.redpacketcenter.RedpacketPoolService;
import com.netx.shopping.service.redpacketcenter.RedpacketRecordService;
import com.netx.utils.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * <p>
 * 网商-红包池 前端控制器
 * </p>
 *
 * @author 黎子安
 * @since 2018-05-09
 */
@Service("newRedpacketPoolAction")
public class RedpacketPoolAction {

    @Autowired
    RedpacketPoolService redpacketPoolService;

    @Autowired
    RedpacketRecordService redpacketRecordService;

    @Autowired
    RedpacketPoolRecordService redpacketPoolRecordService;

    @Transactional(rollbackFor = Exception.class)
    public boolean upadteRedpacketPool(UpadteRedpacketPoolRequestDto request){
        judgeRedpacketPool();
        Long amount = DatebaseMoney(request.getAmount());
        RedpacketPool redpacketPool = redpacketPoolService.get();
        if(redpacketPool==null){
            return false;
        }
        Long total = checkLong(redpacketPool.getTotalAmount());
        total += request.getWay()==1? amount : -amount;
        //将金额加入红包池
        redpacketPool.setTotalAmount(total);
        redpacketPoolService.updateByRedpacketPool(redpacketPool);
        //添加红包池记录
        RedpacketPoolRecord redpacketPoolRecord = new RedpacketPoolRecord();
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
            RedpacketPool redpacketPool = new RedpacketPool();
            redpacketPoolService.insert(redpacketPool);
        }
    }
}
