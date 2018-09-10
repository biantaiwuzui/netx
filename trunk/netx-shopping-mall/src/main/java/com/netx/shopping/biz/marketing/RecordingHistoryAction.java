package com.netx.shopping.biz.marketing;

import com.netx.shopping.service.marketing.RecordingHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 黎子安
 * @since 2017-12-29
 */
@Service
public class RecordingHistoryAction {

    @Autowired
    RecordingHistoryService recordingHistoryService;

    @Transactional

    public BigDecimal getDirect(String sellerId,Integer type){
        BigDecimal decimal = recordingHistoryService.getDecimal(sellerId,type);
        return decimal==null?new BigDecimal(0):decimal;
    }

    
    public BigDecimal getCommission(String sellerId){
        return getDirect(sellerId,0).add(getDirect(sellerId,1));
    }
}
